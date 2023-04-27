package com.rs.game.npc.global;

import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.player.Hit;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class GenericNPC  {
	
	public abstract void setAttributes(NPC npc);
	
	public void process(NPC npc) { }
	
	public void handleIngoingHit(final Hit hit) { }
	
	public void sendDeath(Optional<Entity> source) { }
	
	public void setRespawnTask() { }
	
	public ObjectArrayList<Entity> getPossibleTargets(NPC npc) {
		return npc.getPossibleTargets();
	}
}