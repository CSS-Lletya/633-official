package com.rs.game.npc.familiar;

import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.player.PlayerCombat;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.utilities.RandomUtils;

public class Forgeregent extends Familiar {

	public Forgeregent(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Inferno";
	}

	@Override
	public String getSpecialDescription() {
		return "A magical attack that disarms an enemy's weapon or shield.";
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
		setNextAnimation(new Animation(7871));
		setNextGraphics(new Graphics(1394));
		World.get().submit(new Task(2) {
			@Override
			protected void execute() {
				target.ifPlayer(player -> {
					int weaponId = player.getEquipment().getWeaponId();
					if (weaponId != -1) {
						if (PlayerCombat.getWeaponAttackEmote(weaponId, 1) != 423) {
							RSInterfacePluginDispatcher.sendRemove(player, 3);
						}
					}
					int shieldId = player.getEquipment().getShieldId();
					if (shieldId != -1) {
						RSInterfacePluginDispatcher.sendRemove(player, 5);
					}
					target.setNextGraphics(new Graphics(1393));
					target.applyHit(new Hit(getOwner(), RandomUtils.inclusive(200), HitLook.MELEE_DAMAGE));
				});
				this.cancel();
			}
		});
		return true;
	}
}
