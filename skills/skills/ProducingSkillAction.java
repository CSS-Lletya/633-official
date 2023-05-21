package skills;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utilities.TextUtils;

import skills.runecrafting.Runecrafting;

/**
 * The skill action that represents an action where one item in an inventory is
 * replaced with a new one. This type of skill action is somewhat basic and
 * requires that a player have the item to be removed.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code COOKING}.
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see DestructionSkillAction
 * @see HarvestingSkillAction
 */
public abstract class ProducingSkillAction extends SkillHandler {
	
	/**
	 * Creates a new {@link ProducingSkillAction}.
	 * @param player the player this skill action is for.
	 * @param position the position the player should face.
	 */
	public ProducingSkillAction(Player player, Optional<WorldTile> position) {
		super(player, position);
	}
	
	@Override
	public final boolean canRun(Task t) {
		Optional<Item[]> removeItem = removeItem();
		
		//Looking if player has empty space for produce items.
		if (!player.getInventory().hasFreeSlots() && !canIgnoreIventoryCheck()) {
			return false;
		}
		
		//removing items from the test container.
		if(removeItem.isPresent()) {
			//if player missing any items check.
			if(!getPlayer().getInventory().containsItems(removeItem.get())) {
				//loop checking specifics if message not present.
				if(!message().isPresent()) {
					for(Item item : removeItem.get()) {
						if(item == null)
							continue;
						if(!getPlayer().getInventory().containsItem(item)) {
							String anyOrEnough = item.getAmount() == 1 ? "any" : "enough";
							getPlayer().getPackets().sendGameMessage("You don't have " + anyOrEnough + " " + TextUtils.appendPluralCheck(item.getDefinitions().getName()) + ".");
							return false;
						}
					}
				} else {
					player.getPackets().sendGameMessage(message().get());
				}
				return false;
			}
		}
		//producing action
		onProduce(t, false);
		return true;
	}
	
	@Override
	public final void execute(Task t) {
		player.getSkills().addXp(getSkillId(), experience());
		removeItem().ifPresent(getPlayer().getInventory()::removeItems);
		produceItem().ifPresent(getPlayer().getInventory()::addItems);
		onProduce(t, true);
	}
	/**
	 * The method executed upon production of an item.
	 * @param t the task executing this method.
	 * @param success determines if the production was successful or not.
	 */
	public void onProduce(Task t, boolean success) {

	}
	
	/**
	 * The item that will be removed upon production.
	 * @return the item that will be removed.
	 */
	public abstract Optional<Item[]> removeItem();
	
	/**
	 * The item that will be added upon production.
	 * @return the item that will be added.
	 */
	public abstract Optional<Item[]> produceItem();
	
	/**
	 * The message that will be sent when the player doesn't
	 * have the items required.
	 * @return the alphabetic value which represents the message.
	 */
	public Optional<String> message() {
		return Optional.empty();
	}
	
	@Override
	public boolean isPrioritized() {
		return false;
	}
	
	/**
	 * Used in very specific cases such as attempting
	 * to {@link Runecrafting} with a full inventory of essences.
	 * @return
	 */
	public boolean canIgnoreIventoryCheck() {
		return false;
	}
	
}
