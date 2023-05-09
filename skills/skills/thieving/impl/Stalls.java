package skills.thieving.impl;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.TextUtils;

import skills.Skills;
import skills.thieving.Thieving;

/**
 * Represents functionality for stealing from a stall.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class Stalls extends Thieving {

	/**
	 * The definition for this stall.
	 */
	private final StallData stall;

	/**
	 * The object node this player is interacting with.
	 */
	private final GameObject object;

	/**
	 * The possible loot stealing the stall.
	 */
	private final Item loot;

	/**
	 * Constructs a new {@link Stalls}.
	 * @param player {@link #getPlayer()}.
	 * @param stall the stall this player is stealing from.
	 * @param object the object this player is interacting with.
	 */
	public Stalls(Player player, StallData stall, GameObject object) {
		super(player, Optional.of(object));
		this.stall = stall;
		this.object = object;
		this.loot = RandomUtils.random(stall.loot);
	}

	/**
	 * The animation when stealing from stalls
	 */
	private static final Animation ANIMATION = new Animation(832);

	/**
	 * Object ids of empty stalls
	 */
	private static final int[] EMPTY_STALLS = new int[]{634, 620};

	@Override
	public int requirement() {
		return stall.requirement;
	}

	@Override
	public Optional<Animation> startAnimation() {
		return Optional.of(ANIMATION);
	}

	@Override
	public boolean canInit() {
		String name = object.getDefinitions().getName();
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
	public void onSubmit() {
		
	}

	@Override
	public void onExecute(Task t) {
		t.cancel();
	}

	@Override
	public void onStop(boolean success) {
		if(success && stall == StallData.SEED)
			World.get().submit(new StallTask(this, object));
	}

	@Override
	public Item loot() {
		return loot;
	}

	@Override
	public int delay() {
		return 1;
	}

	@Override
	public boolean instant() {
		return false;
	}

	@Override
	public boolean canExecute() {
		return !object.isDisabled();
	}

	@Override
	public double experience() {
		return stall.experience;
	}

	/**
	 * The enumerated type whose elements represent the data for stealing from a stall.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum StallData {
		FOOD_STALL(new int[]{4875, 1276}, EMPTY_STALLS[1], 1, new Item[]{new Item(1963, 1)}, 23, 3),
		CRAFTING_STALL(new int[]{4874}, EMPTY_STALLS[1], 35, new Item[]{new Item(19650, 1)}, 35, 3),
		GENERAL_STALL(new int[]{4876}, EMPTY_STALLS[1], 50, new Item[]{new Item(4073, 1)}, 70, 4),
		MAGIC_STALL(new int[]{4877}, EMPTY_STALLS[1], 75, new Item[]{new Item(14061, 1)}, 150, 8),
		SCIMITAR_STALL(new int[]{4878}, EMPTY_STALLS[1], 90, new Item[]{new Item(6611, 1)}, 220, 12),
		SEED(new int[]{7053, 3125}, EMPTY_STALLS[0], 80, new Item[]{new Item(5318, 1), new Item(5319, 1), new Item(5324, 3), new Item(5323, 2), new Item(5321, 2), new Item(5305, 4), new Item(5307, 2), new Item(5308, 2), new Item(5306, 3), new Item(5309, 2), new Item(5310, 1), new Item(5311, 1), new Item(5101, 1), new Item(5102, 1), new Item(5103, 1), new Item(5104, 1), new Item(5105, 1), new Item(5106, 1), new Item(5096, 1), new Item(5097, 1), new Item(5098, 1), new Item(5099, 1), new Item(5100, 1), new Item(5291, 1), new Item(5292, 1), new Item(5293, 1), new Item(5294, 1), new Item(5295, 1), new Item(5296, 1), new Item(5297, 1), new Item(5298, 1), new Item(5299, 1), new Item(5300, 1), new Item(5301, 1), new Item(5302, 1), new Item(5303, 1), new Item(5304, 1), new Item(5280, 1), new Item(5281, 1)}, 200, 14),
		/*VEGETABLE(new int[]{4706}, EMPTY_STALLS[0], 1, new Item[]{new Item(995, 50)}, 10, 2),
		BAKERS(new int[]{2561}, EMPTY_STALLS[0], 5, new Item[]{new Item(995, 100)}, 16, 3),
		TEA(new int[]{635}, EMPTY_STALLS[0], 5, new Item[]{new Item(995, 500)}, 16, 7),
		SILK(new int[]{2560}, EMPTY_STALLS[0], 20, new Item[]{new Item(995, 1000)}, 24, 8),
		WINE(new int[]{14011}, EMPTY_STALLS[0], 22, new Item[]{new Item(995, 2500)}, 27, 16),
		SEED(new int[]{7053}, EMPTY_STALLS[0], 27, new Item[]{new Item(995, 5000)}, 10, 11),
		FUR(new int[]{2563}, EMPTY_STALLS[0], 35, new Item[]{new Item(995, 75000)}, 36, 15),
		FISH(new int[]{4277}, EMPTY_STALLS[0], 42, new Item[]{new Item(995, 10000)}, 42, 16),
		CROSSBOW(new int[]{17031}, EMPTY_STALLS[0], 49, new Item[]{new Item(995, 12500)}, 52, 11),
		SILVER(new int[]{2565}, EMPTY_STALLS[0], 50, new Item[]{new Item(995, 15000)}, 54, 20),
		CUSTOMS_EVIDENCE_FILES(new int[]{-1}, -1, 63, new Item[]{new Item(995, 17500)}, 75, 20),
		SPICE(new int[]{2564}, EMPTY_STALLS[0], 65, new Item[]{new Item(995, 20000)}, 81, 40),
		GEM(new int[]{2562}, EMPTY_STALLS[0], 75, new Item[]{new Item(995, 35000)}, 160, 80)*/;

		/**
		 * The object identification for this stall.
		 */
		public final int[] objectId;

		/**
		 * The object identification for an empty stall.
		 */
		private final int emptyStallId;

		/**
		 * The required level to steal from this stall.
		 */
		private final int requirement;

		/**
		 * The loot you get from stealing for this stall.
		 */
		private final Item[] loot;

		/**
		 * The experience gained for stealing from this stall.
		 */
		private final double experience;

		/**
		 * The time it takes for the stall to respawn.
		 */
		private final int respawnTime;

		/**
		 * Constructs a new {@link StallData} enumerator.
		 * @param objectId {@link #objectId}.
		 * @param emptyStallId {@link #emptyStallId}.
		 * @param requirement {@link #requirement}.
		 * @param loot {@link #loot}.
		 * @param experience {@link #experience}.
		 * @param respawnTime {@link #respawnTime}.
		 */
		StallData(int[] objectId, int emptyStallId, int requirement, Item[] loot, int experience, int respawnTime) {
			this.objectId = objectId;
			this.emptyStallId = emptyStallId;
			this.requirement = requirement;
			this.loot = loot;
			this.experience = experience;
			this.respawnTime = respawnTime;
		}

	}

	/**
	 * The class which submits respawning tasks for stalls.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static class StallTask extends Task {

		/**
		 * The stall we're submitting this task for.
		 */
		private final Stalls stall;

		/**
		 * The main stall object.
		 */
		private final GameObject object;

		/**
		 * The saved stall id.
		 */
		private final int id;

		/**
		 * Constructs a new {@link StallTask}.
		 * @param stall the stall being used.
		 * @param object the stall's object node.
		 */
		StallTask(Stalls stall, GameObject object) {
			super(stall.stall.respawnTime, false);
			this.stall = stall;
			this.object = object;
			this.id = object.getId();
		}

		@Override
		public void onSubmit() {
			object.setDisabled(true);
			object.setId(stall.stall.emptyStallId);
//			object.publish();
		}

		@Override
		public void execute() {
			object.setDisabled(false);
			object.setId(id);
//			stall.object.publish();
			this.cancel();
		}

	}

	@Override
	public int getSkillId() {
		return Skills.THIEVING;
	}
}