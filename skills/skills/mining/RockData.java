package skills.mining;

import com.rs.game.item.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RockData {
	
	PURE_ESSENCE(new int[] {2491}, new Item[] { new Item(7936)}, 1, 5.0, 2, 1, 1, 1),
	ESSENCE(new int[] {2491}, new Item[] { new Item(1436, 1)}, 1, 5.0, 2, 1, 1, 1),
	GEM_ROCK(new int[] {11194,11195, 11364}, Mining.GEMS, 40, 65.0, 150, 6, 4, 0.06),
	CLAY(new int[] {11189, 11190, 11191, 15503, 15504, 15505, 31062, 31063, 31064, 32429, 32430, 32431}, new Item[] { new Item(434)}, 1, 5.0, 2, 1, 5, 0.3),
	TIN(new int[] {5776, 5777, 5778, 11933, 11934, 11935, 11957, 11958, 11959, 31077, 31078, 31079, }, new Item[] { new Item(438)}, 1, 17.5, 5, 1, 1, 0.5),
	COPPER(new int[] {5780, 5779, 5781, 11936, 11937, 11938, 11960, 11961, 11962, 31080, 31081, 31082}, new Item[] { new Item(436)}, 1, 15.5, 2, 1, 1, 0.5),
	IRON(new int[] {5773, 5774, 5775, 11954, 11955, 11956, 14856, 14857, 14858, 31071, 31072, 31073, 32441, 32442, 32443, 37307, 37308, 37309}, new Item[] { new Item(440)}, 15, 35.0, 17, 3, 3, 0.2),
	SILVER(new int[] {2311, 11165, 11186, 11187, 11188, 11948, 11949, 11950, 15579, 15580, 15581, 31074, 31075, 31076, 32444, 32445, 32446, 37304, 37305, 37306}, new Item[] { new Item(442)}, 20, 40.0, 160, 3, 3, 0.2),
	COAL(new int[] {5770, 5771, 5772, 11930, 11931, 11932, 14850, 14851, 14852, 31068, 31069, 31070, 32426, 32427, 32428}, new Item[] { new Item(453)}, 30, 50.0, 12, 3, 4, 0.2),
	GOLD(new int[] {5768, 5769, 11183, 11184, 11185, 11951, 11952, 11953, 15576, 15577, 15578, 31065, 31066, 31067, 32432, 32433, 32434, 37310, 37312}, new Item[] { new Item(444)}, 40, 65.0, 180, 3, 3, 0.1),
	MITHRIL(new int[] {5784, 5786, 11942, 11943, 11944, 11945, 11946, 11947, 14853, 14854, 14855, 31086, 31087, 31088, 32438, 32439, 32440}, new Item[] { new Item(447)}, 55, 80.0, 300, 6, 4, 0.05),
	ADAMANTITE(new int[] {5782, 5783, 11939, 11940, 11941, 11963, 11964, 11965, 14862, 14863, 14864, 31083, 31084, 31085, 32435, 32436, 32437}, new Item[] { new Item(449)}, 70, 95.0, 600, 8, 8, 0.03),
	RUNITE(new int[] {14859, 14860, 14861}, new Item[] { new Item(451)}, 85, 125.0, 900, 12, 16, 0.01),
	LRC_COAL(new int[] {5999}, new Item[] { new Item(453)}, 30, 50.0, 12, 3, 500, 0.2),
	LRC_GOLD(new int[] {45076}, new Item[] { new Item(444)}, 40, 65.0, 180, 3, 500, 0.1),
	LRC_MINERALS(new int[] {45076}, new Item[] { new Item(15263)}, 73,25, 25, 3, 500, 0.6), //faster mining instead of 5-28 lengthy rng. change on demand.
	;
	/**
	 * The regular and empRockType.TY rock identification for this rock.
	 */
	@Getter
	private final int[] object;
	
	/**
	 * The item this rock produces.
	 */
	@Getter
	private final Item[] item;
	
	/**
	 * The requirement required to mine this rock.
	 */
	@Getter
	private final int requirement;
	
	/**
	 * The experience gained for mining this rock.
	 */
	@Getter
	private final double experience;
	
	/**
	 * The respawn identification for this rock.
	 */
	@Getter
	private final int respawnTime;
	
	/**
	 * The prospect delay when prospecting for this rock.
	 */
	@Getter
	private final int prospectdelay;
	
	/**
	 * The amount of ores this rock can produce.
	 */
	@Getter
	private final int oreCount;
	
	/**
	 * The success rate for mining this rock.
	 */
	@Getter
	private final double success;

	@Override
	public final String toString() {
		return name().toLowerCase();
	}
}