package com.rs.content.quests.impl;

import com.rs.content.quests.Quest;
import com.rs.content.quests.QuestAttribute;
import com.rs.game.item.Item;
import com.rs.game.item.ItemNames;
import com.rs.game.map.GameObject;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

import skills.Skills;

public class DemoQuest extends Quest {

	@Override
	public String getName() {
		return "Test Quest";
	}

	@Override
	public String[] getReward() {
		return new String[] {"Nothing, it's over!"};
	}

	@Override
	public int getComponentId() {
		return 0;
	}

	@Override
	public String[] getInformation(Player player) {
		if (!startedQuest(player))
            return new String[]{"You have not yet started this quest!", "This is just a demo class & quest text", "This is just a demo class & quest text", "This is just a demo class & quest text", "This is just a demo class & quest text", "This is just a demo class & quest text", "This is just a demo class & quest text", "This is just a demo class & quest text"};
        switch (getQuestStage(player)) {
            case 1:
                return new String[]{"I should talk to the Lumbridge Cook firstly."};
            case 2:
                return new String[]{"I have completed the quest: " + getName()};
            default:
                return new String[]{};
        }
	}

	@Override
	public void addRequirements(Player player) {
        addQuestRequirement(new QuestAttribute("I must have at least 50 Cooking", player.getSkills().getLevel(Skills.COOKING) >= 50, false));
        addQuestRequirement(new QuestAttribute("I must be willing to take damage when passing by monsters", true, true));
	}

	@Override
	public int rewardItemForDisplay() {
		return ItemNames.CAKE;
	}

	@Override
	public int questPointReward() {
		return 1;
	}

	@Override
	public boolean handleDroppedItem(Player player, Item item) {
		return false;
	}

	/*	NOTE: 
	 * If you want you can use this method of interactions for npcs/object
	 * or utilize the plugin classes. These are optonal boolean methods. Yo dont need
	 * them if you use the Plugin system.
	 */
	@Override
	public boolean handleNPC(Player player, NPC npc, int option) {
		if (npc.getId() == 0) {
			npc.getDefinitions().doAction(option, "Talk-to", () -> {

			});
		}
		return false;
	}
	
	@Override
	public boolean handleObject(Player player, GameObject object) {
		return false;
	}

	@Override
	public int stageCount() {
		return 2;
	}
}