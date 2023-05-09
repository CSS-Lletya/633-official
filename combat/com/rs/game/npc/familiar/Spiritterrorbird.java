package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import skills.Skills;

public class Spiritterrorbird extends Familiar {

	public Spiritterrorbird(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Tireless Run";
	}

	@Override
	public String getSpecialDescription() {
		return "Restores the player's run energy, by half the players agility level rounded up.";
	}

	@Override
	public int getBOBSize() {
		return 12;
	}

	@Override
	public int getSpecialAmount() {
		return 8;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		if (player.getDetails().getRunEnergy() == 100) {
			player.getPackets().sendGameMessage("This wouldn't effect you at all.");
			return false;
		}
		int newLevel = getOwner().getSkills().getLevel(Skills.AGILITY) + 2;
		double runEnergy = player.getDetails().getRunEnergy() + (Math.round(newLevel / 2));
		if (newLevel > getOwner().getSkills().getLevelForXp(Skills.AGILITY) + 2)
			newLevel = getOwner().getSkills().getLevelForXp(Skills.AGILITY) + 2;
		setNextAnimation(new Animation(8229));
		setNextGraphics(new Graphics(1521));
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.getSkills().set(Skills.AGILITY, newLevel);
		player.getMovement().setRunEnergy(runEnergy > 100 ? 100 : runEnergy);
		return true;
	}
}
