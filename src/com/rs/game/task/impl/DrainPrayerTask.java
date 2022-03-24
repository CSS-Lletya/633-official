package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;

public final class DrainPrayerTask extends Task {
	
	/**
	 * Creates a new {@link DrainPrayerTask}.
	 */
	public DrainPrayerTask() {
		super(1, false);
	}
	
	@Override
	public void execute() {
		World.players().filter(p -> !p.isDead() && p.getPrayer().hasPrayersOn()).forEach(p -> p.getPrayer().processPrayerDrain());
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new DrainPrayerTask());
	}
}