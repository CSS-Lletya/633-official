package com.rs.net.packets.logic.impl;

import com.rs.game.map.World;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;

@LogicPacketSignature(packetId = 44, packetSize = 3, description = "The Third menu option for a Player")
public class PlayerOptionThreePacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		final boolean forceRun = stream.readUnsignedByte() == 1;
		int playerIndex = stream.readUnsignedShort128();
		final Player p2 = World.getPlayers().get(playerIndex);
		if (p2 == null || p2 == player || p2.isDead() || p2.isFinished()
				|| !player.getMapRegionsIds().contains(p2.getRegionId()))
			return;
		if (player.getMovement().isLocked())
			return;
		if (forceRun)
			player.setRun(forceRun);
		player.getMovement().stopAll();
		player.setRouteEvent(new RouteEvent(p2, () -> {
			if (player.getMapZoneManager().execute(player, controller -> !controller.canPlayerOption4(p2))) {
				return;
			}
			player.getMovement().stopAll();
			if (player.isCantTrade() || player.getCurrentMapZone().isPresent()) {
				player.getPackets().sendGameMessage("You are busy.");
				return;
			}
			if (p2.getInterfaceManager().containsScreenInter() || p2.isCantTrade()
					|| p2.getCurrentMapZone().isPresent()

					|| p2.getMovement().isLocked()) {
				player.getPackets().sendGameMessage("The other player is busy.");
				return;
			}
			if (!p2.withinDistance(player, 14)) {
				player.getPackets().sendGameMessage("Unable to find target " + p2.getDisplayName());
				return;
			}
			if (p2.getAttributes().get(Attribute.TRADE_TARGET).get() == player) {
				p2.getAttributes().get(Attribute.TRADE_TARGET).set(null);
				player.getTrade().openTrade(p2);
				p2.getTrade().openTrade(player);
				return;
			}
			player.getAttributes().get(Attribute.TRADE_TARGET).set(p2);
			player.getPackets().sendGameMessage("Sending " + p2.getDisplayName() + " a request...");
			p2.getPackets().sendTradeRequestMessage(player);
		}));
	}
}