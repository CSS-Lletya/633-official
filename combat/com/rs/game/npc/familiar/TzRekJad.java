package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;

/**
 * Represents the TzRek-Jad pet.
 * 
 * @author Emperor
 * 
 */
public final class TzRekJad extends Familiar {

	/**
	 * Constructs a new {@code TzRekJad} {@code Object}.
	 * 
	 * @param owner The owner.
	 * @param tile  The world tile to spawn on.
	 */
	public TzRekJad(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "null";
	}

	@Override
	public String getSpecialDescription() {
		return "null";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 0;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return null;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}

}