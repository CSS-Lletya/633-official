package skills.thieving.impl;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.TextUtils;

import skills.Skills;
import skills.thieving.Thieving;

/**
 * Represents functionality for pickpocketing from various npcs.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @author Dennis
 */
public final class Pickpocketing extends Thieving {

	/**
	 * The definition for this theft.
	 */
	private final PickpocketData definition;

	/**
	 * The current mob we're interacting with.
	 */
	private final NPC mob;

	/**
	 * The possible loot for pickpocketing.
	 */
	private final Item loot;

	/**
	 * Represents the animation specific to pickpocketing.
	 */
	private static final Animation ANIMATION = Animations.PICKPOCKET;

	/**
	 * Represents the mob animation(hitting player).
	 */
	private static final Animation NPC_ANIMATION = Animations.NPC_PICKPOCKET_FAIL_RESPONSE;

	/**
	 * Represents the animation specific to pickpocketing.(block animation)
	 */
	private static final Animation STUN_ANIMATION = Animations.PICKPOCKET_STUN;

	/**
	 * The graphic id when player is stunned
	 */
	private static final Graphics STUN_GRAPHIC = new Graphics(80, 100, 50);

	/**
	 * Constructs a new {@link Pickpocketing}.
	 * @param player {@link #getPlayer()}.
	 * @param data the definition of this theft.
	 * @param mob the mob this player is stealing from.
	 */
	public Pickpocketing(Player player, PickpocketData data, NPC mob) {
		super(player, Optional.of(mob.getLastWorldTile()));
		this.definition = data;
		this.mob = mob;
		this.loot = RandomUtils.random(definition.loot);
	}
	
	@Override
	public boolean failure() {
		if (getPlayer().getSkills().getLevel(Skills.THIEVING) == 1) {
			return false;
		}
		if(mob.getDefinitions().getName().contains("Master")) {
			return !RandomUtils.success(((((double) 5 / 833) * getPlayer().getSkills().getLevel(Skills.THIEVING)) + ((double) 17 / 49)));
		}
		return (RandomUtils.inclusive(getPlayer().getSkills().getLevel(Skills.THIEVING) + RandomUtils.inclusive(5)) < (RandomUtils.inclusive(definition.requirement) + RandomUtils.inclusive((5))));
	}

