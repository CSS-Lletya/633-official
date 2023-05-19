package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;

public class Bloatedleech extends Familiar {

	public Bloatedleech(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Blood Drain";
	}

	@Override
	public String getSpecialDescription() {
		return "Heals stat damage, poison, and disease but sacrifices some life points.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 5;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		final int damage = RandomUtils.inclusive(100) + 50;
		if (player.getHitpoints() - damage <= 0) {
			player.getPackets().sendGameMessage("You don't have enough life points to use this special.");
			return false;
		}
		if (player.isPoisoned())
			player.getPoisonDamage().set(0);
		player.getSkills().restoreSkills();
		player.applyHit(new Hit(player, damage, HitLook.DESEASE_DAMAGE));
		setNextGraphics(new Graphics(1419));
		player.setNextGraphics(new Graphics(1420));
		return true;
	}
}