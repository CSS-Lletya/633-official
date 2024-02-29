package skills.runecrafting;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.utilities.RandomUtility;

import lombok.AllArgsConstructor;
import skills.Skills;

public class CombinationRunes extends Runecrafting {

    public CombinationRunes(Player player, GameObject object, Altar altar) {
        super(player, object, altar);
    }

    public static void craftComboRune(Player player, int runeId, int level, double exp, int finalItem, int talisman) {
        if (!canCraftRune(player, runeId, level, finalItem, talisman))
            return;
        int amount = calculateAmount(player, runeId);
        player.setNextGraphics(Graphic.RUNECRAFTING);
        player.setNextAnimation(Animations.RUNECRAFTING);
        player.getMovement().lock(1);
        boolean magicImbue = Optional.ofNullable((Boolean) player.getAttributes().get(Attribute.MAGIC_IMBUED).getBoolean()).orElse(false);
        if (!magicImbue)
            player.getInventory().deleteItem(talisman, 1);
        player.getInventory().deleteItem(runeId, amount);
        player.getInventory().deleteItem(new Item(PURE_ESSENCE.getId(), amount));
        if (RandomUtility.random(100) <= 50 && player.getEquipment().getAmuletId() != 5521) {
            player.getPackets().sendGameMessage("You fail to craft " + ItemDefinitions.getItemDefinitions(runeId).getName().toLowerCase() + "s into " + ItemDefinitions.getItemDefinitions(finalItem).getName().toLowerCase() + "s.");
            return;
        }
        player.getInventory().addItem(finalItem, amount);
        double totalXp = exp * amount;
        player.getSkills().addExperience(Skills.RUNECRAFTING, totalXp);
        player.getPackets().sendGameMessage("You bind the temple's power into " + ItemDefinitions.getItemDefinitions(finalItem).getName().toLowerCase() + "s.");
    }

    private static boolean canCraftRune(Player player, int runeId, int level, int finalItem, int talisman) {
        return player.getSkills().getLevel(Skills.RUNECRAFTING) >= level &&
                player.getInventory().containsItem(talisman, 1) &&
                player.getInventory().getItems().getNumberOf(PURE_ESSENCE) > 0 &&
                player.getInventory().getAmountOf(runeId) > 0;
    }

    private static int calculateAmount(Player player, int runeId) {
        return Math.min(player.getInventory().getItems().getNumberOf(PURE_ESSENCE), player.getInventory().getAmountOf(runeId));
    }

    @AllArgsConstructor
    public enum CombinationRunesStore {

        MIST_RUNES(556, 555, 2478, 2480, 4695, 6, 8.0, 8.5, OuraniaAltar.AIR_TALISMAN, OuraniaAltar.WATER_TALISMAN),
        DUST_RUNE(556, 557, 2478, 2481, 4696, 10, 8.3, 9.0, OuraniaAltar.AIR_TALISMAN, OuraniaAltar.EARTH_TALISMAN),
        MUD_RUNE(555, 557, 2480, 2481, 4698, 13, 9.3, 9.5, OuraniaAltar.WATER_TALISMAN, OuraniaAltar.EARTH_TALISMAN),
        SMOKE_RUNE(556, 554, 2478, 2482, 4697, 15, 8.5, 9.5, OuraniaAltar.AIR_TALISMAN, OuraniaAltar.FIRE_TALISMAN),
        STEAM_RUNE(555, 554, 2480, 2482, 4694, 19, 9.3, 10.0, OuraniaAltar.WATER_TALISMAN, OuraniaAltar.FIRE_TALISMAN),
        LAVA_RUNE(557, 554, 2481, 2482, 4699, 23, 10.0, 10.5, OuraniaAltar.EARTH_TALISMAN, OuraniaAltar.FIRE_TALISMAN);

        public int itemId;
        public int itemId2;
        public int objectId;
        public int objectId2;
        public int finalItem;
        public int level;
        public double exp;
        public double exp2;
        public int talisman;
        public int talisman2;
    }
}