package com.rs.game.player.content;

import com.rs.game.map.GameObject;
import com.rs.utilities.RandomUtility;

import lombok.AllArgsConstructor;

public final class LivingRockCavern {

	@AllArgsConstructor
	public static enum Rocks {
		COAL_ROCK_1(new GameObject(5999, 10, 1, 3690, 5146, 0)),
		COAL_ROCK_2(new GameObject(5999, 10, 2, 3690, 5125, 0)),
		COAL_ROCK_3(new GameObject(5999, 10, 0, 3687, 5107, 0)),
		COAL_ROCK_4(new GameObject(5999, 10, 1, 3674, 5098, 0)),
		COAL_ROCK_5(new GameObject(5999, 10, 2, 3664, 5090, 0)),
		COAL_ROCK_6(new GameObject(5999, 10, 3, 3615, 5090, 0)),
		COAL_ROCK_7(new GameObject(5999, 10, 1, 3625, 5107, 0)),
		COAL_ROCK_8(new GameObject(5999, 10, 3, 3647, 5142, 0)),
		GOLD_ROCK_1(new GameObject(45076, 10, 1, 3667, 5075, 0)),
		GOLD_ROCK_2(new GameObject(45076, 10, 0, 3637, 5094, 0)),
		GOLD_ROCK_3(new GameObject(45076, 10, 0, 3677, 5160, 0)),
		GOLD_ROCK_4(new GameObject(45076, 10, 1, 3629, 5148, 0));

		public GameObject rock;

	}

	private static void respawnRock(final Rocks rock) {
		GameObject.spawnTempGroundObject(rock.rock, RandomUtility.inclusive(8) + 3 * 60);
	}
	
	public static void init() {
		for (Rocks rock : Rocks.values())
			respawnRock(rock);
	}
}