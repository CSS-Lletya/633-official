package com.rs.game.system.scripts.context;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.system.scripts.ScriptContext;

/**
 * Handles a send item message to a player.
 * @author 'Vexia
 *
 */
public class ItemMessageInstruction extends ScriptContext {

	/**
	 * The item id.
	 */
	private final int id;
	
	/**
	 * The message to send.
	 */
	private final String message;

	/**
	 * Constructs a new {@code PlainMessageInstruction} {@code Object}.
	 */
	public ItemMessageInstruction() {
		this(-1, null);
	}
	
	/**
	 * Constructs a new {@code PlainMessageInstruction} {@code Object}.
	 * @param messages the messages.
	 */
	public ItemMessageInstruction(int id, final String message) {
		super("itemmessage");
		super.setInstant(false);
		this.id = id;
		this.message = message;
	}

	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		player.getDialogueInterpreter().sendItemMessage(id, message);
		player.getInventory().addItem(new Item(id));
		return true;
	}

	@Override
	public ScriptContext parse(Object... params) {
		int id = (Integer) params[0];
		String message = (String) params[1];
		ItemMessageInstruction context = new ItemMessageInstruction(id, message);
		context.parameters = params;
		return context;
	}
}