package skills.woodcutting.sawmill;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.zone.impl.SawmillMapZone;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

public class ConveyorBeltHopper extends Action {

    private SawmillMapZone sawmill;
    private int amount;

    public ConveyorBeltHopper(int amount, SawmillMapZone sawmill) {
        this.amount = amount;
        this.sawmill = sawmill;
    }

    @Override
    public boolean start(Player player) {
        return process(player);
    }

    @Override
    public boolean process(Player player) {
        if (sawmill.isPlanksFull()) {
            player.getPackets().sendGameMessage("The conveyor belt is full.");
            return false;
        }
        if (!player.getInventory().containsItem(1511, 1)) {
            player.getPackets().sendGameMessage("You have no logs to put on the conveyor belt.");
            return false;
        }
        return amount > 0;
    }

    @Override
    public int processWithDelay(Player player) {
        player.setNextAnimation(Animations.PLACING_SOMETHING_OVERHEAD);
        player.getInventory().deleteItem(new Item(1511));
        sawmill.addPlank(player);
        return 2;
    }

    @Override
    public void stop(Player player) {
    }
}