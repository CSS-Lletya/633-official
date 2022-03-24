package com.rs.game.player.actions;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

/**
 * Handles an Action that a Player creates
 * @author Dennis
 *
 */
public final class ActionManager {
	
	public ActionManager() {
		action = Optional.empty();
	}
	
	/**
	 * The Action the Player creates
	 */
	@Getter
	private Optional<Action> action;
	
	/**
	 * The Action delay
	 */
	@Getter
	@Setter
	private int actionDelay;

	/**
	 * Handles the processing of an Action
	 */
	public void process() {
		if (getAction().isPresent() && !getAction().get().process()) {
			forceStop();	
		}
		if (!getAction().isPresent()) {
			return;
		}
		if (actionDelay > 0) {
			actionDelay--;
			return;
		}
		int delay = getAction().get().processWithDelay();
		if (delay == -1) {
			forceStop();
			return;
		}
		addActionDelay(actionDelay += delay);
	}

	/**
	 * Sets the Action that the Player has requested
	 * @param actionEvent
	 * @return action
	 */
	public void setAction(Action actionEvent) {
		if (!actionEvent.start())
			return;
		action = Optional.of(actionEvent);
	}

	/**
	 * Forcivly stops the Players current Action
	 */
	public void forceStop() {
		getAction().ifPresent(presentAction ->  {
			presentAction.stop();
		});
		action = Optional.empty();
	}

	/**
	 * Adds an additional delay to the action delay
	 * @param delay
	 */
	public void addActionDelay(int delay) {
		this.actionDelay += delay;
	}
}