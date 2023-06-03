package com.rs.plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * @author Dennis
 */
public class InventoryPluginDispatcher {
	
	/**
	 * The object map which contains all the interface on the world.
	 */
	private static final Object2ObjectOpenHashMap<InventoryWrapper, InventoryListener> ITEMS = new Object2ObjectOpenHashMap<>();
	
	/**
	 * Executes the specified Item if it's registered.
	 * @param player the player executing the item.
	 * @param parts the string which represents a item.
	 */
	public static boolean execute(Player player, Item item, int slot, int optionId) {
	    Optional<InventoryListener> optionalItem = getItem(item.getId());
	    if (optionalItem.isPresent()) {
	        InventoryListener specifiedItem = optionalItem.get();
	        specifiedItem.execute(player, item, slot, optionId);
	        return true;
	    }
	    return false;
	}

	/**
	 * Executes the specified interface if it's registered.
	 * 
	 * @param player the player executing the interface.
	 * @param parts  the string which represents a interface.
	 */
	public static void execute(Player player, Item firstItem, Item secondItem) {
		getItem(firstItem.getId()).ifPresent(inter -> inter.execute(player, firstItem, secondItem));
	}

	/**
	 * Gets a Item which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<InventoryListener> getItem(int itemId) {
		return ITEMS.values()
	            .stream()
	            .filter(inventoryType -> isCorrectItemId(inventoryType, itemId))
	            .findFirst();
	}
	
	private static boolean isCorrectItemId(InventoryListener InventoryType, int itemId) {
		ItemDefinitions itemDef = ItemDefinitions.getItemDefinitions(itemId);
		InventoryWrapper signature = InventoryType.getClass().getAnnotation(InventoryWrapper.class);
		return Arrays.stream(signature.itemNames()).anyMatch(itemName -> itemDef.getName().toLowerCase().contains(itemName))
			|| Arrays.stream(signature.itemId()).anyMatch(id -> itemId == id);
	}
	
	/**
	 * Loads all the Items into the {@link #ITEMS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<InventoryListener> inventoryItem = Utility.getClassesInDirectory("com.rs.plugin.impl.inventory").stream().map(clazz -> (InventoryListener) clazz).collect(Collectors.toList());
		inventoryItem.forEach(id -> ITEMS.put(id.getClass().getAnnotation(InventoryWrapper.class), id));
	}
	
	/**
	 * Reloads all the Items into the {@link #ITEMS} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the commands in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		ITEMS.clear();
		load();
		LogUtility.log(LogType.INFO, "Reloaded Inventory Plugins");
	}
}