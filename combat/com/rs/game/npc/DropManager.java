package com.rs.game.npc;

import java.util.HashMap;

import com.rs.game.item.FloorItem;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

/**
 * The static-utility class that manages all of the {@link DropTable}s
 * including the process of dropping the items when an {@link Mob} is killed.
 * @author lare96 <http://github.org/lare96>
 */
public final class DropManager {
	
	/**
	 * The {@link HashMap} that consists of the drops for {@link Mob}s.
	 */
	@Getter
	private final static Int2ObjectOpenHashMap<DropTable> TABLES = new Int2ObjectOpenHashMap<>();
	
	/**
	 * Mob sharing the same table drop redirects.
	 */
	@Getter
	public final static Int2IntArrayMap REDIRECTS = new Int2IntArrayMap();
	
	/**
	 * Drops the items in {@code victim}s drop table for {@code killer}. If the
	 * killer doesn't exist, the items are dropped for everyone to see.
	 * @param killer the killer, may or may not exist.
	 * @param victim the victim that was killed.
	 */
	public static void dropItems(Player killer, NPC victim) {
		DropTable table = TABLES.get(victim.getId());
		if(table == null) {
			return;
		}
		WorldTile pos = victim.getLastWorldTile();
		final WorldTile lastMobLocation = pos;
		table.toItems(killer, victim).parallelStream().filter(drop -> drop != null).forEach(drop -> FloorItem.createGroundItem(drop, lastMobLocation, killer, false, 180, true));
	}
}