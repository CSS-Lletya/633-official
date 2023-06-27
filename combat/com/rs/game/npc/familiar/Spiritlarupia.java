package com.rs.game.npc.familiar;

import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;

import skills.Skills;
import skills.summoning.Summoning.Pouch;

public class Spiritlarupia extends Familiar {

	public Spiritlarupia(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Rending";
	}

	@Override
	public String getSpecialDescription() {
		return "Attacks the player's opponent with a magic attack, and also drains opponent's Strength. ";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		Player player = getOwner();
		final int damage = RandomUtils.inclusive(107);
		setNextGraphics(new Graphics(1370));
		setNextAnimation(new Animation(7919));
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		World.sendProjectile(this, target, 1371, 34, 16, 30, 35, 16, 0);
		if (damage > 20)
			target.ifPlayer(targetSelected -> targetSelected.getSkills().set(Skills.STRENGTH,
						targetSelected.getSkills().getLevel(Skills.STRENGTH) - (damage / 20)));
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				target.applyHit(new Hit(getOwner(), damage, HitLook.MAGIC_DAMAGE));
				this.cancel();
			}
		});
		return true;
	}
}
