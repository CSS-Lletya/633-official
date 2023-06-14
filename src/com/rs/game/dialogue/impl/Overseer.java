package com.rs.game.dialogue.impl;

import com.rs.content.mapzone.impl.SawmillMapZone;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

public class Overseer extends DialogueEventListener {

	private SawmillMapZone zone;

	public Overseer(Player player, NPC npc, SawmillMapZone zone) {
		super(player, npc);
		this.zone = zone;
	}

	@Override
	public void start() {
		zone = (SawmillMapZone) player.getCurrentMapZone().get();
		npc(sad, "Yes? What do you want? I'm very busy!");
		option("Select an option...", "I'd like to complete the job order", () -> {
			if (!zone.isOrderCompleted(player)) {
				npc(sad, "I don't think you've got all the planks you <br>need for this order. Come back when you're done.");
			} else {
				npc(happy, "Good job! I'll fill in the paperwork and send them on<br> their way.");
				zone.finishJob(player);
			}
		}, "What is this place?", () -> {
			player(happy, "What is this place?");
			npc(happy,
					"This is the sawmill training area, where we take<br> more qualified woodcutters on work experience.");
			player(happy, "Work experience?");
			npc(happy, "Yeah. You work, you gain experience.");
			npc(happy, "You DO NOT gain planks, as I have to tell everyone.");
			player(question, "Not even a few...");
			npc(angry_2, "NO PLANKS!");
			player(happy, "Okay. okay.");
		}, "Who are you?", () -> {
			npc(happy, "The name's Jill. I'm the sawmill overseer.");
			player(happy, "Pleased to meet y-");
			npc(angry_2, "I'm in charge of this place, soo no fooling around!");
		}, "What should I be doing?", () -> {
			npc(happy,
					"Well, first grab a job from the board. There's short<br> and long jobs: pick whichever you fancy.");
			npc(happy, "Then you stock up on wood from the piles and stack it in the hopper.");
			npc(happy,
					"The machine will do its magic and, before you know <br>it, you'll have a stack of planks at the other end.");
			npc(happy, "Take these, with a saw, to the workbench to cut the <br>planks you need.");
			npc(happy,
					"All the finished planks go straight into the cart, but<br> you can take out any excess planks and use them<br> for the next batch, if you like.");
			npc(happy, "Just let me know when you're ready to send an order.");
		}, "Nothing, thanks.", () -> {
			this.complete();
		});
	}

}