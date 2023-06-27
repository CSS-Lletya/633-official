package com.rs.game.player.type.impl;

import com.rs.game.Entity;
import com.rs.game.player.Player;
import com.rs.game.player.type.CombatEffect;

/**
 * The combat effect applied when a player needs to be skulled.
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSkullEffect extends CombatEffect {
	
	/**
	 * Creates a new {@link CombatSkullEffect}.
	 */
	public CombatSkullEffect() {
		super(50);
	}
	
	@Override
	public boolean apply(Entity entity) {
		if(entity.isPlayer()) {
			Player player = (Player) entity;
			if(player.getDetails().getSkullTimer().get() > 0) {
				return false;
			}
			player.getDetails().getSkullTimer().set(3000);
			player.getDetails().getSkullId().set(WHITE_SKULL);
			player.getDetails().getStatistics().addStatistic("Skulled");
			player.getAppearance().getAppeareanceData();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeOn(Entity entity) {
		if(entity.isPlayer()) {
			Player player = (Player) entity;
			if(player.getDetails().getSkullTimer().get() <= 0) {
				player.getDetails().getSkullId().set(NO_SKULL);
				player.getAppearance().getAppeareanceData();
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void process(Entity entity) {
		if(entity.isPlayer()) {
			Player player = (Player) entity;
			player.getDetails().getSkullTimer().decrementAndGet(50, 0);
		}
	}
	
	@Override
	public boolean onLogin(Entity entity) {
		if(entity.isPlayer()) {
			Player player = (Player) entity;
			if(player.getDetails().getSkullTimer().get() > 0) {
				player.getDetails().getSkullId().set(WHITE_SKULL);
				return true;
			}
		}
		return false;
	}
	
	private final byte WHITE_SKULL = 0, NO_SKULL = -1;
}