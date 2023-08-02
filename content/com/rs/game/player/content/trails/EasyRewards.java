package com.rs.game.player.content.trails;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;

public class EasyRewards extends TreasureTrailsManager {

    public static final double VERY_RARE = 0.225;
    public static final double RARE = 0.75;
    public static final double COMMON = 75.0;
    public static final double UNCOMMON = 15.0;
    protected static EasyRewardStore[] items = EasyRewardStore.values();

    public static void generateRewards(final Player player) {
        int amount = RandomUtility.getRandom(3);
        for (int i = 0; i <= 3 + amount; i++) {
            EasyRewardStore reward = getRandomReward();
            int itemAmount = RandomUtility.random(reward.getMin(), reward.getMax());
            int itemId = reward.getItemId2() > 0 ? RandomUtility.random(reward.getItemId(), reward.getItemId2()) : reward.getItemId();
            if ((reward.getChance() == RARE || reward.getChance() == VERY_RARE) && reward.getMin() <= 1)
                itemAmount = 1;
            player.getClueScrollRewards().add(new Item(itemId, itemAmount));
        }
    }

    protected static EasyRewardStore getRandomReward() {
        while (true) {
            double chance = RandomUtility.getRandom(100);
            EasyRewardStore reward = items[RandomUtility.getRandom(items.length - 1)];
            if ((reward.getChance()) > chance)
                return reward;
            else
                continue;
        }
    }

    public enum EasyRewardStore {

        /**
         * General Uncommon Drops
         */

        BLACK_FULL_HELM(1165, UNCOMMON), BLACK_PLATEBODY(1125, UNCOMMON), BLACK_PLATELEGS(1077, UNCOMMON),
        BLACK_LONGSWORD(1297, UNCOMMON), BLACK_BATTLEAXE(1367, UNCOMMON), BLACK_HATCHET(1361, UNCOMMON),
        BLACK_DAGGER(1217, UNCOMMON), STEEL_PICKAXE(1269, UNCOMMON), COIF(1169, UNCOMMON), STUDDED_BODY(1133, UNCOMMON),
        STUDDEN_CHAPS(1097, UNCOMMON), WILLOW_SHORTBOW(849, UNCOMMON), STAFF_OF_AIR(1381, UNCOMMON),
        WILLOW_LONGBOW(847, UNCOMMON), AMULET_OF_MAGIC(1727, UNCOMMON), WILLOW_COMPOSITE_BOW(10280, RARE),

        /**
         * Runes
         */

        AIR_RUNE(556, UNCOMMON, 30, 50), MIND_RUNE(558, UNCOMMON, 30, 50), WATER_RUNE(555, UNCOMMON, 30, 50), EARTH_RUNE(557, UNCOMMON, 30, 50),
        FIRE_RUNE(554, UNCOMMON, 30, 50), BODY_RUNE(559, UNCOMMON, 30, 50), CHAOS_RUNE(562, UNCOMMON, 5, 10), NATURE_RUNE(561, UNCOMMON, 5, 10),
        LAW_RUNE(563, UNCOMMON, 5, 10),

        /**
         * Noted Food
         */

        COOKED_TROUT(334, UNCOMMON, 6, 10), COOKED_SALMON(330, UNCOMMON, 6, 10),

        /**
         * Uniques
         */

        AMULET_OF_MAGIC_TRIMMED(10366, RARE),

        BLACK_FULL_HELM_T(2587, VERY_RARE), BLACK_PLATEBODY_T(2583, VERY_RARE), BLACK_PLATELEGS_T(2585, VERY_RARE),
        BLACK_PLATESKIRT_T(3472, VERY_RARE), BLACK_KITESHIELD_T(2589, VERY_RARE),

        BLACK_FULL_HELM_G(2595, VERY_RARE), BLACK_PLATEBODY_G(2591, VERY_RARE), BLACK_PLATELEGS_G(2593, VERY_RARE),
        BLACK_PLATESKIRT_G(3473, VERY_RARE), BLACK_KITESHIELD_G(2597, VERY_RARE),

        BLACK_SHIELD_H1(7332, VERY_RARE), BLACK_SHIELD_H2(7338, VERY_RARE), BLACK_SHIELD_H3(7344, VERY_RARE),
        BLACK_SHIELD_H4(7350, VERY_RARE), BLACK_SHIELD_H5(7356, VERY_RARE),

        BLACK_HELM_H1(10306, VERY_RARE), BLACK_HELM_H2(10308, VERY_RARE), BLACK_HELM_H3(10310, VERY_RARE),
        BLACK_HELM_H4(10312, VERY_RARE), BLACK_HELM_H5(10314, VERY_RARE),

