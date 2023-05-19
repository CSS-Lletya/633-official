package com.rs.game.movement;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Holds functionality for the possible directions players can face
 * when using this mask.
 * @author <a href="http://www.runeserver.org/members/stand+up/">Stand Up</a>
 */
public enum ForcedMovementDirection {
	NORTH(0), EAST(1), SOUTH(2), WEST(3), NORTH_EAST(4), SOUTH_EAST(5), SOUTH_WEST(6), NORTH_WEST(7);
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<ForcedMovementDirection> VALUES = Sets.immutableEnumSet(EnumSet.allOf(ForcedMovementDirection.class));
	
	/**
	 * The value of this viewpoint.
	 */
	private final int value;
	
	/**
	 * Constructs a new {@link ForcedMovementDirection}.
	 * @param value The value of this viewpoint.
	 */
	ForcedMovementDirection(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the direction identifier.
	 * @return direction's value.
	 */
	public int getId() {
		return value;
	}
	
	/**
	 * Checks if the identifier matches the value of the viewpoint.
	 * @param identifier The identifier to check for matches.
	 * @return An {@link Optional#of} if there was a match, {@link Optional#empty} otherwise.
	 */
	public static Optional<ForcedMovementDirection> getDirection(int identifier) {
		return VALUES.stream().filter(direction -> direction.value == identifier).findAny();
	}
	
	/**
	 * Gets the direction for movement.
	 * @param diffX The difference between 2 xcoordinates.
	 * @param diffY The difference between 2 ycoordinates.
	 * @return The direction.
	 */
	public static ForcedMovementDirection getDirection(int diffX, int diffY) {
		if(diffX < 0) {
			if(diffY < 0) {
				return SOUTH_WEST;
			} else if(diffY > 0) {
				return NORTH_WEST;
			}
			return WEST;
		} else if(diffX > 0) {
			if(diffY < 0) {
				return SOUTH_EAST;
			} else if(diffY > 0) {
				return NORTH_EAST;
			}
			return EAST;
		}
		if(diffY < 0) {
			return SOUTH;
		}
		return NORTH;
	}
}
