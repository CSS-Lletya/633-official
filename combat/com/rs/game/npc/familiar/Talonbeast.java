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

import skills.summoning.Summoning.Pouch;

public class Talonbeast extends Familiar {

	public Talonbeast(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Deadly Claw";
	}

	@Override
	public String getSpecialDescription() {
		return "A magical attack that hits 3 times. It is similar to its normal attack, but may hit higher (80 per hit, adding up to a max of 240) and will also hit more often through metal armour.";
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
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(5989));
		setNextGraphics(new Graphics(1519));
		World.sendProjectile(this, target, 1520, 34, 16, 30, 35, 16, 0);
		World.get().submit(new Task(1) {
			int ticks;

			@Override
			protected void execute() {
				if (ticks++ == 3) {
					this.cancel();
					return;
				}
				target.applyHit(new Hit(getOwner(), RandomUtils.inclusive(80), HitLook.MAGIC_DAMAGE));
				this.cancel();
			}
		});
		return false;
	}
}
