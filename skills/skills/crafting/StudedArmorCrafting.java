package skills.crafting;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.DestructionSkillAction;
import skills.Skills;

public final class StudedArmorCrafting extends DestructionSkillAction {
	
	private final Item studedItem;
	
	public StudedArmorCrafting(Player player, Item crabItem) {
		super(player, Optional.empty());
		this.studedItem = crabItem;
	}

	@Override
	public boolean canExecute() {
		if (studedItem.getId() == 1129 && player.getSkills().getLevel(Skills.CRAFTING) < 41) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of 41 to continue this action.");
			return false;
		}
		if (studedItem.getId() == 1095 && player.getSkills().getLevel(Skills.CRAFTING) < 44) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of 44 to continue this action.");
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
			if (player.getInventory().canRemove((studedItem.getId() == 1129 ? 1129 : 1095), 1)){
				player.getInventory().deleteItem(new Item(2370));
				player.getInventory().addItem(new Item(studedItem.getId() == 1129 ? 1133 : 1097));
				player.getDetails().getStatistics().addStatistic("Items_Crafted");
		        player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions((studedItem.getId() == 1129 ? 1129 : 1095)).getName() + "_Crafted");

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
		return false;
	}

	@Override
	public double experience() {
		return studedItem.getId() == 1129 ? 40 : 42;
	}

	public int getSkillId() {
		return Skills.CRAFTING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}