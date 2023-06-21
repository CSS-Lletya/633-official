package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;
import com.rs.utilities.loaders.ShopsHandler;

public final class ShopRestockTask extends Task {
	
	/**
	 * Creates a new {@link ShopRestockTask}.
	 */
	public ShopRestockTask() {
		super(1, false);
	}
	
	@Override
	public void execute() {
		ShopsHandler.restoreShops();
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new ShopRestockTask());
	}
}