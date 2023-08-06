package com.rs.game.player;

import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.item.ItemWeights;
import com.rs.game.item.ItemsContainer;

public final class Equipment {

	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_WEAPON = 3, SLOT_CHEST = 4,
			SLOT_SHIELD = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13,
			SLOT_AURA = 14;

	private ItemsContainer<Item> items;

	private transient Player player;
	private transient double equipmentWeight;

	static final int[] DISABLED_SLOTS = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 };

	public Equipment() {
		items = new ItemsContainer<Item>(15, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		player.getPackets().sendItems(94, items);
		refresh(null);
	}

	public void checkItems() {
		for (int i = 0; i < items.getSize(); i++) {
			Item item = items.get(i);
			if (item == null)
				continue;
			if (!ItemConstants.canWear(item, player)) {
				items.set(i, null);
				player.getInventory().addItemDrop(item.getId(), item.getAmount());
			}
		}
		if (player.getDetails().getQuestPoints().get() != GameConstants.TOTAL_QUEST_POINTS
				&& player.getEquipment().containsAny(ItemNames.QUEST_POINT_CAPE_9813, ItemNames.QUEST_POINT_HOOD_9814)
				&& player.getDetails().getRights() != Rights.ADMINISTRATOR) {
			player.getPackets().sendGameMessage(
                    "One or more Quests has been released, please complete them to continue wearing your Quest Cape.");
			player.getEquipment().getItems().set(Equipment.SLOT_CAPE, null);
			player.getEquipment().refresh(Equipment.SLOT_CAPE);
			player.getAppearance().generateAppearenceData();
			player.getInventory().addOrBank(new Item(9813));
        }
	}

	public void refresh(int... slots) {
		if (slots != null) {
			player.getPackets().sendUpdateItems(94, items, slots);
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		double w = 0;
		for (Item item : items.getItems()) {
			if (item == null)
				continue;
			w += ItemWeights.getWeight(item, true);
		}
		equipmentWeight = w;
		player.getPackets().sendWeight();
	}

	public void reset() {
		items.reset();
		init();
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public void sendExamine(int slotId) {
		Item item = items.get(slotId);
		if (item == null)
			return;
//		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public boolean containsOneItem(int... itemIds) {
		for (int itemId : itemIds) {
			if (items.containsOne(new Item(itemId, 1)))
				return true;
		}
		return false;
	}
	
	public boolean contains(Item[] itemList) {
		for (Item item : itemList) {
			if (items.contains(item))
				return true;
		}
		return false;
	}

	public static boolean hideArms(Item item) {
		  String name = item.getName().toLowerCase(); if(name.contains("d'hide body") ||
		  name.contains("dragonhide body") || name.equals("stripy pirate shirt") ||
		  (name.contains("chainbody") && (name.contains("iron") ||
		  name.contains("bronze") || name.contains("steel") || name.contains("black")
		  || name.contains("mithril") || name.contains("adamant") ||
		  name.contains("rune") || name.contains("white"))) ||
		  name.equals("leather body") || name.equals("hardleather body") ||
		  name.contains("studded body")) return false;
		 
		return item.getDefinitions().getEquipType() == 6;
	}

	public static boolean hideHair(Item item) {
		return item.getDefinitions().getEquipType() == 8;
	}

	public static boolean showBeard(Item item) {
		String name = item.getName().toLowerCase();
		return !hideHair(item) || name.contains("horns") || name.contains("hat") || name.contains("afro")
				|| name.contains("cowl") || name.contains("tattoo") || name.contains("headdress")
				|| name.contains("hood") || (name.contains("mask") && !name.contains("h'ween"))
				|| (name.contains("helm") && !name.contains("full")) || name.contains("chicken")
				|| name.contains("coif") || name.contains("antler") || name.contains("snelm") 
				|| name.contains("bandana");
	}

	public static int getItemSlot(int itemId) {
		//karam vessel you can wear haha, if you want to force slot its here you go
		if (IntStream.of(3157).anyMatch(id -> itemId == id))
			return -1;
		return ItemDefinitions.getItemDefinitions(itemId).getEquipSlot();
	}

	public static boolean isTwoHandedWeapon(Item item) {
		return item.getDefinitions().getEquipType() == 5;
	}

	public int getWeaponRenderEmote() {
		Item weapon = items.get(3);
		if (weapon == null)
			return 1426;
		return weapon.getDefinitions().getRenderAnimId();
	}

	public boolean hasShield() {
		return items.get(5) != null;
	}

	public int getWeaponId() {
		Item item = items.get(SLOT_WEAPON);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getChestId() {
		Item item = items.get(SLOT_CHEST);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getHatId() {
		Item item = items.get(SLOT_HAT);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getShieldId() {
		Item item = items.get(SLOT_SHIELD);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getLegsId() {
		Item item = items.get(SLOT_LEGS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void removeAmmo(int ammoId, int ammount) {
		if (ammount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
			player.getAppearance().generateAppearenceData();
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, ammount));
			refresh(SLOT_ARROWS);
		}
	}

	public int getAuraId() {
		Item item = items.get(SLOT_AURA);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getCapeId() {
		Item item = items.get(SLOT_CAPE);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getRingId() {
		Item item = items.get(SLOT_RING);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getAmmoId() {
		Item item = items.get(SLOT_ARROWS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void deleteItem(int itemId, int amount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public int getBootsId() {
		Item item = items.get(SLOT_FEET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getGlovesId() {
		Item item = items.get(SLOT_HANDS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}
	
	public boolean wearingArmour() {
		return getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null || getItem(SLOT_AMULET) != null
				|| getItem(SLOT_WEAPON) != null || getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null || getItem(SLOT_FEET) != null
				|| getItem(SLOT_AURA) != null;
	}

	public int getAmuletId() {
		Item item = items.get(SLOT_AMULET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public boolean hasTwoHandedWeapon() {
		Item weapon = items.get(SLOT_WEAPON);
		return weapon != null && isTwoHandedWeapon(weapon);
	}
	
	public double getEquipmentWeight() {
		return equipmentWeight;
	}

    public boolean containsAny(int... itemIds) {
        for (int itemId : itemIds) {
            if (items.containsOne(new Item(itemId, 1)))
                return true;
        }
        return false;
    }

	/**
	 * Determines if this container contains all {@code identifiers}.
	 * @param identifiers The identifiers to check this container for.
	 * @return {@code true} if this container has all {@code identifiers}, {@code false} otherwise.
	 */
	public final boolean containsAll(int... identifiers) {
		for(int id : identifiers) {
			boolean found = false;
			for(Item item : items.getItems()) {
				if(item == null)
					continue;
				if(item.getId() == id) {
					found = true;
					break;
				}
			}
			if(!found)
				return false;
		}
		return true;
	}
}
