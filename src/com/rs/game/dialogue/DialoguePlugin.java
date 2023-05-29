package com.rs.game.dialogue;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

/**
 * Represents a dialogue plugin.
 * @author Emperor
 *
 */
public abstract class DialoguePlugin {
	
	/**
	 * Represents the red string.
	 */
	protected static final String RED = "<col=8A0808>";
	
	/**
	 * Represents the blue string.
	 */
	protected static final String BLUE = "<col=08088A>";

	/**
	 * The player.
	 */
	protected Player player;

	/**
	 * The dialogue interpreter.
	 */
	protected ScriptDialogueInterpreter interpreter;

	/**
	 * Two options interface.
	 */
	protected final int TWO_OPTIONS = 228;

	/**
	 * Three options interface.
	 */
	protected final int THREE_OPTIONS = 230;

	/**
	 * Four options interface.
	 */
	protected final int FOUR_OPTIONS = 232;	

	/**
	 * Five options interface.
	 */
	protected final int FIVE_OPTIONS = 234;

	/**
	 * The NPC the player is talking with.
	 */
	protected NPC npc;

	/**
	 * The current dialogue stage.
	 */
	protected int stage;

	/**
	 * If the dialogue is finished.
	 */
	protected boolean finished;

	/**
	 * Constructs a new {@code DialoguePlugin} {@code Object}.
	 */
	public DialoguePlugin() {
		/*
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code DialoguePlugin} {@code Object}.
	 * @param player The player.
	 */
	public DialoguePlugin(Player player) {
		this.player = player;
		if (player != null) {
			this.interpreter = player.getDialogueInterpreter();
		}
	}

	/**
	 * Initializes this plugin.
	 */
	public void init() {
		for (int id : getIds()) {
			ScriptDialogueInterpreter.add(id, this);
		}
	}

	/**
	 * Closes <b>(but does not end)</b> the dialogue.
	 * @return {@code True} if the dialogue succesfully closed.
	 */
	public boolean close() {
		player.getInterfaceManager().closeChatBoxInterface();
		finished = true;
		return true;
	}

	/**
	 * Increments the stage variable.
	 */
	public void increment() {
		stage++;
	}

	/**
	 * Increments the stage variable.
	 * @return The stage variable.
	 */
	public int getAndIncrement() {
		return stage++;
	}

	/**
	 * Ends the dialogue.
	 */
	public void end() {
		if (interpreter != null) {
			interpreter.close();
		}
	}

	/**
	 * Opens the dialogue.
	 * @param args The arguments.
	 * @return {@code True} if the dialogue plugin succesfully opened.
	 */
	public abstract boolean open(Object...args);

	/**
	 * Handles the progress of this dialogue..
	 * @param args The arguments.
	 * @return {@code True} if the dialogue has started.
	 */
	public abstract boolean handle(int interfaceId, int buttonId);

	/**
	 * Gets the ids of the NPCs using this dialogue plugin.
	 * @return The array of NPC ids.
	 */
	public abstract int[] getIds();

	/**
	 * Checks if the dialogue plugin is finished.
	 * @return {@code True} if so.
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the stage.
	 * @param i the stage.
	 */
	public void setStage(int i) {
		this.stage = i;
	}

}