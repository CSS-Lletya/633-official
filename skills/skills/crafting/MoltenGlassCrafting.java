package skills.crafting;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public class MoltenGlassCrafting extends DestructionSkillAction {

	public MoltenGlassCrafting(Player player) {
		super(player, Optional.empty());
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
			if (player.getInventory().canRemove(ItemNames.BUCKET_1925, 1) || player.getInventory().canRemove(ItemNames.BUCKET_OF_SAND_1783, 1)) {
				player.getInventory().replaceItems(new Item(ItemNames.SODA_ASH_1781), new Item(ItemNames.MOLTEN_GLASS_1775));
				player.getPackets().sendGameMessage("You heat the sand and soda ash in the furnace to make glass.");
				player.getDetails().getStatistics().addStatistic("Molten_Glass_made");
				player.setNextAnimation(Animations.SMELTING_INSIDE_FURNACE);
			}
			
		}
		if (!player.getInventory().containsAny(ItemNames.SODA_ASH_1781))
			t.cancel();
	}

	@Override
	public int delay() {
		return 3;
	}

	@Override
	public boolean instant() {
		return false;
	}

	@Override
	public double experience() {
		return 20;
	}

	public int getSkillId() {
		return Skills.COOKING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}