package com.rs.game.npc.familiar;

import com.rs.game.Entity;
import com.rs.game.item.Item;
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

public class Spiritcoraxatrice extends Familiar {

	private int chocoTriceEgg;

	public Spiritcoraxatrice(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		chocoTriceEgg++;
		if (chocoTriceEgg == 500)
			addChocolateEgg();
	}

	private void addChocolateEgg() {
		getBob().getBeastItems().add(new Item(12109, 1));
		chocoTriceEgg = 0;
	}

	@Override
	public String getSpecialName() {
		return "Petrifying Gaze";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts damage and drains a combat stat, which varies according to the type of cockatrice.";
	}

	@Override
	public int getBOBSize() {
		return 30;
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
		setNextAnimation(new Animation(7766));
		setNextGraphics(new Graphics(1467));
		World.sendProjectile(this, target, 1468, 34, 16, 30, 35, 16, 0);

		target.ifPlayer(targetSelected -> {
			int level = targetSelected.getSkills().getTrueLevel(Skills.SUMMONING);
			int drained = 3;
			if (level - drained > 0)
				drained = level;
			targetSelected.getSkills().drainLevel(Skills.SUMMONING, drained);
		});
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				target.applyHit(new Hit(getOwner(), RandomUtility.inclusive(100), HitLook.MELEE_DAMAGE));
				this.cancel();
			}
		});
		return true;
	}
}
