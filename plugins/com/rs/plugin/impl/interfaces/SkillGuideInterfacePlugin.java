package com.rs.plugin.impl.interfaces;

import com.rs.constants.InterfaceVars;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.IntegerInputAction;

import skills.LevelUp;

@RSInterfaceSignature(interfaceId = {320, 499})
public class SkillGuideInterfacePlugin implements RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) throws Exception {
		if (player.getInterfaceManager().containsChatBoxInter())
			player.getInterfaceManager().closeChatBoxInterface();
		if (interfaceId == 499)
			updateSkillGuide(player, componentId);
		if (packetId == 11)
				sendSkillGuide(player, componentId);
		if (packetId == 9) {
			int skillComponentId = player.getSkills().getTargetIdByComponentId(componentId);
            player.getSkills().setSkillTargetEnabled(skillComponentId, false);
            player.getSkills().setSkillTargetValue(skillComponentId, 0);
            player.getSkills().setSkillTargetUsingLevelMode(skillComponentId, false);
		}
		if (packetId == 31 || packetId == 29) {
			int skillComponentId = player.getSkills().getTargetIdByComponentId(componentId);
			boolean usingLevel = packetId == 29;
			player.getAttributes().get((usingLevel ? Attribute.SET_LEVEL : Attribute.SET_XP)).set(skillComponentId);
			player.getPackets().sendInputIntegerScript(
					"Please enter target " + (usingLevel ? "level" : "xp") + " you want to set: ",
					new IntegerInputAction() {
						@Override
						public void handle(int input) {
							if (usingLevel) {
								int levelTarget = input;
								Integer skillId = player.getAttributes().get(Attribute.SET_LEVEL).getInt();
								int curLevel = player.getSkills()
										.getLevelForXp(player.getSkills().getSkillIdByTargetId(skillId));
								if (curLevel >= (skillId == 24 ? 120 : 99)) {
									return;
								}
								if (levelTarget > (skillId == 24 ? 120 : 99)) {
									levelTarget = skillId == 24 ? 120 : 99;
								}
								if (levelTarget < player.getSkills()
										.getLevel(player.getSkills().getSkillIdByTargetId(skillId))) {
									return;
								}
								System.out.println("id: " + input);
								player.getSkills().setSkillTarget(true, skillId, input);
							} else {
								int xpTarget = input;
								Integer skillId = player.getAttributes().get(Attribute.SET_XP).getInt();
								if (xpTarget < player.getSkills()
										.getXp(player.getSkills().getSkillIdByTargetId(skillId))
										|| player.getSkills()
												.getXp(player.getSkills().getSkillIdByTargetId(skillId)) >= 200000000) {
									return;
								}
								if (xpTarget > 200000000) {
									xpTarget = 200000000;
								}
								player.getSkills().setSkillTarget(false, skillId, xpTarget);
							}
						}
					});
		}
	}
    /**
     * Sends the skill guide interface.
     *
     * @param player   The player.
     * @param buttonId The button id.
     * @return {@code True}.
     */
    private boolean sendSkillGuide(Player player, int buttonId) {
        int slot = 0;
        for (slot = 0; slot < SKILL_GUIDE_DATA.length; slot++) {
            if (SKILL_GUIDE_DATA[slot][0] == buttonId) {
                break;
            }
        }
        if (player.getSkills().getLeveledUp()[slot]) {
        	player.getVarsManager().sendVar(InterfaceVars.SKILL_CONGRATULATIONS_LEVEL_UP_INFORMATION, SKILL_GUIDE_DATA[slot][1]);
        	 player.getInterfaceManager().sendFullscreenInterface(5, 741);
            player.getSkills().getLeveledUp()[slot] = false;
            LevelUp.sendFlashIcons(player);
            return true;
        }
        int value = SKILL_GUIDE_DATA[slot][2];
        player.getSkills().getLeveledUp()[slot] = false;
        player.getVarsManager().sendVar(InterfaceVars.SKILL_SKILL_GUIDE_DATA, value);
        player.getInterfaceManager().sendFullscreenInterface(5, 499);
        player.getAttributes().get(Attribute.SKILL_GUIDE_MENU).set(value);
        return true;
    }
    
    /**
     * Updates the skill guide when clicking on the skill guide menu.
     *
     * @param player   The player.
     * @param buttonId The buttonId.
     * @return {@code True} if succesful, {@code false} if not..
     */
    private boolean updateSkillGuide(Player player, int buttonId) {
        int skillMenu = (int)player.getAttributes().get(Attribute.SKILL_GUIDE_MENU).get();
        if (skillMenu == -1) {
            return false;
        }
        player.getVarsManager().sendVar(InterfaceVars.SKILL_SKILL_GUIDE_DATA, (1024 * (buttonId - 10)) + skillMenu);
        return true;
    }
    
    /**
     * The skill guide data. (actionbuttonId, levelup config value, normal config value)
     */
    private static final int[][] SKILL_GUIDE_DATA = {
            {200, 10, 1}, {28, 40, 5}, {11, 20, 2}, {193, 50, 6}, {52, 30, 3},
            {76, 60, 7}, {93, 33, 4}, {68, 641, 16}, {165, 660, 18}, {101, 665, 19},
            {44, 120, 15}, {172, 649, 17}, {84, 90, 11}, {179, 115, 14}, {186, 110, 13},
            {36, 75, 9}, {19, 65, 8}, {60, 80, 10}, {118, 673, 20}, {126, 681, 21}, {110, 100, 12},
            {134, 689, 22}, {142, 698, 23}, {150, 705, 24}, {158, 715, 25}
    };
}