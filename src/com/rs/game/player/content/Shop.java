package com.rs.game.player.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.Player;

public class Shop {

	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995, TOKKUL = 6529;

	private String name;
	private Item[] mainStock;
	private int[] defaultQuantity;
	private Item[] generalStock;
	private int money;
	private CopyOnWriteArrayList<Player> viewingPlayers;

	public Shop(String name, int money, Item[] mainStock, boolean isGeneralStore) {
		viewingPlayers = new CopyOnWriteArrayList<Player>();
		this.name = name;
		this.money = money;
		this.mainStock = mainStock;
		defaultQuantity = new int[mainStock.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = mainStock[i].getAmount();
		if (isGeneralStore && mainStock.length < MAX_SHOP_ITEMS)
			generalStock = new Item[MAX_SHOP_ITEMS - mainStock.length];
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getAttributes().getAttributes().put("Shop", this);
		player.setCloseInterfacesEvent(() -> {
			viewingPlayers.remove(player);
			player.getAttributes().getAttributes().remove("Shop");
			player.getAttributes().getAttributes().remove("shop_transaction");
			player.getAttributes().getAttributes().remove("isShopBuying");
			player.getAttributes().getAttributes().remove("ShopSelectedSlot");
			player.getAttributes().getAttributes().remove("ShopSelectedInventory");
		});
//		player.getVarsManager().sendVar(118,
//				generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY);
		player.getVarsManager().sendVar(1496, -1); // sample items container id
													// (TODO: add support for
													// it)
		player.getVarsManager().sendVar(532, money);
		resetSelected(player);
		sendStore(player);
		player.getInterfaceManager().sendInterface(1265); // opens sho
		resetTransaction(player);
		setBuying(player, true);
		if (generalStock != null)
			player.getPackets().sendHideIComponent(1265, 19, false); // unlocks
																		// general
																		// store
																		// icon
		player.getPackets().sendIComponentSettings(1265, 20, 0, getStoreSize(),
				1150); // unlocks stock slots
		sendInventory(player);
		player.getPackets().sendIComponentText(1265, 85, name);
	}

	public void resetTransaction(Player player) {
		setTransaction(player, 1);
	}

	public void increaseTransaction(Player player, int amount) {
		setTransaction(player, getTransaction(player) + amount);
	}

	public int getTransaction(Player player) {
		Integer transaction = (Integer) player.getAttributes().getAttributes().get(
				"shop_transaction");
		return transaction == null ? 1 : transaction;
	}

	public void pay(Player player) {
		Integer selectedSlot = (Integer) player.getAttributes().getAttributes().get(
				"ShopSelectedSlot");
		Boolean inventory = (Boolean) player.getAttributes().getAttributes().get(
				"ShopSelectedInventory");
		if (selectedSlot == null || inventory == null)
			return;
		int amount = getTransaction(player);
		if (inventory)
			sell(player, selectedSlot, amount);
		else
			buy(player, selectedSlot, amount);
	}

	public int getSelectedMaxAmount(Player player) {
		Integer selectedSlot = (Integer) player.getAttributes().getAttributes().get(
				"ShopSelectedSlot");
		Boolean inventory = (Boolean) player.getAttributes().getAttributes().get(
				"ShopSelectedInventory");
		if (selectedSlot == null || inventory == null)
			return 1;
		if (inventory) {
			Item item = player.getInventory().getItem(selectedSlot);
			if (item == null)
				return 1;
			return player.getInventory().getAmountOf(item.getId());
		} else {
			if (selectedSlot >= getStoreSize())
				return 1;
			Item item = selectedSlot >= mainStock.length ? generalStock[selectedSlot
					- mainStock.length]
					: mainStock[selectedSlot];
			if (item == null)
				return 1;
			return item.getAmount();
		}
	}

	public void setTransaction(Player player, int amount) {
		int max = getSelectedMaxAmount(player);
		if (amount > max)
			amount = max;
		else if (amount < 1)
			amount = 1;
		player.getAttributes().getAttributes().put("shop_transaction", amount);
		player.getVarsManager().sendVar(2564, amount);
	}

	public static void setBuying(Player player, boolean buying) {
		player.getAttributes().getAttributes().put("isShopBuying", buying);
		player.getVarsManager().sendVar(2565, buying ? 0 : 1);
	}

	public static boolean isBuying(Player player) {
		Boolean isBuying = (Boolean) player.getAttributes().getAttributes().get(
				"isShopBuying");
		return isBuying != null && isBuying;
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(1266);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(1266, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(1266, 0, 93, 4, 7,
				"Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
	}

	public void buyAll(Player player, int slotId) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		buy(player, slotId, item.getAmount());
	}

	public void buy(Player player, int slotId, int quantity) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getAmount() == 0) {
			player.getPackets().sendGameMessage(
					"There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item, dq);
		int amountCoins = money == COINS ? player.getInventory()
				.getCoinsAmount() : player.getInventory().getItems()
				.getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getAmount() > quantity ? quantity : item.getAmount();

		boolean enoughCoins = maxQuantity >= buyQ;
		if (!enoughCoins) {
			player.getPackets().sendGameMessage(
					"You don't have enough "
							+ ItemDefinitions.getItemDefinitions(money)
									.getName().toLowerCase() + ".");
			buyQ = maxQuantity;
		} else if (quantity > buyQ)
			player.getPackets().sendGameMessage(
					"The shop has run out of stock.");
		if (item.getDefinitions().isStackable()) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getFreeSlots();
			if (buyQ > freeSlots) {
				buyQ = freeSlots;
				player.getPackets().sendGameMessage(
						"Not enough space in your inventory.");
			}
		}
		if (buyQ != 0) {
			int totalPrice = price * buyQ;
			player.getInventory().deleteItem(money, totalPrice);
			player.getInventory().addItem(item.getId(), buyQ);
			item.setAmount(item.getAmount() - buyQ);
			if (item.getAmount() <= 0 && slotId >= mainStock.length)
				generalStock[slotId - mainStock.length] = null;
			refreshShop();
			resetSelected(player);

		}
	}

	public void restoreItems() {
		boolean needRefresh = false;
		for (int i = 0; i < mainStock.length; i++) {
			if (mainStock[i].getAmount() < defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + 1);
				needRefresh = true;
			} else if (mainStock[i].getAmount() > defaultQuantity[i]) {
				mainStock[i].setAmount(mainStock[i].getAmount() + -1);
				needRefresh = true;
			}
		}
		if (generalStock != null) {
			for (int i = 0; i < generalStock.length; i++) {
				Item item = generalStock[i];
				if (item == null)
					continue;
				item.setAmount(item.getAmount() - 1);
				if (item.getAmount() <= 0)
					generalStock[i] = null;
				needRefresh = true;
			}
		}
		if (needRefresh)
			refreshShop();
	}

