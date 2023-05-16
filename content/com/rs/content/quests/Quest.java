package com.rs.content.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.ScrollInterface;
import com.rs.utilities.Colors;

public abstract class Quest {

    /**
     * The list of quest requirements
     */
    public List<QuestAttribute> questRequirements = new ArrayList<QuestAttribute>();

    /**
     * The name of the quest.
     */
    public abstract String getName();

    /**
     * The reward from the quest.
     */
    public abstract String[] getReward();

    /**
     * Used for quest color changing, nothing crucial.
     * @return id
     */
    public abstract int getComponentId();

    /**
     * The requirements of the quest.
     *
     * @return The requirements.
     */
    public abstract String[] getInformation(Player player);

    /**
     * If the player can start the quest.
     *
     * @return
     */
    public boolean canStart(Player player) {
        questRequirements.clear();
        addRequirements(player);
        for (QuestAttribute requirement : questRequirements) {
            if (!requirement.isCanContinue()) {
                return false;
            } else if (requirement.ignoreCondition) {
                return true;
            }
        }
        return true;
    }

    /**
     * Checks if the quest is finished
     */
    public boolean isFinished(Player player) {
        return getQuestStage(player) == getLastStage(player);
    }

    /**
     * Starts a quest for the player
     *
     * @param player The player
     */
    public void startQuest(Player player) {
        setQuestStage(player, getFirstStage(player));
        sendStartInformation(player, new String[]{Colors.white + "You have now started: " + getName() + "!", "<br>" + Colors.white + getInformation(player)[0]});
    }

    /**
     * Handles the adding of requirements
     *
     * @param player       The player
     * @param requirements The requirements
     */
    public abstract void addRequirements(Player player);

    /**
     * Gives the player the extra rewards, this method is not necessary unless
     * the quest has extra rewards
     *
     * @param player The player
     */
    public void giveRewards(Player player) {

    }

    /**
     * Adds a quest requirement to its list of requirements
     *
     * @param requirement The quest requirement
     */
    protected void addQuestRequirement(QuestAttribute requirement) {
        questRequirements.add(requirement);
    }

    public int getLastStage(Player player) {
        return stageCount();
    }

    /**
     * Completes a quest for the player
     *
     * @param player The player
     */
    public void completeQuest(Player player) {
        setQuestStage(player, getLastStage(player));
        if (player.isStarted()) {
            player.getMovement().stopAll();

            int interfaceId = 277;

            int componentLength = 18;

            for (int i = 0; i < componentLength; i++) {
                player.getPackets().sendIComponentText(interfaceId, i, "");
            }

            player.getPackets().sendIComponentText(interfaceId, 3, "Congratulations!");
            player.getPackets().sendIComponentText(interfaceId, 4, "You have completed " + getName() + "!");
            player.getPackets().sendIComponentText(interfaceId, 9, "You are awarded:");
            player.getPackets().sendIComponentText(interfaceId, 6, "Quest Points: " + player.getDetails().getQuestPoints());
            int start = 10;
            for (String reward : getReward()) {
                player.getPackets().sendIComponentText(interfaceId, start, reward);
                start++;
            }
            player.getPackets().sendItemOnIComponent(interfaceId, 5, rewardItemForDisplay(), 1);
            player.getInterfaceManager().sendInterface(interfaceId);
            player.getDetails().setQuestPoints(player.getDetails().getQuestPoints() + questPointReward());
            giveRewards(player);
            player.getInterfaceManager().renderQuestStatus();
        }
    }

    ;

    /**
     * This is the item that'll appear on the
     * Quest completed interface.
     *
     * @return item Id
     */
    public abstract int rewardItemForDisplay();

    public abstract int questPointReward();

    /**
     * Handles a item specifically for a quest.
     *
     * @param player The player
     * @param item   The item
     * @return
     */
    public boolean handleDroppedItem(Player player, Item item) {
    	return false;
    }


    /**
     * Handles a npc interaction specifically for a quest.
     *
     * @param player The player
     * @param npc    The npc
     * @return
     */
    public boolean handleNPC(Player player, NPC npc, int option) {
    	return false;
    }
    
    /**
     * Handles a npc interaction specifically for a quest.
     *
     * @param player The player
     * @param npc    The npc
     * @return
     */
    public boolean handleObject(Player player, GameObject object) {
    	return false;
    }

    /**
     * Gets the first stage in the quest.
     *
     * @return
     */
    public Integer getFirstStage(Player player) {
        return IntStream.of(stageCount() - stageCount() + 1).findAny().orElse(0);
    }

    public abstract int stageCount();

    /**
     * Finds the players quest stage from the map of stages
     *
     * @param player The player who we're getting the stage for
     * @return
     */
    public Integer getQuestStage(Player player) {
        return player.getQuestManager().getStage(getName());
    }

    /**
     * Sets the players quest stage to the parameterized one
     *
     * @param player The player who's having a change in stages
     * @param stage  The stage to set
     */
    public void setQuestStage(Player player, Integer stage) {
        player.getQuestManager().setStage(getName(), stage);
    }

    /**
     * Sends information to the interface.
     *
     * @param text The text
     */
    public void sendStartInformation(Player player, String[] text) {
        ScrollInterface.sendQuestScroll(player, Colors.white + "Quest Start Information", text);
    }

    /**
     * Tells you if the player has started the quest yet
     *
     * @return
     */
    public boolean startedQuest(Player player) {
        return getQuestStage(player) != null;
    }

}
