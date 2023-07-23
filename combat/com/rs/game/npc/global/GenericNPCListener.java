package com.rs.game.npc.global;

import java.util.List;
import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;

public abstract class GenericNPCListener {

	public abstract void setAttributes(NPC npc);

	public void process(NPC npc) {}

	public void handleIngoingHit(final Hit hit) {}

	public void sendDeath(Player killer, Optional<Entity> source) {}

	public void setRespawnTask() {}

	public List<Entity> getPossibleTargets(NPC npc) {
		return npc.getPossibleTargets();
	}
	
	public void reset(NPC npc) {}
	
	public void setTarget(NPC npc, Entity entity) {}
}