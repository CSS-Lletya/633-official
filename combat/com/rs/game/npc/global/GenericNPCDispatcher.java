package com.rs.game.npc.global;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.Entity;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.SneakyThrows;

public class GenericNPCDispatcher {

	private static final Object2ObjectOpenHashMap<GenericNPCSignature, GenericNPCListener> NPC = new Object2ObjectOpenHashMap<>();
	
	@SneakyThrows(Exception.class)
	public NPC create(NPC npc, WorldTile tile) {
		getVerifiedNPC(npc.getId()).ifPresent(mob -> {
			GenericNPCSignature signature = mob.getClass().getAnnotation(GenericNPCSignature.class);
			Arrays.stream(signature.npcId()).forEach(mobId -> new NPC(mobId,tile , null, signature.canBeAttackFromOutOfArea(), signature.isSpawned()) );
		});
		return npc;
	}
	
	public void process(NPC npc) {
		getVerifiedNPC(npc.getId()).ifPresent(mob -> mob.process(npc));
	}
	
	public void handleIngoingHit(NPC npc, final Hit hit) {
		getVerifiedNPC(npc.getId()).ifPresent(mob -> mob.handleIngoingHit(hit));
	}
	
	public void setRespawnTask(NPC npc) {
		getVerifiedNPC(npc.getId()).ifPresent(mob -> mob.setRespawnTask());
	}
	
	public void possibleTargets(NPC npc) {
		getVerifiedNPC(npc.getId()).ifPresent(mob -> mob.getPossibleTargets(npc));
	}
	
	public void sendDeath(Player killer, Optional<Entity> source, Runnable run) {
		if (getVerifiedNPC(Optional.of(source.get().toNPC().getId()).get()).isPresent()) {
			getVerifiedNPC(Optional.of(source.get().toNPC().getId()).get()).ifPresent(mob -> mob.sendDeath(killer, source));
		} else {
			run.run();
		}
		
	}
	
	public void setAttributes(NPC npc) {
		getVerifiedNPC(npc.getId()).ifPresent(mob -> mob.setAttributes(npc));
	}
	
	private Optional<GenericNPCListener> getVerifiedNPC(int id) {
	    return NPC.values().stream()
	            .filter(npc -> isValidID(npc, id))
	            .findFirst();
	}

	private boolean isValidID(GenericNPCListener genericNPC, int mobId) {
		GenericNPCSignature signature = genericNPC.getClass().getAnnotation(GenericNPCSignature.class);
		return Arrays.stream(signature.npcId()).anyMatch(id -> mobId == id);
	}

	public static void load() {
		List<GenericNPCListener> mobLoader = Utility.getClassesInDirectory("com.rs.game.npc.global.impl").stream().map(clazz -> (GenericNPCListener) clazz).collect(Collectors.toList());
		mobLoader.forEach(npcs -> NPC.put(npcs.getClass().getAnnotation(GenericNPCSignature.class), npcs));
	}
}