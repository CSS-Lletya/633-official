package skills.thieving;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;

import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import lombok.Getter;
import lombok.val;

/**
 * @author Kris | 21. okt 2017 : 19:28.03
 * @author Corey 23/11/19
 */
public enum ChestData {
    
    COINS_10_CHEST(13, 12, 7.8, true, 2566, 2604, new Item(995, 10)),
    NATURE_RUNE_CHEST(28, 25, 25, true, 2567, 2604, new Item(995, 3), new Item(561, RandomUtility.inclusive(1,2))),
    COINS_50_CHEST(43, 83, 125, true, 2568, 2604, new Item(995, 50)),
    ARROW_TIP_CHEST(47, 350, 150, true, 2573, 2604, new Item(41, 5), new Item(995, 20)),
    BLOOD_RUNE_CHEST(59, 225, 250, true, 2569, 2604, new Item(995, 500), new Item(565, 2)) {
        @Override
        public void onSuccess(Player player) {
        	player.task(thief -> {
        		player.getPackets().sendGameMessage("Suddenly a second magical trap triggers.");
                player.setNextAnimation(Animations.RESET_ANIMATION);
                player.setNextWorldTile(new WorldTile(2584, 3337));
        	});
        }
    },
    
    PALADIN_CHEST(72, 667, 500, true, 2570, 2604, new Item(995, 1000), new Item(383), new Item(449), new Item(1623)) {
        @Override
        public void onSuccess(Player player) {
        	player.task(thief -> {
        		player.getPackets().sendGameMessage("Suddenly a second magical trap triggers.");
                player.setNextAnimation(Animations.RESET_ANIMATION);
                player.setNextWorldTile(new WorldTile(2696, 3281));
        	});
        }
    },
;
    
    public static final Short2ObjectOpenHashMap<ChestData> data = new Short2ObjectOpenHashMap<>();
    
    static {
        for (ChestData data : values()) {
            ChestData.data.put((short) data.getClosedId(), data);
        }
    }
    
    @Getter
    private final int level, time, closedId, openId;
    @Getter
    private final double experience;
    @Getter
    private final boolean trapped;
    @Getter
    private final Item[] loot;
    @Getter
    private final ChestLoot lootTable;
    
    ChestData(final int level, final int time, final double experience, final boolean trapped, final int closedId, final int openId, final Item... loot) {
        this.level = level;
        this.time = time;
        this.experience = experience;
        this.trapped = trapped;
        this.closedId = closedId;
        this.openId = openId;
        this.loot = loot;
        this.lootTable = null;
    }
    
    ChestData(final int level, final int time, final double experience, final boolean trapped, final int closedId, final int openId, final ChestLoot lootTable) {
        this.level = level;
        this.time = time;
        this.experience = experience;
        this.trapped = trapped;
        this.closedId = closedId;
        this.openId = openId;
        this.loot = null;
        this.lootTable = lootTable;
    }

    ChestData(final int level, final int time, final double experience, final int closedId, final int openId, final ChestLoot lootTable) {
        this.level = level;
        this.time = time;
        this.experience = experience;
        this.trapped = false;
        this.closedId = closedId;
        this.openId = openId;
        this.loot = null;
        this.lootTable = lootTable;
    }
    
    public static ChestData getChest(final int objectId) {
        return data.get((short) objectId);
    }
    
    public void onSuccess(final Player player) {
    }
    
    public void onFailure(final Player player) {
    }
    
    public void onTriggerTrap(final Player player) {
        val hitAmount = RandomUtility.random(player.getMaxHitpoints() / 14, player.getMaxHitpoints() / 8);
        
        player.applyHit(new Hit(player, Math.max(hitAmount, 1), HitLook.REGULAR_DAMAGE));
        player.getAudioManager().sendSound(0);
        player.getPackets().sendGameMessage("You have activated a trap on the chest.");
    }
    
    public interface ChestLoot {
        Item[] generateLoot();
    }
    
}
