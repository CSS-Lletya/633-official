package skills.woodcutting.sawmill;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

public class StackOfLogs extends Action {

    private int amount;

    public StackOfLogs(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean start(Player player) {
        if (process(player)) {
            player.getPackets().sendGameMessage((amount == 1 ? "You select a suitable log" : "You start selecting suitable logs") + " from the pile.", true);
            return true;
        }
        return false;
    }

    @Override
    public boolean process(Player player) {
        if (!player.getInventory().hasFreeSlots()) {
            player.getPackets().sendGameMessage("Your inventory is full.");
            return false;
        }
        return amount > 0;
    }

    @Override
    public int processWithDelay(Player player) {
    	player.getAudioManager().sendSound(Sounds.PICK_FLAX);
        player.setNextAnimation(Animations.DROP_SOMETHING_SLOWLY);
        player.getInventory().addItem(new Item(1511));
        return amount-- == 1 ? -1 : 1;
    }

    @Override
    public void stop(Player player) {
    }

}
