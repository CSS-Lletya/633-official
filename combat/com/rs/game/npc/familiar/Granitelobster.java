package com.rs.game.npc.familiar;

import com.rs.game.Entity;
import com.rs.game.item.Item;
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

import skills.Skills;

public class Granitelobster extends Familiar {

	@SuppressWarnings("unused")
	private int forageTicks;

	public Granitelobster(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		super.processNPC();
//	boolean isFishing = getOwner().getActionManager().getAction() != null && getOwner().getActionManager().getAction() instanceof Fishing;
//	if (isFishing) {
//	    forageTicks++;
//	    if (forageTicks == 300)
//		giveReward();
//	}
	}

	@SuppressWarnings("unused")
	private void giveReward() {
		boolean isShark = RandomUtils.inclusive(3) == 0;
		int foragedItem = isShark ? 383 : 371;
		if (!isShark)
			getOwner().getSkills().addXp(Skills.FISHING, 30);
		getBob().getBeastItems().add(new Item(foragedItem, 1));
		forageTicks = 0;
	}

	@Override
	public String getSpecialName() {
		return "Crushing Claw";
	}

	@Override
	public String getSpecialDescription() {
		return "May inflict up to 140 life points of magic damage and temporarily decrease an opponent's Defence by five levels.";
	}

	@Override
	public int getBOBSize() {
		return 30;
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
		setNextAnimation(new Animation(8118));
		setNextGraphics(new Graphics(1351));
		World.get().submit(new Task(1) {
			@Override
			protected void execute() {
				World.get().submit(new Task(1) {
					@Override
					protected void execute() {
						if (RandomUtils.inclusive(5) == 0) {
							target.ifPlayer(targetSelected -> targetSelected.getSkills().set(Skills.DEFENCE,
									targetSelected.getSkills().getLevel(Skills.DEFENCE)));
						}
						target.applyHit(new Hit(getOwner(), RandomUtils.inclusive(140), HitLook.MELEE_DAMAGE));
						target.setNextGraphics(new Graphics(1353));
						this.cancel();
					}
				});
				World.sendProjectile(npc, target, 1352, 34, 16, 30, 35, 16, 0);
				this.cancel();
			}
		});
		return true;
	}
}
