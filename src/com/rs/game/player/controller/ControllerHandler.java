package com.rs.game.player.controller;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.rs.game.player.Player;
import com.rs.game.player.controller.impl.TestController;
import com.rs.game.player.controller.impl.WildernessController;

import lombok.Setter;

/**
 * TODO: Item on object, process npc death
 * @author Dennis
 * @author lare96 <http://github.com/lare96>
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class ControllerHandler {

	@Setter
	private static ImmutableSet<Controller> CONTROLLERS = ImmutableSet.of(new TestController(), new WildernessController());
	
	/**
	 * The method that executes {@code action} for {@code player}.
	 * @param player the player to execute the action for.
	 * @param action the backed controller action to execute.
	 */
	public static void executeVoid(Player player, Consumer<Controller> action) {
		Optional<Controller> controller = getController(player);
		controller.ifPresent(action::accept);
	}
	
	/**
	 * The method that executes {@code function} for {@code player} that returns
	 * a result.
	 * @param player the player to execute the function for.
	 * @param defaultValue the default value to return if the player isn't in a minigame.
	 * @param function the function to execute that returns a result.
	 */
	public static boolean execute(Player player, Function<Controller, Boolean> function) {
		Optional<Controller> controller = getController(player);
		return !controller.isPresent() ? true : function.apply(controller.get());
	}
	
	/**
	 * Retrieves the controller that {@code player} is currently in.
	 * @param player the player to determine the controller for.
	 * @return the controller that the player is currently in.
	 */
	public static Optional<Controller> getController(Player player) {
		Optional<Controller> controller = player.getCurrentController();
		if(controller.isPresent() && controller.get().contains(player)) {
			return controller;
		}
		if(controller.isPresent() && !controller.get().contains(player)) {
			player.setCurrentController(Optional.empty());
			return Optional.empty();
		}
		Optional<Controller> staticController = CONTROLLERS.stream().filter(playerController -> playerController.contains(player)).findAny();
		if(staticController.isPresent() && staticController.get().contains(player)) {
			return staticController;
		}
		if(staticController.isPresent() && !staticController.get().contains(player)) {
			player.setCurrentController(Optional.empty());
			return Optional.empty();
		}
		return controller;
	}
}