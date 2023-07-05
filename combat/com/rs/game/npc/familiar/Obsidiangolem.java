package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import skills.Skills;
import skills.summoning.Summoning.Pouch;

public class Obsidiangolem extends Familiar {

	public Obsidiangolem(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Volcanic Strength";
	}

	@Override
	public String getSpecialDescription() {
		return "Gives +9 strength levels to the player.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.getSkills().set(Skills.STRENGTH, player.getSkills().getTrueLevel(Skills.STRENGTH) + 9);
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1300));
		setNextGraphics(new Graphics(1465));
		setNextAnimation(new Animation(8053));
		return true;
	}
}
