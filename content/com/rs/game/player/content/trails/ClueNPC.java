package com.rs.game.player.content.trails;

import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;

public class ClueNPC extends NPC {

	private transient Player target;

	public ClueNPC(Player target, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, null, canBeAttackFromOutOfArea, true);
		this.target = target;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (target.isFinished() || !withinDistance(target, 10)) {
			target.getTreasureTrailsManager().setPhase(0);
			deregister();
			return;
		}
	}

	public Player getTarget() {
		return target;
	}
}
