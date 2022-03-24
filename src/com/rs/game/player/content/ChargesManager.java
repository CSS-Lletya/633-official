package com.rs.game.player.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class ChargesManager {

	private transient Player player;
	
	private Object2ObjectOpenHashMap<Integer, Integer> charges = new Object2ObjectOpenHashMap<>();

	public ChargesManager() {
		charges = new Object2ObjectOpenHashMap<Integer, Integer>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		Item[] items = player.getEquipment().getItems().getItems();
		for (int slot = 0; slot < items.length; slot++) {
			Item item = items[slot];
			if (item == null)
				continue;
			if (player.getAttackedByDelay() > Utility.currentTimeMillis()) {
				int newId = ItemConstants.getDegradeItemWhenCombating(item
						.getId());
				if (newId != -1) {
					item.setId(newId);
					player.getEquipment().refresh(slot);
					player.getAppearance().generateAppearenceData();
					player.getPackets().sendGameMessage(
							"Your " + item.getDefinitions().getName()
									+ " degraded.");
				}
			}
			int defaultCharges = ItemConstants.getItemDefaultCharges(item
					.getId());
			if (defaultCharges == -1)
				continue;
			if (ItemConstants.itemDegradesWhileWearing(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
			else if (player.getAttackedByDelay() > Utility.currentTimeMillis()
					&& ItemConstants.itemDegradesWhileCombating(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
		}
	}

	public void die() {
		die(null, null);
	}

	public void die(Integer[] slots, Integer[] slots2) {
		Item[] equipItems = player.getEquipment().getItems().getItems();
		Item[] invItems = player.getInventory().getItems().getItems();

		if (slots == null) {
			for (int slot = 0; slot < equipItems.length; slot++) {
				if (equipItems[slot] != null
						&& degradeCompletly(equipItems[slot]))
					player.getEquipment().getItems().set(slot, null);
			}
			for (int slot = 0; slot < invItems.length; slot++) {
				if (invItems[slot] != null && degradeCompletly(invItems[slot]))
					player.getInventory().getItems().set(slot, null);
			}
		} else {
			for (int slot : slots) {
				if (slot >= 16) {
					if (invItems[slot - 16] != null
							&& degradeCompletly(invItems[slot - 16]))
						player.getInventory().getItems().set(slot - 16, null);
				} else {
					if (equipItems[slot - 1] != null
							&& degradeCompletly(equipItems[slot - 1]))
						player.getEquipment().getItems().set(slot - 1, null);
				}
			}
			for (int slot : slots2) {
				if (slot >= 16) {
					if (invItems[slot - 16] != null
							&& degradeCompletly(invItems[slot - 16]))
						player.getInventory().getItems().set(slot - 16, null);
				} else {
					if (equipItems[slot - 1] != null
							&& degradeCompletly(equipItems[slot - 1]))
						player.getEquipment().getItems().set(slot - 1, null);
				}
			}
		}
	}

	public static final String REPLACE = "##";

	public void checkPercentage(String message, int id, boolean reverse) {
		int charges = getCharges(id);
		int maxCharges = ItemConstants.getItemDefaultCharges(id);
		int percentage = reverse ? (charges == 0 ? 0
				: (100 - (charges * 100 / maxCharges))) : charges == 0 ? 100
				: (charges * 100 / maxCharges);
		player.getPackets().sendGameMessage(
				message.replace(REPLACE, String.valueOf(percentage)));
	}

	public void checkCharges(String message, int id) {
		player.getPackets().sendGameMessage(
				message.replace(REPLACE, String.valueOf(getCharges(id))));
	}

	public int getCharges(int id) {
		Integer c = charges.get(id);
		return c == null ? 0 : c;
	}

	/*
	 * -1 inv
	 */
	public void addCharges(int id, int amount, int wearSlot) {
		int maxCharges = ItemConstants.getItemDefaultCharges(id);
		if (maxCharges == -1) {
			System.out.println("This item cant get charges atm " + id);
			return;
		}
		Integer c = charges.get(id);
		int amt = c == null ? amount : amount + c;
		if (amt > maxCharges)
			amt = maxCharges;
		if (amt <= 0) {
			int newId = ItemConstants.getItemDegrade(id);
			if (newId == -1) {
				if (wearSlot == -1)
					player.getInventory().deleteItem(id, 1);
				else
					player.getEquipment().getItems().set(wearSlot, null);
			} else if (wearSlot == -1) {
				player.getInventory().deleteItem(id, 1);
				player.getInventory().addItem(newId, 1);
			} else {
				Item item = player.getEquipment().getItem(wearSlot);
				if (item == null)
					return;
				item.setId(newId);
				player.getEquipment().refresh(wearSlot);
				player.getAppearance().generateAppearenceData();
			}
			resetCharges(id);
		} else
			charges.put(id, amt);
	}

	public void resetCharges(int id) {
		charges.remove(id);
	}

	/*
	 * return disapear;
	 */
	public boolean degradeCompletly(Item item) {
		int defaultCharges = ItemConstants.getItemDefaultCharges(item.getId());
		if (defaultCharges == -1)
			return false;
		while (true) {
			if (ItemConstants.itemDegradesWhileWearing(item.getId())
					|| ItemConstants.itemDegradesWhileCombating(item.getId())) {
				charges.remove(item.getId());
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId == -1)
					return ItemConstants.getItemDefaultCharges(item.getId()) == -1 ? false
							: true;
				item.setId(newId);
			} else {
				int newId = ItemConstants.getItemDegrade(item.getId());
				if (newId != -1) {
					charges.remove(item.getId());
					item.setId(newId);
				}
				break;
			}
		}
		return false;
	}

	public void wear(int slot) {
		Item item = player.getEquipment().getItems().get(slot);
		if (item == null)
			return;
		int newId = ItemConstants.getDegradeItemWhenWear(item.getId());
		if (newId == -1)
			return;
		player.getEquipment().getItems().set(slot, new Item(newId, 1));
		player.getEquipment().refresh(slot);
		player.getAppearance().generateAppearenceData();
		player.getPackets().sendGameMessage(
				"Your " + item.getDefinitions().getName() + " degraded.");
	}

	private void degrade(int itemId, int defaultCharges, int slot) {
		Integer c = charges.remove(itemId);
		if (c == null)
			c = defaultCharges;
		else {
			c--;
			if (c == 0) {
				int newId = ItemConstants.getItemDegrade(itemId);
				player.getEquipment().getItems()
						.set(slot, newId != -1 ? new Item(newId, 1) : null);
				if (newId == -1)
					player.getPackets().sendGameMessage(
							"Your "
									+ ItemDefinitions
											.getItemDefinitions(itemId)
											.getName() + " became into dust.");
				else
					player.getPackets().sendGameMessage(
							"Your "
									+ ItemDefinitions
											.getItemDefinitions(itemId)
											.getName() + " degraded.");
				player.getEquipment().refresh(slot);
				player.getAppearance().generateAppearenceData();
				return;
			}
		}
		charges.put(itemId, c);
	}

}
