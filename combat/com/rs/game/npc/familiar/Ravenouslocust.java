package com.rs.game.npc.familiar;

import com.rs.game.Entity;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Foods.Food;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

public class Ravenouslocust extends Familiar {

	public Ravenouslocust(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Famine";
	}

	@Override
	public String getSpecialDescription() {
		return "Eats a peice of an opponent's food.";
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
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Entity target = (Entity) object;
		final Familiar npc = this;
		setNextGraphics(new Graphics(1346));
		setNextAnimation(new Animation(7998));
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				World.sendProjectile(npc, target, 1347, 34, 16, 30, 35, 16, 0);
				World.get().submit(new Task(1) {
					@Override
					protected void execute() {
						target.setNextGraphics(new Graphics(1348));
						target.ifPlayer(target -> {
							itemLoop: for (Item item : target.getInventory().getItems().getItems()) {
								if (item == null)
									continue;
								Food food = Food.forId(item.getId());
								if (food == null)
									continue;
								target.getInventory().deleteItem(item);
								break itemLoop;
							}
						});
						this.cancel();
					}
				});
				this.cancel();
			}
		});
		return true;
	}
}
