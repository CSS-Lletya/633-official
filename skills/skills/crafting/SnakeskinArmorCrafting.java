package skills.crafting;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtility;

import lombok.AllArgsConstructor;
import skills.ProducingSkillAction;
import skills.Skills;

public final class SnakeskinArmorCrafting extends ProducingSkillAction {
	
	private final SnakeData definitions;
	/**
	 * Constructs a new {@link SnakeskinArmorCrafting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public SnakeskinArmorCrafting(Player player, SnakeData definitions) {
		super(player, Optional.empty());
		this.definitions = definitions;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
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
		return Optional.of(new Item[]{new Item(6289, definitions.amount), new Item(ItemNames.THREAD_1734)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(definitions.item)});
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
		if (player.getSkills().getLevel(Skills.CRAFTING) < definitions.requirement) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of "+definitions.requirement+" to continue this action.");
			return false;
		}
		if (!player.getInventory().containsAny(6289)) {
			player.getPackets().sendGameMessage("You do not have enough " + ItemDefinitions.getItemDefinitions(6289).getName() + " to make a " + ItemDefinitions.getItemDefinitions(definitions.item).getName() + ".");
			return false;
		}
		return true;
	}
	
	@Override
	public double experience() {
		return definitions.xp;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
	
	@AllArgsConstructor
	public enum SnakeData {
		BODY(6322, 15, 53, 55),
		LEGS(6324, 12, 51, 50),
		VAMB(6330, 8, 47, 35),
		BANDANA(6326, 5, 48, 45),
		BOOTS(6328, 6, 45, 30);
		
		/**
		 * The item which needs to be stringed.
		 */
		public final int item;
		
		/**
		 * The item which is stringed.
		 */
		private final int amount;
		
		private final int requirement;
		
		private final int xp;
	}
}