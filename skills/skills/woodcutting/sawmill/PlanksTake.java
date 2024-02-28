package skills.woodcutting.sawmill;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.zone.impl.SawmillMapZone;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

public class PlanksTake extends Action {

    private SawmillMapZone sawmill;
    private int amount;

    public PlanksTake(int amount, SawmillMapZone sawmill) {
        this.amount = amount;
        this.sawmill = sawmill;
    }

    @Override
    public boolean start(Player player) {
        return process(player);
    }

    @Override
    public boolean process(Player player) {
        if (!player.getInventory().hasFreeSlots()) {
            player.getPackets().sendGameMessage("Your inventory is full.");
            return false;
        }
        if (!sawmill.hasPlanks()) {
            player.getPackets().sendGameMessage("You have no planks left.");
            return false;
        }
        return amount > 0;
    }

    @Override
    public int processWithDelay(Player player) {
        player.setNextAnimation(Animations.DROP_SOMETHING_SLOWLY);
        player.getInventory().addItem(new Item(960));
        sawmill.removePlank(player);
        return amount-- == 1 ? -1 : 1;
    }

	@Override
	public void stop(Player player) {
		// TODO Auto-generated method stub
		
	}
}
