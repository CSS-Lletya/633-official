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
import com.rs.utilities.RandomUtility;

import skills.Skills;
import skills.summoning.Summoning.Pouch;

public class Spiritjelly extends Familiar {

	public Spiritjelly(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Dissolve";
	}

	@Override
	public String getSpecialDescription() {
		return "A magic attack that does up to 136 magic damage and drains the target's attack stat.";
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
	public boolean submitSpecial(Object object) {// TODO get special anim
		final Entity target = (Entity) object;
		Player player = getOwner();
		final int damage = RandomUtility.inclusive(100);
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		World.sendProjectile(this, target, 1359, 34, 16, 30, 35, 16, 0);
		if (damage > 20)
			target.ifPlayer(targetSelected -> targetSelected.getSkills().set(Skills.ATTACK,
					targetSelected.getSkills().getLevel(Skills.ATTACK) - (damage / 20)));
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				target.applyHit(new Hit(getOwner(), damage, HitLook.MAGIC_DAMAGE));
				target.setNextGraphics(new Graphics(1360));
				this.cancel();
			}
		});
		return true;
	}
}
