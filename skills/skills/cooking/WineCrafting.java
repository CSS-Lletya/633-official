package skills.cooking;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.WineTask;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public final class WineCrafting extends DestructionSkillAction {

	int itemUsed;

	public WineCrafting(Player player, int itemUsed) {
		super(player, Optional.empty());
		this.itemUsed = itemUsed;
	}

	@Override
	public boolean canExecute() {
		if (player.getSkills().getLevel(Skills.COOKING) < 35) {
			player.getPackets().sendGameMessage("You need a cooking level of 35 to do this.");
			return false;
		}
		return true;
	}

	@Override
	public Item destructItem() {
		return null;
	}
	
	@Override
	public boolean manualRemoval() {
		return true;
	}

	@Override
	public void onDestruct(Task t, boolean success) {
		if (success) {
			int amount = player.getInventory().getNumberOf(1995) + player.getBank().getAmountOf(1995);
			if (player.getInventory().canRemove(1937, 1) && player.getInventory().canRemove(1987, 1)) {
				player.getInventory().addItem(new Item(1995));
				player.getAction().setAction(new WineTask(amount));
			}
		}
	}

	@Override
	public int delay() {
		return 2;
	}

	@Override
	public boolean instant() {
		return false;
	}

	@Override
	public double experience() {
		return 0;
	}

	public int getSkillId() {
		return Skills.COOKING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}