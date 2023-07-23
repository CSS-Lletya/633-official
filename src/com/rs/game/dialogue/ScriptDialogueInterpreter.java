package com.rs.game.dialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.system.scripts.ScriptContext;
import com.rs.game.system.scripts.ScriptManager;
import com.rs.game.system.scripts.context.ItemMessageInstruction;
import com.rs.game.system.scripts.context.NPCDialInstruction;
import com.rs.game.system.scripts.context.OptionDialInstruction;
import com.rs.game.system.scripts.context.PDialInstruction;
import com.rs.game.system.scripts.context.PlainMessageInstruction;

/**
 * Handles the dialogues.
 * @author Emperor
 *
 */
public class ScriptDialogueInterpreter {

	/**
	 * The dialogue plugins.
	 */
	private static final Map<Integer, DialoguePlugin> PLUGINS = new HashMap<>();

	/**
	 * The dialogue scripts.
	 */
	private static final Map<Integer, ScriptContext> SCRIPTS = new HashMap<>();
	
	/**
	 * a List of dialogue actions.
	 */
	private final List<DialogueAction> actions = new ArrayList<>();

	/**
	 * The currently opened dialogue.
	 */
	private DialoguePlugin dialogue;

	/**
	 * Scripted dialogue current stage.
	 */
	private ScriptContext dialogueStage;

