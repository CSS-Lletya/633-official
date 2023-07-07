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

public class Spiritspider extends Familiar {

	public Spiritspider(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Egg Spawn";
	}

	@Override
	public String getSpecialDescription() {
		return "Spawns a random amount of red eggs around the familiar.";
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
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		setNextAnimation(new Animation(8267));
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		WorldTile tile = this;
		// attemps to randomize tile by 4x4 area
		for (int trycount = 0; trycount < RandomUtils.inclusive(10); trycount++) {
			tile = new WorldTile(this, 2);
			if (World.getTileAttributes().isTileFree(this.getPlane(), tile.getX(), tile.getY(), player.getSize()))
				return true;
			World.sendGraphics(new Graphics(1342), tile);
			FloorItem.addGroundItem(new Item(223, 1), tile, player, true, 120);
		}
		return true;
	}
}
