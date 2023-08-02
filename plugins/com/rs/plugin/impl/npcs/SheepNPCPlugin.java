package com.rs.plugin.impl.npcs;

import com.rs.constants.Sounds;
import com.rs.cores.CoresManager;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.Ticks;

@NPCSignature(name = { "Sheep" }, npcId = {})
public class SheepNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		int npcId = npc.getId();
		if (!player.getInventory().containsOneItem(1735)) {
			player.getPackets().sendGameMessage("You need a pair of shears to shear the sheep.");
			return;
		}
		switch (RandomUtility.getRandom(3)) {
		case 0:
			player.getMovement().lock(2);
			player.getAudioManager().sendNearbyPlayerSound(Sounds.SHEEP_FAILED_SHEERING, 15);
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
				player.getPackets().sendGameMessage("You shear the sheep of it's fleece.");
			}
			break;
		}
	}
}