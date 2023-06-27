package com.rs.game.npc.familiar;

import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;

import skills.summoning.Summoning.Pouch;

public class Fruitbat extends Familiar {

	private static final transient int[] FRUITS = new int[] { 5972, 5974, 2102, 2120, 1963, 2108, 5982 };

	private int fruitTicks;

	public Fruitbat(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		fruitTicks++;
		if (fruitTicks == 500)
			addFruitReward();
	}

	private void addFruitReward() {
		getBob().getBeastItems().add(new Item(FRUITS[RandomUtils.inclusive(FRUITS.length)], 1));
		fruitTicks = 0;
	}

	@Override
	public String getSpecialName() {
		return "Fruitfall";
	}

	@Override
	public String getSpecialDescription() {
		return "Bat can get up to eight fruit and drop them on the ground around the player.";
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
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		WorldTile tile = this;
		for (int trycount = 0; trycount < RandomUtils.inclusive(8); trycount++) {
			tile = new WorldTile(this, 2);
			if (World.isTileFree(this.getPlane(), tile.getX(), tile.getY(), player.getSize()))
				return true;
			World.sendGraphics(new Graphics(1331), tile);
			FloorItem.addGroundItem(new Item(FRUITS[RandomUtils.inclusive(FRUITS.length)], 1), tile, player, true, 120);
		}
		return false;
	}
}
