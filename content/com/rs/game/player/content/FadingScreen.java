package com.rs.game.player.content;

import com.rs.cores.CoresManager;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.utilities.Utility;

import lombok.SneakyThrows;

public class FadingScreen {
	
	public static void fade(final Player player, final Runnable event) {
		player.getMovement().lock();
		unfade(player, fade(player), event);
	}

	public static void unfade(final Player player, long startTime, final Runnable event) {
		unfade(player, 2, startTime, event);
	}

	@SneakyThrows(Throwable.class)
	public static void unfade(final Player player, long endTime, long startTime, final Runnable event) {
		long leftTime = endTime - (Utility.currentTimeMillis() - startTime);
		if (leftTime > 0) {
			CoresManager.schedule(() -> {
				unfade(player, event);
			}, (int) leftTime);
		} else
			unfade(player, event);
	}

	@SneakyThrows(Throwable.class)
	public static void unfade(final Player player, Runnable event) {
		event.run();
		World.get().submit(new Task(1) {
			@Override
			protected void execute() {
				CoresManager.schedule(() -> {
					player.getInterfaceManager().closeInterfaces();
					player.getMovement().unlock();
				}, 2);
				this.cancel();
			}
		});
	}

	public static long fade(Player player, long fadeTime) {
		player.getInterfaceManager().sendFullscreenInterface(0, 115);
		return Utility.currentTimeMillis() + fadeTime;
	}

	public static long fade(Player player) {
		return fade(player, 0);
	}
}