	/**
	 * The current dialogue key.
	 */
	private int key;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code DialogueInterpreter} {@code Object}.
	 * @param player The player.
	 */
	public ScriptDialogueInterpreter(Player player) {
		this.player = player;
	}

	/**
	 * @param dialogue the dialogue to set.
	 */
	public void setDialogue(DialoguePlugin dialogue) {
		this.dialogue = dialogue;
	}

	/**
	 * Opens the dialogue for the given dialogue type.
	 * @param dialogueType The dialogue type.
	 * @param args the args.
	 * @return {@code True} if successful.
	 */
	public boolean open(String dialogueType, Object...args) {
		return open(getDialogueKey(dialogueType), args);
	}

	/**
	 * Opens the dialogue for the given NPC id.
	 * @param dialogueKey The dialogue key (usually NPC id).
	 * @param args The arguments.
	 * @return {@code True} if successful.
	 */
	public boolean open(int dialogueKey, Object...args) {
		key = dialogueKey;
		if (args.length < 1) {
			args = new Object[] { dialogueKey };
		}
		ScriptContext script = SCRIPTS.get(dialogueKey);
		if (script != null) {
			Object[] arguments = new Object[args.length + 1];
			for (int i = 0; i < args.length; i++) {
				arguments[i + 1] = args[i];
			}
			arguments[0] = player;
			startScript(script, arguments);
			return true;
		}
		return true;
	}
	
	/**
	 * Starts a dialogue script.
	 * @param script The script.
	 * @param args The arguments.
	 */
	public void startScript(ScriptContext script, Object...args) {
		startScript(key, script, args);
	}

	/**
	 * Starts a dialogue script.
	 * @param dialogueKey The dialogue key.
	 * @param script The script.
	 * @param args The arguments.
	 */
	public void startScript(int dialogueKey, ScriptContext script, Object...args) {
		key = dialogueKey;
		(dialogueStage = script).execute(args);
		if (script != null && script.isInstant()) {
			dialogueStage = script = ScriptManager.run(script, args);
		}
	}
	
	/**
	 * Handles an dialogue input.
	 * @param componentId The id of the chatbox component.
	 * @param buttonId The button id.
	 */
	public void handle(int componentId, int buttonId) {
		if (dialogueStage != null) {
			dialogueStage = ScriptManager.run(dialogueStage, player, key, buttonId);
			if (!(dialogueStage instanceof OptionDialInstruction || dialogueStage instanceof PDialInstruction || dialogueStage instanceof NPCDialInstruction || dialogueStage instanceof PlainMessageInstruction || dialogueStage instanceof ItemMessageInstruction)) {
				player.getInterfaceManager().closeChatBoxInterface();
			}
			return;
		}
		player.getDialogueInterpreter().getDialogue().handle(componentId, buttonId);
	}

	/**
	 * Closes the current dialogue.
	 * @return {@code True} if successful.
	 */
	public boolean close() {
		if (dialogue != null || dialogueStage != null) {
			actions.clear();
			if (dialogueStage != null) {
				dialogueStage = null;
				player.getInterfaceManager().closeChatBoxInterface();
			}
			if (dialogue != null && dialogue.close()) {
				dialogue = null;
			}
		}
		return dialogue == null && dialogueStage == null;
	}

	/**
	 * Puts a dialogue plugin on the mapping.
	 * @param id The NPC id (or {@code 1 << 16 | dialogueId} when the dialogue isn't for an NPC).
	 * @param plugin The plugin.
	 */
	public static void add(int id, DialoguePlugin plugin) {
		if (PLUGINS.containsKey(id)) {
			throw new IllegalArgumentException("Dialogue " + (id & 0xFFFF) + " is already in use - [old=" + PLUGINS.get(id).getClass().getSimpleName() + ", new=" + plugin.getClass().getSimpleName() + "]!");
		}
		PLUGINS.put(id, plugin);
	}

	/**
	 * Adds a dialogue script for the given key.
	 * @param dialogueKey The dialogue key.
	 * @param context The dialogue script.
	 */
	public static void add(int dialogueKey, ScriptContext context) {
		if (SCRIPTS.containsKey(dialogueKey)) {
			throw new IllegalArgumentException("Dialogue " + dialogueKey + " is already in use - [old="
					+ SCRIPTS.get(dialogueKey).getClass().getSimpleName() + ", new="
					+ context.getClass().getSimpleName() + "]!");
		}
		SCRIPTS.put(dialogueKey, context);
	}
	
	/**
	 * Gets the script context for the given dialogue key.
	 * @param key The dialogue key.
	 * @return The script context.
	 */
	public static ScriptContext getScript(int key) {
		return SCRIPTS.get(key);
	}
	
	/**
	 * Send a message with an item next to it.
	 * @param itemId The item id.
	 * @param message The message.
	 */
	public void sendItemMessage(int itemId, String...messages) {
		String message = messages[0];
		for (int i = 1; i < messages.length; i++) {
			message += "<br>" + messages[i];
		}
		player.getInterfaceManager().sendChatBoxInterface(131);
		player.getPackets().sendIComponentText(131, 1, message);
		player.getPackets().sendItemOnIComponent(131, 2, itemId, 1);
		player.getPackets().sendHideIComponent(131, 3, false);
	}
	
	/**
	 * Send a message with an item next to it.
	 * @param itemId The item id.
	 * @param message The message.
	 */
	public void sendBasicMessage(String...messages) {
		String message = messages[0];
		for (int i = 1; i < messages.length; i++) {
			message += "<br>" + messages[i];
		}
		player.getInterfaceManager().sendChatBoxInterface(210);
		player.getPackets().sendIComponentText(210, 1, message);
		player.getPackets().sendHideIComponent(210, 2, false);
	}
	

	/**
	 * Send dialogues based on the amount of specified messages.
	 * @param entity The entity.
	 * @param expression The entity's facial expression.
	 * @param messages The messages.
	 * @return The chatbox component.
	 */
	public void sendEntityDialogues(Entity entity, int expression, String... messages) {
		sendEntityDialogue(entity instanceof Player ? -1 : ((NPC) entity).getId(), expression, false, messages);
	}
	

	public void sendEntityDialogue(Entity entity, int expression, boolean hide, String... messages) {
		sendEntityDialogues(entity, expression, messages);
	}

	public void sendEntityDialogue(int npcId, int expression, boolean hideContinue, String[] messages) {
		if (messages.length < 1 || messages.length > 4) {
			return;
		}
		boolean npc = npcId > -1;
		int interfaceId = (npc ? 240 : 63) + messages.length;
		if (expression == -1) {
			expression = Mood.plain_talking;
		}
		player.getPackets().sendIComponentText(interfaceId, 3, (npc ? NPCDefinitions.getNPCDefinitions(npcId).getName() : player.getDisplayName()));
		for (int i = 0; i < messages.length; i++) {
			player.getPackets().sendIComponentText(interfaceId, (i + 4), messages[i].toString().replace("@name", player.getDisplayName()));
		}
		if (npc)
			player.getPackets().sendNPCOnIComponent(interfaceId, 2, npcId);
		else
			player.getPackets().sendPlayerOnIComponent(interfaceId, 2);
		player.getPackets().sendHideIComponent(interfaceId, 5, hideContinue);
		player.getPackets().sendIComponentAnimation(expression, interfaceId, 2);
		player.getInterfaceManager().sendChatBoxInterface(interfaceId);
	}
	
	/**
	 * Send options based on the amount of specified options.
	 * @param title The title.
	 * @param options The options.
	 */
	public void sendOptions(Object title, String... options) {
		int interfaceId = 224 + (2 * options.length);
		if (options.length < 2 || options.length > 5) {
			return;
		}
		for (int i = 0; i < options.length; i++) {
			player.getPackets().sendIComponentText(interfaceId, i + 2, options[i].toString());
		}
		player.getInterfaceManager().sendChatBoxInterface(interfaceId);
	}

	/**
	 * Checks if the dialogue for the given id is added.
	 * @param id The NPC id/dialogue id.
	 * @return {@code True} if so.
	 */
	public static boolean contains(int id) {
		return PLUGINS.containsKey(id);
	}

	/**
	 * Gets the currently opened dialogue.
	 * @return The dialogue plugin.
	 */
	public DialoguePlugin getDialogue() {
		return dialogue;
	}

	/**
	 * Reserves a key for the name.
	 * @param name The name.
	 * @return The key.
	 */
	public static int getDialogueKey(String name) {
		return 1 << 16 | name.hashCode();
	}

	/**
	 * Gets the dialogueStage.
	 * @return The dialogueStage.
	 */
	public ScriptContext getDialogueStage() {
		return dialogueStage;
	}

	/**
	 * Sets the dialogueStage.
	 * @param dialogueStage The dialogueStage to set.
	 */
	public void setDialogueStage(ScriptContext dialogueStage) {
		this.dialogueStage = dialogueStage;
	}

	/**
	 * Adds a dialogue action.
	 * @param action the action.
	 */
	public void addAction(DialogueAction action) {
		actions.clear();
		actions.add(action);
	}
	
	/**
	 * Gets the actions.
	 * @return The actions.
	 */
	public List<DialogueAction> getActions() {
		return actions;
	}
}