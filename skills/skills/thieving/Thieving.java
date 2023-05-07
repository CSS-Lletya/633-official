package skills.thieving;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.SkillHandler;
import skills.Skills;

/**
 * Represents functionality for a thieving {@link SkillAction}.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public abstract class Thieving extends SkillHandler {
	
	/**
	 * Constructs a new {@link Thieving} {@link SkillAction}.
	 * @param player the player performing this skill action.
	 * @param position the position this player should face.
	 */
	public Thieving(Player player, Optional<WorldTile> position) {
		super(player, position);
	}
	
	/**
	 * The requirement required.
	 * @return the identifier which identifies the requirement.
	 */
	public abstract int requirement();
	
	/**
	 * The loot the player receives upon stealing from this stall.
	 * @return the array of items.
	 */
	public abstract Item loot();
	
	/**
	 * The method executed upon stealing from this stall.
	 */
	public abstract void onExecute(Task t);
	
	/**
	 * The method which gets executed as soon as the skill action is stopped.
	 * @param success determines whether the theft was a success or not.
	 */
	public void onStop(boolean success) {

	}
	
	/**
	 * The method which identifies if this player can initialise the theft.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canInit() {
		return true;
	}
	
	/**
	 * Indicates if this player failed to thief.
	 * @return <true> if the player did, <false> otherwise.
	 */
	public boolean failure() {
		return false;
	}
	
	@Override
	public boolean initialize() {
		return canInit();
	}
	
	@Override
	public void execute(Task t) {
		onExecute(t);
	}
	
	@Override
	public void onStop() {
		if(!failure() && canExecute()) {
			onStop(true);
			if(!failure()) {
				getPlayer().getInventory().addItem(loot());
				getPlayer().getSkills().addXp(Skills.THIEVING, experience());
			}
		}
		onStop(false);
	}
	
	@Override
	public boolean isPrioritized() {
		return false;
	}
}