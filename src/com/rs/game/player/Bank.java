package com.rs.game.player;

import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.InterfaceVars;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.attribute.Attribute;
import com.rs.utilities.ItemExamines;

@SuppressWarnings("all")
public class Bank {

	// tab, items
	public Item[][] bankTabs;
	private int lastX;

	/**
	 * Bankpin related code.
	 */
	
	private long recoveryDelay, activatedTime, lockPunishment;
	private byte[] actualPin, requestedPin;
	private transient byte stage;

	private transient Player player;
	private transient int currentTab;
	private transient Item[] lastContainerCopy;
	private transient boolean withdrawNotes;
	private transient boolean insertItems;
	private transient int total_size = 0;

	private static final long MAX_BANK_SIZE = 506;

	public Bank() {
		bankTabs = new Item[1][0];
	}
	
    public void refreshTotalSize() {
        if (total_size < 0) {
            total_size = 0;
        }
        int usedFreeSlots = total_size > 403 ? 403 : total_size;
        player.getPackets().sendGlobalConfig(1038, usedFreeSlots);
        player.getPackets().sendGlobalConfig(192, total_size + usedFreeSlots);
        player.getPackets().sendGlobalConfig(638, usedFreeSlots);

    }
    

	public void removeItem(int id) {
		if (bankTabs != null) {
			for (int i = 0; i < bankTabs.length; i++) {
				for (int i2 = 0; i2 < bankTabs[i].length; i2++) {
					if (bankTabs[i][i2].getId() == id)
						bankTabs[i][i2].setId(0); // dwarf remains
				}
			}
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		if (bankTabs == null || bankTabs.length == 0)
			bankTabs = new Item[1][0];
	}

	@SuppressWarnings("null")
	public void setItem(int slotId, int amt) {
		Item item = getItem(slotId);
		if (item == null) {
			item.setAmount(amt);
			refreshItems();
			refreshTabs();
			refreshViewingTab();
			refreshTotalSize();
		}
	}

	public void refreshTabs() {
		for (int slot = 1; slot < 9; slot++)
			refreshTab(slot);
	}

	public int getTabSize(int slot) {
		if (slot >= bankTabs.length)
			return 0;
		return bankTabs[slot].length;
	}

	public void withdrawLastAmount(int bankSlot) {
		withdrawItem(bankSlot, lastX);
	}

	public void withdrawItemButOne(int fakeSlot) {
		int[] fromRealSlot = getRealSlot(fakeSlot);
		Item item = getItem(fromRealSlot);
		if (item == null)
			return;
		if (item.getAmount() <= 1) {
			player.getPackets().sendGameMessage(
					"You only have one of this item in your bank");
			return;
		}
		withdrawItem(fakeSlot, item.getAmount() - 1);
	}

	public void depositLastAmount(int bankSlot) {
		depositItem(bankSlot, lastX, true);
	}

	public void depositAllInventory(boolean banking) {
		if (Bank.MAX_BANK_SIZE - getBankSize() < player.getInventory()
				.getItems().getSize()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your bank.");
			return;
		}
		for (int i = 0; i < 28; i++)
			depositItem(i, Integer.MAX_VALUE, false);
		refreshTab(currentTab);
		refreshItems();
		 if (banking)
	            refreshTotalSize();
		 player.getDetails().getStatistics().addStatistic("Items_Deposited");
	}

	public void depositAllBob(boolean banking) {
		Familiar familiar = player.getFamiliar();
		if (familiar == null || familiar.getBob() == null)
			return;
		int space = addItems(familiar.getBob().getBeastItems().getItems(),
				banking);
		if (space != 0) {
			for (int i = 0; i < space; i++)
				familiar.getBob().getBeastItems().set(i, null);
			familiar.getBob().sendInterItems();
		}
		if (space < familiar.getBob().getBeastItems().getSize()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your bank.");
			return;
		}
		player.getDetails().getStatistics().addStatistic("Items_Deposited");
		 if (banking)
	            refreshTotalSize();
	}

	public void depositAllEquipment(boolean banking) {
		int space = addItems(player.getEquipment().getItems().getItems(),
				banking);
		if (space != 0) {
			for (int i = 0; i < space; i++)
				player.getEquipment().getItems().set(i, null);
			player.getEquipment().init();
			player.getAppearance().generateAppearenceData();
		}
		if (space < player.getEquipment().getItems().getSize()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your bank.");
			return;
		}
		player.getDetails().getStatistics().addStatistic("Items_Deposited");
	    refreshTotalSize();
	}

	public void collapse(int tabId) {
		if (tabId == 0 || tabId >= bankTabs.length)
			return;
		Item[] items = bankTabs[tabId];
		for (Item item : items)
			removeItem(getItemSlot(item.getId()), item.getAmount(), false, true);
		for (Item item : items)
			addItem(item.getId(), item.getAmount(), 0, false);
		refreshTabs();
		refreshItems();
	}

	public void switchItem(int fromSlot, int toSlot, int fromComponentId,
			int toComponentId) {
		if (toSlot == 65535) {
			int toTab = toComponentId >= 74 ? 10 - (84 - toComponentId)
					: 8 - ((toComponentId - 46) / 2);
			System.out.println("BankLength: " + (bankTabs.length) + " toTab: " + toTab);
			if (toTab < 0 || toTab > 9)
				return;
			if (bankTabs.length  == toTab) {
				int[] fromRealSlot = getRealSlot(fromSlot);
				if (fromRealSlot == null)
					return;
				if (toTab == fromRealSlot[0]) {
					switchItem(fromSlot, getStartSlot(toTab));
					return;
				}
				Item item = getItem(fromRealSlot);
				if (item == null)
					return;
				removeItem(fromSlot, item.getAmount(), false, true);
				createTab();
				bankTabs[bankTabs.length - 1] = new Item[] { item };
				refreshTab(fromRealSlot[0]);
				refreshTab(toTab);
				refreshItems();
				refreshTotalSize();
			} else if (bankTabs.length > toTab) {
				int[] fromRealSlot = getRealSlot(fromSlot);
				if (fromRealSlot == null)
					return;
				if (toTab == fromRealSlot[0]) {
					switchItem(fromSlot, getStartSlot(toTab));
					return;
				}
				Item item = getItem(fromRealSlot);
				if (item == null)
					return;
				boolean removed = removeItem(fromSlot, item.getAmount(), false,
						true);
				if (!removed)
					refreshTab(fromRealSlot[0]);
				else if (fromRealSlot[0] != 0 && toTab >= fromRealSlot[0])
					toTab -= 1;
				refreshTab(fromRealSlot[0]);
				addItem(item.getId(), item.getAmount(), toTab, true);
				refreshTotalSize();
			}
		} else
			switchItem(fromSlot, toSlot);
		refreshTotalSize();
	}

	public void switchItem(int fromSlot, int toSlot) {
		int[] fromRealSlot = getRealSlot(fromSlot);
		Item fromItem = getItem(fromRealSlot);
		if (fromItem == null)
			return;
		int[] toRealSlot = getRealSlot(toSlot);
		Item toItem = getItem(toRealSlot);
		if (toItem == null)
			return;
		bankTabs[fromRealSlot[0]][fromRealSlot[1]] = toItem;
		bankTabs[toRealSlot[0]][toRealSlot[1]] = fromItem;
		refreshTab(fromRealSlot[0]);
		if (fromRealSlot[0] != toRealSlot[0])
			refreshTab(toRealSlot[0]);
		refreshItems();
		refreshTotalSize();
	}

	public void openDepositBox() {//here we close shit off
		player.getInterfaceManager().sendInterface(11);
		player.getInterfaceManager().closeInventory();
		player.getInterfaceManager().closeEquipment();
		sendBoxInterItems();
		player.getPackets().sendIComponentText(11, 13,
				"Bank Of " + GameConstants.SERVER_NAME + " - Deposit Box");
		player.setCloseInterfacesEvent(() -> {
			player.getInterfaceManager().sendInventory();
			player.getInventory().unlockInventoryOptions();
			player.getInterfaceManager().sendEquipment();
		});
	}

	public void sendBoxInterItems() {
		player.getPackets().sendInterSetItemsOptionsScript(11, 17, 93, 6, 5,
				"Deposit-1", "Deposit-5", "Deposit-10", "Deposit-All",
				"Deposit-X", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(11, 17, 0, 27, 0,
				1, 2, 3, 4, 5);
	}

	public void openBank() {
		player.getMovement().stopAll();
        if (total_size == 0) {
            for (int i = 0; i < bankTabs.length; i++)
                total_size += getTabSize(i);
        }
		player.getVarsManager().sendVarBit(8348, 0);
		player.getPackets().sendRunScript(2319);
		//script 9
		IntStream.rangeClosed(40, 43).forEach(id -> player.getPackets().sendHideIComponent(762, id, false));
		player.getAttributes().get(Attribute.IS_BANKING).set(true);
		lastContainerCopy = null;
		player.getInterfaceManager().sendInterface(762);
		player.getInterfaceManager().sendInventoryInterface(763);
		refreshViewingTab();
		refreshTabs();
		unlockButtons();
		sendItems();
		refreshLastX();
		refreshTotalSize();
		player.setCloseInterfacesEvent(() -> {
			player.getAttributes().get(Attribute.IS_BANKING).set(false);
		});
	}

	public void refreshLastX() {
		player.getVarsManager().sendVar(InterfaceVars.BANK_LAST_X, lastX);
	}

	public void createTab() {
		int slot = bankTabs.length;
		Item[][] tabs = new Item[slot + 1][];
		System.arraycopy(bankTabs, 0, tabs, 0, slot);
		tabs[slot] = new Item[0];
		bankTabs = tabs;
		total_size++;
	}

	public void destroyTab(int slot) {
		Item[][] tabs = new Item[bankTabs.length - 1][];
		System.arraycopy(bankTabs, 0, tabs, 0, slot);
		System.arraycopy(bankTabs, slot + 1, tabs, slot, bankTabs.length - slot
				- 1);
		bankTabs = tabs;
		if (currentTab != 0 && currentTab >= slot)
			currentTab--;
	}

	public boolean hasBankSpace() {
		return getBankSize() < MAX_BANK_SIZE;
	}
	
	public void deleteItem(int bankSlot) {
		removeItem(bankSlot);
		refreshItems();
	}
	
	public boolean withdrawItemDel(int itemId) {
		boolean refresh = true;
		boolean forceDestroy = true;
		int[] slot = getItemSlot(itemId);
		if (slot == null)
			return false;
		Item item = bankTabs[slot[0]][slot[1]];
		boolean destroyed = false;
		if (bankTabs[slot[0]].length == 1 && (forceDestroy || bankTabs.length != 1)) {
			destroyTab(slot[0]);
			if (refresh)
				refreshTabs();
			destroyed = true;
		}
		return destroyed;
	}
	

    public void forceDeleteItem(int[] slots, int quantity) {
        if (quantity < 1)
            return;
        if (slots == null)
            return;
        Item item = getItem(slots);
        if (item == null)
            return;
        if (item.getAmount() < quantity)
            item = new Item(item.getId(), item.getAmount());
        else
            item = new Item(item.getId(), quantity);
        boolean noted = false;
        ItemDefinitions defs = item.getDefinitions();
        if (withdrawNotes) {
            if (!defs.isNoted() && defs.getCertId() != -1) {
                item.setId(defs.getCertId());
                noted = true;
            } else
                player.getPackets().sendGameMessage("You cannot withdraw this item as a note.");
        }
        if (noted || defs.isStackable()) {
            if (player.getInventory().getItems().containsOne(item)) {
                int slot = player.getInventory().getItems().getThisItemSlot(item);
                Item invItem = player.getInventory().getItems().get(slot);
                if (invItem.getAmount() + item.getAmount() <= 0) {
                    item.setAmount(Integer.MAX_VALUE - invItem.getAmount());
                    player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
                }
            } else if (!player.getInventory().hasFreeSlots()) {
                player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
                return;
            }
        } else {
            int freeSlots = player.getInventory().getFreeSlots();
            if (freeSlots == 0) {
                player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
                return;
            }
            if (freeSlots < item.getAmount()) {
                item.setAmount(freeSlots);
                player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
            }
        }
        removeItem(slots, item.getAmount(), true, false);
    }

	public void withdrawItem(int bankSlot, int quantity) {
		if (quantity < 1)
			return;
		Item item = getItem(getRealSlot(bankSlot));
		if (item == null)
			return;
		if (item.getAmount() < quantity)
			item = new Item(item.getId(), item.getAmount());
		else
			item = new Item(item.getId(), quantity);
		boolean noted = false;
		ItemDefinitions defs = item.getDefinitions();
		if (withdrawNotes) {
			if (!defs.isNoted() && defs.getCertId() != -1) {
				item.setId(defs.getCertId());
				noted = true;
			} else
				player.getPackets().sendGameMessage(
						"You cannot withdraw this item as a note.");
		}
		if (noted || defs.isStackable()) {
			if (player.getInventory().getItems().containsOne(item)) {
				int slot = player.getInventory().getItems()
						.getThisItemSlot(item);
				Item invItem = player.getInventory().getItems().get(slot);
				if (invItem.getAmount() + item.getAmount() <= 0) {
					item.setAmount(Integer.MAX_VALUE - invItem.getAmount());
					player.getPackets().sendGameMessage(
							Inventory.INVENTORY_FULL_MESSAGE);
				}
			} else if (!player.getInventory().hasFreeSlots()) {
				player.getPackets().sendGameMessage(
						Inventory.INVENTORY_FULL_MESSAGE);
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (freeSlots == 0) {
				player.getPackets().sendGameMessage(
						Inventory.INVENTORY_FULL_MESSAGE);
				return;
			}
			if (freeSlots < item.getAmount()) {
				item.setAmount(freeSlots);
				player.getPackets().sendGameMessage(
						Inventory.INVENTORY_FULL_MESSAGE);
			}
		}
		removeItem(bankSlot, item.getAmount(), true, false);
		player.getDetails().getStatistics().addStatistic("Items_Withdrawn");
		player.getInventory().addItem(item);
	}

	public void sendExamine(int fakeSlot) {
		int[] slot = getRealSlot(fakeSlot);
		if (slot == null)
			return;
		Item item = bankTabs[slot[0]][slot[1]];
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void depositItem(int invSlot, int quantity, boolean refresh) {
		if (quantity < 1 || invSlot < 0 || invSlot > 27)
			return;
		Item item = player.getInventory().getItem(invSlot);
		if (item == null)
			return;
		int amt = player.getInventory().getItems().getNumberOf(item);
		if (amt < quantity)
			item = new Item(item.getId(), amt);
		else
			item = new Item(item.getId(), quantity);
		ItemDefinitions defs = item.getDefinitions();
		int originalId = item.getId();
		if (defs.isNoted() && defs.getCertId() != -1)
			item.setId(defs.getCertId());
		Item bankedItem = getItem(item.getId());
		if (bankedItem != null) {
			if (bankedItem.getAmount() + item.getAmount() <= 0) {
				item.setAmount(Integer.MAX_VALUE - bankedItem.getAmount());
				player.getPackets().sendGameMessage(
						"Not enough space in your bank.");
			} else if (bankedItem.getAmount() + item.getAmount() >= Integer.MAX_VALUE) {
				player.getPackets().sendGameMessage(
						"Could not bank your " + item.getName());
				return;
			}
		} else if (!hasBankSpace()) {
			player.getPackets().sendGameMessage(
					"Not enough space in your bank.");
			return;
		}
		if (ItemConstants.canBankItem(player, item)) {
			player.getPackets().sendGameMessage("You cannot bank this item.");
			return;
		}
		player.getInventory().deleteItem(invSlot,
				new Item(originalId, item.getAmount()));
		player.getDetails().getStatistics().addStatistic("Items_Deposited");
		addItem(item, refresh);
	}

	public void addItem(Item item, boolean refresh) {
		addItem(item.getId(), item.getAmount(), refresh);
	}

	public int addItems(Item[] items, boolean refresh) {
		int space = (int) (MAX_BANK_SIZE - getBankSize());
		if (space != 0) {
			space = (space < items.length ? space : items.length);
			for (int i = 0; i < space; i++) {
				if (items[i] == null)
					continue;
				addItem(items[i], false);
			}
			if (refresh) {
				refreshTabs();
				refreshItems();
				refreshTotalSize();
			}
		}
		return space;
	}

	public void addItem(int id, int quantity, boolean refresh) {
		addItem(id, quantity, currentTab, refresh);
	}

	public void addItem(int id, int quantity, int creationTab, boolean refresh) {
		int[] slotInfo = getItemSlot(id);
		if (slotInfo == null) {
			if (creationTab >= bankTabs.length)
				creationTab = bankTabs.length - 1;
			if (creationTab < 0) // fixed now, alex
				creationTab = 0;
			int slot = bankTabs[creationTab].length;
			Item[] tab = new Item[slot + 1];
			System.arraycopy(bankTabs[creationTab], 0, tab, 0, slot);
			tab[slot] = new Item(id, quantity);
			bankTabs[creationTab] = tab;
			if (refresh) {
                refreshTabs();
                refreshItems();
            }
			total_size++;
		} else {
			Item item = bankTabs[slotInfo[0]][slotInfo[1]];
			bankTabs[slotInfo[0]][slotInfo[1]] = new Item(item.getId(),
					item.getAmount() + quantity);
		}
		if (refresh) {
            refreshItems();
            refreshTotalSize();
        }
	}

	public boolean removeItem(int fakeSlot, int quantity, boolean refresh,
			boolean forceDestroy) {
		return removeItem(getRealSlot(fakeSlot), quantity, refresh,
				forceDestroy);
	}

	public boolean removeItem(int[] slot, int quantity, boolean refresh,
			boolean forceDestroy) {
		if (slot == null)
			return false;
		Item item = bankTabs[slot[0]][slot[1]];
		boolean destroyed = false;
		if (quantity >= item.getAmount()) {
			if (bankTabs[slot[0]].length == 1
					&& (forceDestroy || bankTabs.length != 1)) {
				destroyTab(slot[0]);
				if (refresh)
					refreshTabs();
				destroyed = true;
			} else {
				Item[] tab = new Item[bankTabs[slot[0]].length - 1];
				System.arraycopy(bankTabs[slot[0]], 0, tab, 0, slot[1]);
				System.arraycopy(bankTabs[slot[0]], slot[1] + 1, tab, slot[1],
						bankTabs[slot[0]].length - slot[1] - 1);
				bankTabs[slot[0]] = tab;
				if (refresh)
					refreshTab(slot[0]);
			}
		} else {
			bankTabs[slot[0]][slot[1]] = new Item(item.getId(),
					item.getAmount() - quantity);
		}
		total_size --;
		if (refresh) {
			refreshItems();
			refreshTotalSize();
		}
		return destroyed;
	}

	public Item getItem(int id) {
		for (int slot = 0; slot < bankTabs.length; slot++) {
			for (Item item : bankTabs[slot])
				if (item.getId() == id)
					return item;
		}
		return null;
	}

	public int[] getItemSlot(int id) {
		for (int tab = 0; tab < bankTabs.length; tab++) {
			for (int slot = 0; slot < bankTabs[tab].length; slot++)
				if (bankTabs[tab][slot].getId() == id)
					return new int[] { tab, slot };
		}
		return null;
	}

	public Item getItem(int[] slot) {
		if (slot == null)
			return null;
		return bankTabs[slot[0]][slot[1]];
	}

	public int getStartSlot(int tabId) {
		int slotId = 0;
		for (int tab = 1; tab < (tabId == 0 ? bankTabs.length : tabId); tab++)
			slotId += bankTabs[tab].length;

		return slotId;

	}

	public int[] getRealSlot(int slot) {
		for (int tab = 1; tab < bankTabs.length; tab++) {
			if (slot >= bankTabs[tab].length)
				slot -= bankTabs[tab].length;
			else
				return new int[] { tab, slot };
		}
		if (slot >= bankTabs[0].length)
			return null;
		return new int[] { 0, slot };
	}

	public void refreshViewingTab() {
		player.getVarsManager().sendVarBit(InterfaceVars.BANK_REFRESH_LAST_VIEWING_TAB, currentTab + 1);
	}

	public void refreshTab(int slot) {
		if (slot == 0)
			return;
		player.getVarsManager().sendVarBit(InterfaceVars.BANK_REFRESH_SPECIFIED_TAB + (slot - 1), getTabSize(slot));
	}

	public void sendItems() {
		player.getPackets().sendItems(95, getContainerCopy());
	}

	public void refreshItems(int[] slots) {
		player.getPackets().sendUpdateItems(95, getContainerCopy(), slots);
	}

	public Item[] getContainerCopy() {
		if (lastContainerCopy == null)
			lastContainerCopy = generateContainer();
		return lastContainerCopy;
	}

	public void refreshItems() {
		refreshItems(generateContainer(), getContainerCopy());
		refreshTotalSize();
	}

	public void refreshItems(Item[] itemsAfter, Item[] itemsBefore) {
		if (itemsBefore.length != itemsAfter.length) {
			lastContainerCopy = itemsAfter;
			sendItems();
			return;
		}
		int[] changedSlots = new int[itemsAfter.length];
		int count = 0;
		for (int index = 0; index < itemsAfter.length; index++) {
			if (itemsBefore[index] != itemsAfter[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		lastContainerCopy = itemsAfter;
		refreshItems(finalChangedSlots);
	}

	public int getBankSize() {
		int size = 0;
		for (int i = 0; i < bankTabs.length; i++)
			size += bankTabs[i].length;
		return size;
	}

	public Item[] generateContainer() {
		Item[] container = new Item[getBankSize()];
		int count = 0;
		for (int slot = 1; slot < bankTabs.length; slot++) {
			System.arraycopy(bankTabs[slot], 0, container, count,
					bankTabs[slot].length);
			count += bankTabs[slot].length;
		}
		System.arraycopy(bankTabs[0], 0, container, count, bankTabs[0].length);
		return container;
	}

	public void unlockButtons() {
		// unlock bank inter all options
		player.getPackets().sendIComponentSettings(762, 93, 0, 516, 2622718);
		// unlock bank inv all options - Broken, need to fix item selection
		player.getPackets().sendIComponentSettings(763, 0, 0, 27, 2425982);
	}

	public void switchWithdrawNotes() {
		withdrawNotes = !withdrawNotes;
	}

	public void switchInsertItems() {
		insertItems = !insertItems;
		player.getVarsManager().sendVar(InterfaceVars.BANK_SWITCH_INSERT_MODES, insertItems ? 1 : 0);
	}
	
	public boolean isInsertItems() {
		return insertItems;
	}
	
	public void insertItem(int fromSlot, int toSlot) {
		int[] fromRealSlot = getRealSlot(fromSlot);
		Item fromItem = getItem(fromRealSlot);
		if (fromItem == null)
			return;

		int[] toRealSlot = getRealSlot(toSlot);
		Item toItem = getItem(toRealSlot);
		if (toItem == null)
			return;

		if (toRealSlot[0] != fromRealSlot[0])
			return;

		if (toRealSlot[1] > fromRealSlot[1]) {
			for (int slot = fromRealSlot[1]; slot < toRealSlot[1]; slot++) {
				Item temp = bankTabs[toRealSlot[0]][slot];
				bankTabs[toRealSlot[0]][slot] = bankTabs[toRealSlot[0]][slot + 1];
				bankTabs[toRealSlot[0]][slot + 1] = temp;
			}
		} else if (fromRealSlot[1] > toRealSlot[1]) {
			for (int slot = fromRealSlot[1]; slot > toRealSlot[1]; slot--) {
				Item temp = bankTabs[toRealSlot[0]][slot];
				bankTabs[toRealSlot[0]][slot] = bankTabs[toRealSlot[0]][slot - 1];
				bankTabs[toRealSlot[0]][slot - 1] = temp;
			}
		}
		refreshItems();
	}

	public void setCurrentTab(int currentTab) {
		if (currentTab >= bankTabs.length)
			return;
		this.currentTab = currentTab;
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
	}
	
	public void clearBank() {
		bankTabs = new Item[1][0];
	}
	
	public boolean hasItem(int i) {
		return getItem(i) != null;
	}

	public int getAmountOf(int itemUsed) {
		for (Item bank : player.getBank().getContainerCopy()) {
            if (bank == null)
                continue;
            if (bank.getId() == itemUsed)
            	return bank.getAmount();
        }
		return 0;
	}
}
