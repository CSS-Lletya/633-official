package com.rs.game.npc.familiar;

import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import skills.summoning.Summoning.Pouch;

public class Macaw extends Familiar {

	private int specialLock = -1;

	public Macaw(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Herbcall";
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (specialLock > 0)
			specialLock--;
		else if (specialLock == 0) {
			specialLock = -1;
			getOwner().getPackets().sendGameMessage("Your macaw feels rested and ready for flight again.");
		}
	}

	@Override
	public String getSpecialDescription() {
		return "Can produce one herb, all herbs up to and including Torstol, within a 60 second range.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		if (specialLock > 0) {
			getOwner().getPackets().sendGameMessage("Your macaw is too tired to continue searching for herbs.");
			return false;
		}
		specialLock = 100;
		getOwner().setNextGraphics(new Graphics(1300));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(8013));
		final WorldTile tile = new WorldTile(getOwner().getX() - 1, getOwner().getY(), getOwner().getPlane());
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				World.sendGraphics(new Graphics(1321), tile);
				World.get().submit(new Task(2) {
					@Override
					protected void execute() {
						setNextAnimation(new Animation(8014));
						this.cancel();
					}
				});
				this.cancel();
			}
		});
//	Herbs herb;
//	if (Utils.getRandom(100) >= 5)
//	    herb = Herbs.values()[Utils.random(Herbs.values().length)];
//	else
//	    herb = Herbs.values()[Utils.getRandom(3)];
//	World.addGroundItem(new Item(herb.getHerbId(), 1), tile, getOwner(), true, 180);
		return true;
	}
}
