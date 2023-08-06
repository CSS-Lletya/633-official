package skills.crafting;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtility;

import skills.ProducingSkillAction;
import skills.Skills;

public final class HardLeatherCrafting extends ProducingSkillAction {
	
	/**
	 * Constructs a new {@link HardLeatherCrafting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public HardLeatherCrafting(Player player) {
		super(player, Optional.empty());
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			LeatherItemCrafting.handleThreadRemoval(player);
			if (RandomUtility.random(30) == 5) {
				player.getInventory().deleteItem(new Item(ItemNames.NEEDLE_1733));
				player.getPackets().sendGameMessage("Your needle broke.");
				t.cancel();
			}
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.CRAFTING_LEATHER);
	}
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(ItemNames.HARD_LEATHER_1743)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(ItemNames.HARDLEATHER_BODY_1131)});
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
	public boolean initialize() {
		return true;
	}
	
	@Override
	public boolean canExecute() {
		if (player.getSkills().getLevel(Skills.CRAFTING) < 28) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of 28 to continue this action.");
			return false;
		}
		return true;
	}
	
	@Override
	public double experience() {
		return 35;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
}