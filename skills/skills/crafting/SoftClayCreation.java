package skills.crafting;

import java.util.Optional;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public final class SoftClayCreation extends DestructionSkillAction {

	Item waterSourceItem;
	
	public SoftClayCreation(Player player, Item waterSourceItem) {
		super(player, Optional.empty());
		this.waterSourceItem = waterSourceItem;
	}

	@Override
	public boolean canExecute() {
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
			if (player.getInventory().canRemove(waterSourceItem.getId(), 1) && player.getInventory().canRemove(ItemNames.CLAY_434, 1)) {
				player.getInventory().addItem(new Item(ItemNames.SOFT_CLAY_1761));
			} 
		}
		t.cancel();
	}

	@Override
	public int delay() {
		return 2;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public double experience() {
		return 0;
	}

	public int getSkillId() {
		return Skills.CRAFTING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}