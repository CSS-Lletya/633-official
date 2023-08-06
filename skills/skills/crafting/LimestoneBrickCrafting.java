package skills.crafting;

import java.util.Optional;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtility;

import lombok.val;
import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Represents Limestone crafting
 * 
 * Credits to Kris for success formula
 * @author Dennis
 *
 */
public class LimestoneBrickCrafting extends ProducingSkillAction {
	
	/**
	 * Constructs a new {@link LimestoneBrickCrafting}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 */
	public LimestoneBrickCrafting(Player player) {
		super(player, Optional.empty());
	}
	
	@Override
	public boolean manualMode() {
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			if (isSuccessful()) {
				player.getSkills().addExperience(Skills.CRAFTING, 6);
				player.getInventory().deleteItem(new Item(ItemNames.LIMESTONE_3211));
				player.getInventory().addItem(new Item(ItemNames.LIMESTONE_BRICK_3420));
			} else {
				player.getSkills().addExperience(Skills.CRAFTING, 1.5);
				player.getInventory().deleteItem(new Item(ItemNames.LIMESTONE_3211));
				player.getInventory().addItem(new Item(ItemNames.ROCK_968));
			}
		}
		if (player.getInventory().getNumberOf(ItemNames.LIMESTONE_3211) < 1)
			t.cancel();
	}
    
	private static final int MAXIMUM_SUCCESS_LEVEL = 40;
    private static final double BASE_SUCCESS_PROBABILITY = 0.75;
    private static final double MAXIMUM_SUCCESS_PROBABILITY = 1.0;
    
	private boolean isSuccessful() {
		val spreadSuccess = MAXIMUM_SUCCESS_PROBABILITY - BASE_SUCCESS_PROBABILITY;
		val successPerLevel = spreadSuccess / MAXIMUM_SUCCESS_LEVEL;
		val successProbability = BASE_SUCCESS_PROBABILITY + (player.getSkills().getTrueLevel(Skills.CRAFTING) * successPerLevel);
		val succeeded = RandomUtility.randomDouble() <= successProbability;
		return succeeded;
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.empty();
	}
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.empty();
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.empty();
	}
	
	@Override
	public int delay() {
		return 2;
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
		if (player.getSkills().getLevel(Skills.CRAFTING) < 12) {
			player.getPackets()
					.sendGameMessage("You need a Crafting level of 12 to turn the limestone into a brick.");
			return false;
		}
		return true;
	}
	
	@Override
	public double experience() {
		return 6;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
}