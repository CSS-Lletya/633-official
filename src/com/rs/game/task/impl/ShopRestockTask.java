package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;

public final class ShopRestockTask extends Task {
	
	/**
	 * Creates a new {@link ShopRestockTask}.
	 */
	public ShopRestockTask() {
		super(30, false);
	}
	
	@Override
	public void execute() {
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new ShopRestockTask());
	}
}