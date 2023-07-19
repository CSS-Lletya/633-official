package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

import skills.SkillcapeMasters;

public class SkillMasterDialogue extends DialogueEventListener {

	private final SkillcapeMasters master;

	public SkillMasterDialogue(Player player, NPC npc, SkillcapeMasters master) {
		super(player, npc);
		this.master = master;
	}

	@Override
	public void start() {
		option("What is that cape you're wearing?", () -> {
			npc(happy_plain_eyebrows_up, "Ah, this is a Skillcape of " + master.name() + ". <br> I have mastered the art of " + master.name().toLowerCase()
					+ "<br>and wear it proudly to show others.");
			player(afraid, "Hmm, interesting.");
			if (player.getSkills().getTrueLevel(master.ordinal()) >= 99) {
				npc(happy, "Ah, but I see you are already "+master.verb+", perhaps you have <br>come to me to purchase a Skillcape of "+master.name()+"<br>, and thus join the elite few who<br> have mastered <br>this exacting skill?");
				option("Yes, I'd like to buy one please.", () -> {
					player(sad, "99,000 coins? That's much too expensive.");
					npc(angry_2, "...");
				}, "I think I have the money right here, actually.", () -> master.startTransaction(player));
			} else {
				option("Please tell me more about skillcapes.", () -> {
					player(happy, "Please tell me more about skillcapes.");
					npc(happy, " Of course. Skillcapes are a symbol of achievement. Only people who have mastered a skill and <br> reached level 99 can get their hands on them and gain the benefits they carry.");
				}, "Bye.", () -> this.complete());
			}
		}, "Bye.", () -> this.complete());
	}
}