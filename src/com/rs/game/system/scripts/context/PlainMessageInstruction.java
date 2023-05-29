package com.rs.game.system.scripts.context;

import java.util.Arrays;

import com.rs.game.player.Player;
import com.rs.game.system.scripts.ScriptContext;

/**
 * Handles a send dialogue plain message to a player.
 * @author Emperor
 *
 */
public class PlainMessageInstruction extends ScriptContext {

	/**
	 * The messages to send.
	 */
	private final String[] messages;

	/**
	 * Constructs a new {@code PlainMessageInstruction} {@code Object}.
	 */
	public PlainMessageInstruction() {
		this(null);
	}
	
	/**
	 * Constructs a new {@code PlainMessageInstruction} {@code Object}.
	 * @param messages the messages.
	 */
	public PlainMessageInstruction(String[] messages) {
		super("plainmessage");
		super.setInstant(false);
		this.messages = messages;
	}

	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		player.getDialogueInterpreter().sendBasicMessage(messages);
		player.getDialogueInterpreter().setDialogueStage(this);
		return true;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(messages);
	}

	@Override
	public ScriptContext parse(Object... params) {
		String[] messages = new String[params.length];
		int messageIndex = 0;
		for (int i = 0; i < params.length; i++) {
			Object o = params[i];
			if (o instanceof String) {
				messages[messageIndex++] = (String) o;
			}
		}
		if (messageIndex != messages.length) {
			messages = Arrays.copyOf(messages, messageIndex);
		}
		PlainMessageInstruction context = new PlainMessageInstruction(messages);
		context.parameters = params;
		return context;
	}
}