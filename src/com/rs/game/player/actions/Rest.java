package com.rs.game.player.actions;

import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;

public class Rest extends Action {

	public Rest(Player player, Optional<Entity> entity) {
		super(player, Optional.empty());
	}

	private static int[][] REST_DEFS = { { 5713, 1549, 5748 },
			{ 11786, 1550, 11788 }, { 5713, 1551, 2921 }
	};

	private int index;

	@Override
	public boolean start() {
		if (!process())
			return false;
		index = RandomUtils.inclusive(REST_DEFS.length -1);
		getPlayer().setResting((byte) 1);
		getPlayer().getInterfaceManager().sendRunButtonConfig();
		getPlayer().setNextAnimation(new Animation(REST_DEFS[index][0]));
		getPlayer().getAppearance().setRenderEmote((short) REST_DEFS[index][1]);
		return true;
	}

	@Override
	public boolean process() {
		if (getPlayer().getPoisonDamage().get() > 0) {
			getPlayer().getPackets().sendGameMessage(
					"You can't rest while you're poisoned.");
			return false;
		}
		if (getPlayer().getCombatDefinitions().isUnderCombat()) {
			getPlayer().getPackets().sendGameMessage(
					"You can't rest until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay() {
		return 0;
	}

	@Override
	public void stop() {
		getPlayer().setResting((byte) 0);
		getPlayer().getInterfaceManager().sendRunButtonConfig();
		getPlayer().setNextAnimation(new Animation(REST_DEFS[index][2]));
		getPlayer().getAppearance().setRenderEmote((short) -1);
	}
}