package com.rs.game.npc.familiar;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.net.encoders.other.Graphics;

public class Bunyip extends Familiar {

	private int restoreTicks;

	public Bunyip(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Swallow Whole";
	}

	@Override
	public String getSpecialDescription() {
		return "Eat an uncooked fish and gain the correct number of life points corresponding to the fish eaten if you have the cooking level to cook the fish.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 7;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		restoreTicks++;
		if (restoreTicks == 20) { // approx 15 secs
			getOwner().heal(20);
			getOwner().setNextGraphics(new Graphics(1507));
			restoreTicks = 0;
		}
	}

	@Override
	public boolean submitSpecial(Object object) {
		Item item = getOwner().getInventory().getItem((Integer) object);
		if (item == null)
			return false;
//	for (Fish fish : Fish.values()) {
//	    if (fish.getId() == item.getId()) {
//		if (getOwner().getSkills().getLevel(Skills.COOKING) < fish.getLevel()) {
//		    getOwner().getPackets().sendGameMessage("Your cooking level is not high enough for the bunyip to eat this fish.");
//		    return false;
//		} else {
//		    getOwner().setNextGraphics(new Graphics(1316));
//		    getOwner().setNextAnimation(new Animation(7660));
////		    getOwner().heal(Food.forId(Cookables.forId((short) item.getId()).getProduct().getId()).getHeal() * 10);
//		    getOwner().getInventory().deleteItem(item.getId(), item.getAmount());
//		    return true;// stop here
//		}
//	    }
//	}
		getOwner().getPackets().sendGameMessage("Your bunyip cannot eat this.");
		return false;
	}
}
