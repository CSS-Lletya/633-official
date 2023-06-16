package skills.woodcutting.sawmill;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

public class ReturnLogs extends Action {

    @Override
    public boolean start(Player player) {
        return process(player);
    }

    @Override
    public boolean process(Player player) {
        return player.getInventory().getAmountOf(1511) > 0 ;
    }

    @Override
    public int processWithDelay(Player player) {
    	player.getAudioManager().sendSound(Sounds.ITEM_DROPPING_TO_GROUND);
        player.setNextAnimation(Animations.DROP_SOMETHING_SLOWLY);
        player.getInventory().deleteItem(new Item(1511));
        player.getPackets().sendGameMessage("You return a log to the pile.");
        return 1;
    }

    @Override
    public void stop(Player player) {
    }
}