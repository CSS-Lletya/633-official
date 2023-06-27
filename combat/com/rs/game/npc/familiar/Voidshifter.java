package com.rs.game.npc.familiar;

import java.util.Optional;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;

import skills.summoning.Summoning.Pouch;

public class Voidshifter extends Familiar {

	public Voidshifter(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (getOwner().getHitpoints() <= (getOwner().getMaxHitpoints() * .1) && !getOwner().isDead()) {
			getOwner().getPackets().sendGameMessage("Your void shifter sacrafices its life to protect you.");
			Magic.sendTeleportSpell((Player) getOwner(), 14388, -1, 1503, 1502, 0, 0, new WorldTile(2662, 2649, 0), 3,
					true, Magic.OBJECT_TELEPORT);
			sendDeath(Optional.of(getOwner()));
			return;
		}
	}

	@Override
	public String getSpecialName() {
		return "Call To Arms";
	}

	@Override
	public String getSpecialDescription() {
		return "Teleports the player to Void Outpost.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 4;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Magic.sendTeleportSpell((Player) object, 14388, -1, 1503, 1502, 0, 0, new WorldTile(2662, 2649, 0), 3, true,
				Magic.OBJECT_TELEPORT);
		return true;
	}
}
