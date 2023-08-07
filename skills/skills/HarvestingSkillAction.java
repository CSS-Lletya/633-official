package skills;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtility;

import lombok.Getter;
import lombok.Setter;

/**
 * The skill action that represents an action where items are periodically added
 * to and removed from an inventory based on a success factor. This type of
 * skill action is more complicated and requires that a player have the items to
 * be removed and the space for the items to harvest.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code FISHING} and {@code WOODCUTTING}.
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see DestructionSkillAction
 * @see ProducingSkillAction
 */
public abstract class HarvestingSkillAction extends SkillHandler {
	
	/**
	 * The factor boost that determines the success rate for harvesting based on
	 * skill level. The higher the number the less frequently harvest will be
	 * obtained. A value higher than {@code 99} or lower than {@code 0} will
	 * throw an {@link IllegalStateException}.
	 */
	private static final int SUCCESS_FACTOR = 10;
	
	/**
	 * Creates a new {@link HarvestingSkillAction}.
	 * @param player the player this skill action is for.
	 * @param position the position the player should face.
	 */
	public HarvestingSkillAction(Player player, Optional<WorldTile> position) {
		super(player, position);
	}
	
	@Override
	public final boolean canRun(Task t) {
		Optional<Item[]> removeItems = removeItems();
		Item[] harvestItems = harvestItems();
		if(removeItems.isPresent() && !getPlayer().getInventory().containsItems(removeItems.get())) {
			getPlayer().getPackets().sendGameMessage("You do not have the required items to perform this!");
			return false;
		}
		if(!getPlayer().getInventory().hasFreeSlots()) {
			onHarvest(t, harvestItems, false);
			getPlayer().getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
			return false;
		}
		return true;
	}
	
	@Override
	public void execute(Task t) {
		Preconditions.checkState(SUCCESS_FACTOR >= 0 && SUCCESS_FACTOR <= 99, "Invalid success factor for harvesting!");
		int factor = (getPlayer().getSkills().getLevel(getSkillId()) / SUCCESS_FACTOR);
		double boost = (factor * 0.01);
		if(RandomUtility.success((successFactor() + boost))) {
			Optional<Item[]> removeItems = removeItems();
			Item[] harvestItems = harvestItems();
			
			for(Item item : harvestItems) {
				if(item == null)
					continue;
				if(item.getDefinitions() == null)
					continue;
				if (!isIgnoreResourceGather()) {
					getPlayer().getInventory().addItem(item);
					if (harvestMessage() && item.getDefinitions() != null && item.getDefinitions().getName() != null) {
						getPlayer().getPackets()
								.sendGameMessage("You get some " + item.getDefinitions().getName() + ".");
					}
				}
				player.getSkills().addExperience(getSkillId(), experience());
				onHarvest(t, harvestItems, true);
				removeItems.ifPresent(getPlayer().getInventory()::removeItems);
			}
		}
	}
	
	@Override
	public int delay() {
		return 2;
	}
	
	/**
	 * The method executed upon harvest of the items.
	 * @param t the task executing this method.
	 * @param items the items being harvested.
	 * @param success determines if the harvest was successful or not.
	 */
	public void onHarvest(Task t, Item[] items, boolean success) {

	}
	
	/**
	 * The success factor for the harvest. The higher the number means the more
	 * frequently harvest will be obtained.
	 * @return the success factor.
	 */
	public abstract double successFactor();
	
	/**
	 * The items to be removed upon a successful harvest.
	 * @return the items to be removed.
	 */
	public abstract Optional<Item[]> removeItems();
	
	/**
	 * The items to be harvested upon a successful harvest.
	 * @return the items to be harvested.
	 */
	public abstract Item[] harvestItems();
	
	/**
	 * Determines if a message should be sent upon successfully harvesting.
	 * @return {@code true} if a message should be sent, {@code false} otherwise.
	 */
	public boolean harvestMessage() {
		return true;
	}
	
	@Override
	public boolean isPrioritized() {
		return false;
	}
	
	@Getter
	@Setter
	public boolean ignoreResourceGather = false;
}
