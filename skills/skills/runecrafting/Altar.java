package skills.runecrafting;

import com.rs.game.map.WorldTile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Altar {
	AIR(2478, Rune.AIR, 1, 7.0, new WorldTile(2841, 4829,0), true, 1438, 2452),
	MIND(2479, Rune.MIND, 2, 9.0, new WorldTile(2793, 4828,0), false, 1448, 2453),
	WATER(2480, Rune.WATER, 5, 12.0, new WorldTile(3483, 4835,0), true, 1444, 2454),
	EARTH(2481, Rune.EARTH, 9, 15.0, new WorldTile(2655, 4830,0), true, 1440, 2455),
	FIRE(2482, Rune.FIRE, 14, 18.0, new WorldTile(2574, 4849,0), true, 1442, 2456),
	BODY(2483, Rune.BODY, 20, 20.0, new WorldTile(2521, 4835,0), false, 1446, 2457),
	COSMIC(2484, Rune.COSMIC, 27, 50.0, new WorldTile(2162, 4833,0), false, 1454, 2458),
	CHAOS(2487, Rune.CHAOS, 35, 55.0, new WorldTile(2281, 4837,0), false, 1452, 2461),
	NATURE(2486, Rune.NATURE, 44, 60.0, new WorldTile(2400, 4835,0), false, 1462, 2460),
	LAW(2485, Rune.LAW, 54, 75.0, new WorldTile(2464, 4818,0), false, 1458, 2459),
	DEATH(2488, Rune.DEATH, 65, 80.0, new WorldTile(2208, 4830,0), false, 1456, 2462),
	BLOOD(30624, Rune.BLOOD, 80, 90.0, new WorldTile(2466, 4888,1), false, 1450, 2464);

	/**
	 * The object identification for this altar.
	 */
	@Getter
	private final int objectId;

	/**
	 * The {@link Rune} this altar produces.
	 */
	@Getter
	private final Rune rune;

	/**
	 * The requirement for entering this altar.
	 */
	@Getter
	private final int requirement;

	/**
	 * The experience identifier for this altar.
	 */
	@Getter
	private final double experience;

	/**
	 * The WorldTile the player will be moved to upon entering.
	 */
	@Getter
	private final WorldTile WorldTile;

	/**
	 * Determines if this altar can accept both runes.
	 */
	@Getter
	private final boolean diverse;

	/**
	 * The talisman id.
	 */
	@Getter
	private final int talisman;
	
	/**
	 * The outside object id.
	 */
	@Getter
	private final int outisdeObject;
	
}
