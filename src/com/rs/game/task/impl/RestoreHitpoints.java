package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;

import lombok.SneakyThrows;

public final class RestoreHitpoints extends Task {
	
	/**
	 * Creates a new {@link RestoreHitpoints}.
	 */
	public RestoreHitpoints() {
		super(60, false);
	}
	
	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		World.players().forEach(player -> player.restoreHitPoints());
		World.npcs().forEach(npc -> npc.restoreHitPoints());
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new RestoreHitpoints());
	}
}