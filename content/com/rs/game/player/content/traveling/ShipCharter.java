package com.rs.game.player.content.traveling;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.utilities.TextUtils;
import com.rs.utilities.Utility;
/**
 * Represents a class used to charter ships.
 * @author 'Vexia
 * 
 */
public class ShipCharter {


	/**
	 * Represents the ring of charos item.
	 */
	private final static Item RING_OF_CHAROS = new Item(6465);

	/**
	 * Method used to open the ship chartering interface.
	 * @param player the player.
	 */
	public static void open(final Player player) {
		player.getInterfaceManager().sendInterface(95);
	}

	/**
	 * Method used to handle a ship charter.
	 * @param player the player.
	 * @param button the button.
	 */
	public static void handle(final Player player, final int button) {
		Destination destination = Destination.forButton(button);
		if (destination == null){
			return;
		}
		final int cost = getCost(player, destination);
		player.getInterfaceManager().closeInterfaces();
		player.dialogue(new DialogueEventListener(player) {
			@Override
			public void start() {
				option("Yes, pay fare of " + Utility.getFormattedNumber(cost) + "gp.", () -> {
					if (!player.getInventory().canPay(cost)) {
						player.getPackets().sendGameMessage("You do not have enough money to pay for that.");
					} else
						destination.sail(player);
				}, "Nevermind", () -> {
					complete();
				});
			}
		});
	}

	/**
	 * Method used to get the cost of the charter.
	 * @param player the player.
	 * @param destination the destination.
	 * @return the cost.
	 */
	public final static int getCost(final Player player, Destination destination) {
		int cost = destination.getCost(player, destination);
		if (player.getEquipment().containsAny(RING_OF_CHAROS.getId())) {//TODO: cabin fever quest.
			cost -= Math.round((cost / 2));
		} 
		return cost;
	}

	/**
	 * Represents the destination to travel to.
	 * @author 'Vexia
	 * @date 28/11/2013
	 */
	public enum Destination {
		CATHERBY(new WorldTile(2792, 3417, 1), 25, new int [] {480, -1, 480, 625, 1600, 3250, 1000, 1600, 3200, 3400}, new WorldTile(2797, 3414, 0), 3, 14),
		PORT_PHASMATYS(new WorldTile(3705, 3503, 1), 24, new int [] {3650, 3250, 1850, -1, -1, -1, 2050, 1850, 3200, 1100}, new WorldTile(3702, 3502, 0), 2, 13),
		CRANDOR(new WorldTile(2792, 3417, 1), 32, new int [] {-1, 480, 480, 925, 400, 3650, 1600, 400, 3200, 3800}, null, 10, 21),
		BRIMHAVEN(new WorldTile(2763, 3238,1), 28, new int [] {-1, 480, 480, 925, 400, 3650, 1600, 400, 3200, 3800}, new WorldTile(2760, 3238, 0), 6, 17),
		PORT_SARIM(new WorldTile(3038, 3189, 1), 30, new int [] {1600, 1000, -1, 325, 1280, 650, 1280, 400, 3200, 1400}, new WorldTile(3039, 3193, 0), 8, 19),
		PORT_TYRAS(new WorldTile(2142, 3122, 0), 23, new int [] {3200, 3200, 3200, 1600, 3200, 3200, 3200, 3200, -1, 3200}, new WorldTile(2143, 3122, 0), 1, 12),
		KARAMJA(new WorldTile(2957, 3158, 1), 27, new int [] {200, 480, -1, 225, 400, 1850, -1, 200, 3200, 2000}, new WorldTile(2954, 3156, 0), 5, 16),
		PORT_KHAZARD(new WorldTile(2674, 3141, 1), 29, new int [] {1600, 1000, -1, 325, 180, 650, 1280, 400, 3200, 1400}, new WorldTile(2674, 3144, 0), 7, 18),
		SHIPYARD(new WorldTile(3001, 3032, 0), 26, new int [] {400, 1600, 200, 225, 720, 1850, 400, -1, 3200, 900}, new WorldTile(3001, 3032, 0), 4, 15),
		OO_GLOG(new WorldTile(2623, 2857, 0), 33, new int [] {300, 3400, 2000, 550, 5000, 2800, 1400, 900, 3200, -1}, new WorldTile(2622, 2857, 0), 11, 22),
		MOS_LE_HARMLESS(new WorldTile(3671, 2931, 0), 31, new int [] {725, 625, 1025, -1, 1025, -1, 325, 275, 1600, 500}, new WorldTile(3671, 2933, 0), 9, 20);

