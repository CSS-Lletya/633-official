package com.rs.game.player.actions;

import java.util.Optional;

import com.rs.game.player.Player;
import com.rs.game.player.content.Emotes.Emote;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;

public class Rest extends Action {

	private final int restingType;
	
	public Rest(Player player, int type) {
		super(player, Optional.empty());
		this.restingType = type;
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
		if (restingType == 4)
			getPlayer().getVarsManager().sendVar(173, 4);
		else
			getPlayer().getInterfaceManager().sendRunButtonConfig();
		getPlayer().setNextAnimation(new Animation(REST_DEFS[index][0]));
		getPlayer().getAppearance().setRenderEmote(REST_DEFS[index][1]);
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
		if (getPlayer().getDetails().getRunEnergy() == 100)
            return 0;
        if (getPlayer().getDetails().getRunEnergy() > 98)
        	getPlayer().getDetails().setRunEnergy(100);
        else if (getPlayer().getDetails().getRunEnergy() <= 98)
        	getPlayer().getDetails().setRunEnergy(getPlayer().getDetails().getRunEnergy() + 1);
		return 2;
	}

	//this apparently gets ignored entirely.
	@Override
	public void stop() {
		Emote.setNextEmoteEnd(getPlayer());
		getPlayer().setResting((byte) 0);
		getPlayer().getInterfaceManager().sendRunButtonConfig();
		getPlayer().getAppearance().setRenderEmote((short) -1);
		getPlayer().setNextAnimation(new Animation(REST_DEFS[index][2]));
	}
}