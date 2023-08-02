package com.rs.game.player.content.trails;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;

public class EliteRewards extends TreasureTrailsManager {

    public static final double MEGA_RARE = 0.030;
    public static final double VERY_RARE = 0.225;
    public static final double RARE = 0.75;
    public static final double COMMON = 75.0;
    public static final double UNCOMMON = 15.0;
    protected static EliteRewardStore[] items = EliteRewardStore.values();

    protected static void generateRewards(final Player player) {
        int amount = RandomUtility.getRandom(3);
        for (int i = 0; i <= 2 + amount; i++) {
            EliteRewardStore reward = getRandomReward();
            int itemAmount = RandomUtility.random(reward.getMin(), reward.getMax());
            int itemId = reward.getItemId2() > 0 ? RandomUtility.random(reward.getItemId(), reward.getItemId2())
                    : reward.getItemId();
            if ((reward.getChance() == RARE || reward.getChance() == VERY_RARE || reward.getChance() == MEGA_RARE)
                    && reward.getMin() <= 1)
                itemAmount = 1;
            player.getClueScrollRewards().add(new Item(itemId, itemAmount));
        }
    }

    protected static EliteRewardStore getRandomReward() {
        while (true) {
            double chance = RandomUtility.getRandom(100);
            EliteRewardStore reward = items[RandomUtility.getRandom(items.length - 1)];
            if ((reward.getChance()) > chance) {
                return reward;
            }
        }
    }

    public enum EliteRewardStore {

        /**
         * General Uncommon Drops
         */

        RUNE_PLATEBODY(1127, UNCOMMON), RUNE_PLATELEGS(1079, UNCOMMON), RUNE_PLATESKIRT(1093, UNCOMMON),
        RUNE_KITESHIELD(1201, UNCOMMON), RUNE_CROSSBOW(9185, UNCOMMON), DRAGON_DAGGER(1215, UNCOMMON),
        DRAGON_MACE(1434, UNCOMMON), DRAGON_LONGSWORD(1305, UNCOMMON), ONYX_BOLT_TIPS(9194, UNCOMMON, 8, 12),

        /**
         * Runes
         */

        LAW_RUNE(563, UNCOMMON, 50, 75), DEATH_RUNE(560, UNCOMMON, 50, 75), BLOOD_RUNE(565, UNCOMMON, 50, 75),
        SOUL_RUNE(566, UNCOMMON, 50, 75),

        /**
         * Jewerly
         */

        DRAGONSTONE_BRACELET(11115, UNCOMMON), DRAGONSTOE_NECKLACE(1664, UNCOMMON), DRAGONSTONE_RING(1645, UNCOMMON),

        /**
         * Noted Food
         */

        TUNA_POTATO(7061, UNCOMMON, 15, 20), SUMMER_PIE(7219, UNCOMMON, 15, 20),

        /**
         * Resources
         */

        OAK_PLANK(8779, UNCOMMON, 60, 80), TEAK_PLANK(8781, UNCOMMON, 40, 50), MAHOGANY_PLANK(8783, UNCOMMON, 20, 30),
        RUNITE_BAR(2364, UNCOMMON, 1, 3), TOOTH_HALF_KEY(985, UNCOMMON), LOOP_HALF_KEY(987, UNCOMMON),
        PALM_TREE_SEED(5289, UNCOMMON), YEW_SEED(5315, UNCOMMON), MAGIC_SEED(5316, UNCOMMON),

        /**
         * Uniques
         */

        SARADOMIN_BOW(19143, VERY_RARE), GUTHIX_BOW(19146, VERY_RARE), ZAMORAK_BOW(19149, VERY_RARE),

        BRONZE_DRAGON_MASK(19296, VERY_RARE), IRON_DRAGON_MASK(19299, VERY_RARE), STEEL_DRAGON_MASK(19302, VERY_RARE),
        MITHRIL_DRAGON_MASK(19305, VERY_RARE), FROST_DRAGON_MASK(19293, VERY_RARE),

        BAT_STAFF(19327, VERY_RARE), WOLF_STAFF(19329, VERY_RARE), DRAGON_STAFF(19323, VERY_RARE),
        CAT_STAFF(19331, VERY_RARE), PENGUIN_STAFF(19325, VERY_RARE),

        DRAGON_FULL_HELM_OR_KIT(19346, VERY_RARE), DRAGON_PLATELEGS_SKIRT_OR_KIT(19348, VERY_RARE),
        DRAGON_PLATEBODY_OR_KIT(19350, VERY_RARE), DRAGON_SQ_SHIELD_OR_KIT(19352, VERY_RARE),

        DRAGON_FULL_HELM_SP_KIT(19354, VERY_RARE), DRAGON_PLATELEGS_SKIRT_SP_KIT(19356, VERY_RARE),
        DRAGON_PLATEBODY_SP_KIT(19358, VERY_RARE), DRAGON_SQ_SP_KIT(19360, VERY_RARE),

        FURY_ORNAMENT_KIT(19333, VERY_RARE),

        /**
         * Mega rare table
         */

        THIRD_AGE_DRUIDIC_STAFF(19308, MEGA_RARE), THIRD_AGE_DRUIDIC_CLOAK(19311, MEGA_RARE),
        THIRD_AGE_DRUIDIC_WREATH(19314, MEGA_RARE), THIRD_AGE_DRUIDIC_ROBE_TOP(19317, MEGA_RARE),
        THIRD_AGE_DRUIDIC_ROBE(19320, MEGA_RARE),

        /**
         * Shared Rewards
         */

        STARVED_EFFIGY(18778, RARE),

        SARADOMIN_ARROW(19152, RARE, 20, 100),

        GUTHIX(19157, RARE, 20, 100),

        ZAMORAK(19162, RARE, 20, 100),

        COINS(995, UNCOMMON, 10000, 15000),

        COINS2(995, UNCOMMON, 20000, 30000),

        PURPLE_SWEETS(10476, UNCOMMON, 9, 23),

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

        private EliteRewardStore(int itemId, double chance) {
            this(itemId, -1, chance, 1, 1);
        }

        private EliteRewardStore(int itemId, int itemId2, double chance) {
            this(itemId, itemId2, chance, 1, 1);
        }

        private EliteRewardStore(int itemId, double chance, int amount) {
            this(itemId, -1, chance, amount, amount);
        }

        private EliteRewardStore(int itemId, double chance, int min, int max) {
            this(itemId, -1, chance, min, max);
        }

        private EliteRewardStore(int itemId, int itemId2, double chance, int min, int max) {
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
