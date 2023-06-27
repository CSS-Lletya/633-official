package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import skills.summoning.Summoning.Pouch;

public class Unicornstallion extends Familiar {

	public Unicornstallion(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Healing Aura";
	}

	@Override
	public String getSpecialDescription() {
		return "Heals 15% of your health points.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public boolean isAgressive() {
		return false;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		if (player.getHitpoints() == player.getMaxHitpoints()) {
			player.getPackets()
					.sendGameMessage("You need to have at least some damage before being able to heal yourself.");
			return false;
		} else {
			player.setNextAnimation(new Animation(7660));
			player.setNextGraphics(new Graphics(1300));
			int percentHealed = (int) (player.getMaxHitpoints() * .1);
			player.heal(percentHealed);
		}
		return true;
	}

}
