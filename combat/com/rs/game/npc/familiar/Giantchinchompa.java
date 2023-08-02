package com.rs.game.npc.familiar;

import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.player.Hit.HitLook;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import skills.summoning.Summoning.Pouch;

public class Giantchinchompa extends Familiar {

	public Giantchinchompa(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Explode";
	}

	@Override
	public String getSpecialDescription() {
		return "Explodes, damaging nearby enemies.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		setNextAnimation(new Animation(7750));
		setNextGraphics(new Graphics(1310));
		setNextForceTalk(new ForceTalk("Squeek!"));
		Player player = getOwner();
		ObjectArrayList<Short> playerIndexes = World.getRegion(player.getRegionId()).getPlayersIndexes();
		if (playerIndexes != null) {
			for (int playerIndex : playerIndexes) {
				Player p2 = World.getPlayers().get(playerIndex);
				if (p2 == null || p2.isDead() || p2 != player || !p2.isRunning() || !p2.withinDistance(player, 2))
					continue;
				p2.applyHit(new Hit(this, RandomUtility.inclusive(130), HitLook.MAGIC_DAMAGE));
			}
			return true;
		}
		return false;
	}
}
