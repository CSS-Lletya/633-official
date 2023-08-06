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

@GenericNPCSignature(npcId = {8832, 8833,8834})
public class LivingRockCreature extends GenericNPCListener {

	@Override
	public void setAttributes(NPC npc) {
		npc.setForceTargetDistance((byte) 6);
		npc.setForceAgressive(true);
	}

	@Override
	public void process(NPC npc) {
		npc.checkAgressivity();
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
					transformIntoRemains(killer, source.get().toNPC(), source.get());
					this.cancel();
				}
				loop++;
			}
		});
	}

    public void transformIntoRemains(Player killer, NPC npc, Entity source) {
        final int remainsId = npc.getId() + 5;
        npc.transformIntoNPC((npc.getId() == 37600 ? 37601 : remainsId));
        npc.getMovement().lock();
        killer.getMovement().stopAll();
        killer.getSkillAction().ifPresent(skill -> skill.cancel());
        World.get().submit(new Task(3 * 60) {
			@Override
			protected void execute() {
				if (remainsId == npc.getId())
                    takeRemains(killer, npc);
				cancel();
			}
		});
    }
    
    public void takeRemains(Player killer, NPC npc) {
    	npc.setId((npc.getId() == 37601 ? 37600 : npc.getId() - 5));
    	npc.setNextWorldTile(npc.getRespawnTile());
        npc.deregister();
        if (!npc.isSpawned())
        	npc.setRespawnTask();
    }
}