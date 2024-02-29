package skills.runecrafting;

import java.util.stream.IntStream;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;

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
			Altar randomRune = store[RandomUtility.getRandom(store.length)];
			while (actualLevel < randomRune.getRequirement())
				randomRune = store[RandomUtility.getRandom(store.length)];
			Item item = new Item(randomRune.getRune().getItem().getId(), 1);
			player.getInventory().addItem(item);
			player.getDetails().getStatistics()
					.addStatistic(ItemDefinitions.getItemDefinitions(item.getId()).getName() + "_Runecrafted")
					.addStatistic("Runes_Crafted");
		});
        player.getPackets().sendGameMessage("You bind the temple's power into runes.");
    }
    
    public final static int 
    AIR_TIARA = 5527, MIND_TIARA = 5529, WATER_TIARA = 5531, BODY_TIARA = 5533, EARTH_TIARA = 5535,
            FIRE_TIARA = 5537, COSMIC_TIARA = 5539, NATURE_TIARA = 5541, CHAOS_TIARA = 5543, LAW_TIARA = 5545,
            DEATH_TIARA = 5547, BLOOD_TIARA = 5549, SOUL_TIARA = 5551, ASTRAL_TIARA = 9106, OMNI_TIARA = 13655,

    AIR_TALISMAN = 1438, MIND_TALISMAN = 1448, WATER_TALISMAN = 1444, EARTH_TALISMAN = 1440,
            FIRE_TALISMAN = 1442, BODY_TALISMAN = 1446, LAW_TALISMAN = 1458, NATURE_TALISMAN = 1462,
            CHAOS_TALISMAN = 1452, BLOOD_TALISMAN = 1450, DEATH_TALISMAN = 1456, COSMIC_TALISMAN = 1454,
            SOUL_TALISMAN = 1460;
}