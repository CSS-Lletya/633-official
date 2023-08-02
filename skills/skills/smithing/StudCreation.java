package skills.smithing;

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

public class StudCreation extends DestructionSkillAction {

	public StudCreation(Player player) {
		super(player, Optional.empty());
	}

	@Override
	public boolean canExecute() {
		if (player.getSkills().getLevel(Skills.CRAFTING) < 41) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of 41 to continue this action.");
			return false;
		}
		return true;
	}

	@Override
	public Item destructItem() {
		return new Item(ItemNames.STEEL_BAR_2353);
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if (success) {
			player.getInventory().addItem(new Item(ItemNames.STEEL_STUDS_2370));
			player.getPackets().sendGameMessage("You carefully craft Steel Studs from the bar.");
			player.getDetails().getStatistics().addStatistic("Items_Crafted");
			player.getDetails().getStatistics()
					.addStatistic(ItemDefinitions.getItemDefinitions(ItemNames.STEEL_STUDS_2370).getName() + "_Crafted");
		}
		if (!player.getInventory().containsAny(ItemNames.STEEL_BAR_2353))
			t.cancel();
	}

	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.USING_HAMMER_ON_ANVIL);
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
		return 37.5;
	}

	public int getSkillId() {
		return Skills.COOKING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}