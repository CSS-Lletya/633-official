package skills;

import java.util.Optional;
import java.util.OptionalInt;

import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.game.task.impl.SkillActionTask;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.net.encoders.other.Animation;

import lombok.Getter;

/**
 * Handler for Skills
 * @author Dennis
 *
 */
public abstract class SkillHandler {

	@Getter
	public transient Player player;
	public transient WorldTile tile;
	
	public SkillHandler(Player player, WorldTile tile) {
		this.player = player;
		this.tile = tile;
	}
	
	/**
	 * The Skill ID
	 */
	public abstract int getSkillId();
	
	/**
	 * The experience given from this skill action.
	 * @return the experience given.
	 */
	public abstract double experience();
	
	/**
	 * Checks if we can execute this Skill Event
	 * @return
	 */
	public abstract boolean canExecute();
	
	/**
	 * Executes the Skill Event
	 */
	public abstract void execute(Task task);
	
	/**
	 * Initializes this skill action and performs any pre-checks, <b>this method is only executed
	 * one<b>.
	 * @return <true> if the skill action can proceed, <false> otherwise.
	 */
	public abstract boolean initialize();
	
	/**
	 * Determines if this skill action should be executed instantly rather than
	 * after the delay.
	 * @return <true> if this skill action should be instant, <false> otherwise.
	 */
	public abstract boolean instant();
	
	/**
	 * Determines if this skill action can be ran.
	 * @param t the task to determine this for.
	 * @return {@code true} if it can, {@code false} otherwise.
	 */
	public boolean canRun(Task task) {
		return true;
	}
	
	/**
	 * The animation played periodically during this skill action.
	 * @return the animation played.
	 */
	public Optional<Animation> animation() {
		return Optional.empty();
	}
	
	/**
	 * The animation played instantly during this skill action.
	 * @return the animation played.
	 */
	public Optional<Animation> startAnimation() {
		return Optional.empty();
	}
	
	/**
	 * The priority determines if this skill can be overriden by another skill.
	 * @return <true> if the skill can be overriden by other skills, <false> otherwise.
	 */
	public abstract boolean isPrioritized();
	
	/**
	 * The delay in between playing animations.
	 * @return the numerical value which determines the time to play the animation again.
	 */
	public OptionalInt animationDelay() {
		return OptionalInt.empty();
	}
	
	/**
	 * The delay intervals of this skill action in ticks.
	 * @return the delay intervals.
	 */
	public abstract int delay();
	
	/**
	 * The method executed when this skill action is submitted.
	 */
	public void onSubmit() {
	
	}
	
	/**
	 * The method executed when another skill action is submitted.
	 * @param other other skill action.
	 */
	public void onSkillAction(SkillHandler other) {
	
	}
	
	/**
	 * The method executed each periodical sequence of this skill action.
	 */
	public void onSequence(Task t) {
	
	}
	
	/**
	 * The method executed when this skill action is stopped.
	 */
	public void onStop() {
	
	}
	
	public WorldPacketsEncoder getPackets() {
		return player.getPackets();
	}
	
	/**
	 * Starts this skill action by submitting a new skill action task.
	 */
	public final void start() {
		if(player.getSkillAction().isPresent() && player.getSkillAction().get().getAction().isPrioritized() && !this.isPrioritized()) {
			getPlayer().getPackets().sendGameMessage("You currently cannot do this.");
			return;
		}
		player.getSkillAction().ifPresent(action -> action.getAction().onSkillAction(this));
		
		/** This will cancel previous skill, if you're mining and you try to fletch,
		 * mining will get stopped, and fletching will start.*/
		if(player.getSkillAction().isPresent())
			stop();
		
		
		SkillActionTask task = new SkillActionTask(this);
		getPlayer().setSkillAction(Optional.of(task));
		World.get().submit(task);
	}
	
	/**
	 * Stops this skill action effectively.
	 */
	public final void stop() {
		SkillActionTask task = player.getSkillAction().get();
		task.setRunning(false);
		task.getAction().onStop();
		task.getAction().getPlayer().setSkillAction(Optional.of(task));
	}
}