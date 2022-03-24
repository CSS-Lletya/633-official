package com.rs.game.npc.familiar;

import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;

public class Evilturnip extends Familiar {

	public Evilturnip(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Evil flames";
	}

	@Override
	public String getSpecialDescription() {
		return "Magic based attack which will drain the enemy's magic level some and heal the Evil turnip a little.";
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
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(8251));
		World.sendProjectile(this, target, 1330, 34, 16, 30, 35, 16, 0);
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				int hitDamage = RandomUtils.inclusive(100);
				target.applyHit(new Hit(getOwner(), hitDamage, HitLook.MAGIC_DAMAGE));
				target.setNextGraphics(new Graphics(1329));
				heal(hitDamage / 5);
				this.cancel();
			}
		});
		return true;
	}
}