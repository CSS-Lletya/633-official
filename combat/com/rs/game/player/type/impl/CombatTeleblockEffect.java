package com.rs.game.player.type.impl;

import com.rs.game.Entity;
import com.rs.game.player.Player;
import com.rs.game.player.type.CombatEffect;

/**
 * The combat effect applied when a player needs to be teleblocked.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatTeleblockEffect extends CombatEffect {
	
	/**
	 * Creates a new {@link CombatTeleblockEffect}.
	 */
	public CombatTeleblockEffect() {
		super(50);
	}
	
	@Override
	public boolean apply(Entity c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.getDetails().getTeleBlockDelay().get() > 0) {
				return false;
			}
			player.getDetails().getTeleBlockDelay().set((player.getPrayer().usingPrayer(0, 17)
					|| player.getPrayer().usingPrayer(1, 7) ? 3000 / 2 : 3000));
			player.getPackets().sendGameMessage("You have just been teleblocked!");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeOn(Entity c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.getDetails().getTeleBlockDelay().get() <= 0) {
				player.getPackets().sendGameMessage("You feel the effects of the teleblock spell go away.");
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void process(Entity c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			player.getDetails().getTeleBlockDelay().decrementAndGet(50, 0);
		}
	}
	
	@Override
	public boolean onLogin(Entity c) {
		if(c.isPlayer()) {
			Player player = (Player) c;
			if(player.getDetails().getTeleBlockDelay().get() > 0)
				return true;
		}
		return false;
	}
}
