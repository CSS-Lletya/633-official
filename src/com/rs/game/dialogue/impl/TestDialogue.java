package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

public class TestDialogue extends DialogueEventListener {

	public TestDialogue(Player player, NPC npc) {
		super(player, npc);
	}

	@Override
	public void start() {
		mes("yo sup i'm a regular message");
		player(happy, "lol okay");
		option("Yes", () -> {
			System.out.println("hi");
		}, "No", () -> {
			System.out.println("bye");
		}, "maybe", () -> {
			System.out.println("bye");
		}, "idk", () -> {
			player(happy, "lol okay");
			option("Yes", () -> {
				System.out.println("hi");
			}, "No", () -> {
				System.out.println("bye");
			});
			System.out.println("byess");
		});
		npc(sad, "I'm sad man..");
		item(1050, "I'm a santa hat mate.");
	}
}