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

import skills.summoning.Summoning.Pouch;

public class Spiritdagannoth extends Familiar {

	public Spiritdagannoth(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Spike Shot";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts damage to your target from up to 180 hitpoints.";
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
		final Familiar npc = this;
		getOwner().setNextGraphics(new Graphics(1316));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(7787));
		setNextGraphics(new Graphics(1467));
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				World.get().submit(new Task(1) {
					@Override
					protected void execute() {
						int hitDamage = RandomUtility.inclusive(180);
						if (hitDamage > 0) {
							if (target.isPlayer())
								((Player) target).getMovement().lock(6);
							else
								target.getMovement().addFreezeDelay(6000);
						}
						target.applyHit(new Hit(getOwner(), hitDamage, HitLook.MAGIC_DAMAGE));
						target.setNextGraphics(new Graphics(1428));
						this.cancel();
					}
				});
				World.sendProjectile(npc, target, 1426, 34, 16, 30, 35, 16, 0);
				this.cancel();
			}
		});

		return true;
	}
}
