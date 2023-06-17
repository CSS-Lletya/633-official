package com.rs.game.player.content;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.rs.game.item.Item;
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
	private Player player;
	
	/**
	 * Represents the interface being used to be created.
	 */
	public final int interfaceId;

	/**
	 * Creates a new Interface.
	 * @param player
	 * @param interfaceId
	 */
	public InterfaceCreator(Player player, int interfaceId) {
		this.interfaceId = interfaceId;
		this.player = player;
		player.getInterfaceManager().sendInterface(interfaceId);
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
		new InterfaceCreator(player, interfaceId);
		return this;
	}
}