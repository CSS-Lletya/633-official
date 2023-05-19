package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

public class Abyssaltitan extends Familiar {

	public Abyssaltitan(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Essence Shipment";
	}

	@Override
	public String getSpecialDescription() {
		return "Sends all your inventory and beast's essence to your bank.";
	}

	@Override
	public int getBOBSize() {
		return 7;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		if (getOwner().getBank().hasBankSpace()) {
			if (getBob().getBeastItems().getUsedSlots() == 0) {
				getOwner().getPackets().sendGameMessage("Your familiar has no essence to deposit.");
				return false;
			}
			getOwner().getBank().depositAllBob(false);
			getOwner().setNextGraphics(new Graphics(1316));
			getOwner().setNextAnimation(new Animation(7660));
			return true;
		}
		return false;
	}
}
