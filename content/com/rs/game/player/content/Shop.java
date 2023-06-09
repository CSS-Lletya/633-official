package com.rs.game.player.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.InterfaceVars;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.utilities.ItemExamines;
import com.rs.utilities.Utility;
import com.rs.utilities.loaders.ShopItem;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import skills.Skills;

public class Shop {

	private static final int INTERFACE = 620;
	private static final int INVENTORY_INTERFACE = 621;
	
	private static final int MAX_SHOP_ITEMS = 40;
	public static final int COINS = 995, TOKKUL = 6529;

	private String name;
	private ShopItem[] mainStock;
	private int[] defaultQuantity;
	private ShopItem[] generalStock;
	private int money;
	private CopyOnWriteArrayList<Player> viewingPlayers;
	
	private boolean buyOnly;
	
	public Shop(String name, int money, ShopItem[] shopItems, boolean isGeneralStore, boolean buyOnly) {
		viewingPlayers = new CopyOnWriteArrayList<>();
		this.name = name;
		this.money = money;
		mainStock = shopItems;
		defaultQuantity = new int[shopItems.length];
		for (int i = 0; i < defaultQuantity.length; i++)
			defaultQuantity[i] = shopItems[i].getItem().getAmount();
		if (isGeneralStore && shopItems.length < MAX_SHOP_ITEMS)
			generalStock = new ShopItem[MAX_SHOP_ITEMS - shopItems.length];
		this.buyOnly = buyOnly;
		for (ShopItem item : mainStock) {
			if (item == null)
				continue;
			item.init();
		}
	}

	public boolean isGeneralStore() {
		return generalStock != null;
	}

	public void addPlayer(final Player player) {
		viewingPlayers.add(player);
		player.getAttributes().get(Attribute.SHOP).set(this);
		player.setCloseInterfacesEvent(() -> {
			viewingPlayers.remove(player);
			player.getAttributes().get(Attribute.SHOP).set(null);
			player.getAttributes().get(Attribute.SHOP_TRANSACTION).set(null);
			player.getAttributes().get(Attribute.IS_SHOP_BUYING).set(null);
			player.getAttributes().get(Attribute.SHOP_SELECTED_SLOT).set(null);
			player.getAttributes().get(Attribute.SHOP_SELECTED_INVENTORY).set(null);
		});
		sendStore(player);
		sendInventory(player);
		player.getVarsManager().sendVar(InterfaceVars.SHOP_ITEM_COMPONENTS, 4);
		player.getVarsManager().sendVar(InterfaceVars.SHOP_KEY, -1);
		player.getInterfaceManager().sendInterface(INTERFACE);
		setBuying(player, true);
		refreshShop();
		player.getPackets().sendIComponentText(INTERFACE, 20, name);
	}

	public int getTransaction(Player player) {
		Integer transaction = (Integer) player.getAttributes().get(Attribute.SHOP_TRANSACTION).get();
		return transaction == null ? 1 : transaction;
	}

	public void pay(Player player) {
		Integer selectedSlot = (Integer) player.getAttributes().get(Attribute.SHOP_SELECTED_SLOT).get();
		Boolean inventory = (Boolean) player.getAttributes().get(Attribute.SHOP_SELECTED_INVENTORY).getBoolean();
		if (selectedSlot == null || inventory == null)
			return;
		int amount = getTransaction(player);
		if (inventory)
			sell(player, selectedSlot, amount);
		else
			buy(player, selectedSlot, amount);
	}

	public static void setBuying(Player player, boolean buying) {
		player.getAttributes().get(Attribute.IS_SHOP_BUYING).set(buying);
		player.getVarsManager().sendVar(InterfaceVars.SHOP_BUYING_STATE, buying ? 0 : 1);
	}

	public static boolean isBuying(Player player) {
		Boolean isBuying = (Boolean) player.getAttributes().get(Attribute.IS_SHOP_BUYING).getBoolean();
		return isBuying != null && isBuying;
	}

