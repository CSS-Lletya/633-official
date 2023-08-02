package com.rs.plugin.impl.objects;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtility;

@ObjectSignature(objectId = {}, name = {"crate", "crates", "boxes", "bookcase", "drawers", "closed chest", "open chest"})
public class SearchablesObjectPlugin extends ObjectListener {

	GameObject interacting;
	
	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		interacting = object;
		object.doAction(optionId, object.getId(), "search", () -> player.getPackets().sendGameMessage(
                "You search the " + object.getDefinitions().getName().toLowerCase() + " but find nothing."));
		
		if (object.getDefinitions().getNameContaining("drawers")) {
			object.doAction(optionId, object.getId(), "open", () -> {
				interacting = new GameObject(getOpenId(object.getId()), object.getType(),
                        object.getRotation(), object.getX(), object.getY(), object.getPlane());
                player.faceObject(interacting);
                player.getAudioManager().sendSound(Sounds.DRAWER_OPEN);
                player.getMovement().lock(2);
                GameObject.spawnObjectTemporary(interacting, 60);
                player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
			object.doAction(optionId, object.getId(), "close", () -> {
				interacting = new GameObject(getCloseId(object.getId()), object.getType(), object.getRotation(),
						object.getX(), object.getY(), object.getPlane());
				player.faceObject(interacting);
				player.getAudioManager().sendSound(Sounds.DRAWER_CLOSED);
                player.getMovement().lock(2);
				GameObject.removeObject(interacting);
				player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
		}
		if (object.getDefinitions().getNameContaining("closed chest") || object.getDefinitions().getNameContaining("open chest")) {
			object.doAction(optionId, object.getId(), "open", () -> {
				boolean nextItemChest = ObjectDefinitions.getObjectDefinitions(object.getId() + 1).getName().contains("chest");
				if (!nextItemChest)
					return;
				if (object.getId() == 172) {
					if (!player.getInventory().containsAny(989)) {
						player.getPackets().sendGameMessage("This chest is securely locked shut.");
						return;
					}
					player.getInventory().deleteItem(CRYSTAL_KEY);
					Reward reward = Reward.getReward(player);
					for (Item i : reward.getItems()) {
						if (!player.getInventory().addItem(i))
							FloorItem.updateGroundItem(i, player, player);
					}
					player.getPackets().sendGameMessage("You unlock the chest with your key.");
					player.getPackets().sendGameMessage("You find some teasure in the chest!");
				}
				interacting = new GameObject(getOpenId(object.getId()), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane());
				player.faceObject(interacting);
				player.getAudioManager().sendSound(Sounds.CHEST_OPEN);
                player.getMovement().lock(2);
                GameObject.spawnObjectTemporary(interacting, (object.getId() == 172 ? 2 : 60));
                player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
			object.doAction(optionId, object.getId(), "shut", () -> {
				interacting = new GameObject(getCloseId(object.getId()), object.getType(), object.getRotation(),
						object.getX(), object.getY(), object.getPlane());
				player.faceObject(interacting);
				player.getAudioManager().sendSound(Sounds.CHEST_CLOSED);
                player.getMovement().lock(2);
				GameObject.removeObject(interacting);
				player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
		}
	}
	
	/**
	 * Represents the cyrstal key.
	 */
	private static final Item CRYSTAL_KEY = new Item(989);
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == CRYSTAL_KEY.getId() && object.getId() == 172)
			execute(player, object, 1);
	}
	
    private int getOpenId(int objectId) {
        return objectId + 1;
    }
    
    private int getCloseId(int objectId) {
        return objectId - 2;
    }
    
	/**
	 * Represents a crystal ches reward.
	 * 
	 * @author 'Vexia
	 */
	public enum Reward {
		FIRST(39.69, new Item(1631, 1), new Item(1969, 1), new Item(995, 2000)), SECOND(16.72, new Item(1631, 1)),
		THIRD(10.57, new Item(1631, 1), new Item(371, 5), new Item(995, 1000)),
		FOURTH(7.73, new Item(1631, 1), new Item(556, 50), new Item(555, 50), new Item(557, 50), new Item(554, 50),
				new Item(559, 50), new Item(558, 50), new Item(565, 10), new Item(9075, 10), new Item(566, 10)),
		FIFTH(6.55, new Item(1631, 1), new Item(454, 100)),
		SIXTH(4.23, new Item(1631, 1), new Item(1603, 2), new Item(1601, 2)),
		SEVENTH(3.67, new Item(1631, 1), new Item(985, 1), new Item(995, 750)),
		EIGHT(3.51, new Item(1631, 1), new Item(2363, 3)),
		NINTH(3.26, new Item(1631, 1), new Item(987, 1), new Item(995, 750)),
		TENTH(2.75, new Item(1631, 1), new Item(441, 150)), ELEVENTH(1.06, new Item(1631, 1), new Item(1183, 1)),
		TWELFTH(0.26, new Item(1631, 1), new Item(1079, 1)), // FOR MALES!
		TWELFTH_FEMALE(0.26, new Item(1631, 1), new Item(1093, 1)); // FOR SHEMALES(Female)

		/**
		 * Represents the item rewards.
		 */
		private final Item[] items;

		/**
		 * Represents the chance of getting the item.
		 */
		private final double chance;

		/**
		 * Constructs a new {@code CrystalChestPlugin} {@code Object}.
		 * 
		 * @param chance the chance.
		 * @param items  the item.
		 */
		Reward(double chance, Item... items) {
			this.chance = chance;
			this.items = items;
		}

		/**
		 * Gets the reward.
		 * 
		 * @param player the player.
		 * @return the reward.
		 */
		public static Reward getReward(final Player player) {
			int totalChance = 0;
			for (Reward r : values()) {
				if (r == Reward.TWELFTH_FEMALE && player.getAppearance().isMale()) {
					continue;
				}
				totalChance += r.getChance();
			}
			final int random = RandomUtility.random(totalChance);
			int total = 0;
			for (Reward r : values()) {
				total += r.getChance();
				if (random < total) {
					return r;
				}
			}
			return null;
		}

		/**
		 * Gets the items.
		 * 
		 * @return The items.
		 */
		public Item[] getItems() {
			return items;
		}

		/**
		 * Gets the chance.
		 * 
		 * @return The chance.
		 */
		public double getChance() {
			return chance;
		}
	}
}