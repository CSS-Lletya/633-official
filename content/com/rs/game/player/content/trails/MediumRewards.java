package com.rs.game.player.content.trails;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;

public class MediumRewards extends TreasureTrailsManager {

    public static final double VERY_RARE = 0.225;
    public static final double RARE = 0.75;
    public static final double COMMON = 75.0;
    public static final double UNCOMMON = 15.0;
    protected static MediumRewardStore[] items = MediumRewardStore.values();

    protected static void generateRewards(final Player player) {
        int amount = RandomUtility.getRandom(3);
        for (int i = 0; i <= 2 + amount; i++) {
            MediumRewardStore reward = getRandomReward();
            int itemAmount = RandomUtility.random(reward.getMin(), reward.getMax());
            int itemId = reward.getItemId2() > 0 ? RandomUtility.random(reward.getItemId(), reward.getItemId2()) : reward.getItemId();
            if ((reward.getChance() == RARE || reward.getChance() == VERY_RARE) && reward.getMin() <= 1)
                itemAmount = 1;
            player.getClueScrollRewards().add(new Item(itemId, itemAmount));
        }
    }

    protected static MediumRewardStore getRandomReward() {
        while (true) {
            double chance = RandomUtility.getRandom(100);
            MediumRewardStore reward = items[RandomUtility.getRandom(items.length - 1)];
            if ((reward.getChance()) > chance)
                return reward;
            else
                continue;
        }
    }

    public enum MediumRewardStore {

        /**
         * General Uncommon Drops
         */

        ADAMANT_FULL_HELM(1161, UNCOMMON), ADAMANT_PLATEBODY(1123, UNCOMMON), ADAMANT_PLATELEGS(1073, UNCOMMON),
        ADAMANT_LONGSWORD(1301, UNCOMMON), ADAMANT_DAGGER(1211, UNCOMMON), ADAMANT_BATTLEAXE(1371, UNCOMMON),
        ADAMANT_HATCHET(1357, UNCOMMON), ADAMANT_PICKAXE(1271, UNCOMMON), GREEN_D_HIDE_BODY(1135, UNCOMMON),
        GREEN_D_HIDE_CHAPS(1099, UNCOMMON), YEW_SHORTBOW(857, UNCOMMON), FIRE_BATTLESTAFF(1393, UNCOMMON),
        YEW_LONGBOW(855, UNCOMMON), AMULET_OF_POWER(1731, UNCOMMON), YEW_COMPOSITE_BOW(10282, RARE),
        STRENGTH_AMULET_T(10364, RARE),

        /**
         * Runes
         */

        FIRE_RUNE(554, UNCOMMON, 50, 100), WATER_RUNE(555, UNCOMMON, 50, 100), AIR_RUNE(556, UNCOMMON, 50, 100),
        EARTH_RUNE(557, UNCOMMON, 50, 100), MIND_RUNE(558, UNCOMMON, 50, 100), CHAOS_RUNE(562, UNCOMMON, 10, 20),
        NATURE_RUNE(561, UNCOMMON, 10, 20), LAW_RUNE(563, UNCOMMON, 10, 20), DEATH_RUNE(560, UNCOMMON, 10, 20),

        /**
         * Noted Food
         */

        COOKED_LOBSTER(380, UNCOMMON, 8, 12), COOKED_SWORDFISH(374, UNCOMMON, 8, 12),

        /**
         * Uniques
         */

        RANGER_BOOTS(2577, VERY_RARE), WIZARD_BOOTS(2579, VERY_RARE),

        ADAMANT_FULL_HELM_T(2605, VERY_RARE), ADAMANT_PLATEBODY_T(2599, VERY_RARE),
        ADAMANT_PLATELEGS_T(2601, VERY_RARE), ADAMANT_PLATESKIRT_T(3474, VERY_RARE),
        ADAMANT_KITESHIELD_T(2603, VERY_RARE),

        ADAMANT_FULL_HELM_G(2613, VERY_RARE), ADAMANT_PLATEBODY_G(2607, VERY_RARE),
        ADAMANT_PLATELEGS_G(2609, VERY_RARE), ADAMANT_PLATESKIRT_G(3475, VERY_RARE),
        ADMAANT_KITESHIELD_G(2611, VERY_RARE),

        ADAMANT_SHIELD_H1(10666, VERY_RARE), ADAMANT_SHIELD_H2(10669, VERY_RARE), ADAMANT_SHIELD_H3(10672, VERY_RARE),
        ADAMANT_SHIELD_H4(10675, VERY_RARE), ADAMANT_SHIELD_H5(10678, VERY_RARE),