	public void sendInventory(Player player) {
		player.getInterfaceManager().sendInventoryInterface(INVENTORY_INTERFACE);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendUnlockIComponentOptionSlots(INVENTORY_INTERFACE, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
		player.getPackets().sendInterSetItemsOptionsScript(INVENTORY_INTERFACE, 0, 93, 4, 7,
				"Value", "Sell 1", "Sell 5", "Sell 10", "Sell 50", "Examine");
	}


	public void buy(Player player, int slotId, int quantity) {
		if (slotId >= getStoreSize())
			return;
		ShopItem item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		if (item.getItem().getAmount() == 0) {
			player.getPackets().sendGameMessage(
					"There is no stock of that item at the moment.");
			return;
		}
		int dq = slotId >= mainStock.length ? 0 : defaultQuantity[slotId];
		int price = getBuyPrice(item.getItem(), dq);
		int amountCoins = money == COINS ? player.getInventory()
				.getCoinsAmount() : player.getInventory().getItems()
				.getNumberOf(money);
		int maxQuantity = amountCoins / price;
		int buyQ = item.getItem().getAmount() > quantity ? quantity : item.getItem().getAmount();

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
		if (item.getItem().getDefinitions().isStackable()) {
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
			player.getInventory().addItem(item.getItem().getId(), buyQ);
			item.getItem().setAmount(item.getItem().getAmount() - buyQ);
			if (item.getItem().getAmount() <= 0 && slotId >= mainStock.length)
				generalStock[slotId - mainStock.length] = null;
			refreshShop();
			resetSelected(player);

		}
	}

	private boolean addItem(int itemId, int quantity) {
		for (ShopItem item : mainStock) {
			if (item.getItem().getId() == itemId) {
				item.getItem().setAmount(item.getItem().getAmount() + quantity);
				refreshShop();
				return true;
			}
		}
		if (isGeneralStore()) {
			for (ShopItem item : generalStock) {
				if (item == null)
					continue;
				if (item.getItem().getId() == itemId) {
					item.getItem().setAmount(item.getItem().getAmount() + quantity);
					refreshShop();
					return true;
				}
			}
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null) {
					generalStock[i] = new ShopItem(itemId, quantity);
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
		if (buyOnly) {
			player.getPackets().sendGameMessage("You cannot sell items to this shop.");
			return;
		}
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
			if (mainStock[i].getItem().getId() == itemId)
				return defaultQuantity[i];
		return -1;
	}

	public void resetSelected(Player player) {
		player.getAttributes().get(Attribute.SHOP_SELECTED_SLOT).set(null);
		player.getVarsManager().sendVar(InterfaceVars.SHOP_RESET_SELECTED, -1);
	}

	public void sendInfo(Player player, int slotId, boolean inventory) {
		if (!inventory && slotId >= getStoreSize())
			return;
		if (!inventory) {
			ShopItem item = slotId >= mainStock.length ? generalStock[slotId - mainStock.length] : mainStock[slotId];
			if (item == null)
				return;
			int price = getBuyPrice(item.getItem(), slotId >= mainStock.length ? 0 : defaultQuantity[slotId]);
			player.getPackets().sendGameMessage(item.getItem().getDefinitions().getName() + ": shop will " + (!inventory ? "sell" : "buy") + " for " + Utility.getFormattedNumber(price) + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
			sendInfo(player, item.getItem());
		} else {
			Item[] stock = player.getInventory().getItems().getItems();
			if (slotId >= stock.length)
				return;
			Item item = stock[slotId];
			if (item == null)
				return;
			int price = getSellPrice(item, getDefaultQuantity(item.getId()));
			player.getPackets().sendGameMessage(item.getDefinitions().getName() + ": shop will " + (!inventory ? "sell" : "buy") + " for " + Utility.getFormattedNumber(price) + " " + ItemDefinitions.getItemDefinitions(money).getName().toLowerCase() + ".");
		}	
	}

	public int getBuyPrice(Item item, int dq) {
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
		ShopItem item = slotId >= mainStock.length ? generalStock[slotId
				- mainStock.length] : mainStock[slotId];
		if (item == null)
			return;
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item.getItem()));
	}

	public void refreshShop() {
		for (Player player : viewingPlayers) {
			sendStore(player);
			player.getPackets().sendIComponentSettings(INTERFACE, 25, 0, 240, 1150);
		}
	}
	
	public int getStoreSize() {
		return mainStock.length
				+ (isGeneralStore() ? generalStock.length : 40);
	}

	public void sendStore(Player player) {
		Item[] stock = new Item[mainStock.length + (generalStock != null ? generalStock.length : 0)];
		for (int i = 0; i < stock.length; i++)
			if (i >= mainStock.length) {
				ShopItem item = generalStock[i - mainStock.length];
				if (item == null)
					continue;
				stock[i] = item.getItem();
			} else {
				ShopItem item = mainStock[i];
				if (item == null)
					continue;
				stock[i] = item.getItem();
			}
		player.getPackets().sendItems(4, stock);
	}
	
	public void restoreItems() {
		boolean needRefresh = false;
		for (ShopItem element : mainStock) {
			if (element == null)
				continue;
			if (element.tickRestock())
				needRefresh = true;
		}
		if (generalStock != null)
			for (int i = 0; i < generalStock.length; i++) {
				if (generalStock[i] == null)
					continue;
				if (generalStock[i].tickRestock())
					needRefresh = true;
				if (generalStock[i].getItem().getAmount() <= 0)
					generalStock[i] = null;
			}
		if (needRefresh) {
			refreshShop();
		}
	}

	// NOTE: combat rework will require this bonuses to be updated.
	public static void sendInfo(Player player, Item item) {
		player.getInterfaceManager().sendInventoryInterface(449);
		player.getPackets().sendGlobalConfig(741, item.getId());
		player.getPackets().sendGlobalString(25, ItemExamines.getExamine(item));
		player.getPackets().sendGlobalString(34, ""); // quest id for some items
		int[] bonuses = new int[18];
		ItemDefinitions defs = item.getDefinitions();
		bonuses[CombatDefinitions.STAB_ATTACK] += defs.getStabAttack();
		bonuses[CombatDefinitions.SLASH_ATTACK] += defs.getSlashAttack();
		bonuses[CombatDefinitions.CRUSH_ATTACK] += defs.getCrushAttack();
		bonuses[CombatDefinitions.MAGIC_ATTACK] += defs.getMagicAttack();
		bonuses[CombatDefinitions.RANGE_ATTACK] += defs.getRangeAttack();
		bonuses[CombatDefinitions.STAB_DEF] += defs.getStabDef();
		bonuses[CombatDefinitions.SLASH_DEF] += defs.getSlashDef();
		bonuses[CombatDefinitions.CRUSH_DEF] += defs.getCrushDef();
		bonuses[CombatDefinitions.MAGIC_DEF] += defs.getMagicDef();
		bonuses[CombatDefinitions.RANGE_DEF] += defs.getRangeDef();
		bonuses[CombatDefinitions.SUMMONING_DEF] += defs.getSummoningDef();
		bonuses[CombatDefinitions.ABSORB_MELEE] += defs.getAbsorptionMeleeBonus();
		bonuses[CombatDefinitions.ABSORB_MAGIC] += defs.getAbsorptionMageBonus();
		bonuses[CombatDefinitions.ABSORB_RANGE] += defs.getAbsorptionRangeBonus();
		bonuses[CombatDefinitions.STRENGTH_BONUS] += defs.getStrengthBonus();
		bonuses[CombatDefinitions.RANGED_STR_BONUS] += defs.getRangedStrBonus();
		bonuses[CombatDefinitions.PRAYER_BONUS] += defs.getPrayerBonus();
		bonuses[CombatDefinitions.MAGIC_DAMAGE] += defs.getMagicDamage();
		boolean hasBonus = false;
		for (int bonus : bonuses)
			if (bonus != 0) {
				hasBonus = true;
				break;
			}
		if (hasBonus) {
			Object2ObjectOpenHashMap<Integer, Integer> requiriments = item.getDefinitions()
					.getWearingSkillRequiriments();
			if (requiriments != null && !requiriments.isEmpty()) {
				String reqsText = "";
				for (int skillId : requiriments.keySet()) {
					if (skillId > 24 || skillId < 0)
						continue;
					int level = requiriments.get(skillId);
					if (level < 0 || level > 120)
						continue;
					if (skillId == Skills.ATTACK && level == 1) {
						continue;
					}
					if (skillId == Skills.FIREMAKING && level == 61) {
						continue;
					}
					boolean hasReq = player.getSkills().getLevelForXp(skillId) >= level;
					reqsText += "<br>" + (hasReq ? "<col=00ff00>" : "<col=ff0000>") + "Level " + level + " "
							+ Skills.SKILL_NAME[skillId];
				}
				player.getPackets().sendGlobalString(26, "<br>Worn on yourself, requiring: " + reqsText);
			} else
				player.getPackets().sendGlobalString(26, "<br>Worn on yourself");
			player.getPackets().sendGlobalString(35,
					"<br>Attack<br><col=ffff00>+" + bonuses[CombatDefinitions.STAB_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.SLASH_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.CRUSH_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.MAGIC_ATTACK] + "<br><col=ffff00>+"
							+ bonuses[CombatDefinitions.RANGE_ATTACK] + "<br><col=ffff00>---" + "<br>Strength"
							+ "<br>Ranged Strength" + "<br>Magic Damage" + "<br>Absorve Melee" + "<br>Absorve Magic"
							+ "<br>Absorve Ranged" + "<br>Prayer Bonus");
			player.getPackets().sendGlobalString(36, "<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning");
			player.getPackets().sendGlobalString(52, "<<br>Defence<br><col=ffff00>+"
					+ bonuses[CombatDefinitions.STAB_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.SLASH_DEF]
					+ "<br><col=ffff00>+" + bonuses[CombatDefinitions.CRUSH_DEF] + "<br><col=ffff00>+"
					+ bonuses[CombatDefinitions.MAGIC_DEF] + "<br><col=ffff00>+" + bonuses[CombatDefinitions.RANGE_DEF]
					+ "<br><col=ffff00>+" + bonuses[CombatDefinitions.SUMMONING_DEF] + "<br><col=ffff00>+"
					+ bonuses[CombatDefinitions.STRENGTH_BONUS] + "<br><col=ffff00>"
					+ bonuses[CombatDefinitions.RANGED_STR_BONUS] + "<br><col=ffff00>"
					+ bonuses[CombatDefinitions.MAGIC_DAMAGE] + "%<br><col=ffff00>"
					+ bonuses[CombatDefinitions.ABSORB_MELEE] + "%<br><col=ffff00>"
					+ bonuses[CombatDefinitions.ABSORB_MAGIC] + "%<br><col=ffff00>"
					+ bonuses[CombatDefinitions.ABSORB_RANGE] + "%<br><col=ffff00>"
					+ bonuses[CombatDefinitions.PRAYER_BONUS]);
		} else {
			player.getPackets().sendGlobalString(26, "");
			player.getPackets().sendGlobalString(35, "");
			player.getPackets().sendGlobalString(36, "");
			player.getPackets().sendGlobalString(52, "");
		}

	}
}