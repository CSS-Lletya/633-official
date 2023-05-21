package skills.runecrafting;

import java.util.stream.IntStream;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;

import skills.Skills;

public class OuraniaAltar {

    public final static int PURE_ESSENCE = 7936;

    private static Altar[] store = Altar.values();

    public static void craftRune(Player player) {
        int runes = player.getInventory().getItems().getNumberOf(PURE_ESSENCE);
        if (runes > 0)
            player.getInventory().deleteItem(PURE_ESSENCE, runes);
        if (runes == 0) {
            player.getPackets().sendGameMessage("You don't have any pure essence.");
            return;
        }
        int actualLevel = player.getSkills().getLevel(Skills.RUNECRAFTING);
        double totalXp = 5 * runes;
        player.getSkills().addXp(Skills.RUNECRAFTING, totalXp);
        player.setNextGraphics(new Graphics(186));
        player.setNextAnimation(new Animation(791));
        player.getMovement().lock(1);
        player.getInventory().deleteItem(PURE_ESSENCE, runes);
        IntStream.range(0, runes).forEach(rune -> {
        	Altar randomRune = store[RandomUtils.getRandom(store.length - 1)];
            while (actualLevel < randomRune.getRequirement())
                randomRune = store[RandomUtils.getRandom(store.length - 1)];
            Item item = new Item(randomRune.getRune().getItem().getId(), 1);
            player.getInventory().addItem(item);
        });
        player.getPackets().sendGameMessage("You bind the temple's power into runes.");
    }
}