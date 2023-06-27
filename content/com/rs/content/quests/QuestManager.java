package com.rs.content.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.utilities.Colors;
import com.rs.utilities.Utility;

public class QuestManager {

    /**
     * The list of possible quests.
     */
    private transient static final Map<String, Quest> SYSTEM_QUESTS = new HashMap<String, Quest>();
    /**
     * The map of quests in progression - bugged
     */
    private Map<String, Quest> progressed = new HashMap<String, Quest>();
    /**
     * The map of stages of quests
     */
    private Map<String, Integer> stages = new HashMap<String, Integer>();

    /**
     * The player
     */
    private transient Player player;

    // bugged trying to load Quest with no args
    public QuestManager() {
    	if (progressed == null)
    		progressed = new HashMap<String, Quest>();
    	if (stages == null)
    		stages = new HashMap<String, Integer>();
    }

    /**
     * Loads all of the quests for players to do.
     */
    public static void load() {
        for (Object packet : Utility.getClassesInDirectory(QuestManager.class.getPackage().getName() + ".impl")) {
            Quest quest = (Quest) packet;
            getQuests().put(quest.getClass().getSimpleName(), quest);
        }
    }

    /**
     * Colourizes text in the note tab
     *
     * @param colour The colour id for the text
     * @param noteId The note id to colour
     * @return
     */
    public static int colourize(int colour, int noteId) {
        return (int) (Math.pow(4, noteId) * colour);
    }

