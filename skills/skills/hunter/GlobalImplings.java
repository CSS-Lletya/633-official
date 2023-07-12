package skills.hunter;

import java.util.Random;

import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;

/**
 * Handles Globally spawned implings throughout the game respawns every 30
 * minutes.
 * 
 * @author Dennis
 *
 */
public class GlobalImplings {

	/**
	 * The random instance used.
	 */
	private static final Random RANDOM = new Random();

	/**
	 * The possible locations.
	 */
	public static final WorldTile[] LOCATIONS = new WorldTile[] {
			/*
			 * Camelot
			 */
			new WorldTile(2718 + RANDOM.nextInt(50), 3441 + RANDOM.nextInt(20), 0),
			/*
			 * Duel arena entrance
			 */
			new WorldTile(3300 + RANDOM.nextInt(10), 3250 + RANDOM.nextInt(10), 0),
			/*
			 * Castle wars entrance
			 */
			new WorldTile(2478 + RANDOM.nextInt(20), 3068 + RANDOM.nextInt(20), 0),
			/*
			 * Lumbridge
			 */
			new WorldTile(3175 + RANDOM.nextInt(10), 3261 + RANDOM.nextInt(10), 0),
			/*
			 * Draynor
			 */
			new WorldTile(3092 + RANDOM.nextInt(5), 3227 + RANDOM.nextInt(5), 0),
			/*
			 * Falador (east)
			 */
			new WorldTile(3071 + RANDOM.nextInt(10), 3355 + RANDOM.nextInt(10), 0),
			/*
			 * Varrock (west)
			 */
			new WorldTile(3131 + RANDOM.nextInt(5), 3401 + RANDOM.nextInt(5), 0),
			/*
			 * Ardougne (south)
			 */
			new WorldTile(2630 + RANDOM.nextInt(10), 3217 + RANDOM.nextInt(10), 0),
			/*
			 * Yanille (north)
			 */
			new WorldTile(2594 + RANDOM.nextInt(20), 3121 + RANDOM.nextInt(20), 0),
			/*
			 * Red salamander hunting place (ZMI altar)
			 */
			new WorldTile(2436 + RANDOM.nextInt(15), 3206 + RANDOM.nextInt(15), 0),
			/*
			 * Shilo hunting area (graahk/larupia/..)
			 */
			new WorldTile(2763 + RANDOM.nextInt(20), 3003 + RANDOM.nextInt(20), 0),
			/*
			 * Rimmington
			 */
			new WorldTile(2947 + RANDOM.nextInt(20), 3219 + RANDOM.nextInt(20), 0),
			/*
			 * Karamja volcano
			 */
			new WorldTile(2856 + RANDOM.nextInt(15), 3150 + RANDOM.nextInt(15), 0),
			/*
			 * Brimhaven dungeon entrance
			 */
			new WorldTile(2733 + RANDOM.nextInt(10), 3146 + RANDOM.nextInt(10), 0),
			/*
			 * Sophanem
			 */
			new WorldTile(3299 + RANDOM.nextInt(5), 2784 + RANDOM.nextInt(5), 0),
			/*
			 * Tree gnome stronghold
			 */
			new WorldTile(2438 + RANDOM.nextInt(20), 3420 + RANDOM.nextInt(20), 0),
			/*
			 * Zanaris (center, just north of grain field)
			 */
			new WorldTile(2416 + RANDOM.nextInt(3), 4456 + RANDOM.nextInt(3), 0),
	};

	/**
	 * Randomly selects implings
	 * @return
	 */
	public static int getRandomImplingId() {
		FlyingEntities[] implings = FlyingEntities.values();
		int random = RandomUtils.random(1000);
		if (random < 3)
			return implings[RandomUtils.random(3)].getNpcId();
		if (random < 80)
			return implings[RandomUtils.random(5)].getNpcId();
		if (random < 300)
			return implings[RandomUtils.random(7)].getNpcId();
		return implings[RandomUtils.random(5)].getNpcId();
	}

	/**
	 * Spawns the imps.
	 */
	public static void spawnImps() {
		World.npcs().filter(impId -> impId.getId() == getRandomImplingId())
				.forEach(imps -> imps.setNextGraphics(new Graphics(86)));
		NPC.spawnNPC(getRandomImplingId(), LOCATIONS[RANDOM.nextInt(LOCATIONS.length)], null, false);
	}
}