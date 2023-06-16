package com.rs.game.task.impl;

import com.rs.game.player.Player;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.content.traveling.Gliders;
import com.rs.game.task.Task;

import lombok.SneakyThrows;

public final class GliderTask extends Task {

	/**
	 * Represents the player.
	 */
	private final Player player;

	/**
	 * Represents the glider.
	 */
	private final Gliders glider;

	/**
	 * Represents the count.
	 */
	private int count;

	/**
	 * Constructs a new {@code GliderPulse.java} {@Code Object}
	 * 
	 * @param delay
	 * @param checks
	 */
	public GliderTask(int delay, Player player, Gliders glider) {
		super(delay);
		this.player = player;
		this.glider = glider;
		player.getMovement().lock();
	}

	@Override
	@SneakyThrows(Throwable.class)
	public void execute() {
		final boolean crash = glider == Gliders.LEMANTO_ADRA;
		if (count == 1) {
			FadingScreen.fade(player);
			player.getVarsManager().sendVar(153, glider.getConfig());
			player.getPackets().sendBlackOut(2);
		} else if (count == 2) {
			if (crash) {
				player.getPackets().sendCameraShake(4, 4, 1200, 4, 4);
				player.getPackets()
						.sendGameMessage("The glider almost gets blown from its path as it withstands heavy winds.");
			}
		} else if (count == 5) {
			player.getMovement().unlock();
			player.setNextWorldTile(glider.getLocation());
		} else if (count == 8) {
			if (crash) {
				player.getPackets().sendGameMessage("The glider becomes uncontrolable and crashes down...");
				player.getPackets().sendStopCameraShake();
			}
			player.getInterfaceManager().closeInterfaces();
			player.getPackets().sendBlackOut(0);
			player.getInterfaceManager().closeInterfaces();
			player.getVarsManager().sendVar(0, glider.getConfig());
			this.cancel();
		}
		count++;
	}

	@Override
	public void onCancel() {

	}
}