package skills.runecrafting;

import java.util.stream.IntStream;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
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
        player.getSkills().addExperience(Skills.RUNECRAFTING, totalXp);
        player.setNextGraphics(Graphic.RUNECRAFTING);
        player.setNextAnimation(Animations.RUNECRAFTING);
        player.getMovement().lock(1);
        player.getInventory().deleteItem(PURE_ESSENCE, runes);
		IntStream.range(0, runes).forEach(rune -> {
			Altar randomRune = store[RandomUtils.getRandom(store.length)];
			while (actualLevel < randomRune.getRequirement())
				randomRune = store[RandomUtils.getRandom(store.length)];
			Item item = new Item(randomRune.getRune().getItem().getId(), 1);
			player.getInventory().addItem(item);
			player.getDetails().getStatistics()
					.addStatistic(ItemDefinitions.getItemDefinitions(item.getId()).getName() + "_Runecrafted")
					.addStatistic("Runes_Crafted");
		});
        player.getPackets().sendGameMessage("You bind the temple's power into runes.");
    }
}