package skills.mining;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import lombok.AllArgsConstructor;
import skills.DestructionSkillAction;

public final class AxeAssembly extends DestructionSkillAction {

	private final AxeData data;

	public AxeAssembly(Player player, AxeData axeDatas) {
		super(player, Optional.empty());
		this.data = axeDatas;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public Item destructItem() {
		return new Item(466);
	}

	@Override
	public void onDestruct(Task t, boolean success) {
		if (success) {
			if (player.getInventory().canRemove(data.head, 1)) {
				player.getInventory().addItem(new Item(data.product));
				player.getPackets().sendGameMessage("You skillfully reattach the pick head to the pickaxe handle.");
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
		return true;
	}

	@Override
	public double experience() {
		return 0;
	}

	public int getSkillId() {
		return -1;
	}

	@Override
	public boolean initialize() {
		return true;
	}
	
	@AllArgsConstructor
	public enum AxeData {
		BRONZE(480, 1265),
		IRON(482,1267),
		STEEL(484, 1269),
		MITHRIL(486, 1273),
		ADAMANT(488, 1271),
		RUNE(490, 1275),
		;
		public int head, product;
	}
}