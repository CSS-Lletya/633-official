package com.rs.content.mapzone;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.rs.GameConstants;
import com.rs.game.player.Player;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

/**
 * @author Dennis
 */
public final class MapZoneManager {
	
	/**
	 * Submits a new Map Zone for the Player to enter.
	 * @param player
	 * @param zone
	 */
	public void submitMapZone(Player player, MapZone zone) {
		player.getCurrentMapZone().ifPresent(currentZone -> {
			currentZone.finish(player);
			player.setCurrentMapZone(Optional.empty());
		});
		player.setCurrentMapZone(Optional.of(zone));
		player.getCurrentMapZone().ifPresent(newZone -> newZone.start(player));
	}
	
	/**
	 * The method that executes {@code action} for {@code player}.
	 * @param player the player to execute the action for.
	 * @param action the backed controller action to execute.
	 */
	public void executeVoid(Player player, Consumer<MapZone> action) {
		getMapZone(player).ifPresent(action::accept);
	}
	
	/**
	 * The method that executes {@code function} for {@code player} that returns
	 * a result.
	 * @param player the player to execute the function for.
	 * @param defaultValue the default value to return if the player isn't in a map zone.
	 * @param function the function to execute that returns a result.
	 */
	public boolean execute(Player player, Function<MapZone, Boolean> function) {
		return !getMapZone(player).isPresent() ? false : function.apply(getMapZone(player).get());
	}
	
	/**
	 * Retrieves the map zone that {@code player} is currently in.
	 * @param player the player to determine the map zone for.
	 * @return the map zone that the player is currently in.
	 */
	public Optional<MapZone> getMapZone(Player player) {
		Optional<MapZone> mapZone = player.getCurrentMapZone();
		if(mapZone.isPresent() && !mapZone.get().contains(player)) {
			if (GameConstants.DEBUG)
				LogUtility.log(LogType.ERROR,
						"[Map Zone Error] Player: " + player.getUsername() + "'s current map zone is: "
								+ mapZone.get().getMapeZoneName() + " but wasn't inside, ending their map zone session.");
			mapZone.get().finish(player);
			player.setCurrentMapZone(Optional.empty());
			return Optional.empty();
		}
		if(mapZone.isPresent() && mapZone.get().contains(player))
			return mapZone;
		return Optional.empty();
	}
	
	public void endMapZoneSession(Player player) {
		player.getCurrentMapZone().ifPresent(currentZone -> {
			currentZone.finish(player);
			player.setCurrentMapZone(Optional.empty());
		});
	}
}