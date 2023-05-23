package com.rs.plugin.impl.npcs;

import com.rs.cores.CoresManager;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.NPCType;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Ticks;

@NPCSignature(name = { "Sheep" }, npcId = {})
public class SheepNPCPlugin implements NPCType {

	@Override
	public void execute(Player player, NPC npc, int option) throws Exception {
		int npcId = npc.getId();
		if (!player.getInventory().containsOneItem(1735)) {
			player.getPackets().sendGameMessage("You need some shears to shear the sheep.");
			return;
		}
		switch (RandomUtils.getRandom(3)) {
		case 0:
			player.getMovement().lock(2);
			npc.playSound(756, 1);
			LinkedTaskSequence runSequence = new LinkedTaskSequence();
			npc.addWalkSteps(npcId, npcId, 4, true);
			runSequence.connect(1, () -> npc.setRunState(true)).connect(1, () -> npc.setRunState(false)).start();
			player.getPackets().sendGameMessage("The sheep runs away from you.");
			break;
		case 1:
		case 2:
		case 3:
			if (player.getInventory().addItem(npc.getId() == 8876 ? 15415 : 1737, 1)) {
				npc.transformIntoNPC(42);
				player.setNextAnimation(new Animation(893));
				CoresManager.schedule(() -> npc.transformIntoNPC(npcId), Ticks.fromSeconds(15));
			}
			break;
		}
	}
}