package com.rs.plugin.impl.objects.region;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtility;

import skills.mining.PickaxeData;
import skills.woodcutting.Hatchet;

@ObjectSignature(objectId = {7151, 7145, 7143, 7153, 7152, 7144, 7148, 7147, 7146, 7150, 7154, 7133, 7132, 7141, 7129, 7130, 7131, 7140, 7139, 7137, 7136, 7135, 7134, 2473, 17010}, name = {})
public class AbyssRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		int id = object.getId();
		if (id == 7133) { // nature rift
            player.setNextWorldTile(new WorldTile(2398, 4841, 0));
        } else if (id == 7132) { // cosmic rift
            player.setNextWorldTile(new WorldTile(2162, 4833, 0));
        } else if (id == 7141) { // blood rift
            player.setNextWorldTile(new WorldTile(2462, 4891, 1));
        } else if (id == 7129) { // fire rift
            player.setNextWorldTile(new WorldTile(2584, 4836, 0));
        } else if (id == 7130) { // earth rift
            player.setNextWorldTile(new WorldTile(2660, 4839, 0));
        } else if (id == 7131) { // body rift
            player.setNextWorldTile(new WorldTile(2527, 4833, 0));
        } else if (id == 7140) { // mind rift
            player.setNextWorldTile(new WorldTile(2794, 4830, 0));
        } else if (id == 7139) { // air rift
            player.setNextWorldTile(new WorldTile(2845, 4832, 0));
        } else if (id == 7137) { // water rift
        	System.out.println("?");
            player.setNextWorldTile(new WorldTile(3482, 4836, 0));
        } else if (id == 7136) { // death rift
            player.setNextWorldTile(new WorldTile(2207, 4836, 0));
        } else if (id == 7135) { // law rift
            player.setNextWorldTile(new WorldTile(2464, 4834, 0));
        } else if (id == 7134) { // chaotic rift
            player.setNextWorldTile(new WorldTile(2269, 4843, 0));
        }
		AbbysalObstacle.forObject(object).handle(player, object);
	}

	/**
	 * Represents an obstacle in an abbsyal.
	 * 
	 * @author 'Vexia
	 * @date 02/11/2013
	 */
	public enum AbbysalObstacle {
		BOIL("burn-down", new WorldTile[] { new WorldTile(3024, 4833, 0), new WorldTile(3053, 4830, 0) }, 7145, 7151) {
			@Override
			public void handle(final Player player, final GameObject object) {
				if (!player.getInventory().containsAny(590)) {
					player.getPackets().sendGameMessage("You don't have a tinderbox to burn it.");
					return;
				}
				player.setNextAnimation(Animations.ATTEMPT_FIRE_LIGHTING);
				player.getMovement().lock(3);
				World.get().submit(new Task(1) {
					int tick;
					@Override
					protected void execute() {
						switch (tick++) {
						case 1:
							player.getPackets().sendGameMessage("You attempt to burn your way through..");
							break;
						case 4:
							if (RandomUtility.random(3) != 1) {
								player.getPackets().sendGameMessage("...and manage to burn it down and get past.");
								player.setNextWorldTile(getLocations()[getIndex(object)]);
								player.setNextAnimation(Animations.RESET_ANIMATION);
								GameObject.removeObjectTemporary(object, 2);
							} else {
								player.getPackets().sendGameMessage("You fail to set it on fire.");
							}
							cancel();
						}
					}
				});
			}
		},
		MINE("mine", new WorldTile[] { new WorldTile(3030, 4821, 0), new WorldTile(3048, 4822, 0) }, 7143, 7153) {
			@Override
			public void handle(final Player player, final GameObject object) {
				for (PickaxeData axes : PickaxeData.values()) {
					if (!player.getInventory().containsAny(axes.getItem().getId())) {
						player.getPackets().sendGameMessage("You need a pickaxe in order to do that.");
						return;
					}
				}
				player.setNextAnimation(Animations.BASIC_MINING);
				player.getMovement().lock(3);
				World.get().submit(new Task(1) {
					int tick;
					@Override
					protected void execute() {
						switch (tick++) {
						case 1:
							player.getPackets().sendGameMessage("You attempt to mine your way through..");
							break;
						case 4:
							if (RandomUtility.random(3) != 1) {
								player.getPackets().sendGameMessage("...and manage to break through the rock.");
								player.setNextWorldTile(getLocations()[getIndex(object)]);
								player.setNextAnimation(Animations.RESET_ANIMATION);
								GameObject.removeObjectTemporary(object, 2);
							} else {
								player.getPackets().sendGameMessage("...but fail to break-up the rock.");
							}
							cancel();
						}
					}
				});
			}
		},
		CHOP("chop", new WorldTile[] { new WorldTile(3050, 4824, 0), new WorldTile(3028, 4824, 0) }, 7152, 7144) {
			@Override
			public void handle(final Player player, final GameObject object) {
				if(Hatchet.getDefinition(player).orElse(null) == null) {
					player.getPackets().sendGameMessage("You need a hatchet in order to do that.");
					return;
				}
				player.setNextAnimation(Animations.BASIC_WOODCUTTING);
				player.getMovement().lock(3);
				World.get().submit(new Task(1) {
					int tick;
					@Override
					public void execute() {
						switch (tick++) {
						case 1:
							player.getPackets().sendGameMessage("You attempt to chop your way through...");
							break;
						case 4:
							if (RandomUtility.random(3) != 1) {
								player.getPackets().sendGameMessage("...and manage to chop down the tendrils.");
								player.setNextWorldTile(getLocations()[getIndex(object)]);
								player.setNextAnimation(Animations.RESET_ANIMATION);
								GameObject.removeObjectTemporary(object, 2);
							} else {
								player.getPackets().sendGameMessage("You fail to cut through the tendrils.");
							}
							cancel();
						}
						return;
					}
				});
			}
		},
		SQUEEZE("squeeze-through", new WorldTile[] { new WorldTile(3048, 4842, 0), new WorldTile(3031, 4842, 0) }, 7148, 7147) {
			@Override
			public void handle(final Player player, final GameObject object) {
				player.setNextAnimation(Animations.BEING_SQUEEZE_THROUGH);
				player.getMovement().lock(3);
				player.getMovement().lock(3);
				World.get().submit(new Task(1) {
					int tick;
					@Override
					public void execute() {
						switch (tick++) {
						case 1:
							player.getPackets().sendGameMessage("You attempt to squeeze through the narrow gap...");
							break;
						case 2:
							player.getPackets().sendGameMessage("...and you manage to crawl through.");
							player.setNextWorldTile(getLocations()[getIndex(object)]);
							player.setNextAnimation(Animations.RESET_ANIMATION);
							cancel();
							break;
						}
					}
				});
			}
		},
		DISTRACT("distract", new WorldTile[] { new WorldTile(3029, 4841, 0), new WorldTile(3051, 4838, 0) }, 7146,
				7150) {
			@Override
			public void handle(final Player player, final GameObject object) {
				int[] emotes = { 855, 856, 857, 858, 859, 860, 861, 862, 863, 864, 865, 866, 2113, 2109, 2111, 2106,
						2107, 2108, 0x558, 2105, 2110, 2112, 0x84F, 0x850, 1131, 1130, 1129, 1128, 1745, 3544, 3543,
						2836 };
				int index = RandomUtility.random(emotes.length);
				player.setNextAnimation(new Animation(emotes[index]));
				player.getMovement().lock(3);
				World.get().submit(new Task(1) {
					int tick;
					@Override
					public void execute() {
						switch (tick++) {
						case 1:
							player.getPackets()
									.sendGameMessage("You use your thieving skills to misdirect the eyes...");
							break;
						case 4:
							if (RandomUtility.random(3) != 1) {
								player.getPackets().sendGameMessage("...and sneak past while they're not looking.");
								player.setNextWorldTile(getLocations()[getIndex(object)]);
								player.setNextAnimation(Animations.RESET_ANIMATION);
							} else {
								player.getPackets().sendGameMessage("You fail to distract the eyes.");
							}
							cancel();
						}
					}
				});
			}
		},
		PASSAGE("go-through", new WorldTile[] { new WorldTile(3040, 4844, 0) }, 7154) {
			@Override
			public void handle(final Player player, final GameObject object) {
				player.setNextWorldTile(getLocations()[0]);
			}
		};
		;

		/**
		 * Constructs a new {@code RunecraftingOptionPlugin} {@code Object}.
		 * 
		 * @param locations the locations.
		 * @param objects   the objects.
		 */
		AbbysalObstacle(final String option, WorldTile[] locations, int... objects) {
			this.option = option;
			this.objects = objects;
			this.locations = locations;
			this.option = option;
		}

		/**
		 * Represents the option.
		 */
		private String option;

		/**
		 * Represents the corssing WorldTile.
		 */
		private final WorldTile[] locations;

		/**
		 * Represents the object id.
		 */
		private final int[] objects;

		/**
		 * Gets the locations.
		 * 
		 * @return The locations.
		 */
		public WorldTile[] getLocations() {
			return locations;
		}

		/**
		 * Gets the objects.
		 * 
		 * @return The objects.
		 */
		public int[] getObjects() {
			return objects;
		}

		/**
		 * Method used to get the abbysal obstacle.
		 * 
		 * @param object the object.
		 * @return the <code>AbbysalObstacle</code> or <code>Null</code>.
		 */
		public static AbbysalObstacle forObject(final GameObject object) {
			for (AbbysalObstacle obstacle : values()) {
				for (int i : obstacle.getObjects()) {
					if (i == object.getId()) {
						return obstacle;
					}
				}
			}
			return null;
		}

		/**
		 * Method used to get the index.
		 * 
		 * @param object the object.
		 * @return the index.
		 */
		public int getIndex(final GameObject object) {
			for (int i = 0; i < objects.length; i++) {
				if (getObjects()[i] == object.getId()) {
					return i;
				}
			}
			return 0;
		}

		/**
		 * Methhod used to handle the obstacle.
		 * 
		 * @param player the player.
		 * @param object the object.
		 */
		public void handle(final Player player, final GameObject object) {

		}

		/**
		 * Gets the option.
		 * 
		 * @return The option.
		 */
		public String getOption() {
			return option;
		}
	}
}