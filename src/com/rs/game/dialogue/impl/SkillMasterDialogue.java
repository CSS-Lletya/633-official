package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import skills.SkillcapeMasters;

public class SkillMasterDialogue extends DialogueEventListener {

	private final SkillcapeMasters master;

	public SkillMasterDialogue(Player player, SkillcapeMasters master, int npcIds) {
		super(player, npcIds);
		this.master = master;
	}

	@Override
	public void start() {
		option("What is that cape you're wearing?", () -> {
			npc(happy_plain_eyebrows_up, "Ah, this is a Skillcape of " + master.name() + ". I have mastered the art of " + master.name().toLowerCase()
					+ "and wear it proudly to show others.");
			player(happy, "Hmm, interesting.");
			if (player.getSkills().getTrueLevel(master.ordinal()) >= 99) {
				npc(happy, "Ah, but I see you are already "+master.verb+", perhaps you have come to me to purchase a Skillcape of "+master.name()+" and thus join the elite few who have mastered this exacting skill?");
				option("99,000 coins? That's much too expensive.", () -> {
					player(sad, "99,000 coins? That's much too expensive.");
					npc(angry_2, "...");
				}, "I think I have the money right here, actually.", () -> master.startTransaction(player));
			} else {
				option("Please tell me more about skillcapes.", () -> {
					player(happy, "Please tell me more about skillcapes.");
					npc(happy, " Of course. Skillcapes are a symbol of achievement. Only people who have mastered a skill and reached level 99 can get their hands on them and gain the benefits they carry.");
				}, "Bye.", this::complete);
			}
		}, "Bye.", this::complete);
	}
}