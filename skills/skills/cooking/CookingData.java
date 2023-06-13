package skills.cooking;

import com.google.common.collect.ImmutableSet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CookingData {
	BREAD(2307, 1, 2309, 12, 2311, 30),
	PITTA_BREAD(1863, 58, 1865, 69, 1867, 40),
	PLAIN_PIZZA(2287, 35, 2289, 68, 2305, 100),

	REDBERRY_PIE(2321, 10, 2325, 21, 2329, 70),
	MEAT_PIE(2319, 20, 2327, 31, 2329, 110),
	MUD_PIE(7168, 29, 7170, 40, 2329, 120),
	APPLE_PIE(2317, 30, 2323, 41, 2329, 130),
	GARDEN_PIE(7176, 34, 7178, 45, 2329, 135),
	FISH_PIE(7186, 47, 7188, 58, 2329, 140),
	ADMIRAL_PIE(7196, 70, 7198, 81, 2329, 210),
	WILD_PIE(7206, 85, 7208, 96, 2329, 240),
	SUMMER_PIE(7216, 95, 7218, 99, 2329, 260),

	MEAT(2132, 1, 2142, 31, 2146, 30),
	BEAR_MEAT(2136, 1, 2142, 31, 2146, 30),
	RAT_MEAT(2134, 1, 2142, 31, 2146, 30),
	UGTHANKI_MEAT(1859, 1, 2142, 31, 2146, 40),
	YAK_MEAT(10816, 1, 2142, 31, 2146, 40),
	
	CHICKEN(2138, 1, 2140, 31, 2144, 15),
	CRAYFISH(13435, 1, 13433, 30, 13437, 30),
	SHRIMP(317, 1, 315, 34, 323, 20),
	SARDINE(327, 1, 325, 38, 369, 20),
	ANCHOVIES(321, 1, 319, 34, 323, 25),
	HERRING(345, 5, 347, 37, 357, 40),
	MACKEREL(353, 10, 355, 45, 357, 30),
	TROUT(335, 15, 333, 50, 343, 30),
	COD(341, 18, 339, 39, 343, 35),
	PIKE(349, 20, 351, 52, 343, 40),
	SALMON(331, 25, 329, 58, 343, 50),
	SLIMY_EEL(3379, 28, 3381, 56, 3383, 55),
	TUNA(359, 30, 361, 63, 367, 60),
	CAVE_EEL(5001, 38, 5003, 72, 5002, 70),
	LOBSTER(377, 40, 379, 74, 381, 75),
	BASS(363, 43, 365, 80, 367, 80),
	SWORDFISH(371, 45, 373, 86, 375, 90),
	LAVA_EEL(2148, 53, 2149, 89, 3383, 110),
	MONKFISH(7944, 62, 7946, 92, 7948, 120),
	SHARK(383, 80, 385, 94, 387, 130),
	MANTA_RAY(389, 91, 391, 100, 393, 140),
	ROCKTAIL(15270, 93, 15272, 100, 15274, 160);

	public static final ImmutableSet<CookingData> VALUES = ImmutableSet.copyOf(values());
	
	@Getter
	private final int rawId, level, cookedId, masterLevel, burntId, experience;

	@Override
	public final String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
}