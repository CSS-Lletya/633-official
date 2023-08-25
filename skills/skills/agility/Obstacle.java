package skills.agility;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utilities.MutableNumber;

import skills.Skills;

/**
 * Represents an Agility {@link Obstacle} being interacted with to train the
 * skill Agility.
 * 
 * @author Dennis
 *
 */
public interface Obstacle {

	/**
	 * Initializes the beginning event of a successful movement Note: Failing
	 * {@link Obstacle} isn't possible as of the design of this system, optionally
	 * can be added if players would generally want that feature.
	 * 
	 * @param player
	 */
	void start(Player player, GameObject object);

	/**
	 * Finalizes the completed & successful obstacle event
	 * 
	 * @param player
	 */
	void end(Player player, GameObject object);

	/**
	 * The initial message being sent upon first interaction
	 * 
	 * @return
	 */
	default String getStartMessage() {
		return null;
	}

	/**
	 * The final message sent after completing the Agility {@link Obstacle}
	 * 
	 * @return
	 */
	default String getEndMessage() {
		return null;
	}

	/**
	 * The Agility course key/stage container to check and increment with. This is
	 * an optional return because the {@link Player} may be interacting with a
	 * standard Agility {@link Obstacle} outside of an Agility course (Shortcut)
	 * 
	 * @param player
	 * @return
	 */
	Optional<MutableNumber> stageKey(Player player); 
	
	/**
	 * Executes the interaction with an Agility {@link Obstacle}
	 * 
	 * @param player
	 * @param object
	 */
	default void execute(Player player, GameObject object) {
		player.getMovement().stopAll();
		if (player.getSkills().getTrueLevel(Skills.AGILITY) < AgilityHandler.getLevelRequired(this, object)) {
			player.getPackets().sendGameMessage("You need an Agility level of at least "
					+ AgilityHandler.getLevelRequired(this, object) + " to use this Agility shortcut.");
			return;
		}
		final boolean run = player.isRun();
		player.setRunHidden(false);
		player.getMovement().lock();
		if (getStartMessage() != null)
			player.getPackets().sendGameMessage(getStartMessage());
		start(player, object);
		World.get().submit(new Task(AgilityHandler.getDuration(this, object)) {
			@Override
			protected void execute() {
				finish(player, object);
				player.setRunHidden(run);
				checkExperience(player, object);
				end(player, object);
				cancel();
			}
		});
	}

	/**
	 * Finalizes the interaction of an Agility {@link Obstacle}
	 * 
	 * @param player
	 * @param object
	 */
	default void finish(final Player player, final GameObject object) {
		player.getMovement().unlock();
		player.getAppearance().setRenderEmote(-1);
		if (getEndMessage() != null)
			player.getPackets().sendGameMessage(getEndMessage());
	}

	/**
	 * Checks whether or not to give experience to the {@link Player} based on the players current stage in the specific Agility course
	 * @param player
	 * @param object
	 */
	default void checkExperience(Player player, GameObject object) {
		if (!stageKey(player).isPresent()) {
			player.getSkills().addExperience(Skills.AGILITY, AgilityHandler.getRewardedExperience(this, object));
			return;
		}
		stageKey(player).ifPresent(course -> {
			if (course.get() == AgilityHandler.getStageId(this, object)) {
				course.getAndIncrement();
				player.getSkills().addExperience(Skills.AGILITY, AgilityHandler.getRewardedExperience(this, object));
			}
		});
	}
}