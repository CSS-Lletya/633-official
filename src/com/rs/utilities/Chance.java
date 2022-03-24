package com.rs.utilities;

/**
 * The enumerated type whose elements represent the chance rates.
 * @author lare96 <http://github.com/lare96>
 */
public enum Chance {

	/**
	 * Describing an {@code ALWAYS} chance (100%, 1 in 1 chance).
	 */
	ALWAYS(1),

	/**
	 * Describing a {@code VERY_COMMON} chance (25%, 1 in 4 chance).
	 */
	VERY_COMMON(4),

	/**
	 * Describing an {@code COMMON} chance (10%, 1 in 10 chance).
	 */
	COMMON(10),

	/**
	 * Describing an {@code UNCOMMON} chance (6.25%, 1 in 16 chance).
	 */
	UNCOMMON(16),

	/**
	 * Describing an {@code VERY_UNCOMMON} chance (4%, 1 in 40 chance).
	 * chance is now
	 */
	VERY_UNCOMMON(40),

	/**
	 * Describing an {@code RARE} chance (2%, 1 in 100 chance).
	 */
	RARE(100),

	/**
	 * Describing an {@code VERY_RARE} chance (1.428%, 1 in 125 chance).
	 */
	VERY_RARE(125),

	/**
	 * Describing an {@code EXTREMELY_RARE} chance (0.8%, 1 in 200 chance).
	 */
	EXTREMELY_RARE(200),

	/**
	 * Describing an {@code FUCKING_RARE} chance (0.8%, 1 in 400 chance).
	 */
	FUCKING_RARE(400);

	private final double roll;

	Chance(double denominator) {
		this.roll = 1D / denominator;
	}

	public double getRoll() {
		return roll;
	}

}
