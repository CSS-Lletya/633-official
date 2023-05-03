package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
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
		World.players().forEach(Player::restoreHitPoints);
		World.npcs().forEach(NPC::restoreHitPoints);
	}
	
	@Override
	public void onCancel() {
		World.get().submit(new RestoreHitpoints());
	}
}