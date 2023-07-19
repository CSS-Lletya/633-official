package com.rs.plugin.impl.npcs;

import java.util.stream.IntStream;

import com.rs.game.dialogue.impl.SkillMasterDialogue;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

import skills.SkillcapeMasters;

@NPCSignature(name = {}, npcId = { 8269, 705, 961, 682, 802, 1658, 847, 4906, 575, 308, 4946, 805, 3295, 455, 437, 2270,
		3299, 13632, 5113, 9713 })
public class SkillcapeMastersNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1) {
			int[] npcIds = { 8269, 705, 961, 682, 802, 1658, 847, 4906, 575, 308, 4946, 805, 3295, 455, 437, 2270, 3299,
					13632, 5113, 9713 };
			SkillcapeMasters[] skillcapeMasters = { SkillcapeMasters.Strength, SkillcapeMasters.Defence,
					SkillcapeMasters.Constitution, SkillcapeMasters.Ranging, SkillcapeMasters.Prayer,
					SkillcapeMasters.Magic, SkillcapeMasters.Cooking, SkillcapeMasters.Woodcutting,
					SkillcapeMasters.Fletching, SkillcapeMasters.Fishing, SkillcapeMasters.Firemaking,
					SkillcapeMasters.Crafting, SkillcapeMasters.Mining, SkillcapeMasters.Herblore,
					SkillcapeMasters.Agility, SkillcapeMasters.Thieving, SkillcapeMasters.Farming,
					SkillcapeMasters.Runecrafting, SkillcapeMasters.Hunter, SkillcapeMasters.Dungeoneering };

			IntStream.range(0, npcIds.length).filter(i -> npc.getId() == npcIds[i]).findFirst()
					.ifPresent(index -> player.dialogue(new SkillMasterDialogue(player, npc, skillcapeMasters[index])));
		}
	}
}