	@Override
	public int requirement() {
		return definition.requirement;
	}

	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(ANIMATION);
	}

	@Override
	public boolean canInit() {
		String name = mob.getDefinitions().getName();
		if (getPlayer().getSkills().getLevel(Skills.THIEVING) < requirement()) {
			getPlayer().getPackets().sendGameMessage("You need a thieving level of " + requirement() + " to steal from " + TextUtils.appendIndefiniteArticle(name) + ".");
			return false;
		}
		if(!getPlayer().getInventory().hasFreeSlots()) {
			getPlayer().getPackets().sendGameMessage("You don't have enough inventory space for the loot.");
			return false;
		}
		if (!player.getDetails().getThievingStun().elapsed(1800)) {
			return false;
		}
		player.getDetails().getThievingStun().reset();
		return true;
	}

	@Override
	public Item loot() {
		return loot;
	}

	@Override
	public void onSubmit() {

	}

	@Override
	public void onExecute(Task t) {
		if(failure()) {
			mob.setNextForceTalk(new ForceTalk("What do you think you're doing?"));
			if(mob.getDefinitions().hasAttackOption())
				mob.setNextAnimation(new Animation(mob.getCombatDefinitions().getAttackAnim()));
			else
				mob.setNextAnimation(NPC_ANIMATION);
			int hit = RandomUtils.inclusive(1, definition.damage);
			getPlayer().applyHit(new Hit(player, hit, HitLook.MELEE_DAMAGE));
			getPlayer().setNextAnimation(STUN_ANIMATION);
			getPlayer().setNextGraphics(STUN_GRAPHIC);
			getPlayer().getMovement().lock(definition.seconds);
		} else {
			getPlayer().getInventory().addItem(loot);
			getPlayer().getPackets().sendGameMessage("You pick the victims pocket.");
		}
		t.cancel();
	}

	@Override
	public int delay() {
		return 3;
	}

	@Override
	public boolean instant() {
		return false;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public double experience() {
		return definition.experience;
	}

	public enum PickpocketData {
		MAN(new int[]{1, 2, 3, 4, 5, 6, 16, 24, 170, 351, 663, 728, 729, 1086, 3223, 3224,
	            3225, 3915, 5923, 7873, 7874, 7875, 7876, 7877, 7878, 7879, 7909, 7910, 8010, 8011,
	            12345, 12346, 12347, 25, 352, 353, 354, 360, 361, 362, 363, 2776, 3226, 5924, 7880,
	            7881, 7882, 7883, 7884, 8012, 8013}, new Item[]{new Item(995, 3)}, 1, 8, 3, 10),
		FARMER(new int[]{7, 291, 1377, 1757, 1758, 1759, 1760, 1761, 3917, 4925, 7128, 7129, 7130, 7131}, new Item[]{new Item(995, 9), new Item(5318, 1)}, 10, 14.5, 3, 10),
		FEMALE_HAM_MEMBER(new int[]{1715}, new Item[]{new Item(882, 20), new Item(1351, 1), new Item(1265, 1), new Item(1349, 1), new Item(1267, 1), new Item(886, 20), new Item(1353, 1), new Item(1207, 1), new Item(1129, 1), new Item(4170, 1), new Item(4302, 1), new Item(4298, 1), new Item(4300, 1), new Item(4304, 1), new Item(4306, 1), new Item(4308, 1), new Item(4310, 1), new Item(995, 21), new Item(319, 1), new Item(2138, 1), new Item(668, 1), new Item(453, 1), new Item(440, 1), new Item(1739, 1), new Item(314, 5), new Item(1734, 6), new Item(1733, 1), new Item(1511, 1), new Item(686, 1), new Item(697, 1), new Item(1625, 1), new Item(1627, 1), new Item(1617, 1), new Item(199, 1), new Item(201, 1), new Item(203, 1)}, 15, 18.5, 3, 30),
		MALE_HAM_MEMBER(new int[]{1714, 1716}, new Item[]{new Item(882, 20), new Item(1351, 1), new Item(1265, 1), new Item(1349, 1), new Item(1267, 1), new Item(886, 20), new Item(1353, 1), new Item(1207, 1), new Item(1129, 1), new Item(4170, 1), new Item(4302, 1), new Item(4298, 1), new Item(4300, 1), new Item(4304, 1), new Item(4306, 1), new Item(4308, 1), new Item(4310, 1), new Item(995, 21), new Item(319, 1), new Item(2138, 1), new Item(668, 1), new Item(453, 1), new Item(440, 1), new Item(1739, 1), new Item(314, 5), new Item(1734, 6), new Item(1733, 1), new Item(1511, 1), new Item(686, 1), new Item(697, 1), new Item(1625, 1), new Item(1627, 1), new Item(199, 1), new Item(201, 1), new Item(203, 1)}, 20, 22.5, 3, 30),
		HAM_GUARD(new int[]{1710, 1711, 1712}, new Item[]{new Item(882, 20), new Item(1351, 1), new Item(1265, 1), new Item(1349, 1), new Item(1267, 1), new Item(886, 20), new Item(1353, 1), new Item(1207, 1), new Item(1129, 1), new Item(4170, 1), new Item(4302, 1), new Item(4298, 1), new Item(4300, 1), new Item(4304, 1), new Item(4306, 1), new Item(4308, 1), new Item(4310, 1), new Item(995, 21), new Item(319, 1), new Item(2138, 1), new Item(668, 1), new Item(453, 1), new Item(440, 1), new Item(1739, 1), new Item(314, 5), new Item(1734, 6), new Item(1733, 1), new Item(1511, 1), new Item(686, 1), new Item(697, 1), new Item(1625, 1), new Item(1627, 1), new Item(199, 1), new Item(201, 1), new Item(203, 1), new Item(5321, 4), new Item(5323, 4), new Item(5319, 4), new Item(5324, 1)}, 20, 22.5, 3, 30),
		WARRIOR_WOMAN(new int[]{15}, new Item[]{new Item(995, 18)}, 25, 26, 3, 20),
		AL_KHARID_WARRIOR(new int[]{18}, new Item[]{new Item(995, 18)}, 25, 26, 3, 20),
		ROGUE(new int[]{187, 2267, 2268, 2269, 8122}, new Item[]{new Item(995, 120), new Item(556, 8), new Item(1523, 1), new Item(1219, 1), new Item(1993, 1), new Item(2357, 1), new Item(1227, 1)}, 32, 35.5, 3, 20),
		GUARD(new int[]{9, 10, 5920, 3408}, new Item[]{new Item(995, 700), new Item(2351, 1), new Item(199, 1), new Item(313, 4), new Item(453, 1), new Item(562, 12), new Item(712, 1), new Item(950, 1), new Item(1623, 1)}, 40, 46.5, 3, 20),
		POLLNIVIAN_BEARDED_BANDIT(new int[]{1880, 1881}, new Item[]{new Item(995, 40)}, 45, 65, 3, 50),
		DESERT_BANDIT(new int[]{1926, 1927, 1928, 1929, 1930, 1931}, new Item[]{new Item(995, 30), new Item(179, 1), new Item(1523, 1)}, 53, 79.5, 3, 30),
		KNIGHT_OF_ARDOUGNE(new int[]{23, 26}, new Item[]{new Item(995, 50)}, 55, 84.3, 3, 30),
		POLLNIVIAN_BANDIT(new int[]{1883, 1884}, new Item[]{new Item(995, 50)}, 55, 84.3, 3, 50),
		YANILLE_WATCHMEN(new int[]{32}, new Item[]{new Item(995, 60), new Item(2309, 1)}, 65, 137.5, 3, 30),
		MENAPHITE_THUG(new int[]{1905}, new Item[]{new Item(995, 60)}, 65, 137.5, 3, 50),
		PALADIN(new int[]{20, 365, 2256}, new Item[]{new Item(995, 80), new Item(562, 2)}, 70, 151.75, 4, 3),
		GNOME(new int[]{66, 67, 68}, new Item[]{new Item(995, 300), new Item(557, 1), new Item(444, 1), new Item(569, 1), new Item(2150, 1), new Item(2162, 1)}, 75, 198.5, 4, 10),
		MASTER_FARMER(new int[]{2234, 2235, 3299}, new Item[]{new Item(5318, 1), new Item(5319, 1), new Item(5324, 3), new Item(5323, 2), new Item(5321, 2), new Item(5305, 4), new Item(5307, 2), new Item(5308, 2), new Item(5306, 3), new Item(5309, 2), new Item(5310, 1), new Item(5311, 1), new Item(5101, 1), new Item(5102, 1), new Item(5103, 1), new Item(5104, 1), new Item(5105, 1), new Item(5106, 1), new Item(5096, 1), new Item(5097, 1), new Item(5098, 1), new Item(5099, 1), new Item(5100, 1), new Item(5291, 1), new Item(5292, 1), new Item(5293, 1), new Item(5294, 1), new Item(5295, 1), new Item(5296, 1), new Item(5297, 1), new Item(5298, 1), new Item(5299, 1), new Item(5300, 1), new Item(5301, 1), new Item(5302, 1), new Item(5303, 1), new Item(5304, 1), new Item(5280, 1), new Item(5281, 1)}, 38, 43, 3, 30),
		HERO(new int[]{21}, new Item[]{new Item(995, 300), new Item(560, 2), new Item(565, 1), new Item(444, 1), new Item(1993, 1), new Item(569, 1), new Item(1601, 1)}, 82, 273.3, 5, 40);

		/**
		 * The identifiers which represents this mob.
		 */
		public final int[] npcId;

		/**
		 * The loot obtained upon pickpocketing this mob.
		 */
		private final Item[] loot;

		/**
		 * The requirement required for pickpocketing this mob.
		 */
		private final int requirement;

		/**
		 * The experience gained upon pickpockting this mob.
		 */
		private final double experience;

		/**
		 * The amount of seconds this player stays stunned upon failing to pickpocket this mob.
		 */
		private final int seconds;

		/**
		 * The amount of damage this players get inflicted upon failing to pickpocket this mob.
		 */
		private final int damage;

		private PickpocketData(int[] npcId, Item[] loot, int requirement, double experience, int seconds, int damage) {
			this.npcId = npcId;
			this.loot = loot;
			this.requirement = requirement;
			this.experience = experience;
			this.seconds = seconds;
			this.damage = damage;
		}
	}

	@Override
	public int getSkillId() {
		return Skills.THIEVING;
	}
}