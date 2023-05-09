package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.player.actions.Rest;
import com.rs.game.task.Task;

import skills.Skills;

public final class RestoreRunEnergyTask extends Task {

	/**
	 * Creates a new {@link RestoreRunEnergyTask}.
	 */
	public RestoreRunEnergyTask() {
		super(2, true);
	}

	private double restoreRate;
	
	@Override
	public void execute() {
		World.players().filter(p -> p.getDetails().getRunEnergy() < 100 && p.getNextRunDirection() < 1).forEach(player -> {
			restoreRate = 0.45D;
			double agilityFactor = 0.01 * player.getSkills().getLevel(Skills.AGILITY);
			//TODO: Weight factors
			
			player.getAction().getPresentAction(Rest.class, () -> restoreRate *= 1.5);
            if (player.isPoisoned())
                restoreRate *= 0.5;
            
			player.getDetails().setRunEnergy(player.getDetails().getRunEnergy() + (restoreRate + agilityFactor));
			player.getPackets().sendRunEnergy();
		});
	}

	@Override
	public void onCancel() {
		World.get().submit(new RestoreRunEnergyTask());
	}
}