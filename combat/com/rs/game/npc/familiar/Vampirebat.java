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

public class Vampirebat extends Familiar {

	public Vampirebat(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Vampyre Touch";
	}

	@Override
	public String getSpecialDescription() {
		return "Deals damage to your opponents, with a maximum hit of 120. It also has a chance of healing your lifepoints by 20.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 4;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = getOwner();
		player.setNextGraphics(new Graphics(1316));
		player.setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(8275));
		setNextGraphics(new Graphics(1323));
		final Entity target = (Entity) object;
		World.get().submit(new Task(1) {
			@Override
			protected void execute() {
				target.applyHit(new Hit(getOwner(), RandomUtils.random(130), HitLook.MAGIC_DAMAGE));
				this.cancel();
			}
		});
		return false;
	}
}
