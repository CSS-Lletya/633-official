package skills.runecrafting;

import lombok.Data;

@Data
public final class RunecraftingMultiplier {

	/**
	 * The requirement to craft multiple runes.
	 */
	private final int requirement;

	/**
	 * The amount to multipy by.
	 */
	private final int multiply;
}
