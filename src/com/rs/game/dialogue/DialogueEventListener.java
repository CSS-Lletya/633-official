package com.rs.game.dialogue;

import java.util.ArrayList;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;

public abstract class DialogueEventListener implements Mood {

	private ArrayList<DialogueEvent> dialogueEvent = new ArrayList<DialogueEvent>();

	private int page, previousOptionPressed;

	protected transient Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	protected Object[] args;

	public DialogueEventListener event(Runnable run) {
		dialogueEvent.add(new DialogueRunnableEvent(run));
		return this;
	}
	
	public DialogueEventListener mes(String... message) {
		dialogueEvent.add(new DialogueEvent((byte) 0, message));
		return this;
	}
	
	public DialogueEventListener dualEntity(int face, String message) {
		dialogueEvent.add(new DialogueDualEntityEvent(face, message));
		return this;
	}

	public DialogueEventListener player(int face, String... message) {
		dialogueEvent.add(new DialogueEntityEvent(true, face, message));
		return this;
	}

	public DialogueEventListener npc(int face, String... message) {
		dialogueEvent.add(new DialogueEntityEvent(false, face, message));
		return this;
	}

	public DialogueEventListener item(int itemId, String message) {
		return item(itemId, 1, message);
	}

	public DialogueEventListener item(int itemId, int amount, String message) {
		dialogueEvent.add(new DialogueItemEvent(itemId, amount, message));
		return this;
	}

	public void option(String option1, Runnable task) {
		dialogueEvent.add(new DialogueOptionEvent("Select an Option", option1, task, "Nevermind", this::complete));
	}

	public void option(String option1, Runnable task1, String option2, Runnable task2) {
		dialogueEvent.add(new DialogueOptionEvent("Select an Option", option1, task1, option2, task2));
	}

	public void option(String option1, Runnable task1, String option2, Runnable task2, String option3, Runnable task3) {
		dialogueEvent.add(new DialogueOptionEvent("Select an Option", option1, task1, option2, task2, option3, task3));
	}

	public void option(String option1, Runnable task1, String option2, Runnable task2, String option3, Runnable task3,
			String option4, Runnable task4) {
		dialogueEvent.add(new DialogueOptionEvent("Select an Option", option1, task1, option2, task2, option3, task3, option4, task4));
	}

	public void option(String option1, Runnable task1, String option2, Runnable task2, String option3,
			Runnable task3, String option4, Runnable task4, String option5, Runnable task5) {
		dialogueEvent.add(new DialogueOptionEvent("Select an Option", option1, task1, option2, task2, option3, task3, option4, task4,
				option5, task5));
	}

	public void removeContinue() {
		dialogueEvent.get(dialogueEvent.size() - 1).setRemoveContinue(true);
	}

	public DialogueEventListener(Player player, Object... args) {
		this.player = player;
		this.args = args;
		player.getInterfaceManager().closeInterfaces();
	}

	public abstract void start();

	public void onClose() {

	}

	public DialogueEventListener begin() {
		setPlayer(player);
		start();
		listenToDialogueEvent(0);
		return this;
	}

	public DialogueEventListener beginBlank() {
		setPlayer(player);
		start();
		return this;
	}

	/*
	 * NOTE: cant cast boolean to dialogue class for attribute. Not sure how to fix
	 * tbh, however not using it for close interfaces works. sigh. sorry.
	 */
	public void complete() {
		player.getInterfaceManager().closeChatBoxInterface();
		onClose();
		player.getAttributes().get(Attribute.DIALOGUE_EVENT).set(null);
		player.getAttributes().get(Attribute.BLANK_DIALOGUE_EVENT).set(null);
	}

	/**
	 * 
	 * @return the name of the option the player clicked
	 */
	public String buttonName() {
		DialogueEvent previousDialogue = dialogueEvent.get(Math.max(0, page));
		if (page > 0 && previousDialogue.getType() == 3) {
			DialogueOptionEvent event = (DialogueOptionEvent) previousDialogue;
			return event.getNames()[previousOptionPressed];
		}
		return "null";
	}

	public int ordinalButton(int button) {
		return new int[] { 0, 0, 0, 1, 2, 3, 4, 5 }[button];
	}
	
	private int interfaceId;

