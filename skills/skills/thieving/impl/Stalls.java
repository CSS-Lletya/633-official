package skills.thieving.impl;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
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
		player.getPackets().sendGameMessage("You attempt to steal " + stall.message + "");
		return true;
	}

	@Override
	public void onSubmit() {
		
	}

	@Override
	public void onExecute(Task t) {
		player.getPackets().sendGameMessage("You steal a " + loot.getName().toLowerCase() + " from the " + object.getDefinitions().getName().toLowerCase() +".");
		player.getDetails().getStatistics()
		.addStatistic(ItemDefinitions.getItemDefinitions(loot.getId()).getName() + "_Taken_From_Stall")
		.addStatistic("Stalls_Thieved");
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
		return 3;
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

	public enum StallData {
		MAGIC_STALL(new int[]{4877}, EMPTY_STALLS[1], 75, new Item[]{new Item(14061, 1)}, 150, 8, "some runes from the magic stall."),
		SCIMITAR_STALL(new int[]{4705, 4707}, EMPTY_STALLS[1], 90, new Item[]{new Item(1323, 1)}, 220, 12, "a scimitar from the scimitar stall."),
		SEED(new int[]{7053, 3125}, EMPTY_STALLS[0], 80, new Item[]{new Item(5305), new Item(5306), new Item(5308), new Item(5319), new Item(5318), new Item(5324), new Item(5322)}, 200, 14, "some seed's from the seed merchant's stall."),
		VEGETABLE(new int[]{4706, 4708}, EMPTY_STALLS[0], 1, new Item[]{new Item(1957, 1), new Item(1965), new Item(1942)}, 10, 2, "some vegetables from the vegetable's merchant stall."),
		BAKERS(new int[]{2561, 6163, 34384}, EMPTY_STALLS[0], 5, new Item[]{new Item(1891, 1), new Item(2309, 1), new Item(1901, 1)}, 16, 3, "a cake from the baker's stall."),
		TEA(new int[]{635, 6574}, EMPTY_STALLS[0], 5, new Item[]{new Item(712)}, 16, 7, "a cup of tea."),
		SILK(new int[]{34383, 2560}, EMPTY_STALLS[0], 20, new Item[]{new Item(950)}, 24, 8, "some silk from the silk stall."),
		WINE(new int[]{34383, 14011}, EMPTY_STALLS[0], 22, new Item[]{new Item(1937), new Item(1939), new Item(1987), new Item(1935), new Item(7919)}, 27, 16, "some wine from the wine stall."),
		FUR(new int[]{34387, 2563}, EMPTY_STALLS[0], 35, new Item[]{new Item(6814), new Item(958)}, 36, 15, "some fur from the fur stall."),
		FISH(new int[]{4277, 4705, 4707}, EMPTY_STALLS[0], 42, new Item[]{new Item(331), new Item(359), new Item(377)}, 42, 16, "some fish from the fish stall."),
		CROSSBOW(new int[]{4277, 4705, 4707}, EMPTY_STALLS[0], 49, new Item[]{new Item(9375), new Item(9420), new Item(9440)}, 52, 11, "something from the crossbow stall."),
		SILVER(new int[]{2565, 6164, 34382}, EMPTY_STALLS[0], 50, new Item[]{new Item(442)}, 54, 20, "some silver from the silver stall."),
		SPICE(new int[]{2564,34386}, EMPTY_STALLS[0], 65, new Item[]{new Item(2007, 1)}, 81, 40, "some spice from the spice stall."),
		GEM(new int[]{2562,6162,34385 }, EMPTY_STALLS[0], 75, new Item[]{new Item(1623)}, 160, 80, "a gem from the gem stall."),
		
		APE_SCIMITAR_STALL(new int[]{4878}, EMPTY_STALLS[1], 90, new Item[]{new Item(1323, 1)}, 220, 12, "a scimitar from the scimitar stall."),
		APE_MAGIC_STALL(new int[]{4877}, EMPTY_STALLS[1], 75, new Item[]{new Item(556), new Item(557), new Item(554), new Item(555), new Item(563)}, 150, 8, "some runes from the magic stall."),
		APE_GENERAL_STALL(new int[]{4876}, EMPTY_STALLS[1], 50, new Item[]{new Item(1931), new Item(2347), new Item(590)}, 70, 4, "a general item from the general stall."),
		APE_FOOD_STALL(new int[]{4875}, EMPTY_STALLS[1], 1, new Item[]{new Item(1963, 1)}, 23, 3, "a banana from the food stall."),
		APE_CRAFTING_STALL(new int[]{4874}, EMPTY_STALLS[1], 35, new Item[]{new Item(19650, 1)}, 35, 3, "a crafting item from the crafting stall."),
		;
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
		
		private final String message;

		/**
		 * Constructs a new {@link StallData} enumerator.
		 * @param objectId {@link #objectId}.
		 * @param emptyStallId {@link #emptyStallId}.
		 * @param requirement {@link #requirement}.
		 * @param loot {@link #loot}.
		 * @param experience {@link #experience}.
		 * @param respawnTime {@link #respawnTime}.
		 */
		StallData(int[] objectId, int emptyStallId, int requirement, Item[] loot, int experience, int respawnTime, String message) {
			this.objectId = objectId;
			this.emptyStallId = emptyStallId;
			this.requirement = requirement;
			this.loot = loot;
			this.experience = experience;
			this.respawnTime = respawnTime;
			this.message = message;
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
		 * Constructs a new {@link StallTask}.
		 * @param stall the stall being used.
		 * @param object the stall's object node.
		 */
		StallTask(Stalls stall, GameObject object) {
			super(stall.stall.respawnTime, false);
			this.stall = stall;
			this.object = object;
		}

		@Override
		public void onSubmit() {
			GameObject.spawnObjectTemporary(new GameObject(stall.stall.emptyStallId, object.getType(), object.getRotation(), object), stall.stall.respawnTime);
		}

		@Override
		public void execute() {
			this.cancel();
		}

	}

	@Override
	public int getSkillId() {
		return Skills.THIEVING;
	}
}