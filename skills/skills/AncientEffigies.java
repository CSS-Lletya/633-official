package skills;

import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

public class AncientEffigies {

    /**
     * Ancient effigies' item ids.
     */
    public static final int STARVED_ANCIENT_EFFIGY = 18778, NOURISHED_ANCIENT_EFFIGY = 18779;
    public static final int SATED_ANCIENT_EFFIGY = 18780;
    public static final int GORGED_ANCIENT_EFFIGY = 18781;
    private static final int DRAGONKIN_LAMP = 18782;
    /**
     * First skill to be nourished.
     */
    public static int[] SKILL_1 = {Skills.AGILITY, Skills.CONSTRUCTION, Skills.COOKING, Skills.FISHING, Skills.FLETCHING, Skills.HERBLORE, Skills.MINING, Skills.SUMMONING};
    /**
     * Second skill to be nourished.
     */
    public static int[] SKILL_2 = {Skills.CRAFTING, Skills.THIEVING, Skills.FIREMAKING, Skills.FARMING, Skills.WOODCUTTING, Skills.HUNTER, Skills.SMITHING, Skills.RUNECRAFTING};

    /**
     * Getting the required level for each effigy.
     *
     * @param id The effigy's item id.
     * @return Required level.
     */
    public static int getRequiredLevel(int id) {
        switch (id) {
            case STARVED_ANCIENT_EFFIGY:
                return 91;
            case NOURISHED_ANCIENT_EFFIGY:
                return 93;
            case SATED_ANCIENT_EFFIGY:
                return 95;
            case GORGED_ANCIENT_EFFIGY:
                return 97;
        }
        return -1;
    }

    /**
     * Getting the message.
     *
     * @param skill The skill
     * @return message
     */
    public static String getMessage(int skill) {
        switch (skill) {
            case Skills.AGILITY:
                return "deftness and precision";
            case Skills.CONSTRUCTION:
                return "buildings and security";
            case Skills.COOKING:
                return "fire and preparation";
            case Skills.FISHING:
                return "life and cultivation";
            case Skills.FLETCHING:
                return "lumber and woodworking";
            case Skills.HERBLORE:
                return "flora and fuana";
            case Skills.MINING:
                return "metalwork and minerals";
            case Skills.SUMMONING:
                return "binding essence and spirits";
        }
        return null;
    }

    /**
     * Getting the experience amount.
     *
     * @param itemId The effigy's item id.
     * @return The amount of experience.
     */
    public static int getExp(int itemId) {
        switch (itemId) {
            case STARVED_ANCIENT_EFFIGY:
                return 15000;
            case NOURISHED_ANCIENT_EFFIGY:
                return 20000;
            case SATED_ANCIENT_EFFIGY:
                return 25000;
            case GORGED_ANCIENT_EFFIGY:
                return 30000;
        }
        return -1;
    }

    /**
     * Investigation of an effigy.
     *
     * @param player The player who is doing investigation.
     * @param id     The effigy item id.
     */
    public static void effigyInvestigation(Player player, int id) {
        Inventory inv = player.getInventory();
        inv.deleteItem(id, 1);
        switch (id) {
            case STARVED_ANCIENT_EFFIGY:
                inv.addItem(NOURISHED_ANCIENT_EFFIGY, 1);
                break;
            case NOURISHED_ANCIENT_EFFIGY:
                inv.addItem(SATED_ANCIENT_EFFIGY, 1);
                break;
            case SATED_ANCIENT_EFFIGY:
                inv.addItem(GORGED_ANCIENT_EFFIGY, 1);
                break;
            case GORGED_ANCIENT_EFFIGY:
            	player.setNextAnimation(new Animation(14177));
				player.setNextGraphics(new Graphics(2692));
                inv.addItem(DRAGONKIN_LAMP, 1);
                break;
        }
    }
}