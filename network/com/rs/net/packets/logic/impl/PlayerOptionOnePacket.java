package com.rs.net.packets.logic.impl;

import java.util.Optional;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerCombat;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.utilities.Utility;

@LogicPacketSignature(packetId = 25, packetSize = 3, description = "The First menu option for a Player")
public class PlayerOptionOnePacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		int playerIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.readUnsignedByte128() == 1;
		Player p2 = World.getPlayers().get(playerIndex);
		if (forceRun)
			player.setRun(forceRun);
		player.getMovement().stopAll();

		if (p2 == null || p2 == player || p2.isDead() || p2.isFinished()
				|| !player.getMapRegionsIds().contains(p2.getRegionId()))
			return;
		if (player.getMovement().isLocked() || player.getLastAnimationEnd() > Utility.currentTimeMillis()
				|| player.getMapZoneManager().execute(player, controller -> !controller.canPlayerOption1(player, p2)))
			return;
		if (!player.isCanPvp())
			return;
		if (player.getMapZoneManager().execute(player, controller -> !controller.canAttack(player, p2))) {
			return;
		}
		if (!player.isCanPvp() || !p2.isCanPvp()) {
			player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
			return;
		}
		if (!p2.isMultiArea() || !player.isMultiArea()) {
			if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utility.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You are already in combat.");
				return;
			}
			if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utility.currentTimeMillis()) {
				if (p2.getAttackedBy().isNPC()) {
					p2.setAttackedBy(player); // changes enemy to player,
					// player has priority over
					// npc on single areas
				} else {
					player.getPackets().sendGameMessage("That player is already in combat.");
					return;
				}
			}
		}

		player.getAction().setAction(new PlayerCombat(player, Optional.of(p2)));
	}
}