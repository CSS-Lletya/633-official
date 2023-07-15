package com.rs.game.player.content.traveling;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import lombok.AllArgsConstructor;
import skills.magic.TeleportType;

public class TeleportTabs {

	@AllArgsConstructor
	private enum TeleTab {
		VARROCK(8007, new WorldTile(3217, 3426, 0)),
		LUMBRIDGE(8008, new WorldTile(3222, 3218, 0)),
		FALADOR(8009, new WorldTile(2965, 3379, 0)),
		CAMELOT(8010, new WorldTile(2758, 3478, 0)),
		ARDOUGNE(8011, new WorldTile(2660, 3306, 0)),
		WATCHTOWER(8012, new WorldTile(2549, 3114, 2)),
		HOUSE(8013, null),

		RIMMINGTON(18809, new WorldTile(2954, 3225, 0)),
		TAVERLY(18810, new WorldTile(2882, 3451, 0)),
		POLLNIVNEACH(18811, new WorldTile(3339, 3004, 0)),
		RELLEKA(18812, new WorldTile(2668, 3631, 0)),
		BRIMHAVEN(18813, new WorldTile(2757, 3177, 0)),
		YANILLE(18814, new WorldTile(2546, 3095, 0)),
		TROLLHEIM(20175, new WorldTile(2891, 3676, 0)),

		RUNECRAFTING_GUILD(13598, new WorldTile(1696, 5465, 2)),
		AIR_ALTAR(13599, new WorldTile(3125, 3406, 0)),
		MIND_ALTAR(13600, new WorldTile(2980, 3513, 0)),
		WATER_ALTAR(13601, new WorldTile(3184, 3163, 0)),
		EARTH_ALTAR(13602, new WorldTile(3303, 3477, 0)),
		FIRE_ALTAR(13603, new WorldTile(3309, 3251, 0)),
		BODY_ALTAR(13604, new WorldTile(3051, 3441, 0)),
		COSMIC_ALTAR(13605, new WorldTile(2407, 4383, 0)),
		CHAOS_ALTAR(13606, new WorldTile(2281, 4837, 0)),
		NATURE_ALTAR(13607, new WorldTile(2865, 3022, 0)),
		LAW_ALTAR(13608, new WorldTile(2857, 3379, 0)),
		DEATH_ALTAR(13609, new WorldTile(1864, 4638, 0)),
		BLOOD_ALTAR(13610, new WorldTile(3559, 9778, 0)),
		ASTRAL_ALTAR(13611, new WorldTile(2150, 3862, 0));
	
		private final int itemId;
		private final WorldTile tile;
		
		public static final ImmutableSet<TeleTab> VALUES = Sets.immutableEnumSet(EnumSet.allOf(TeleTab.class));
	}

	/**
	 * This controls the teleTab that you use.
	 */
	public static boolean useTeleTab(final Player player, int itemId) {
		TeleTab.VALUES.stream().filter(tab -> tab.itemId == itemId).forEach(tablet -> {
			if (player.getCurrentMapZone().isPresent() && !player.getCurrentMapZone().get().processItemTeleport(player, tablet.tile)) {
				return;
			}
			player.getInventory().deleteItem(new Item(tablet.itemId));
			player.resetReceivedHits();
			player.getMovement().lock(5);
			TeleportType.TABLET.checkSpecialCondition(player, tablet.tile);
		});
		return true;
	}
}