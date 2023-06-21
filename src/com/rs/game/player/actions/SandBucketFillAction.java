package com.rs.game.player.actions;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

public class SandBucketFillAction extends Action {

    public static final int EMPTY_BUCKET = 1925, BUCKET_OF_SAND = 1783;

    @Override
    public boolean start(Player player) {
        return true;
    }

    @Override
    public boolean process(Player player) {
        if (!player.getInventory().containsItem(EMPTY_BUCKET, 1)) {
            player.getPackets().sendGameMessage("You have no empty buckets to put the sand in.");
            return false;
        }
        return true;
    }

    @Override
    public int processWithDelay(Player player) {
    	player.getAudioManager().sendSound(Sounds.FILL_BUCKET_WITH_SAND);
        player.setNextAnimation(Animations.BUCKET_SCOOPING);
        player.getInventory().replaceItems(new Item(EMPTY_BUCKET), new Item(BUCKET_OF_SAND));
        player.getPackets().sendGameMessage("You fill the bucket with sand.", true);
        return 1;
    }

    @Override
    public void stop(Player player) {
    }
}