	public void listenToDialogueEvent(int button) {
		DialogueEvent previousDialogue = dialogueEvent.get(Math.max(0, page - 1));

		if (page > 0 && previousDialogue.getType() == 3) {
			DialogueOptionEvent event = (DialogueOptionEvent) previousDialogue;
			previousOptionPressed = ordinalButton(button);
			Runnable task = event.getTasks()[previousOptionPressed];
			if (task == null) {
				complete();
				return;
			}
			task.run();
		}

		if (page >= dialogueEvent.size()) {
			complete();
			return;
		}

		DialogueEvent dialogue = dialogueEvent.get(page);

		page++;

		switch (dialogue.getType()) {
		case 0:
			interfaceId = 209 + dialogue.getTexts().length;
			for (int line = 0; line < dialogue.getTexts().length; line++)
				player.getPackets().sendIComponentText(interfaceId, 1+ line, dialogue.getTexts()[line]);
			player.getInterfaceManager().sendChatBoxInterface(interfaceId);

			break;
		case 1: {
			DialogueEntityEvent event = (DialogueEntityEvent) dialogue;

			NPC npc = args.length > 0 ? (NPC) args[0] : null;
			
			if (event.isPlayer()) {
				interfaceId = 63 + dialogue.getTexts().length;
				for (int line = 0; line < dialogue.getTexts().length; line++)
					player.getPackets().sendIComponentText(interfaceId, 4 + line, dialogue.getTexts()[line]);
				player.getInterfaceManager().sendChatBoxInterface(interfaceId);
				player.getPackets().sendIComponentText(interfaceId, 3, player.getDisplayName());
				player.getPackets().sendPlayerOnIComponent(interfaceId, 2);
				player.getPackets().sendIComponentAnimation(event.getFace(), interfaceId, 2);

			} else {
				interfaceId = 240 + dialogue.getTexts().length; 
				for (int line = 0; line < dialogue.getTexts().length; line++)
					player.getPackets().sendIComponentText(interfaceId, 4 + line, dialogue.getTexts()[line]);
				player.getInterfaceManager().sendChatBoxInterface(interfaceId);
				player.getPackets().sendIComponentText(interfaceId, 3, header(npc));
				player.getPackets().sendNPCOnIComponent(interfaceId, 2, npc.getId());
				player.getPackets().sendIComponentAnimation(event.getFace(), interfaceId, 2);
			}

			break;
		}
		case 2: {
			DialogueItemEvent event2 = (DialogueItemEvent) dialogue;
			player.getInterfaceManager().sendChatBoxInterface(131);
			player.getPackets().sendIComponentText(131, 1, dialogue.getText());
			player.getPackets().sendItemOnIComponent(131, 2, event2.getItemId(), event2.getAmount());
			player.getPackets().sendHideIComponent(131, 3, event2.isRemoveContinue());

			break;
		}
		case 3: {
			DialogueOptionEvent event3 = (DialogueOptionEvent) dialogue;
			int index = 2;

			String[] options = event3.getOptionTextArray();
			int interfaceId = 229
					+ (options.length == 2 ? -1 : options.length == 5 ? options.length + 1 : options.length - 1);
			for (String string : options) {
				player.getPackets().sendIComponentText(interfaceId, index++, string);
			}

			player.getInterfaceManager().sendChatBoxInterface(interfaceId);

			break;
		}
		case 4:
			DialogueRunnableEvent runnableEvent = (DialogueRunnableEvent) dialogue;
			runnableEvent.getRun().run();
			break;
		case 5:
			NPC npc = args.length > 0 ? (NPC) args[0] : null;
			DialogueDualEntityEvent event3 = (DialogueDualEntityEvent) dialogue;
			
			player.getInterfaceManager().sendChatBoxInterface(136);
			player.getPackets().sendIComponentText(136, 0, "Both");
			player.getPackets().sendIComponentText(136, 1, dialogue.getText());
			player.getPackets().sendPlayerOnIComponent(136, 2);
			player.getPackets().sendNPCOnIComponent(136, 3, npc.getId());
			player.getPackets().sendIComponentAnimation(event3.getFace(), 136, 2);
			player.getPackets().sendIComponentAnimation(event3.getFace(), 136, 3);
			//note: There's no continue dialogue button, so this is a time sequenced dialogue (usually like both entities saying "oh-noeee")
			break;
//		case 4: {
//			/*
//			 * NOTE: No actual function, this is just a reference point to show that there
//			 * can be two items in the items box, otherwise use type 2 method.
//			 */
//			DialogueItemEvent event4 = (DialogueItemEvent) dialogue;
//			player.getInterfaceManager().sendChatBoxInterface(131);
//			player.getPackets().sendItemOnIComponent(131, 0, event4.getItemId(), event4.getAmount());
//			player.getPackets().sendItemOnIComponent(131, 2, event4.getItemId(), event4.getAmount());
//			player.getPackets().sendIComponentText(131, 1, event4.getText());
//			player.getPackets().sendHideIComponent(131, 3, event4.isRemoveContinue());
//
//			break;
//		}
		}
	}

	private String header(NPC npc) {
		NPCDefinitions defs = npc.getDefinitions();
		if (npc.getId() == 3820) {
			return "Wise Old man";
		} else
			return defs.name;
	}

	public static boolean continueDialogue(Player player, int i) {
		DialogueEventListener dialogue = (DialogueEventListener) player.getAttributes().get(Attribute.DIALOGUE_EVENT).get();
		if (dialogue == null)
			return false;
		dialogue.listenToDialogueEvent(i);
		return true;
	}
	
	public static boolean continueBlankDialogue(Player player, int i) {
		DialogueEventListener dialogueBlank = (DialogueEventListener) player.getAttributes().get(Attribute.BLANK_DIALOGUE_EVENT).get();
		if (dialogueBlank == null)
			return false;
		dialogueBlank.listenToDialogueEvent(i);
		return true;
	}
}