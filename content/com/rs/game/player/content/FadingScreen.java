package com.rs.game.player.content;

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

	public static void unfade(final Player player, int startTime, final Runnable event) {
		unfade(player, 2, startTime, event);
	}

	@SneakyThrows(Throwable.class)
	public static void unfade(final Player player, int endTime, int startTime, final Runnable event) {
		int leftTime = (int) (endTime - (Utility.currentTimeMillis() - startTime));
		if (leftTime > 0) {
			World.get().submit(new Task(leftTime) {
    			@Override
    			protected void execute() {
    				unfade(player, event);
    				cancel();
    			}
    		});
		} else
			unfade(player, event);
	}

	@SneakyThrows(Throwable.class)
	public static void unfade(final Player player, Runnable event) {
		event.run();
		World.get().submit(new Task(3) {
			@Override
			protected void execute() {
				player.getInterfaceManager().closeInterfaces();
				player.getMovement().unlock();
				cancel();
			}
		});
	}
	
	public static int fade(Player player, int fadeTime) {
		player.getInterfaceManager().sendFullscreenInterface(0, 115);
		return (int) (Utility.currentTimeMillis() + fadeTime);
	}

	public static int fade(Player player) {
		return fade(player, 0);
	}
}