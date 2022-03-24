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

public class Giantent extends Familiar {

	public Giantent(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Acorn Missile";
	}

	@Override
	public String getSpecialDescription() {
		return "Hits all players around a tile radius (not including you) with damage that could inflict up to 187 points.";
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
		Player player = getOwner();
		player.setNextAnimation(new Animation(7858));
		player.setNextGraphics(new Graphics(1316));
		World.get().submit(new Task(1) {
			@Override
			protected void execute() {
				World.get().submit(new Task(2) {
					@Override
					protected void execute() {
						target.applyHit(new Hit(getOwner(), RandomUtils.inclusive(150), HitLook.MAGIC_DAMAGE));
						target.setNextGraphics(new Graphics(1363));
						this.cancel();
					}
				});
				World.sendProjectile(npc, target, 1362, 34, 16, 30, 35, 16, 0);
				this.cancel();
			}
		});
		return true;
	}
}
