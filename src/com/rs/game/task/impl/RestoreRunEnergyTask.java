package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;

import skills.Skills;

public final class RestoreRunEnergyTask extends Task {

	/**
	 * Creates a new {@link RestoreRunEnergyTask}.
	 */
	public RestoreRunEnergyTask() {
		super(2, true);
	}

	@Override
	public void execute() {
		World.players().filter(p -> p.getDetails().getRunEnergy() < 100 && p.getMovement().getWalkSteps().isEmpty()).forEach(p -> {
			double restoreRate = 0.45D;
			double agilityFactor = 0.01 * p.getSkills().getLevel(Skills.AGILITY);
			p.getDetails().setRunEnergy((byte) (p.getDetails().getRunEnergy() + (restoreRate + agilityFactor)));
			p.getPackets().sendRunEnergy();
		});
	}

	@Override
	public void onCancel() {
		World.get().submit(new RestoreRunEnergyTask());
	}
}