package skills.magic;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import skills.Skills;

/**
 * @Andreas - AvalonPK
 */

public class EnchantingBolts extends Action {

    public static final int AIR_RUNE = 556, WATER_RUNE = 555, EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558,
            NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560, BLOOD_RUNE = 565, SOUL_RUNE = 566,
            ASTRAL_RUNE = 9075, LAW_RUNE = 563, BODY_RUNE = 559,
            COSMIC_RUNE = 564;
    private Enchant enchant;
    private int ticks;

    public EnchantingBolts(Enchant enchant, int ticks) {
        this.enchant = enchant;
        this.ticks = ticks;
    }

    public static int maxMakeQuantity(Enchant fletch, Item item) {
        return 10;
    }

    public static Enchant isEnchanting(Item first, Item second) {
        Enchant enchant = Enchant.forId(first.getId());
        return enchant;
    }

    @Override
    public boolean start(Player player) {
        if (!process(player))
            return false;
        player.getMovement().stopAll();
        return true;
    }

    @Override
    public boolean process(Player player) {
        if (player.getSkills().getLevel(Skills.MAGIC) < enchant.getLevel()) {
            player.getPackets().sendGameMessage("You need a level of " + enchant.getLevel() + " magic to enchant this.");
            return false;
        }
        Item[] runes = enchant.getRunes();
        for (Item rune : runes) {
            if (!player.getInventory().containsItem(rune.getId(), rune.getAmount())) {
			    player.getPackets().sendGameMessage("You don't have enough "
			            + ItemDefinitions.getItemDefinitions(rune.getId()).getName() + "s to enchant this.");
			    return false;
			}
        }
        if (!player.getInventory().containsItem(enchant.getBaseId(), 1)) {
            player.getPackets().sendGameMessage("You don't have any "
                    + ItemDefinitions.getItemDefinitions(enchant.getBaseId()).getName() + " to enchant.");
            return false;
        }
        if (ticks <= 0)
            return false;
        return true;
    }

    public Enchant getEnchant() {
        return enchant;
    }

    @Override
    public int processWithDelay(Player player) {
        ticks--;
        int amount = maxMakeQuantity(enchant, new Item(enchant.getNewId()));
        if (!player.getInventory().containsItem(enchant.getBaseId(), amount))
            amount = player.getInventory().getNumberOf(enchant.getBaseId());
        player.getInventory().addItem(enchant.getNewId(), amount);
        Item[] runes = enchant.getRunes();
        for (Item rune : runes)
            player.getInventory().deleteItem(rune.getId(), rune.getAmount());
        player.getInventory().deleteItem(enchant.getBaseId(), amount);
        player.setNextAnimation(new Animation(4462));
        player.setNextGraphics(new Graphics(759));
        player.getAudioManager().sendSound(Sounds.BOLT_ENCHANTING);
        player.getPackets().sendGameMessage("You enchant " + amount + " "
                + ItemDefinitions.getItemDefinitions(enchant.getNewId()).getName() + ".");
        player.getSkills().addXp(Skills.MAGIC, enchant.getXp() * amount);
        player.getDetails().getStatistics().addStatistic("Bolts_Enchanted").addStatistic(ItemDefinitions.getItemDefinitions(enchant.getBaseId()).getName() +"_Enchantments");
        if (!player.getInventory().containsItem(enchant.getBaseId(), 1)) {
            player.getPackets().sendGameMessage("You don't have any "
                    + ItemDefinitions.getItemDefinitions(enchant.getBaseId()).getName() + " left to enchant.");
            return -1;
        }
        return 2;
    }

    @Override
    public void stop(final Player player) {
    	
    }

    public enum Enchant {

        OPAL(879, 9236, 0.9, 4, new Item(COSMIC_RUNE, 1), new Item(AIR_RUNE, 2)),

        SAPPHIRE(9337, 9240, 1.7, 7, new Item(COSMIC_RUNE, 1), new Item(WATER_RUNE, 1), new Item(MIND_RUNE, 1)),

        JADE(9335, 9237, 1.9, 14, new Item(COSMIC_RUNE, 1), new Item(EARTH_RUNE, 2)),

        PEARL(880, 9238, 2.9, 24, new Item(COSMIC_RUNE, 1), new Item(WATER_RUNE, 2)),

        EMERALD(9338, 9241, 3.7, 27, new Item(COSMIC_RUNE, 1), new Item(AIR_RUNE, 3), new Item(NATURE_RUNE, 1)),

        RED_TOPAZ(9336, 9239, 3.3, 29, new Item(COSMIC_RUNE, 1), new Item(FIRE_RUNE, 2)),

        RUBY(9339, 9242, 5.9, 49, new Item(COSMIC_RUNE, 1), new Item(FIRE_RUNE, 5), new Item(BLOOD_RUNE, 1)),

        DIAMOND(9340, 9243, 6.7, 57, new Item(COSMIC_RUNE, 1), new Item(EARTH_RUNE, 10), new Item(LAW_RUNE, 2)),

        DRAGONSTONE(9341, 9244, 7.8, 68, new Item(COSMIC_RUNE, 1), new Item(EARTH_RUNE, 15), new Item(SOUL_RUNE, 1)),

        ONYX(9342, 9245, 9.7, 87, new Item(COSMIC_RUNE, 1), new Item(FIRE_RUNE, 20), new Item(DEATH_RUNE, 1));

        private static Map<Integer, Enchant> enchanting = new HashMap<Integer, Enchant>();

        static {
            for (Enchant enchant : Enchant.values())
                enchanting.put(enchant.baseId, enchant);
        }

        private int baseId;
        private int newId;
        private double xp;
        private int level;
        private Item runes[];

        private Enchant(int baseId, int newId, double xp, int level, Item... runes) {
            this.baseId = baseId;
            this.newId = newId;
            this.xp = xp;
            this.level = level;
            this.runes = runes;
        }

        public static Enchant forId(int id) {
            return enchanting.get(id);
        }

        public int getBaseId() {
            return baseId;
        }

        public int getNewId() {
            return newId;
        }

        public double getXp() {
            return xp;
        }

        public int getLevel() {
            return level;
        }

        public Item[] getRunes() {
            return runes;
        }
    }
}