        ADAMANT_HELM_H1(10296, VERY_RARE), ADAMANT_HELM_H2(10298, VERY_RARE), ADAMANT_HELM_H3(10300, VERY_RARE),
        ADAMANT_HELM_H4(10302, VERY_RARE), ADAMANT_HELM_H5(10304, VERY_RARE),

        ADAMANT_PLATEBODY_H1(19173, VERY_RARE), ADAMANT_PLATEBODY_H2(19194, VERY_RARE),
        ADAMANT_PLATEBODY_H3(19215, VERY_RARE), ADAMANT_PLATEBODY_H4(19236, VERY_RARE),
        ADAMANT_PLATEBODY_H5(19257, VERY_RARE),

        GREEN_D_HIDE_BODY_G(7370, VERY_RARE), GREEN_D_HIDE_CHAPS_G(7378, VERY_RARE),
        GREEN_D_HIDE_BODY_T(7372, VERY_RARE), GREEN_D_HIDE_CHAPS_T(7380, VERY_RARE),

        SARADOMIN_MITRE(10452, VERY_RARE), GUTHIX_MITRE(10454, VERY_RARE), ZAMOKRA_MITRE(10456, VERY_RARE),
        ARMADYL_MITRE(19374, VERY_RARE), BANDOS_MITRE(19376, VERY_RARE), ANCIENT_MITRE(19378, VERY_RARE),

        SARADOMIN_CLOAK(10446, VERY_RARE), GUTHIX_CLOAK(10448, VERY_RARE), ZAMORAK_CLOAK(10450, VERY_RARE),
        ARMADYL_CLOAK(19368, VERY_RARE), BANDOS_CLOAK(19370, VERY_RARE), ANCIENT_CLOAK(19372, VERY_RARE),

        ARMADYL_STOLE(19392, VERY_RARE), BANDOS_STOLE(19394, VERY_RARE), ANCIENT_STOLE(19396, VERY_RARE),
        ARMADYL_CROZIER(19362, VERY_RARE), BANDOS_CROZIER(19364, VERY_RARE), ANCIENT_CROZIER(19366, VERY_RARE),

        RED_BOATER(7319, VERY_RARE), ORANGE_BOATER(7321, VERY_RARE), GREEN_BOATER(7323, VERY_RARE),
        BLUE_BOATER(7325, VERY_RARE), BLACK_BOATER(7327, VERY_RARE),

        RED_HEADBAND(2645, VERY_RARE), BLACK_HEADBAND(2647, VERY_RARE), BROWN_HEADBAND(2649, VERY_RARE),

        ADAMANT_CANE(13097, VERY_RARE),

        CAT_MASK(13113, VERY_RARE), PENGUIN_MASK(13109, VERY_RARE), WOLF_MASK(13115, VERY_RARE),
        WHITE_UNICORN_MASK(19275, VERY_RARE), BLACK_UNICORN_MASK(19278, VERY_RARE),

        PURPLE_ELEGANT_SHIRT(10416, VERY_RARE), PURPLE_ELEGANT_BLOUSE(10436, VERY_RARE),
        PURPLE_ELEGANT_LEGS(10418, VERY_RARE), PURPLE_ELEGANT_SKIRT(10438, VERY_RARE),

        BLACK_ELEGANT_SHIRT(10400, VERY_RARE), BLACK_ELEGANT_LEGS(10402, VERY_RARE),

        WHITE_ELEGANT_BLOUSE(10420, VERY_RARE), WHITE_ELEGANT_SKIRT(10422, VERY_RARE),


        /**
         * Shared Rewards
         */

        COINS(995, UNCOMMON, 200, 100),

        VERY_RARE_COINS(995, UNCOMMON, 10000, 15000),

        PURPLE_SWEETS(10476, UNCOMMON, 5, 10),

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

        private MediumRewardStore(int itemId, double chance) {
            this(itemId, -1, chance, 1, 1);
        }

        private MediumRewardStore(int itemId, int itemId2, double chance) {
            this(itemId, itemId2, chance, 1, 1);
        }

        private MediumRewardStore(int itemId, double chance, int amount) {
            this(itemId, -1, chance, amount, amount);
        }

        private MediumRewardStore(int itemId, double chance, int min, int max) {
            this(itemId, -1, chance, min, max);
        }

        private MediumRewardStore(int itemId, int itemId2, double chance, int min, int max) {
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