        BLACK_PLATEBODY_H1(19167, VERY_RARE), BLACK_PLATEBODY_H2(19188, VERY_RARE),
        BLACK_PLATEBODY_H3(19209, VERY_RARE), BLACK_PLATEBODY_H4(19230, VERY_RARE),
        BLACK_PLATEBODY_H5(19251, VERY_RARE),

        STUDDED_BODY_G(7362, VERY_RARE), STUDDED_CHAPS_G(7366, VERY_RARE), STUDDED_BODY_T(7364, VERY_RARE),
        STUDDED_CHAPS_T(7368, VERY_RARE),

        BLUE_WIZARD_SKIRT_G(7386, VERY_RARE), BLUE_WIZARD_ROBE_G(7390, VERY_RARE), BLUE_WIZARD_HAT_G(7394, VERY_RARE),

        BLUE_WIZARD_SKIRT_T(7388, VERY_RARE), BLUE_WIZARD_ROBE_T(7392, VERY_RARE), BLUE_WIZARD_HAT_T(7396, VERY_RARE),

        SARADOMIN_ROBE_TOP(10458, VERY_RARE), ZAMORAK_ROBE_TOP(10460, VERY_RARE), GUTHIX_ROBE_TOP(10462, VERY_RARE),
        ARMADYL_ROBE_TOP(19380, VERY_RARE), ANCIENT_ROBE_TOP(19382, VERY_RARE), BANDOS_ROBE_TOP(19384, VERY_RARE),

        SARADOMIN_ROBE_LEGS(10464, VERY_RARE), GUTHIX_ROBE_LEGS(10466, VERY_RARE), ZAMORAK_ROBE_LEGS(10468, VERY_RARE),
        ARMADYL_ROBE_LEGS(19386, VERY_RARE), BANDOS_ROBE_LEGS(19388, VERY_RARE), ANCIENT_ROBE_LEGS(19390, VERY_RARE),

        HIGHWAYMAN_MASK(2631, VERY_RARE),

        BLUE_BERET(2633, VERY_RARE), BLACK_BERET(2635, VERY_RARE), WHITE_BERET(2637, VERY_RARE),

        A_POWDERED_WIG(10392, VERY_RARE), SLEEPING_CAP(10398, VERY_RARE),

        FLARED_TROUSERS(10394, VERY_RARE), PANTALOONS(10396, VERY_RARE), BLACK_CANE(13095, VERY_RARE),

        RED_ELEGANT_SHIRT(10404, VERY_RARE), RED_ELEGANT_BLOUSE(10424, VERY_RARE), RED_ELEGANT_LEGS(10406, VERY_RARE),
        RED_ELEGANT_SKIRT(10426, VERY_RARE),

        GREEN_ELEGANT_SHIRT(10412, VERY_RARE), GREEN_ELEGANT_BLOUSE(10432, VERY_RARE),
        GREEN_ELEGANT_LEGS(10414, VERY_RARE), GREEN_ELEGANT_SKIRT(10434, VERY_RARE),

        BLUE_ELEGANT_SHIRT(10408, VERY_RARE), BLUE_ELEGANT_BLOUSE(10428, VERY_RARE),
        BLUE_ELEGANT_LEGS(10410, VERY_RARE), BLUE_ELEGANT_SKIRT(10430, VERY_RARE),

        /**
         * Shared Rewards
         */

        COINS(995, UNCOMMON, 50, 200),

        RARE_COINS(995, UNCOMMON, 3000, 7000),

        VERY_RARE_COINS(995, UNCOMMON, 10000, 15000),

        PURPLE_SWEETS(10476, UNCOMMON, 2, 6),

        RED_FIRELIGHTER(7329, RARE, 4, 10),

        GREEN_FIRELIGHTER(7330, RARE, 4, 10),

        BLUE_FIRELIGHTER(7331, RARE, 4, 10),

        PURPLE_FIRELIGHTER(10326, RARE, 4, 10),

        WHITE_FIRELIGHTER(10327, RARE, 4, 10),

        RARE_PURPLE_SWEETS(10476, RARE, 8, 12),

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

        private EasyRewardStore(int itemId, double chance) {
            this(itemId, -1, chance, 1, 1);
        }

        private EasyRewardStore(int itemId, int itemId2, double chance) {
            this(itemId, itemId2, chance, 1, 1);
        }

        private EasyRewardStore(int itemId, double chance, int amount) {
            this(itemId, -1, chance, amount, amount);
        }

        private EasyRewardStore(int itemId, double chance, int min, int max) {
            this(itemId, -1, chance, min, max);
        }

        private EasyRewardStore(int itemId, int itemId2, double chance, int min, int max) {
            this.itemId = itemId;
            this.itemId2 = itemId2;
            this.chance = chance;
            this.min = min;
            this.max = max;
        }

        public int getItemId() {
            return itemId;
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

        public int getItemId2() {
            return itemId2;
        }

    }
}
