package com.rs.game.map.zone;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.rs.GameConstants;
import com.rs.game.player.Player;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.MapZoneAdapter;

import lombok.Getter;
import lombok.Setter;

/**
 * A modernized Map Zone handling system, formally known as "Controller" it operates the same.
 * As we save the {@link Player} data into json format we needed to make a {@link MapZoneAdapter} accordingly.
 * @author Dennis
 */
public class MapZoneManager {
	
	/**
	 * Represents the {@link Player} in the {@link MapZone}
	 */
	@Getter
	@Setter
	public Player player;
	
	/**
	 * Submits a new {@link MapZone} for the {@link Player} to enter.
	 * @param player
	 * @param zone
	 */
	public void submitMapZone(MapZone zone) {
		player.setCurrentMapZone(Optional.of(zone));
		player.getCurrentMapZone().ifPresent(newZone -> newZone.start(player));
	}
	
	/**
	 * The method that executes {@code action} for {@code player}.
	 * @param player the player to execute the action for.
	 * @param action the backed controller action to execute.
	 */
	public void executeVoid(Consumer<MapZone> action) {
		getMapZone().ifPresent(action::accept);
	}
	
	/**
	 * The method that executes {@code function} for {@code player} that returns
	 * a result.
	 * @param player the player to execute the function for.
	 * @param defaultValue the default value to return if the player isn't in a map zone.
	 * @param function the function to execute that returns a result.
	 */
	public boolean execute(Function<MapZone, Boolean> function) { 
		return getMapZone().isPresent() ? function.apply(getMapZone().get()) : false;
	}
	
	/**
	 * Retrieves the map zone that {@code player} is currently in.
	 * @param player the player to determine the map zone for.
	 * @return the map zone that the player is currently in.
	 */
	public Optional<MapZone> getMapZone() {
		Optional<MapZone> mapZone = player.getCurrentMapZone();
		mapZone.filter(zone -> !zone.contains(player)).ifPresent(zone -> {
			if (GameConstants.DEBUG)
				LogUtility.log(LogType.ERROR,
						"[Map Zone Error] Player: " + player.getUsername() + "'s current map zone is: "
								+ mapZone.getClass().getSimpleName()
								+ " but wasn't inside, ending their map zone session.");
			zone.finish(player);
			player.setCurrentMapZone(Optional.empty());
		});
		return mapZone == null ? Optional.empty() : mapZone;
	}
	
	/**
	 * Force ends a map zone session.
	 * @param player
	 */
	public void endMapZoneSession(Player player) {
		player.getCurrentMapZone().ifPresent(currentZone -> {
			currentZone.finish(player);
			player.setCurrentMapZone(Optional.empty());
		});
	}
	
	/**
	 * Checks if the inherited class is a valid instance of {@link MapZone}.
	 * This updates the "instanceof" condition and removes the exclusive Casting before executing inherited class code.
	 * @param <T>
	 * @param zoneClass
	 * @return
	 */
	public <T extends MapZone> boolean isValidInstance(Class<T> zoneClass) {
	    return getMapZone()
	            .map(zoneClass::isInstance)
	            .orElse(false);
	}
	
	/**
	 * Checks to see if the current {@link MapZone} is a valid instance of the {@link MapZone}, then executes the supplied consumer.
	 * @param <T>
	 * @param zoneClass
	 * @param consumer
	 * @return
	 */
	public <T extends MapZone> boolean doWithin(Class<T> zoneClass, Consumer<T> consumer) {
	    return getMapZone()
	    		.map(zoneClass::cast)
	            .filter(zoneClass::isInstance)
	            .map(zone -> { consumer.accept(zone); return true; })
	            .orElse(false);
	}
}