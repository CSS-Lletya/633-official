package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;

public class BonusExperienceTimerTask extends Task {

	/**
	 * Creates a new {@link BonusExperienceTimerTask}.
	 */
	public BonusExperienceTimerTask() {
		super(60, true);
	}

	@Override
	public void execute() {
		World.players().forEach(p -> {
			p.getSkills().increaseElapsedBonusMinues();
			p.getSkills().refreshXpBonus();
		});
	}

	@Override
	public void onCancel() {
		World.get().submit(new BonusExperienceTimerTask());
	}
}