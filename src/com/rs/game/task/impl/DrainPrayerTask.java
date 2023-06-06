package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;

public final class DrainPrayerTask extends Task {
	
	/**
	 * Creates a new {@link DrainPrayerTask}.
	 */
	public DrainPrayerTask() {
		super(1, true);
	}
	
	@Override
	public void execute() {
		World.players().filter(p -> p.getPrayer().hasPrayersOn()).forEach(p -> p.getPrayer().processPrayer());
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new DrainPrayerTask());
	}
}