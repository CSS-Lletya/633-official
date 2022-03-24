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

public class Arcticbear extends Familiar {

	public Arcticbear(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Arctic Blast";
	}

	@Override
	public String getSpecialDescription() {
		return "Can hit a maximum of 150 damage, with a chance of stunning the opponent.";
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
	public boolean submitSpecial(Object object) { // TODO find real animation of
		// bear
		final Entity target = (Entity) object;
		final Familiar npc = this;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(4929));
		setNextGraphics(new Graphics(1405));
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				setNextAnimation(new Animation(-1));
				World.sendProjectile(npc, target, 1406, 34, 16, 30, 35, 16, 0);
				World.get().submit(new Task(1) {
					@Override
					protected void execute() {
						target.applyHit(new Hit(getOwner(), RandomUtils.inclusive(150), HitLook.MAGIC_DAMAGE));
						target.setNextGraphics(new Graphics(1407));
						this.cancel();
					}
				});
				this.cancel();
			}
		});
		return true;
	}
}
