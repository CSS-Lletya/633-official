package com.rs.game.player.queue;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

public abstract class PlayerScript implements RSScript<Player> {

	protected Player player;

	@Override
	public final void process(Player player) {
		this.player = player;
		process();
	}

	/**
	 * Processes this {@code PlayerScript} for the given {@code player}.
	 */
	public abstract void process();

	/**
	 * Queues the given script to the player
	 */
	public void script(RSScript<Player> script) {
		player.getScripts().queue(script);
	}

	/**
	 * Delays the player from executing any other scripts until the given ticks
	 * pass.
	 */
	public void delay(int ticks) {
		player.getScripts().delay(ticks);
	}

	/**
	 * Teleports the player to the given location
	 */
	public void teleport(WorldTile destination) {
		player.resetWalkSteps();
		player.setLocation(destination);
	}

	/**
	 * Animates the player
	 */
	public void animate(Animation animationID) {
		player.setNextAnimation(animationID);
	}

	/**
	 * Sends a graphic to the player
	 */
	public void gfx(int gfxID) {
		player.setNextGraphics(new Graphics(gfxID));
	}

	/**
	 * Sends a message to the player
	 */
	public void msg(String string, Object... arguments) {
//		player.sendMessage(string, arguments);
		player.getPackets().sendGameMessage(string);
	}

	/**
	 * Opens an interface for the player
	 */
	public void openInterface(int window) {
		player.getInterfaceManager().sendInterface(window);
	}

	/**
	 * Adds the given item to the player's inventory
	 */
	public void addItem(Item item) {
		player.getInventory().addItem(item);
	}

	/**
	 * Removes the given item from the player's inventory
	 */
	public void removeItem(Item item) {
		player.getInventory().removeItems(item);
	}
	
	public Player getPlayer() {
		return player;
	}
}