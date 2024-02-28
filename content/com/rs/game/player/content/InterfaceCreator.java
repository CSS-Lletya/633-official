package com.rs.game.player.content;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;

/**
 * InterfaceCreator is a utility class designed to simplify interface creating.
 * Especially useful for custom severs, such.
 * @author Dennis
 *
 */
public class InterfaceCreator {

	/**
	 * Represents the Player used in this Interface creation.
	 */
	private final Player player;
	
	/**
	 * Represents the interface being used to be created.
	 */
	public final int interfaceId;

	/**
	 * Represents a collection of items used in a Container.
	 */
	private ItemsContainer<Item> items;

	/**
	 * Creates a new Interface.
	 * @param player
	 * @param interfaceId
	 * @param items
	 */
	public InterfaceCreator(Player player, int interfaceId, ItemsContainer<Item> items) {
		this.interfaceId = interfaceId;
		this.player = player;
		this.items = items;
	}
	
	/**
	 * Creates a new Interface.
	 * @param player
	 * @param interfaceId
	 */
	public InterfaceCreator(Player player, int interfaceId) {
		this.interfaceId = interfaceId;
		this.player = player;
	}
	
	/**
	 * Renders a specific interface as an Overlay
	 * @return
	 */
	public InterfaceCreator renderAsOverlay() {
		player.getInterfaceManager().sendOverlay(interfaceId);
		return this;
	}
	
	/**
	 * Renders a specific interface as a basic interface
	 * @return
	 */
	public InterfaceCreator renderAsInterface() {
		player.getInterfaceManager().sendInterface(interfaceId);
		return this;
	}
	
	/**
	 * Hides a component in the interface.
	 * @param componentId
	 * @return
	 */
	public InterfaceCreator hideComponent(int componentId) {
		player.getPackets().sendHideIComponent(interfaceId, componentId, true);
		return this;
	}
	
	/**
	 * Hides a collection of components for the interface.
	 * @param componentId
	 * @return
	 */
	public InterfaceCreator hideComponents(int... componentId) {
		IntStream.of(componentId).forEach(component -> player.getPackets().sendHideIComponent(interfaceId, component, true));
		return this;
	}
	
	/**
	 * Shows a component in the interface.
	 * @param componentId
	 * @return
	 */
	public InterfaceCreator showComponent(int componentId) {
		player.getPackets().sendHideIComponent(interfaceId, componentId, false);
		return this;
	}
	
	/**
	 * Shows a collection of components in the interface.
	 * @param componentId
	 * @return
	 */
	public InterfaceCreator showComponents(int... componentId) {
		IntStream.of(componentId).forEach(component -> player.getPackets().sendHideIComponent(interfaceId, component, false));
		return this;
	}
	
	/**
	 * Writes a string to a component on an interface
	 * @param componentId
	 * @param context
	 * @return
	 */
	public InterfaceCreator writeString(int componentId, String context) {
		player.getPackets().sendIComponentText(interfaceId, componentId, context);
		return this;
	}
	
	/**
	 * Writes a collection of strings on a collection of components.
	 * @param componentId
	 * @param context
	 * @return
	 */
	public InterfaceCreator writeStrings(int[] componentId, String... context) {
	    Arrays.stream(componentId)
	            .forEach(component -> Arrays.stream(context)
	                    .forEach(text -> player.getPackets().sendIComponentText(interfaceId, component, text))
	            );
	    return this;
	}
	
	/**
	 * Writes a display for component id to the specified params (beginning to end).
	 * @param componentId
	 * @param context
	 * @return
	 */
	public InterfaceCreator showRawIds(int start, int end) {
		IntStream.rangeClosed(start, end).forEach(component -> player.getPackets().sendIComponentText(interfaceId, component, "" + component));
	    return this;
	}
	
	/**
	 * Clears the interface of its components.
	 * @param start
	 * @param end
	 * @return
	 */
	public InterfaceCreator clearBody(int start, int end) {
		IntStream.rangeClosed(start, end).forEach(component -> player.getPackets().sendIComponentText(interfaceId, component, ""));
		return this;
	}
	
	/**
	 * Clears the interface of its components with exception supported.
	 * @param start
	 * @param end
	 * @param ignorableComponents
	 * @return
	 */
	public InterfaceCreator clearBodyExceptions(int start, int end, int... ignorableComponents) {
	    IntStream.rangeClosed(start, end)
	            .filter(component -> !IntStream.of(ignorableComponents).anyMatch(ignored -> ignored == component))
	            .forEach(component -> player.getPackets().sendIComponentText(interfaceId, component, ""));
	    return this;
	}
	
