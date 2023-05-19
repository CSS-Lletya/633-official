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

public class Karamthulhuoverlord extends Familiar {

	public Karamthulhuoverlord(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Doomsphere Device";
	}

	@Override
	public String getSpecialDescription() {
		return "Attacks the target with a powerful water spell that can cause up to 160 life points";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		Player player = getOwner();
		setNextGraphics(new Graphics(7974));
		setNextGraphics(new Graphics(1478));
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		World.sendProjectile(this, target, 1479, 34, 16, 30, 35, 16, 0);
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				target.applyHit(new Hit(getOwner(), RandomUtils.inclusive(163), HitLook.MAGIC_DAMAGE));
				target.setNextGraphics(new Graphics(1480));
				this.cancel();
			}
		});
		return true;
	}
}
