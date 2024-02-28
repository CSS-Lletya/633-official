package com.rs.game.npc.global.impl;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.Entity;
import com.rs.game.map.WorldTile;
import com.rs.game.map.zone.impl.SorceresssGardenMapZone;
import com.rs.game.npc.NPC;
import com.rs.game.npc.global.GenericNPCListener;
import com.rs.game.npc.global.GenericNPCSignature;
import com.rs.game.player.Player;
import com.rs.game.player.content.FadingScreen;

@GenericNPCSignature(npcId = { 5533, 5534, 5535, 5536, 5537, 5538, 5539, 5540, 5541, 5542, 5543, 5544, 5545, 5546, 5547,
		5548, 5549, 5550, 5551, 5552, 5553, 5554, 5555, 5556, 5557, 5558 })
public class ElementalCreature extends GenericNPCListener {

	private transient Player player;
	private boolean beingTeleported = false;
	private int steps;

	private static final WorldTile[][] tiles = { { new WorldTile(2908, 5460, 0), new WorldTile(2898, 5460, 0) },
			{ new WorldTile(2900, 5448, 0), new WorldTile(2900, 5455, 0) },
			{ new WorldTile(2905, 5449, 0), new WorldTile(2899, 5449, 0) },
			{ new WorldTile(2903, 5451, 0), new WorldTile(2903, 5455, 0), new WorldTile(2905, 5455, 0),
					new WorldTile(2905, 5451, 0) },
			{ new WorldTile(2903, 5457, 0), new WorldTile(2917, 5457, 0) },
			{ new WorldTile(2908, 5455, 0), new WorldTile(2917, 5455, 0) },
			{ new WorldTile(2922, 5471, 0), new WorldTile(2922, 5459, 0) },
			{ new WorldTile(2924, 5463, 0), new WorldTile(2928, 5463, 0), new WorldTile(2928, 5461, 0),
					new WorldTile(2924, 5461, 0) },
			{ new WorldTile(2924, 5461, 0), new WorldTile(2926, 5461, 0), new WorldTile(2926, 5458, 0),
					new WorldTile(2924, 5458, 0) },
			{ new WorldTile(2928, 5458, 0), new WorldTile(2928, 5460, 0), new WorldTile(2934, 5460, 0),
					new WorldTile(2934, 5458, 0) },
			{ new WorldTile(2931, 5477, 0), new WorldTile(2931, 5470, 0) },
			{ new WorldTile(2935, 5469, 0), new WorldTile(2928, 5469, 0) },
			{ new WorldTile(2925, 5464, 0), new WorldTile(2925, 5475, 0) },
			{ new WorldTile(2931, 5477, 0), new WorldTile(2931, 5470, 0) },
			{ new WorldTile(2907, 5488, 0), new WorldTile(2907, 5482, 0) },
			{ new WorldTile(2907, 5490, 0), new WorldTile(2907, 5495, 0) },
			{ new WorldTile(2910, 5493, 0), new WorldTile(2910, 5487, 0) },
			{ new WorldTile(2918, 5483, 0), new WorldTile(2918, 5485, 0), new WorldTile(2915, 5485, 0),
					new WorldTile(2915, 5483, 0), new WorldTile(2912, 5483, 0), new WorldTile(2912, 5485, 0),
					new WorldTile(2915, 5485, 0), new WorldTile(2915, 5483, 0) },
			{ new WorldTile(2921, 5486, 0), new WorldTile(2923, 5486, 0), new WorldTile(2923, 5490, 0),
					new WorldTile(2923, 5486, 0) },
			{ new WorldTile(2921, 5491, 0), new WorldTile(2923, 5491, 0), new WorldTile(2923, 5495, 0),
					new WorldTile(2921, 5495, 0) },
			{ new WorldTile(2899, 5466, 0), new WorldTile(2899, 5468, 0), new WorldTile(2897, 5468, 0),
					new WorldTile(2897, 5466, 0), new WorldTile(2897, 5468, 0), new WorldTile(2899, 5468, 0) },
			{ new WorldTile(2897, 5470, 0), new WorldTile(2891, 5470, 0) },
			{ new WorldTile(2897, 5471, 0), new WorldTile(2899, 5471, 0), new WorldTile(2899, 5478, 0),
					new WorldTile(2897, 5478, 0) },
			{ new WorldTile(2896, 5483, 0), new WorldTile(2900, 5483, 0), new WorldTile(2900, 5480, 0),
					new WorldTile(2897, 5480, 0), new WorldTile(2896, 5482, 0) },
			{ new WorldTile(2896, 5483, 0), new WorldTile(2896, 5481, 0), new WorldTile(2891, 5481, 0),
					new WorldTile(2891, 5483, 0) },
			{ new WorldTile(2889, 5485, 0), new WorldTile(2900, 5485, 0) } };

	@Override
	public void setAttributes(NPC npc) {
		npc.setForceTargetDistance((byte) 6);
		npc.setForceAgressive(true);
		npc.setCantInteract(true);
	}

	@Override
	public void process(NPC npc) {
		npc.checkAgressivity();
		int index = npc.getId() - 5533;
		if (!npc.isForceWalking()) {
			if (steps >= tiles[index].length)
				steps = 0;
			npc.setForceWalk(tiles[index][steps]);
			if (npc.withinDistance(tiles[index][steps], 0))
				steps++;
		}
		if (beingTeleported)
				return;
		if (!beingTeleported) {
			for (Entity t : npc.getPossibleTargets()) {
				if (player == null)
					player = (Player) t;
				if (npc.withinDistance(player, 2) && canBeTeleported(npc, player)) {
					beingTeleported = true;
					npc.setNextAnimation(Animations.SG_ELEMENTAL_ARM_SWINGING);
					player.setNextGraphics(Graphic.THREE_RING_TELEPORTING);
					player.getMovement().stopAll();
					player.getMovement().lock(3);
					player.getPackets()
							.sendGameMessage("You've been spotted by an elemental and teleported out of its garden.");
					FadingScreen.fade(player, () -> {
						player.setNextWorldTile(
								SorceresssGardenMapZone.inAutumnGarden(player.getTile()) ? new WorldTile(2913, 5467, 0)
										: (SorceresssGardenMapZone.inSpringGarden(player.getTile())
												? new WorldTile(2916, 5473, 0)
												: (SorceresssGardenMapZone.inSummerGarden(player.getTile())
														? new WorldTile(2910, 5476, 0)
														: new WorldTile(2906, 5470, 0))));
						//force a delay so we can ensure event only takes place once (before would sometimes do multiple (2,3,4 times))
						player.task(3, p -> beingTeleported = false);
					});
					break;
				}
			}
		}
	}
	
	public boolean canBeTeleported(NPC npc, Entity t) {
		final int EAST = 4;
		if (npc.getNextWalkDirection() == EAST && (t.getX() - npc.getX()) < 4 && (t.getY() - npc.getY()) == 0)
			return true;
		return false;
	}
}