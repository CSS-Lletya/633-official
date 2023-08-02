package skills.prayer;

import java.util.Optional;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public final class BlessSpiritShieldCreation extends DestructionSkillAction {

	public BlessSpiritShieldCreation(Player player) {
		super(player, Optional.empty());
	}

	@Override
	public boolean canExecute() {
		if (player.getSkills().getLevel(Skills.PRAYER) < 85) {
			player.getPackets().sendGameMessage("You need a prayer level of 85 to do this.");
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
			if (player.getInventory().canRemove(ItemNames.HOLY_ELIXIR_13754, 1) && player.getInventory().canRemove(ItemNames.SPIRIT_SHIELD_13734, 1)) {
				player.getInventory().addItem(new Item(13736));
				player.dialogue(d -> d.item(13736, "You bless your spirit shield with the power of the holy elixir."));
			}
		}
		t.cancel();
	}

	@Override
	public int delay() {
		return 1;
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
		return Skills.PRAYER;
	}

	@Override
	public boolean initialize() {
		return true;
	}
}