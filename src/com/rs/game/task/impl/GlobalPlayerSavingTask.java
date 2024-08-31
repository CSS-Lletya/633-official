package com.rs.game.task.impl;

import com.rs.cores.PlayerHandlerThread;
import com.rs.game.map.World;
import com.rs.game.task.Task;

public class GlobalPlayerSavingTask extends Task {

	/**
	 * Creates a new {@link GlobalPlayerSavingTask}.
	 */
	public GlobalPlayerSavingTask() {
		super(60 * 30, false);
	}

	@Override
	public void execute() {
		World.players().forEach(PlayerHandlerThread::savePlayer);
	}

	@Override
	public void onCancel() {
		World.get().submit(new GlobalPlayerSavingTask());
	}
}