package com.rs.game.npc.familiar;

import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;

import skills.Skills;

public class Ibis extends Familiar {

	@SuppressWarnings("unused")
	private int forageTicks;

	public Ibis(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
		boolean isSwordFish = RandomUtils.inclusive(3) == 0;
		int foragedItem = isSwordFish ? 371 : 359;
		if (isSwordFish)
			getOwner().getSkills().addXp(Skills.FISHING, 10);
		getBob().getBeastItems().add(new Item(foragedItem, 1));
		forageTicks = 0;
	}

	@Override
	public String getSpecialName() {
		return "Fish rain";
	}

	@Override
	public String getSpecialDescription() {
		return "Makes fish (raw shrimp, bass, cod, and mackerel) fall out of the sky.";
	}

	@Override
	public int getBOBSize() {
		return 10;
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
		final Player player = (Player) object;
		getOwner().setNextGraphics(new Graphics(1300));
		getOwner().setNextAnimation(new Animation(7660));
		setNextAnimation(new Animation(8201));
		final WorldTile firstTile = new WorldTile(player.getX() + 1, player.getY() + 1, player.getPlane());
		final WorldTile secondTile = new WorldTile(player.getX() - 1, player.getY() - 1, player.getPlane());
		World.sendGraphics(player, new Graphics(1337), firstTile);
		World.sendGraphics(player, new Graphics(1337), secondTile);
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				FloorItem.createGroundItem(new Item(1, 1), firstTile, player, true, 180, true);
				FloorItem.createGroundItem(new Item(1, 1), secondTile, player, true, 180, true);
				this.cancel();
			}
		});
		return true;
	}
}
