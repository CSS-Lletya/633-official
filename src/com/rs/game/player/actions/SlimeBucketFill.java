package com.rs.game.player.actions;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.player.Player;

public class SlimeBucketFill extends Action {

    @Override
    public boolean start(Player player) {
        return true;
    }

    @Override
    public boolean process(Player player) {
        return player.getInventory().containsItem(1925, 1);
    }

    @Override
    public int processWithDelay(Player player) {
        return fillBucket(player) ? 1 : 0;
    }

    @Override
    public void stop(Player player) {

    }

    private boolean fillBucket(Player player) {
        if (player.getInventory().containsItem(1925, 1)) {
            player.setNextAnimation(Animations.FILLING_BUCKET_FROM_GROUND);
            player.getInventory().deleteItem(1925, 1);
            player.getInventory().addItem(ItemNames.BUCKET_OF_SLIME_4286, 1);
            return true;
        }
        return false;
    }
}