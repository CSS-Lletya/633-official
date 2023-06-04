package com.rs.game.player.content.trails;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtils;

public class HardRewards extends TreasureTrailsManager {

    public static final double MEGA_RARE = 0.015;
    public static final double VERY_RARE = 0.225;
    public static final double RARE = 0.75;
    public static final double COMMON = 75.0;
    public static final double UNCOMMON = 15.0;
    protected static HardRewardStore[] items = HardRewardStore.values();

    protected static void generateRewards(final Player player) {
        int amount = RandomUtils.getRandom(3);
        for (int i = 0; i <= 2 + amount; i++) {
            HardRewardStore reward = getRandomReward();
            int itemAmount = RandomUtils.random(reward.getMin(), reward.getMax());
            int itemId = reward.getItemId2() > 0 ? RandomUtils.random(reward.getItemId(), reward.getItemId2())
                    : reward.getItemId();
            if ((reward.getChance() == RARE || reward.getChance() == VERY_RARE || reward.getChance() == MEGA_RARE) && reward.getMin() <= 1)
                itemAmount = 1;
            player.getClueScrollRewards().add(new Item(itemId, itemAmount));
        }
    }

    protected static HardRewardStore getRandomReward() {
        while (true) {
            double chance = RandomUtils.getRandom(100);
            HardRewardStore reward = items[RandomUtils.getRandom(items.length - 1)];
            if ((reward.getChance()) > chance) {
                return reward;
            }
        }
    }

    public enum HardRewardStore {

        /**
         * General Uncommon Drops
         */

        RUNE_FULL_HELM(1163, UNCOMMON), RUNE_PLATEBODY(1127, UNCOMMON), RUNE_PLATELEGS(1079, UNCOMMON),
        RUNE_PLATESKIRT(1093, UNCOMMON), RUNE_KITESHIELD(1201, UNCOMMON), RUNE_LONGSWORD(1303, UNCOMMON),
        RUNE_DAGGER(1213, UNCOMMON), RUNE_BATTLEAXE(1373, UNCOMMON), RUNE_HATCHET(1359, UNCOMMON),
        RUNE_PICKAXE(1275, UNCOMMON), BLACK_DHIDE_BODY(2503, UNCOMMON), BLACK_DHIDE_CHAPS(2497, UNCOMMON),
        MAGIC_SHORTBOW(861, UNCOMMON), MAGIC_LONGBOW(859, UNCOMMON), MAGIC_COMPOSITE_BOW(10284, RARE),

        /**
         * Runes
         */

        NATURE_RUNE(561, UNCOMMON, 30, 50), LAW_RUNE(563, UNCOMMON, 30, 50), BLOOD_RUNE(565, UNCOMMON, 20, 30),

        /**
         * Noted Food
         */

        COOKED_LOBSTER(380, UNCOMMON, 12, 15), COOKED_SHARK(386, UNCOMMON, 12, 15),

        /**
         * Teleports
         */

        BANDIT_CAMP_TELEPORT(19476, UNCOMMON, 1, 8), LUMBER_YARD_TELEPORT(19480, UNCOMMON, 1, 8),
        PHOENIX_LAIR_TELEPORT(19478, UNCOMMON, 1, 5), TAI_BWO_WANNAI_TELEPORT(19479, UNCOMMON, 1, 5),

        /**
         * Uniques
         */

        AMULET_OF_GLORY_T4(10354, VERY_RARE), ROBIN_HOOD_HAT(2581, VERY_RARE), ENCHANTED_HAT(7400, VERY_RARE),
        ENCHANTED_TOP(7399, VERY_RARE), ENCHANTED_ROBE(7398, VERY_RARE),

        RUNE_FULL_HELM_T(2627, VERY_RARE), RUNE_PLATEBODY_T(2623, VERY_RARE), RUNE_PLATELEGS_T(2625, VERY_RARE),
        RUNE_PLATESKIRT_T(3477, VERY_RARE), RUNE_KITESHIELD_T(2629, VERY_RARE),

        RUNE_FULL_HELM_G(2619, VERY_RARE), RUNE_PLATEBODY_G(2615, VERY_RARE), RUNE_PLATELEGS_G(2617, VERY_RARE),
        RUNE_PLATESKIRT_G(3476, VERY_RARE), RUNE_KITESHIELD_G(2621, VERY_RARE),

        RUNE_SHIELD_H1(10667, VERY_RARE), RUNE_SHIELD_H2(10670, VERY_RARE), RUNE_SHIELD_H3(10673, VERY_RARE),
        RUNE_SHIELD_H4(10676, VERY_RARE), RUNE_SHIELD_H5(10679, VERY_RARE),

