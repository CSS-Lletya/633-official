package com.rs.net.packets.logic.impl;

import java.util.Optional;

import com.rs.GameConstants;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerCombat;
import com.rs.game.player.content.Magic;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacketListener;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.plugin.NPCPluginDispatcher;
import com.rs.utilities.Utility;

@LogicPacketSignature(packetId = 2, packetSize = 11, description = "An Interface that's used onto a NPC (Magic, etc..)")
public class InterfaceOnNPCPacket implements LogicPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		if (player.getMovement().isLocked() /* || player.getEmotesManager().isDoingEmote() */)
			return;
		int itemId = stream.readUnsignedShortLE128();
		int interfaceHash = stream.readIntV2();
		@SuppressWarnings("unused")
		int interfaceSlot = stream.readUnsignedShort128();
		int npcIndex = stream.readUnsignedShortLE128();
		boolean forceRun = stream.readUnsignedByte128() == 1;
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
		NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.getMovement().stopAll();
		if (forceRun)
			player.setRun(forceRun);
		if (interfaceId != Inventory.INVENTORY_INTERFACE) {
			if (!npc.getDefinitions().hasAttackOption()) {
				player.getPackets().sendGameMessage("You can't attack this npc.");
				return;
			}
		}
		switch (interfaceId) {
		case Inventory.INVENTORY_INTERFACE:
			Item item = player.getInventory().getItem(itemId);
			if (item == null)
				return;
			player.setRouteEvent(new RouteEvent(npc, () -> {
				NPCPluginDispatcher.executeItemOnNPC(player, npc, item);
				if (player.getMapZoneManager().execute(player, controller -> !controller.processItemOnNPC(player, npc, item)))
					return;
				if (npc instanceof Familiar) {
					Familiar familiar = (Familiar) npc;
					if (familiar != player.getFamiliar()) {
						player.getPackets().sendGameMessage("This is not your familiar!");
						return;
					}
				}
			}));
			break;
		case 662:
		case 747:
			if (player.getFamiliar() == null)
				return;
			player.resetWalkSteps();
			if ((interfaceId == 747 && componentId == 15) || (interfaceId == 662 && componentId == 65)
					|| (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 18
					|| interfaceId == 747 && componentId == 24) {
				if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18)) {
					if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
						return;
				}
				if (npc instanceof Familiar) {
					Familiar familiar = (Familiar) npc;
					if (familiar == player.getFamiliar()) {
						player.getPackets().sendGameMessage("You can't attack your own familiar.");
						return;
					}
					if (!player.getFamiliar().canAttack(familiar.getOwner())) {
						player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
						return;
					}
				}
				if (!player.getFamiliar().canAttack(npc)) {
					player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
					return;
				} else {
					player.getFamiliar().setSpecial(
							interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 18);
					player.getFamiliar().setTarget(npc);
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
					player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
							npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
					if (player.getMapZoneManager().execute(player, controller -> !controller.canAttack(player, npc))) {
						return;
					}
					if (npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if (!familiar.canAttack(player)) {
							player.getPackets().sendGameMessage("You can't attack this npc.");
							return;
						}
					} else if (!npc.isForceMultiAttacked()) {
						if (!npc.isMultiArea() || !player.isMultiArea()) {
							if (player.getAttackedBy() != npc
									&& player.getAttackedByDelay() > Utility.currentTimeMillis()) {
								player.getPackets().sendGameMessage("You are already in combat.");
								return;
							}
							if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utility.currentTimeMillis()) {
								player.getPackets().sendGameMessage("This npc is already in combat.");
								return;
							}
						}
					}
					player.getAction().setAction(new PlayerCombat(Optional.of(npc)));
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
			case 84: // air surge
			case 87: // water surge
			case 89: // earth surge
			case 66: // Sara Strike
			case 67: // Guthix Claws
			case 68: // Flame of Zammy
			case 93:
			case 91: // fire surge
			case 99: // storm of Armadyl
			case 36: // bind
			case 55: // snare
			case 81: // entangle
				if (Magic.checkCombatSpell(player, componentId, 1, false)) {
					player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()),
							npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
					if (player.getMapZoneManager().execute(player, controller -> !controller.canAttack(player, npc))) {
						return;
					}
					if (npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.getPackets().sendGameMessage("You can't attack your own familiar.");
							return;
						}
						if (!familiar.canAttack(player)) {
							player.getPackets().sendGameMessage("You can't attack this npc.");
							return;
						}
					} else if (!npc.isForceMultiAttacked()) {
						if (!npc.isMultiArea() || !player.isMultiArea()) {
							if (player.getAttackedBy() != npc
									&& player.getAttackedByDelay() > Utility.currentTimeMillis()) {
								player.getPackets().sendGameMessage("You are already in combat.");
								return;
							}
							if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utility.currentTimeMillis()) {
								player.getPackets().sendGameMessage("This npc is already in combat.");
								return;
							}
						}
					}
					player.getAction().setAction(new PlayerCombat(Optional.of(npc)));
				}
				break;
			}
			break;
		case 430:
			Magic.processLunarSpell(player, componentId, npc);
			break;
		}
		if (GameConstants.DEBUG)
			System.out.println("Spell:" + componentId);
	}
}