	/**
	 * Sends a Player to a specified component (head). This only works on applicable components.
	 * @param componentId
	 * @return
	 */
	public InterfaceCreator sendPlayerToComponent(int componentId) {
		player.getPackets().sendPlayerOnIComponent(interfaceId, componentId);
		return this;
	}
	
	/**
	 * Sends a NPC to a specified component (head). This only works on applicable components.
	 * @param componentId
	 * @return
	 */
	public InterfaceCreator sendNPCToComponent(int componentId, int npcId) {
		player.getPackets().sendNPCOnIComponent(interfaceId, componentId, npcId);
		return this;
	}
	
	/**
	 * Sends a Item to a specified component. This only works on applicable components (Otherwise item name prints).
	 * @param componentId
	 * @param item
	 * @return
	 */
	public InterfaceCreator sendItemToComponent(int componentId, Item item) {
		player.getPackets().sendItemOnIComponent(interfaceId, componentId, item.getId(), item.getAmount());
		return this;
	}
	
	/**
	 * Sends a Sprite to a specified component.This only works on applicable components.
	 * @param componentId
	 * @param spriteId
	 * @return
	 */
	public InterfaceCreator sendSpriteToComponent(int componentId, int spriteId) {
		player.getPackets().sendIComponentSprite(interfaceId, componentId, spriteId);
		return this;
	}
	
	/**
	 * Sends a Animation to a specified component. This only works on applicable components.
	 * @param componentId
	 * @param animationId
	 * @return
	 */
	public InterfaceCreator sendAnimationToComponent(int componentId, int animationId) {
		player.getPackets().sendIComponentAnimation(animationId, interfaceId, componentId);
		return this;
	}
	
	/**
	 * Sends a Model to a specified component. This only works on applicable components.
	 * @param componentId
	 * @param modelId
	 * @return
	 */
	public InterfaceCreator sendModelToComponent(int componentId, int modelId) {
		player.getPackets().sendIComponentModel(interfaceId, componentId, modelId);
		return this;
	}
	
	/**
	 * Resets any known changes to the interface and opens a 'fresh copy' of the interface.
	 * @return
	 */
	public InterfaceCreator reset() {
		player.getInterfaceManager().closeInterfaces();
		if (items != null)
			items.clear();
		new InterfaceCreator(player, interfaceId);
		return this;
	}
	
	
	/**
	 * Refreshes the {@link ItemsContainer} and renders the allocated data
	 * 
	 * Note: {@link #sendInterSetItemsOptionsScript(int, int, int, int, String...)} is required to display items accordingly
	 * as well as {@link #sendUnlockIComponentOptionSlots(int, int, int, int...)} is required to render the option context menu
	 * @param key
	 * @return
	 */
	public InterfaceCreator refreshItemContainer(int key) {
		player.getPackets().sendItems(key, items.getItemsCopy());
		player.getPackets().sendUpdateItems(key, items.getItemsCopy(), 2);
		return this;
	}
	
	/**
	 * Sends the items to the container with specified instructions.
	 * @param componentId
	 * @param key
	 * @param width
	 * @param height
	 * @param options
	 * @return
	 */
	public InterfaceCreator sendInterSetItemsOptionsScript(int componentId, int key, int width, int height, String... options) {
		player.getPackets().sendInterSetItemsOptionsScript(interfaceId, componentId, key, width, height, options);
		return this;
	}
	
	/**
	 * Unlocks the components in context menu
	 * @param componentId
	 * @param fromSlot
	 * @param toSlot
	 * @param optionsSlots
	 * @return
	 */
	public InterfaceCreator sendUnlockIComponentOptionSlots(int componentId, int fromSlot, int toSlot, int... optionsSlots) {
		player.getPackets().sendUnlockIComponentOptionSlots(interfaceId, componentId, fromSlot, toSlot, optionsSlots);
		return this;
	}
	
	/**
	 * Represents a runnable action event that occurs when the interface is being closed.
	 * @param run
	 * @return
	 */
	public InterfaceCreator onClose(Runnable run) {
		player.setCloseInterfacesEvent(run);
		return this;
	}
}