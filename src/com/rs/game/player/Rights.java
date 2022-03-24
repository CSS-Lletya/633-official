package com.rs.game.player;

import java.util.Arrays;

/**
 * The enumerated type whose elements represent the types of authority a player
 * can have.
 * @author lare96 <http://github.com/lare96>
 */
public enum Rights {
	PLAYER(0, 0),
	IRON_MAN(1, 1),
	DESIGNER(2, 1),
	YOUTUBER(3, 1),
	HELPER(8, 1),
	DONATOR(4, 2),
	SUPER_DONATOR(5, 3),
	EXTREME_DONATOR(6, 4),
	GOLDEN_DONATOR(7, 5),
	MODERATOR(9, 7),
	SENIOR_MODERATOR(10, 9),
	ADMINISTRATOR(11, 10);
	
	/**
	 * The value of this rank as seen by the protocol.
	 */
	private final int protocolValue;
	
	/**
	 * The value of this rank as seen by the server. This value will be used to
	 * determine which of the elements are greater than each other.
	 */
	private final int value;
	
	/**
	 * Create a new {@link Rights}.
	 * @param protocolValue the value of this rank as seen by the protocol.
	 * @param value the value of this rank as seen by the server.
	 */
	Rights(int protocolValue, int value) {
		this.protocolValue = protocolValue;
		this.value = value;
	}
	
	/**
	 * Determines if this right is greater than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is greater, {@code false} otherwise.
	 */
	public final boolean greater(Rights other) {
		return value > other.value;
	}
	
	/**
	 * Determines if this right is lesser than the argued right. Please note
	 * that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is lesser, {@code false} otherwise.
	 */
	public final boolean less(Rights other) {
		return value < other.value;
	}
	
	/**
	 * Determines if this right is equal in power to the argued right. Please
	 * note that this method <b>does not</b> compare the Objects themselves, but
	 * instead compares the value behind them as specified by {@code value} in
	 * the enumerated type.
	 * @param other the argued right to compare.
	 * @return {@code true} if this right is equal, {@code false} otherwise.
	 */
	public final boolean equal(Rights other) {
		return value == other.value;
	}
	
	/**
	 * Gets the value of this rank as seen by the protocol.
	 * @return the protocol value of this rank.
	 */
	public final int getProtocolValue() {
		return protocolValue;
	}
	
	public final boolean equals(Rights... rights) {
		return Arrays.stream(rights).anyMatch(right -> this == right);
	}
	
	public final boolean isStaff() {
		return this.equals(MODERATOR) || this.equals(SENIOR_MODERATOR) || this.equals(ADMINISTRATOR) || this.equals(ADMINISTRATOR);
	}
	
	public final boolean isDonator() {
		return this.equals(DONATOR) || this.equals(SUPER_DONATOR) || this.equals(EXTREME_DONATOR) || this.equals(GOLDEN_DONATOR);
	}
	
	/**
	 * Gets the value of this rank as seen by the server.
	 * @return the server value of this rank.
	 */
	public final int getValue() {
		return value;
	}
	
	@Override
	public final String toString() {
		return name().replaceAll("_", " ").toLowerCase();
	}
}