	private boolean addItem(int itemId, int quantity) {
		for (Item item : mainStock) {
			if (item.getId() == itemId) {
				item.setAmount(item.getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (generalStock != null) {
			for (Item item : generalStock) {
				if (item == null)
					continue;
				if (item.getId() == itemId) {
					item.setAmount(item.getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new Item(itemId, quantity);
					refreshShop();
					return true;
				}
			}
		}
		return false;
	}

	public void sell(Player player, int slotId, int quantity) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		int originalId = item.getId();
		if (item.getDefinitions().isNoted()
				&& item.getDefinitions().getCertId() != -1)
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (!ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item, dq);
		int numberOff = player.getInventory().getItems()
				.getNumberOf(originalId);
		if (quantity > numberOff)
			quantity = numberOff;
		if (!addItem(item.getId(), quantity)) {
			player.getPackets().sendGameMessage("Shop is currently full.");
			return;
		}
		player.getInventory().deleteItem(originalId, quantity);
		refreshShop();
		resetSelected(player);
		if (price == 0)
			return;
		player.getInventory().addItem(new Item(money, price * quantity));
	}

	public void sendValue(Player player, int slotId) {
		if (player.getInventory().getItemsContainerSize() < slotId)
			return;
		Item item = player.getInventory().getItem(slotId);
		if (item == null)
			return;
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (!ItemConstants.isTradeable(item) || item.getId() == money) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			return;
		}
		int dq = getDefaultQuantity(item.getId());
		if (dq == -1 && generalStock == null) {
			player.getPackets().sendGameMessage(
					"You can't sell this item to this shop.");
			return;
		}
		int price = getSellPrice(item, dq);
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName()
						+ ": shop will buy for: "
						+ price
						+ " "
						+ ItemDefinitions.getItemDefinitions(money).getName()
								.toLowerCase()
						+ ". Right-click the item to sell.");
	}

	public int getDefaultQuantity(int itemId) {
		for (int i = 0; i < mainStock.length; i++)
			if (mainStock[i].getId() == itemId)
				return defaultQuantity[i];
		return -1;
	}

	public void resetSelected(Player player) {
		player.getAttributes().getAttributes().remove("ShopSelectedSlot");
		player.getVarsManager().sendVar(2563, -1);
	}

	public void sendInfo(Player player, int slotId, boolean inventory) {
		if (!inventory && slotId >= getStoreSize())
			return;
		Item item = inventory ? player.getInventory().getItem(slotId)
				: slotId >= mainStock.length ? generalStock[slotId
						- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getDefinitions().isNoted())
			item = new Item(item.getDefinitions().getCertId(), item.getAmount());
		if (inventory
				&& (!ItemConstants.isTradeable(item) || item.getId() == money)) {
			player.getPackets().sendGameMessage("You can't sell this item.");
			resetSelected(player);
			return;
		}
		resetTransaction(player);
		player.getAttributes().getAttributes().put("ShopSelectedSlot", slotId);
		player.getAttributes().getAttributes()
				.put("ShopSelectedInventory", inventory);
//		player.getVarsManager().sendVar(
//				2561,
//				inventory ? 93 : generalStock != null ? 139
//						: MAIN_STOCK_ITEMS_KEY); // inv key
		player.getVarsManager().sendVar(2562, item.getId());
		player.getVarsManager().sendVar(2563, slotId);
//		player.getPackets().sendGlobalString(362, ItemExamines.getExamine(item));
		player.getPackets().sendGlobalConfig(1876,
				item.getDefinitions().isWearItem() ? 0 : -1); // TODO item pos
																// or usage if
																// has one,
																// setting 0 to
																// allow see
																// stats
		int price = inventory ? getSellPrice(item,
				getDefaultQuantity(item.getId())) : getBuyPrice(item,
				slotId >= mainStock.length ? 0 : defaultQuantity[slotId]);
		player.getPackets().sendGameMessage(
				item.getDefinitions().getName()
						+ ": shop will "
						+ (inventory ? "buy" : "sell")
						+ " for: "
						+ price
						+ " "
						+ ItemDefinitions.getItemDefinitions(money).getName()
								.toLowerCase());
	}

	/*
	 * [clientscript] int get_buy_price(int arg0) { int ivar1; if (arg0 == -1) {
	 * return 0; } arg0 = getRealItem(arg0); ivar1 = getDataByKey('o', 'i', 731,
	 * arg0); if (standart_config_532 == 6529 && ivar1 != -1 && ivar1 > 0) {
	 * return ivar1; } ivar1 = getDataByKey('o', 'i', 733, arg0); if (ivar1 !=
	 * -1 && ivar1 > 0) { return ivar1; } if (getItemAttribute(arg0, 258) == 1
	 * || getItemAttribute(arg0, 259) == 1) { return 99000; } ivar1 =
	 * getItemValue(arg0); if (standart_config_532 == 6529) { ivar1 =
	 * multiplyDivide(3, 2, ivar1); } return max(ivar1, 1); }
	 */

	public int getBuyPrice(Item item, int dq) {
		switch (money) {
		case 24444: // TROHPY
			if (item.getId() >= 24450 && item.getId() <= 24454)
				return 30 + (item.getId() - 24450) * 5;
			if (item.getId() >= 24455 && item.getId() <= 24457)
				return 1500;
			break;
		default:
			int price = ClientScriptMap.getMap(731).getIntValue(item.getId());
			if (money == TOKKUL && price > 0)
				return price;
			price = ClientScriptMap.getMap(733).getIntValue(item.getId());
			if (price > 0)
				return price;
			if (item.getDefinitions().hasShopPriceAttributes())
				return 99000;
			price = item.getDefinitions().getValue();
			if (money == TOKKUL)
				price = (price * 3) / 2;
			return Math.max(price, 1);

		}
		return 1;
	}

	/*
	 * [clientscript] int get_sell_price(int arg0) { int ivar1; if (arg0 == -1)
	 * { return 0; } arg0 = getRealItem(arg0); ivar1 = getDataByKey('o', 'i',
	 * 732, arg0); if (standart_config_532 == 6529 && ivar1 != -1) { return
	 * ivar1; } ivar1 = getDataByKey('o', 'i', 1441, arg0); if (ivar1 != -1 &&
	 * ivar1 > 0) { return ivar1; } ivar1 = max(1,
	 * multiplyDivide(getItemValue(arg0), 100, 30)); return ivar1; }
	 */

	public int getSellPrice(Item item, int dq) {
		int price = ClientScriptMap.getMap(732).getIntValue(item.getId());
		if (money == TOKKUL && price > 0)
			return price;
		price = ClientScriptMap.getMap(1441).getIntValue(item.getId());
		if (price > 0)
			return price;
		return Math.max(1, (item.getDefinitions().getValue() * 30) / 100);

	}

	public void sendExamine(Player player, int slotId) {
		if (slotId >= getStoreSize())
			return;
		Item item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
//		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(620, 25, 0,
					getStoreSize() * 6, 1150);
		}
	}

	public int getStoreSize() {
		return mainStock.length
				+ (generalStock != null ? generalStock.length : 0);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length
				+ (generalStock != null ? generalStock.length : 0)];
		System.arraycopy(mainStock, 0, stock, 0, mainStock.length);
		if (generalStock != null)
			System.arraycopy(generalStock, 0, stock, mainStock.length,
					generalStock.length);
//		player.getPackets().sendItems(
//				generalStock != null ? 139 : MAIN_STOCK_ITEMS_KEY, stock);
	}

}