package com.rs.net.packets.logic.impl;

import java.util.Optional;

import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerCombat;
import com.rs.io.InputStream;
import com.rs.net.packets.logic.LogicPacket;
import com.rs.net.packets.logic.LogicPacketSignature;
import com.rs.utilities.Utility;

@LogicPacketSignature(packetId = 21, packetSize = 3, description = "Attack an NPC")
public class AttackNPCPacket implements LogicPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead())
			return;
		boolean forceRun = stream.readByteC() == 1;
		int npcIndex = stream.readUnsignedShort128();

		NPC npc = World.getNPCs().get(npcIndex);
		if (npc == null || npc.isDead() || npc.isFinished() || !player.getMapRegionsIds().contains(npc.getRegionId())
				|| !npc.getDefinitions().hasAttackOption())
			return;
		if (player.getMovement().isLocked() || player.getNextEmoteEnd() >= Utility.currentTimeMillis())
			return;
		if (player.getMapZoneManager().execute(player, controller -> !controller.canAttack(player, npc))) {
			return;
		}
		if (forceRun) // you scrwed up cutscenes
			player.setRun(forceRun);
		player.getMovement().stopAll();
		player.getSkillAction().ifPresent(skill -> skill.cancel());
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
				if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utility.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You are already in combat.");
					return;
				}
				if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utility.currentTimeMillis()) {
					player.getPackets().sendGameMessage("This npc is already in combat.");
					return;
				}
			}
		}
		//bugged..hmmm
		player.getAction().setAction(new PlayerCombat(player, Optional.of(npc)));
	}
}