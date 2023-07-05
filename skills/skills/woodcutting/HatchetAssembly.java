package skills.woodcutting;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import lombok.AllArgsConstructor;
import skills.DestructionSkillAction;

public final class HatchetAssembly extends DestructionSkillAction {

	private final AxeData data;

	public HatchetAssembly(Player player, AxeData axeDatas) {
		super(player, Optional.empty());
		this.data = axeDatas;
	}

	@Override
	public boolean canExecute() {
		return true;
	}

	@Override
	public Item destructItem() {
		return new Item(492);
	}

	@Override
	public void onDestruct(Task t, boolean success) {
		if (success) {
			if (player.getInventory().canRemove(data.head, 1)) {
				player.getInventory().addItem(new Item(data.product));
				player.getPackets().sendGameMessage("You skillfully reattach the axe head to the axe handle.");
				player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(data.product).getName() + "_Reassembled");
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
		BRONZE(508, 1531),
		IRON(510,1349),
		STEEL(512, 1353),
		BLACK(514, 1361),
		MITHRIL(516, 1355),
		ADAMANT(518, 1357),
		RUNE(520, 1359),
		DRAGON(6743, 6739)
		;
		public int head, product;
	}
}