package com.rs.game.npc.global.impl;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.global.GenericNPCListener;
import com.rs.game.npc.global.GenericNPCSignature;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

@GenericNPCSignature(npcId = {1266, 1268, 2453, 2885, 2886})
public class RockCrabCreature extends GenericNPCListener {
	
	private int realId;
	
	@Override
	public void setAttributes(NPC npc) {
		realId = npc.getId();
		npc.setForceAgressive(true);
	}

	@Override
	public void process(NPC npc) {
		npc.checkAgressivity();
	}
	
	@Override
	public void reset(NPC npc) {
		npc.setId(realId);
		super.reset(npc);
	}

	@Override
	public void setTarget(NPC npc, Entity entity) {
		if (realId == npc.getId())
			npc.setNextNPCTransformation(realId - 1);
		npc.setHitpoints(npc.getMaxHitpoints());
	}
	
	@Override
	public void sendDeath(Player killer, Optional<Entity> source) {
		final NPCCombatDefinitions defs = source.get().toNPC().getCombatDefinitions();
		source.get().toNPC().resetWalkSteps();
		source.get().toNPC().getMovement().lock();
		source.get().toNPC().getCombat().removeTarget();
		source.get().toNPC().setNextAnimation(Animations.RESET_ANIMATION);
		World.get().submit(new Task(1) {
			int loop;
			@Override
			protected void execute() {
				if (loop == 0) {
					source.get().toNPC().setNextAnimation(new Animation(defs.getDeathAnim()));
				} else if (loop >= defs.getDeathDelay()) {
					source.get().toNPC().drop();
					source.get().toNPC().reset();
					this.cancel();
				}
				loop++;
			}
		});
	}
}