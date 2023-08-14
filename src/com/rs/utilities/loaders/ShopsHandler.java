package com.rs.utilities.loaders;

import java.io.File;

import com.rs.game.player.Player;
import com.rs.game.player.content.Shop;
import com.rs.utilities.GSONParser;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class ShopsHandler {

	private static final Object2ObjectArrayMap<String, Shop> SHOPS = new Object2ObjectArrayMap<>();
	private static final Object2ObjectArrayMap<String, ShopDef> SHOP_DEFS = new Object2ObjectArrayMap<>();
	private static final Short2ObjectOpenHashMap<String> NPC_SHOPS = new Short2ObjectOpenHashMap<>();

	private static final String PATH = "data/items/shops/";

	public static void loadShops() {
		loadShopFiles();
	}

	public static void reloadShops() {
		SHOPS.clear();
		SHOP_DEFS.clear();
		NPC_SHOPS.clear();
		loadShopFiles();
	}

	private static void loadShopFiles() {
		try {
			File[] dropFiles = new File(PATH).listFiles();
			for (File f : dropFiles)
				loadFile(f);
		} catch (Throwable e) {
		}
		for (String key : SHOP_DEFS.keySet()) {
			ShopDef shop = SHOP_DEFS.get(key);
			addShop(key, new Shop(shop.getName(), shop.getCurrency(), shop.getItems(), shop.isGeneralStore(), shop.isBuyOnly()));
			if (shop.getNpcIds() != null && shop.getNpcIds().length > 0)
				for (int npcId : shop.getNpcIds())
					NPC_SHOPS.put((short) npcId, key);
		}
	}

	private static void loadFile(File f) {
		try {
			if (f.isDirectory()) {
				for (File dir : f.listFiles())
					loadFile(dir);
				return;
			}
			ShopDef def = (ShopDef) GSONParser.loadFile(f.getAbsolutePath(), ShopDef.class);
			if (def != null)
				SHOP_DEFS.put(f.getName().replace(".json", ""), def);
		} catch(Throwable e) {
			System.err.println("Error loading file: " + f.getPath());
		}
	}

	public static String getShopForNpc(int npcId) {
		return NPC_SHOPS.get((short) npcId);
	}

	public static void restoreShops() {
		for (Shop shop : SHOPS.values())
			shop.restoreItems();
	}

	public static boolean openShop(Player player, String key) {
		Shop shop = getShop(key);
		if (shop == null)
			return false;
		shop.addPlayer(player);
		return true;
	}

	public static Shop getShop(String key) {
		return SHOPS.get(key);
	}

	public static void addShop(String key, Shop shop) {
		SHOPS.put(key, shop);
	}
}