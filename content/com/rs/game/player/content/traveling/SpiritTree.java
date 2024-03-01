package com.rs.game.player.content.traveling;

import java.util.stream.IntStream;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;

public class SpiritTree {

	private static final int TREE_INTERFACE = 864;
	private static final WorldTile[] TELEPORTS = {
			new WorldTile(2554, 3255, 0),
			new WorldTile(3187, 3507, 0),
			new WorldTile(2416, 2852, 0),
			new WorldTile(2339, 3108, 0),
			new WorldTile(2541, 3170, 0),
			new WorldTile(2462, 3445, 0)
	};
	
	public static void openInterface(Player player, boolean isMini) {
		player.getVarsManager().setVarBit(3959, 3);
		player.getInterfaceManager().sendFullscreenInterface(0, TREE_INTERFACE);
		IntStream.range(0,10).forEach(val -> player.getPackets().sendUnlockIComponentOptionSlots(864, val, 0, 100, 0));
		if (player.getRegionId() == 10033 || player.getRegionId() == 12102)
			player.getVarsManager().setVarBit(1469, 0x27b8c61);
		else if (player.getRegionId() == 9781)
			player.getVarsManager().setVarBit(1469, 0x2678d74);
	}
	private static void sendTeleport(Player player, WorldTile tile) {
		player.getInterfaceManager().closeInterfaces(); 
		Magic.sendTeleportSpell(player, 7082, 7084, 1229, 1229, 1, 0, tile, 4, true, Magic.OBJECT_TELEPORT);
	}

	public static void handleSpiritTree(Player player, int slot) {
		if (slot == 0)
			slot = player.getRegionId() == 10033 ? 6 : 5;
		sendTeleport(player, TELEPORTS[slot - 1]);
	}
}
