package skills.cooking;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public final class ChocolateCake extends DestructionSkillAction {

	public ChocolateCake(Player player) {
		super(player, Optional.empty());
	}

	@Override
	public boolean canExecute() {
		if (player.getSkills().getLevel(Skills.COOKING) < 50) {
			player.getPackets().sendGameMessage("You need a cooking level of 50 to do this.");
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
			if (player.getInventory().canRemove(1973, 1) && player.getInventory().canRemove(1891, 1))
				player.getInventory().addItem(new Item(1897));
		}
		t.cancel();
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
		return 30;
	}

	public int getSkillId() {
		return Skills.COOKING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}