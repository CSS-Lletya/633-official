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

public class Prayingmantis extends Familiar {

	public Prayingmantis(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Mantis Strike";
	}

	@Override
	public String getSpecialDescription() {
		return "Uses a magic based attack (max hit 170) which always drains the opponent's prayer and binds if it deals any damage.";
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
		setNextAnimation(new Animation(8071));
		setNextGraphics(new Graphics(1422));
		final int hitDamage = RandomUtility.inclusive(170);
		if (hitDamage > 0)
			target.ifPlayer(player -> player.getPrayer().drainPrayer(hitDamage));

		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				target.setNextGraphics(new Graphics(1423));
				target.applyHit(new Hit(getOwner(), hitDamage, HitLook.MAGIC_DAMAGE));
				this.cancel();
			}
		});
		return true;
	}
}