        RUNE_HELM_H1(10286, VERY_RARE), RUNE_HELM_H2(10288, VERY_RARE), RUNE_HELM_H3(10290, VERY_RARE),
        RUNE_HELM_H4(10292, VERY_RARE), RUNE_HELM_H5(10294, VERY_RARE),

        ZAMORAK_FULL_HELM(2657, VERY_RARE), ZAMORAK_PLATEBODY(2653, VERY_RARE), ZAMORAK_PLATELEGS(2655, VERY_RARE),
        ZAMORAK_PLATESKIRT(3478, VERY_RARE), ZAMORAK_KITESHIELD(2659, VERY_RARE),

        SARADOMIN_FULL_HELM(2665, VERY_RARE), SARADOMIN_PLATEBODY(2661, VERY_RARE),
        SARADOMIN_PLATELEGS(2663, VERY_RARE), SARADOMIN_PLATESKIRT(3479, VERY_RARE),
        SARADOMIN_KITESHIELD(2667, VERY_RARE),

        GUTHIX_FULL_HELM(2673, VERY_RARE), GUTHIX_PLATEBODY(2669, VERY_RARE), GUTHIX_PLATELEGS(2671, VERY_RARE),
        GUTHIX_PLATESKIRT(3480, VERY_RARE), GUTHIX_KITESHIELD(2675, VERY_RARE),

        ANCIENT_FULL_HELM(19407, VERY_RARE), ANCIENT_PLATEBODY(19398, VERY_RARE), ANCIENT_PLATELEGS(19401, VERY_RARE),
        ANCIENT_PLATESKIRT(19404, VERY_RARE), ANCIENT_KITESHIELD(19410, VERY_RARE),

        BANDOS_FULL_HELM(19437, VERY_RARE), BANDOS_PLATEBODY(19428, VERY_RARE), BANDOS_PLATELEGS(19431, VERY_RARE),
        BANDOS_PLATESKIRT(19434, VERY_RARE), BANDOS_KITESHIELD(19440, VERY_RARE),

        ARMADYL_FULL_HELM(19422, VERY_RARE), ARMADYL_PLATEBODY(19413, VERY_RARE), ARMADYL_PLATELEGS(19416, VERY_RARE),
        ARMADYL_PLATESKIRT(19419, VERY_RARE), ARMADYL_KITESHIELD(19425, VERY_RARE),

        BLUE_DHIDE_BODY_G(7374, VERY_RARE), BLUE_DHIDE_CHAPS_G(7382, VERY_RARE), BLUE_DHIDE_BODY_T(7376, VERY_RARE),
        BLUE_DHIDE_CHAPS_T(7384, VERY_RARE),

        SARADOMIN_BRACERS(10384, VERY_RARE), SARADOMIN_BODY(10386, VERY_RARE), SARADOMIN_CHAPS(10388, VERY_RARE),
        SARADOMIN_COIF(10390, VERY_RARE),

        GUTHIX_BRACERS(10376, VERY_RARE), GUTHIX_BODY(10378, VERY_RARE), GUTHIX_CHAPS(10380, VERY_RARE),
        GUTHIX_COIF(10382, VERY_RARE),

        ZAMORAK_BRACERS(10368, VERY_RARE), ZAMORAK_BODY(10370, VERY_RARE), ZAMORAK_CHAPS(10372, VERY_RARE),
        ZAMORAK_COIF(10374, VERY_RARE),

        BANDOS_BRACERS(19451, VERY_RARE), BANDOS_BODY(19453, VERY_RARE), BANDOS_CHAPS(19455, VERY_RARE),
        BANDOS_COIF(19457, VERY_RARE),

        ANCIENT_BRACERS(19443, VERY_RARE), ANCIENT_BODY(19445, VERY_RARE), ANCIENT_CHAPS(19447, VERY_RARE),
        ANCIENT_COIF(19449, VERY_RARE),

        ARMADYL_BRACERS(19459, VERY_RARE), ARMADYL_BODY(19461, VERY_RARE), ARMADYL_CHAPS(19463, VERY_RARE),
        ARMADYL_COIF(19465, VERY_RARE),

        SARADOMIN_STOLE(10470, VERY_RARE), GUTHIX_STOLE(10472, VERY_RARE), ZAMORAK_STOLE(10474, VERY_RARE),
        SARADOMIN_CROZIER(10440, VERY_RARE), GUTHIX_CROZIER(10442, VERY_RARE), ZAMORAK_CROZIER(10444, VERY_RARE),

