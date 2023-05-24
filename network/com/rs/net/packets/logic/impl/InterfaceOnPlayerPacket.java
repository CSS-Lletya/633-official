package com.rs.net.packets.logic.impl;

import java.util.Optional;

import com.rs.GameConstants;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerCombat;
import com.rs.game.player.content.Magic;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.utilities.Utility;

@LogicPacketSignature(packetId = 34, packetSize = 11, description = "An Interface that's used onto a Player (Magic, etc..)")
public class InterfaceOnPlayerPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		if (player.getMovement().isLocked()/* || player.getEmotesManager().isDoingEmote() */)
			return;
		final int itemId = stream.readUnsignedShort128();
		int interfaceSlot = stream.readUnsignedShortLE();
		int playerIndex = stream.readUnsignedShortLE128();
		final boolean forceRun = stream.readUnsignedByteC() == 1;
		int interfaceHash = stream.readInt();
		int interfaceId = interfaceHash >> 16;
		int componentId = interfaceHash - (interfaceId << 16);
		if (Utility.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if (!player.getInterfaceManager().containsInterface(interfaceId))
			return;
		if (componentId == 65535)
			componentId = -1;
		if (componentId != -1 && Utility.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
			return;
		final Player p2 = World.getPlayers().get(playerIndex);
		if (p2 == null || p2 == player || p2.isDead() || p2.isFinished()
				|| !player.getMapRegionsIds().contains(p2.getRegionId()))
			return;
		player.getMovement().stopAll();
		if (forceRun)
			player.setRun(forceRun);
		switch (interfaceId) {
		case 1110:
			if (componentId == 87)
//				ClansManager.invite(player, p2);
				break;
		case Inventory.INVENTORY_INTERFACE:
			final Item item = player.getInventory().getItem(interfaceSlot);
			if (item == null || item.getId() != itemId)
				return;
			player.setRouteEvent(new RouteEvent(p2, () -> {
				if (player.getMapZoneManager().execute(player, controller -> !controller.processItemOnPlayer(player, p2, item)))
					return;
//				if (itemId == 4155)
//					player.getSlayerManager().invitePlayer(p2);
			}));
			break;
		case 662:
		case 747:
			if (player.getFamiliar() == null)
				return;
			player.resetWalkSteps();
			if ((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65)
					|| (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18) {
				if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 24
						|| interfaceId == 747 && componentId == 18)) {
					if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
						return;
				}
				if (!player.isCanPvp() || !p2.isCanPvp()) {
					player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
					return;
				}
				if (!player.getFamiliar().canAttack(p2)) {
					player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
					return;
				} else {
					player.getFamiliar().setSpecial(
							interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18);
					player.getFamiliar().setTarget(p2);
				}
			}
			break;
		case 193:
			switch (componentId) {
			case 28:
			case 32:
			case 24:
			case 20:
			case 30:
			case 34:
			case 26:
			case 22:
			case 29:
			case 33:
			case 25:
			case 21:
			case 31:
			case 35:
			case 27:
			case 23:
				if (Magic.checkCombatSpell(player, componentId, 1, false)) {
					player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()),
							p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
					if (player.getMapZoneManager().execute(player, controller -> !controller.canAttack(player, p2))) {
						return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
						return;
					}
					if (!p2.isMultiArea() || !player.isMultiArea()) {
						if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utility.currentTimeMillis()) {
							player.getPackets()
									.sendGameMessage("That " + (player.getAttackedBy().isPlayer() ? "player" : "npc")
											+ " is already in combat.");
							return;
						}
						if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utility.currentTimeMillis()) {
							if (p2.getAttackedBy().isNPC()) {
								p2.setAttackedBy(player); // changes
								// enemy
								// to player,
								// player has
								// priority over
								// npc on single
								// areas
							} else {
								player.getPackets().sendGameMessage("That player is already in combat.");
								return;
							}
						}
					}
					player.getAction().setAction(new PlayerCombat(player, Optional.of(p2)));
				}
				break;
			}
		case 192:
			switch (componentId) {
			case 25: // air strike
			case 28: // water strike
			case 30: // earth strike
			case 32: // fire strike
			case 34: // air bolt
			case 39: // water bolt
			case 42: // earth bolt
			case 45: // fire bolt
			case 49: // air blast
			case 52: // water blast
			case 58: // earth blast
			case 63: // fire blast
			case 70: // air wave
			case 73: // water wave
			case 77: // earth wave
			case 80: // fire wave
			case 86: // teleblock
			case 84: // air surge
			case 87: // water surge
			case 89: // earth surge
			case 91: // fire surge
			case 99: // storm of armadyl
			case 36: // bind
			case 66: // Sara Strike
			case 67: // Guthix Claws
			case 68: // Flame of Zammy
			case 55: // snare
			case 81: // entangle
				if (Magic.checkCombatSpell(player, componentId, 1, false)) {
					player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()),
							p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
					if (player.getMapZoneManager().execute(player, controller -> !controller.canAttack(player, p2))) {
						return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
						return;
					}
					if (!p2.isMultiArea() || !player.isMultiArea()) {
						if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utility.currentTimeMillis()) {
							player.getPackets()
									.sendGameMessage("That " + (player.getAttackedBy().isPlayer() ? "player" : "npc")
											+ " is already in combat.");
							return;
						}
						if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utility.currentTimeMillis()) {
							if (p2.getAttackedBy().isNPC()) {
								p2.setAttackedBy(player); // changes
								// enemy
								// to player,
								// player has
								// priority over
								// npc on single
								// areas
							} else {
								player.getPackets().sendGameMessage("That player is already in combat.");
								return;
							}
						}
					}
					player.getAction().setAction(new PlayerCombat(player, Optional.of(p2)));
				}
				break;
			}
			break;
		case 430:
			Magic.processLunarSpell(player, componentId, p2);
			break;
		}
		if (GameConstants.DEBUG)
			System.out.println("Spell:" + componentId);
	}
}