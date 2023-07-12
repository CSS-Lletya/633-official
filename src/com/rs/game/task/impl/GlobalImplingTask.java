package com.rs.game.task.impl;

import java.util.stream.IntStream;

import com.rs.game.map.World;
import com.rs.game.task.Task;

import skills.hunter.GlobalImplings;

public class GlobalImplingTask extends Task {

	/**
	 * Creates a new {@link GlobalImplingTask}.
	 */
	public GlobalImplingTask() {
		super(60 * 30, false);
	}

	@Override
	public void execute() {
		IntStream.of(1028, 1029, 1030, 1031, 1032, 1033, 7866, 1034, 1035, 6053, 7845, 6054, 7902, 7903)
				.forEach(imp -> {
					GlobalImplings.spawnImps();
					this.cancel();
				});

	}

	@Override
	public void onCancel() {
		IntStream.of(1028, 1029, 1030, 1031, 1032, 1033, 7866, 1034, 1035, 6053, 7845, 6054, 7902, 7903)
				.forEach(imp -> World.npcs().filter(impId -> impId.getId() == imp).forEach(imps -> imps.deregister()));
		World.get().submit(new GlobalImplingTask());
	}
}