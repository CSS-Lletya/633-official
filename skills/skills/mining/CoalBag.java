package skills.mining;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.IntegerInputAction;

import skills.Skills;

/**
 * Whilst its possible to obtain at 35, only when reaching 40 dungeoneering can
 * you actually afford it.
 * 
 * @author Dennis
 *
 */
public class CoalBag {

	private static final int MAX_COAL_SIZE = 81;

	public static void interact(Player player, int option) {
		if (player.getSkills().getTrueLevel(Skills.DUNGEONEERING) < 35 && player.getSkills().getTrueLevel(Skills.MINING) < 35) {
			player.getPackets().sendGameMessage("You need 35 Dungeoneering and Mining to operate this item.");
			return;
		}
		switch (option) {
		case 1:
			player.getPackets().sendGameMessage("Your coal bag is currently "
					+ player.getDetails().getCoalBagSize().get() + "/" + MAX_COAL_SIZE + ".");
			break;
		case 2:
			withdrawCoal(player, 1);
			break;
		case 3:
			player.getPackets().sendInputIntegerScript("How many would you like to withdraw?", new IntegerInputAction() {
				@Override
				public void handle(int input) {
					withdrawCoal(player, input);
				}
			});
			break;
		}
	}
	
	public static void addCoal(Player player, Item coal) {
		int amt = MAX_COAL_SIZE;
		if (player.getDetails().getCoalBagSize().get() >= amt) {
            player.getPackets().sendGameMessage("Your coal bag is full.");
            return;
        }
		int amount = player.getInventory().getNumberOf(coal.getId());
        if (amount > amt) {
            amount = (amt - player.getDetails().getCoalBagSize().get());
        }
        player.getInventory().deleteItem(coal.getId(), amount);
        player.getDetails().getCoalBagSize().getAndIncrement(amount);
	}
	
    public static void withdrawCoal(Player player, int amount) {
        if (player.getInventory().getFreeSlots() < 1) {
            player.getPackets().sendGameMessage("Not enough space in your inventory.");
            return;
        }
        if (player.getDetails().getCoalBagSize().get() == 0) {
            player.getPackets().sendGameMessage("You have no Coal to withdraw.");
            return;
        }
        if (amount > player.getDetails().getCoalBagSize().get()) {
            amount = player.getDetails().getCoalBagSize().get();
        }
        if (amount > player.getInventory().getFreeSlots()) {
            amount = player.getInventory().getFreeSlots();
        }
        player.getInventory().addItem(453, amount);
        player.getDetails().getCoalBagSize().getAndDecrement(amount);
        player.getPackets().sendGameMessage("You withdraw " + amount + " coal from the coal bag, you now have " + player.getDetails().getCoalBagSize() + "/" + MAX_COAL_SIZE + " remaining.");
    }
}