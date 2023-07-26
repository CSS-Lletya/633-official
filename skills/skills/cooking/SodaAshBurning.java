package skills.cooking;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public final class SodaAshBurning extends DestructionSkillAction {

	public SodaAshBurning(Player player) {
		super(player, Optional.empty());
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public Item destructItem() {
		return new Item(ItemNames.SEAWEED_401);
	}

	@Override
	public void onDestruct(Task t, boolean success) {
		if (success) {
			player.getPackets().sendGameMessage("You burn the seaweed to soda ash.");
			player.setNextAnimation(Animations.COOKING_ON_FIRE);
			player.getInventory().addItem(new Item(ItemNames.SODA_ASH_1781));
			player.getDetails().getStatistics().addStatistic("Soda_Ash_Made");
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
		return 1;
	}

	public int getSkillId() {
		return Skills.COOKING;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}