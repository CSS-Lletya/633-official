package skills.crafting;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.DestructionSkillAction;
import skills.Skills;

public final class CrabShellCreations extends DestructionSkillAction {
	
	private final Item crabItem;
	
	public CrabShellCreations(Player player, Item crabItem) {
		super(player, Optional.empty());
		this.crabItem = crabItem;
	}

	@Override
	public boolean canExecute() {
		if (player.getSkills().getLevel(Skills.CRAFTING) < 15) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of 15 to continue this action.");
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
			if (player.getInventory().canRemove((crabItem.getId() == ItemNames.FRESH_CRAB_CLAW_7536 ? ItemNames.FRESH_CRAB_CLAW_7536 : ItemNames.FRESH_CRAB_SHELL_7538), 1)){
				player.getInventory().addItem(new Item(crabItem.getId() == ItemNames.FRESH_CRAB_CLAW_7536 ? ItemNames.CRAB_CLAW_7537 : ItemNames.CRAB_HELMET_7539));
				player.getDetails().getStatistics().addStatistic("Items_Crafted");
		        player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions((crabItem.getId() == ItemNames.FRESH_CRAB_CLAW_7536 ? ItemNames.FRESH_CRAB_CLAW_7536 : ItemNames.FRESH_CRAB_SHELL_7538)).getName() + "_Crafted");

			}
		}
		t.cancel();
	}

	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.RUB_HANDS_TOGETHER);
	}
	
	@Override
	public int delay() {
		return 3;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public double experience() {
		return 7;
	}

	public int getSkillId() {
		return Skills.CRAFTING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}