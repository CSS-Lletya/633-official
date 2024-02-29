package skills.thieving;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtility;

import lombok.val;
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
	 * The method which identifies if this player can initialise the theft.
	 * @return <true> if the player can, <false> otherwise.
	 */
	public boolean canInit() {
		return true;
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
		if(success(player, requirement())) {
			getPlayer().getInventory().addItem(loot());
			getPlayer().getSkills().addExperience(Skills.THIEVING, experience());
		}
	}
	
	/**

	 * @author Kris | 21. okt 2017 : 14:46.17

	 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}

	 */

	public static boolean success(final Player player, final int requirement) {

		val level = player.getSkills().getLevel(Skills.THIEVING);

		val baseChance = 5d / 833 * level;

		val reqChance = 0.49 - (requirement * 0.0032) - 0.02;

		double chance = baseChance + reqChance;

		return RandomUtility.randomDouble() < chance;

	}
	
	@Override
	public boolean isPrioritized() {
		return false;
	}
}