package com.rs.game.task.impl;

import java.util.Optional;

import com.rs.game.player.Player;
import com.rs.game.task.Task;

import lombok.Getter;
import skills.SkillHandler;

public final class SkillActionTask extends Task {
	
	/**
	 * The player this task is running for.
	 */
	private final Player player;
	
	/**
	 * The skill action we're currently handling.
	 */
	@Getter
	private final SkillHandler action;
	
	/**
	 * The flag which determines if this action was executed.
	 */
	private boolean executed = true;
	
	/**
	 * The counter that determines how many ticks have passed.
	 */
	private transient int counter;
	
	/**
	 * The counter that determines how many ticks have passed for the next animation.
	 */
	private int animationCounter;
	
	/**
	 * Constructs a new {@link SkillActionTask}.
	 * @param action {@link #action}.
	 */
	public SkillActionTask(SkillHandler action) {
		super(1, true);
		this.action = action;
		this.player = action.getPlayer();
	}
	
	@Override
	public void onSubmit() {
		if(!action.initialize()) {
			executed = false;
			this.cancel();
			return;
		}
		if(!action.canRun(this)) {
			executed = false;
			this.cancel();
			return;
		}
		if(action.instant()) {
			if(!player.getSkillAction().isPresent()) {
				this.cancel();
				return;
			}
			if(!action.canExecute()) {
				this.cancel();
				return;
			}
			action.execute(this);
			action.animation().ifPresent(player::setNextAnimation);
			counter = 0;
			animationCounter = 0;
		}
		player.resetWalkSteps();
		action.startAnimation().ifPresent(player::setNextAnimation);
		action.onSubmit();
	}
	
	@Override
	public void execute() {
		if(!player.getSkillAction().isPresent()) {
			this.cancel();
			return;
		}
		
		if(player.getSkillAction().isPresent()) {
			action.onSequence(this);
		}
		
		action.animationDelay().ifPresent(delay -> {
			if(animationCounter++ >= delay) {
				action.animation().ifPresent(player::setNextAnimation);
				animationCounter = 0;
			}
		});
		
		if(++counter >= action.delay()) {
			if(!player.getSkillAction().isPresent()) {
				this.cancel();
				return;
			}
			
			if(!action.canRun(this)) {
				this.cancel();
				return;
			}
			
			if(!action.canExecute()) {
				this.cancel();
				return;
			}
			action.execute(this);
			action.tile.ifPresent(player::setNextFaceWorldTile);
			if(!action.animationDelay().isPresent()) {
				action.animation().ifPresent(player::setNextAnimation);
			}
			counter = 0;
		}
	}
	
	@Override
	public void onCancel() {
		if(executed) {
			action.onStop();
		}
		player.setSkillAction(Optional.empty());
	}
}