package com.rs.plugin.impl.npcs.region;

import com.rs.constants.Animations;
import com.rs.constants.NPCNames;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.Utility;

import skills.Skills;

@NPCSignature(name = {}, npcId = { 11281 })
public class ThievingGuildRegionNPCPlugin extends NPCListener {

	private int timesPickedSession;

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1) {
			player.dialogue(new DialogueEventListener(player, NPCNames.PICKPOCKETING_TRAINER_11281) {
				@Override
				public void start() {
					npc(hearty_laugh, "Go ahead... attempt to pick my pockets.");
				}
			});
		} else if (option == 2) {
			player.getMovement().lock(2);
			player.setNextAnimation(Animations.PICKPOCKET);
			player.getSkills().addExperience(Skills.THIEVING, RandomUtility.random(2, 3));
			timesPickedSession++;
		} else if (option == 3) {
			player.dialogue(new DialogueEventListener(player, NPCNames.PICKPOCKETING_TRAINER_11281) {
				@Override
				public void start() {
					npc(hearty_laugh, "So far you've successfully completed "
							+ Utility.getFormattedNumber(timesPickedSession) + " this session.");
				}
			});
		}
	}
}