    /**
     * Gets the quest by the class from the static map
     *
     * @param clazz The class to get the quest by
     * @return
     */
    public static Quest getQuest(Class<?> clazz) {
        for (Entry<String, Quest> entry : SYSTEM_QUESTS.entrySet()) {
            String questClassName = entry.getKey();
            if (questClassName.equals(clazz.getSimpleName())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Map<String, Quest> getQuests() {
        return SYSTEM_QUESTS;

    }

    public Player setPlayer(Player player) {
        return this.player = player;

    }

    /**
     * Gets the amount of completed quests you have.
     *
     * @return A {@code Integer} {@code Object}
     */
    public int getCompletedQuests() {
        int size = 0;
        for (Entry<String, Quest> entry : getProgressed().entrySet()) {
            Quest quest = entry.getValue();
            if (quest.isFinished(player)) {
                size++;
            }
        }
        return size;
    }

    /**
     * Sends the quest tab information with all the player quests that can be
     * completed.
     */
    public void sendQuestTabInformation() {
        for (int i = 0; i < getQuests().size(); i++) {
            player.getPackets().sendGlobalString(149 + i, "" + getQuestByIndex(i).getName());
        }
        for (int i = 0; i < getQuests().size(); i++) {
            Quest quest = getProgressedQuest(getQuestByIndex(i).getName());
            if (quest == null) {
                quest = getQuestByIndex(i);
            }
        }
        player.getPackets().sendHideIComponent(34, 1, true);
        player.getPackets().sendIComponentText(34, 2, "Quest Points: " + getCompletedQuests());

    }

    /**
     * Gets the quest from the static map by the index id.
     *
     * @param index The index to search by
     * @return A {@code Quest} {@code Object}
     */
    public Quest getQuestByIndex(int index) {
        int size = 0;
        for (Entry<String, Quest> entry : getQuests().entrySet()) {
            if (size == index) {
                return entry.getValue();
            }
            size++;
        }
        return null;
    }

    /**
     * Removes a quest by name from your progressed map
     *
     * @param name The name of the quest to remove.
     */
    public void removeQuest(String name) {
        getProgressed().entrySet().removeIf(entry -> entry.getKey().equalsIgnoreCase(name));

    }

    /**
     * Tells you if the player has completed the quest by the name
     *
     * @param name The quest to check if the player has completed
     * @return A {@code Boolean} object, {@code True} if complete.
     */
    public boolean completedQuest(String name) {
        for (Entry<String, Quest> entry : getProgressed().entrySet()) {
            Quest quest = entry.getValue();
            if (quest.getName().equalsIgnoreCase(name)) {
                if (quest.isFinished(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void sendUnfinshedQuestMessage(String quest){
        player.getPackets().sendGameMessage("You need to have completed the quest: " + quest + " in order to continue.");
    }

    /**
     * This method figures out whether or not you have started a quest.
     *
     * @param quest The quest to check if you have started or not.
     * @return
     */
    private boolean startedQuest(Quest quest) {
        for (Entry<String, Quest> entry : getProgressed().entrySet()) {
            Quest q = entry.getValue();
            if (q.getName().equals(quest.getName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * Gets the quest by the name of it
     *
     * @param name The name of the quest
     * @return
     */
    private Quest getQuestByName(String name) {
        for (Entry<String, Quest> entry : SYSTEM_QUESTS.entrySet()) {
            Quest quest = entry.getValue();
            if (quest.getName().equalsIgnoreCase(name))
                return quest;
        }
        return null;
    }

    /**
     * Sends the information of the quest on the interface.
     *
     * @param string
     */
    public void sendInformation(String name) {
        Quest quest = getProgressedQuest(name);

        if (quest == null) {
            quest = getQuestByName(name);
            if (quest == null) {
                System.err.println("[QUEST-MANAGER]No such quest by name: " + name);
                return;
            }
            quest.setQuestStage(player, null);
        }

        quest.questRequirements.clear();
        quest.addRequirements(player);

        List<String> messages = new ArrayList<String>();
        for (String i : quest.getInformation(player)) {
            messages.add("<br>" + i);
        }
        if (!startedQuest(quest)) {
            if (quest.questRequirements.size() > 0)
                messages.add("<br>");
            if (quest.questRequirements.size() > 0)
                messages.add(Colors.darkRed + "Quest Requirements");
            for (QuestAttribute req : quest.questRequirements) {
                messages.add((req.isCanContinue() && !req.ignoreCondition ? "<str>" : "") + Colors.blue + ""
                        + req.getName());
            }
        }
        String[] info = messages.toArray(new String[messages.size()]);
        ScrollInterface.sendQuestScroll(player, Colors.black + quest.getName(), info);

    }

    /**
     * Sets the stage of the quest
     *
     * @param name  The name of the quest
     * @param stage The stage to set the quest to
     */
    public void setStage(String name, Integer stage) {
        if (getStages().get(name) == null) {
            getStages().put(name, stage);
        } else {
            Iterator<Entry<String, Integer>> it = getStages().entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Integer> entry = it.next();
                String entryName = entry.getKey();
                if (entryName.equals(name)) {
                    it.remove();
                }
            }
            getStages().put(name, stage);
        }
    }

    /**
     * Gets the stage of the quest
     *
     * @param name The name of the quest to get the stage of
     * @return
     */
    public Integer getStage(String name) {
        return getStages().get(name);
    }

    public Integer getSafeStage(String questName) {
        return getStage(questName) == null ? 0 : getStage(questName);
    }

    /**
     * Starts a quest
     *
     * @param name The name of the quest.
     */
    public void startQuest(Class<?> clazz) {
        Quest quest = getQuest(clazz);
        if (quest == null) {
            System.err.println("[QUEST-MANAGER] " + clazz.getName() + " attempted to start quest.");
            return;
        }
        String name = quest.getName();
        if (quest.canStart(player)) {
            getProgressed().put(name, quest);
            quest.startQuest(getPlayer());
            player.getInterfaceManager().renderQuestStatus();
        } else if (!quest.canStart(player)) {
            player.getPackets().sendGameMessage(Colors.red + "You do not have the requirements to start this quest.");
            sendInformation(name);

        }
    }

    /**
     * Completes a quest.
     *
     * @param name
     */
    public void finishQuest(String name) {
        for (Entry<String, Quest> entry : getProgressed().entrySet()) {
            Quest quest = entry.getValue();
            if (quest.getName().equalsIgnoreCase(name)) {
                quest.completeQuest(player);
            }

        }
    }

    /**
     * Loops through the progressed quest map and checks for any quest with a name
     * similar to the one in the parameters
     *
     * @param name The name to check for
     * @return A {@code Quest} {@code Object}
     */
    public Quest getProgressedQuest(String name) {
        for (Entry<String, Quest> entry : getProgressed().entrySet()) {
            Quest quest = entry.getValue();
            if (entry.getKey().equalsIgnoreCase(name)) {
                return quest;
            }
        }
        return null;

    }

    /**
     * Handles the npc interaction for the npc in a quest.
     *
     * @param player The player
     * @param npc    The npc
     * @return
     */
    public boolean handleNPC(Player player, NPC npc, int option) {
        for (Entry<String, Quest> entry : getProgressed().entrySet()) {
            Quest quest = entry.getValue();
            if (quest.getQuestStage(player) == null) {
                continue;
            }
            if (quest.handleNPC(player, npc, option)) {
                return true;
            }
        }
        return false;

    }
    
    /**
     * Handles the object interaction for the object in a quest.
     *
     * @param player The player
     * @param object    The object
     * @return
     */
    public boolean handleObject(Player player, GameObject object) {
        for (Entry<String, Quest> entry : getProgressed().entrySet()) {
            Quest quest = entry.getValue();
            if (quest.getQuestStage(player) == null) {
                continue;
            }
            if (quest.handleObject(player, object)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Handles the item drop interaction for the item in a quest.
     *
     * @param player The player
     * @param item   The item
     * @return
     */
    public boolean handleDropItem(Player player, Item item) {
        for (int i = 0; i < getQuests().size(); i++) {
            Quest quest = getProgressedQuest(getQuestByIndex(i).getName());
            if (quest == null) {
                quest = getQuestByIndex(i);
            }
            if (quest.handleDroppedItem(player, item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;

    }

    public Map<String, Quest> getProgressed() {
        return progressed;
    }

    public void setProgressed(Map<String, Quest> progressed) {
        this.progressed = progressed;
    }

    public Map<String, Integer> getStages() {
        return stages;
    }

    public void setStages(Map<String, Integer> stages) {
        this.stages = stages;
    }
}