		/**
		 * Constructs a new {@code ShipCharter} {@code Object}.
		 * @param WorldTile the WorldTile.
		 * @param button the button.
		 * @param money the money.
		 * @param base the base.
		 * @param childs the childs.
		 */
		Destination(WorldTile WorldTile, int button, int[] costs, WorldTile base, int...components) {
			this.WorldTile = WorldTile;
			this.button = button;
			this.costs = costs;
			this.base = base;
			this.childs = components;
		}

		/**
		 * Represents the WorldTile of the destination.
		 */
		private final WorldTile WorldTile;

		/**
		 * Represents the button of the destination.
		 */
		private final int button;

		/**
		 * Represents the costs from destinationt o destination.
		 */
		private final int costs[];

		/**
		 * Represents the base WorldTile(how we find where we're at)
		 */
		private WorldTile base;

		/**
		 * Represents the childs on the screen.
		 */
		private final int childs[];

		/**
		 * Gets the WorldTile.
		 * @return The WorldTile.
		 */
		public WorldTile getWorldTile() {
			return WorldTile;
		}

		/**
		 * Gets the button.
		 * @return The button.
		 */
		public int getButton() {
			return button;
		}

		/**
		 * Gets the moneys.
		 * @return The moneys.
		 */
		public int[] getCosts() {
			return costs;
		}

		/**
		 * Gets the base.
		 * @return The base.
		 */
		public WorldTile getBase() {
			return base;
		}

		/**
		 * Gets the childs.
		 * @return The childs.
		 */
		public int[] getComponents() {
			return childs;
		}

		/**
		 * Gets the x component.
		 * @return the component.
		 */
		public int getXChild() {
			return childs[0];
		}

		/**
		 * Gets the name component.
		 * @return the component.
		 */
		public int getNameChild() {
			return childs[1];
		}


		/**
		 * Gets the cost of chartering a ship.
		 * @param player the player.
		 * @return the cost.
		 */
		public int getCost(final Player player, final Destination destination) {
			final Destination current = Destination.getFromBase(player);
			if (current == null) {
				return 0;
			}
			final Destination[] costTable = new Destination[] {BRIMHAVEN, CATHERBY, KARAMJA, MOS_LE_HARMLESS, PORT_KHAZARD, PORT_PHASMATYS, PORT_SARIM, SHIPYARD, PORT_TYRAS, OO_GLOG};
			int index = 0;
			for (int i = 0; i < costTable.length; i++) {
				if (costTable[i] == destination) {
					index = i;
					break;
				}
			}
			return current.getCosts()[index];
		}

		/**
		 * Method used to get the destination from the base.
		 * @param WorldTile the WorldTile.
		 * @return the destination.
		 */
		public static Destination getFromBase(WorldTile WorldTile) {
			for (Destination destination : Destination.values()) {
				if (destination.getBase() == null) {
					System.out.println("based null");
					continue;
				}
				if (destination.getBase().getDistance(WorldTile) < 30) {
					return destination;
				}
			}
			return null;
		}
		/**
		 * Method used to get the destination for the button id.
		 * @param button the button.
		 * @return the destination.
		 */
		public static Destination forButton(final int button) {
			for (Destination destination : values()) {
				if (destination.getButton() == button) {
					return destination;
				}
			}
			return null;
		}

		public void sail(Player player) {
			player.getMovement().lock();
			LinkedTaskSequence seq = new LinkedTaskSequence();
			seq.connect(1, () -> {
				player.getPackets().sendBlackOut(2);
				FadingScreen.fade(player);
			});
			seq.connect(4, () -> {
				player.setNextWorldTile(getWorldTile());
				player.getMovement().unlock();
				player.getInterfaceManager().closeInterfaces();
				player.getPackets().sendBlackOut(0);
				player.getPackets().sendGameMessage("You pay the fare and sail to " + TextUtils.formatEnumNaming(name()) + ".");
			}).start();
		}
	}
}