        PIRATES_HAT(2651, VERY_RARE), TAN_CAVALIER(2639, VERY_RARE), DARK_CAVALIER(2641, VERY_RARE),
        BLACK_CAVALIER(2643, VERY_RARE), GREEN_DRAGON_MASK(19281, VERY_RARE), BLUE_DRAGON_MASK(19284, VERY_RARE),
        RED_DRAGON_MASK(19287, VERY_RARE), BLACK_DRAGON_MASK(19290, VERY_RARE),

        RUNE_CANE(13099, VERY_RARE),

        RUNE_BODY_H1(19179, VERY_RARE), RUNE_BODY_H2(19200, VERY_RARE), RUNE_BODY_H3(19221, VERY_RARE),
        RUNE_BODY_H4(19242, VERY_RARE), RUNE_BODY_H5(19263, VERY_RARE),

        TOP_HAT(13101, VERY_RARE),

        /**
         * Mega rare table
         */

        GILDED_FULL_HELM(3486, MEGA_RARE), GILDED_PLATEBODY(3481, MEGA_RARE), GILDED_PLATELEGS(3483, MEGA_RARE),
        GILDED_PLATESKIRT(3485, MEGA_RARE), GILDED_KITESHIELD(3488, MEGA_RARE),

        THIRD_AGE_RANGE_TOP(10330, MEGA_RARE), THIRD_AGE_RANGE_LEGS(10332, MEGA_RARE),
        THIRD_AGE_RANGE_COIF(10334, MEGA_RARE), THIRD_AGE_VAMBRACES(10336, MEGA_RARE),

        THIRD_AGE_ROBE_TOP(10338, MEGA_RARE), THIRD_AGE_ROBE(10340, MEGA_RARE), THIRD_AGE_MAGE_HAT(10342, MEGA_RARE),
        THIRD_AGE_AMULET(10344, MEGA_RARE),

        THIRD_AGE_PLATELEGS(10346, MEGA_RARE), THIRD_AGE_PLATEBODY(10348, MEGA_RARE),
        THIRD_AGE_FULL_HELM(10350, MEGA_RARE), THIRD_AGE_KITESHIELD(10352, MEGA_RARE),

        /**
         * Shared Rewards
         */

        SARADOMIN_ARROW(19152, RARE, 20, 100),

        GUTHIX(19157, RARE, 20, 100),

        ZAMORAK(19162, RARE, 20, 100),

        COINS(995, UNCOMMON, 1000, 5000),

        COINS2(995, UNCOMMON, 10000, 15000),

        PURPLE_SWEETS(10476, UNCOMMON, 7, 15),

        RED_FIRELIGHTER(7329, RARE, 4, 10),

        GREEN_FIRELIGHTER(7330, RARE, 4, 10),

        BLUE_FIRELIGHTER(7331, RARE, 4, 10),

        PURPLE_FIRELIGHTER(10326, RARE, 4, 10),

        WHITE_FIRELIGHTER(10327, RARE, 4, 10),

        PURPLE_SWEETS2(10476, UNCOMMON, 8, 12),

        SARADOMIN_PAGES(3827, 3830, RARE),

        ZAMORAK_PAGES(3831, 3834, RARE),

        GUTHIX_PAGES(3835, 3838, RARE),

        ARMADYL_PAGES(19604, 19607, RARE),

        ANCIENT_PAGES(19608, 19611, RARE),

        BANDOS_PAGES(19600, 19603, RARE);

        private int itemId;
        private int itemId2;
        private int amount;
        private int min;
        private int max;

        private double chance;

        private HardRewardStore(int itemId, double chance) {
            this(itemId, -1, chance, 1, 1);
        }

        private HardRewardStore(int itemId, int itemId2, double chance) {
            this(itemId, itemId2, chance, 1, 1);
        }

        private HardRewardStore(int itemId, double chance, int amount) {
            this(itemId, -1, chance, amount, amount);
        }

        private HardRewardStore(int itemId, double chance, int min, int max) {
            this(itemId, -1, chance, min, max);
        }

        private HardRewardStore(int itemId, int itemId2, double chance, int min, int max) {
            this.itemId = itemId;
            this.itemId2 = itemId2;
            this.chance = chance;
            this.min = min;
            this.max = max;
        }

        public int getItemId() {
            return itemId;
        }

        public int getItemId2() {
            return itemId2;
        }

        public double getChance() {
            return chance;
        }

        public int getAmount() {
            return amount;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

    }
}
