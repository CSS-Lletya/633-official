package com.rs.game.movement;

import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

/**
 * Holds functionality for the 0x400 mask.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public final class ForcedMovementManager {
	
	/**
	 * The movement to manage.
	 */
	@SuppressWarnings("unused")
	private final ForcedMovement movement;
	
	/**
	 * The backing task running for this movement.
	 */
	private final Task t;
	
	/**
	 * Constructs a new {@link ForcedMovement} manager.
	 * @param movement the forced movement.
	 */
	private ForcedMovementManager(ForcedMovement movement) {
		this.movement = movement;
		this.t = new ForcedMovementTask(movement);
	}
	
	/**
	 * Checks if this forced movement can be submitted.
	 * @param character the player attempting to utilize the forced movement.
	 * @return <true> if the player can, <false> otherwise.
	 */
	private static boolean prerequisites(Entity character) {
		if(character.isNPC()) {
			return true;
		}
		Player player = character.toPlayer();
		if(player.isTeleported()) {
			player.getPackets().sendGameMessage("You can't do this while teleporting.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Submits the forced movement to the world.
	 * @param character the character doing the forced movement.
	 * @param movement the movement to submit.
	 */
	public static void submit(Entity character, ForcedMovement movement) {
		if(prerequisites(character)) {
			ForcedMovementManager manager = new ForcedMovementManager(movement);
//			movement.getCharacter().setNextForceMovement(manager.movement);
			World.get().submit(manager.t);
		}
	}
	
	/**
	 * Submits the forced movement to the world.
	 * @param character the character doing the forced movement.
	 * @param movement the movement to submit.
	 * @param skipPrerequisites flag to skip  prerequisites.
	 */
	public static void submit(Entity character, ForcedMovement movement, boolean skipPrerequisites) {
		if(skipPrerequisites || prerequisites(character)) {
			ForcedMovementManager manager = new ForcedMovementManager(movement);
//			movement.getCharacter().setNextForceMovement(manager.movement);
			World.get().submit(manager.t);
		}
	}
	
	/**
	 * The backing task running for this task.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static final class ForcedMovementTask extends Task {
		
		/**
		 * The forced movement this task is running for.
		 */
		private final ForcedMovement movement;
		
		/**
		 * Constructs a new {@link ForcedMovementTask}.
		 * @param movement {@link #movement}.
		 */
		ForcedMovementTask(ForcedMovement movement) {
			super(1, false);
			this.movement = movement;
		}
		
		/**
		 * The timer which moves the players to the destination when it hits zero.
		 */
		private int timer;
		
		/**
		 * Calculates the time to move the player to the destination.
		 * @return the time.
		 */
		private int calculateTimer() {
			int firstSpeed = movement.getFirstSpeed() * 30;
			int secondSpeed = (movement.getFirstSpeed() * 30) + (movement.getSecondSpeed() * 30 + 1);
			firstSpeed = (int) Math.ceil(movement.getFirst().getDistance(movement.getCharacter().getLastWorldTile()) / (firstSpeed * 0.1));
			secondSpeed = (int) Math.ceil(movement.getFirst().getDistance(movement.getSecond()) / (secondSpeed * 0.1));
			return movement.getTimer().isPresent() ? movement.getTimer().getAsInt() : 1 + firstSpeed + secondSpeed;
		}
		
		@Override
		public void onSubmit() {
			this.attach(movement.getCharacter());
			this.timer = calculateTimer();
			movement.setActive(true);
			movement.getCharacter().resetWalkSteps();
			if(movement.getAnimation() != null)
				movement.getCharacter().setNextAnimation(movement.getAnimation());
			movement.getCharacter().ifPlayer(p -> p.getAppearance().generateAppearenceData());
			movement.getCharacter().getMovement().lock();
		}
		
		@Override
		public void execute() {
			if(timer == 0) {
				movement.getCharacter().setNextWorldTile(movement.getSecond());
				movement.getCharacter().resetWalkSteps();
			}
			if(timer == -1) {
				if(movement.getCharacter().isPlayer()) {
					@SuppressWarnings("unused")
					Player player = movement.getCharacter().toPlayer();
//					player.getNextForceMovement().setActive(false);
				}
				this.cancel();
			}
			timer -= 1;
			
		}
		
		@Override
		public void onCancel() {
			if(movement != null) {
				movement.onDestination.ifPresent(Runnable::run);
				movement.getCharacter().getMovement().unlock();
			}
		}
	}
}
