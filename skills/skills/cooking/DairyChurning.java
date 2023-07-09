package skills.cooking;

import com.rs.game.dialogue.CreateActionD;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import skills.Skills;

public class DairyChurning {

    private static Item[][] materials = {{new Item(1927)}, {new Item(1927)}, {new Item(1927)}};
    private static Item[][] products = {{new Item(2130), new Item(1925)}, {new Item(6697), new Item(1925)}, {new Item(1985), new Item(1925)}};
    private static int[] reqs = {21, 38, 48};
    private static double[] xp = {18, 40.5, 64};
    private static int[] anims = {2793, 2793, 2793};

    public static void handleChurnOption(Player player) {
        player.dialogueBlank(new CreateActionD(player, materials, products, xp, anims, reqs, Skills.COOKING, 2));
    }
}
