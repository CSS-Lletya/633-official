package com.rs.game.player.actions;

import com.rs.game.player.Player;
import com.rs.game.player.content.Emotes.Emote;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtility;

public class Rest extends Action {

	private final int restingType;
	
	public Rest(int type) {
		this.restingType = type;
	}

	private static int[][] REST_DEFS = { { 5713, 1549, 5748 },
			{ 11786, 1550, 11788 }, { 5713, 1551, 2921 }
	};

	private int index;

	@Override
	public boolean start(Player player) {
		if (!process(player))
			return false;
		index = RandomUtility.inclusive(REST_DEFS.length -1);
		player.setResting((byte) 1);
		if (restingType == 4)
			player.getVarsManager().sendVar(173, 4);
		else
			player.getInterfaceManager().sendRunButtonConfig();
		player.setNextAnimation(new Animation(REST_DEFS[index][0]));
		player.getAppearance().setRenderEmote(REST_DEFS[index][1]);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (player.getPoisonDamage().get() > 0) {
			player.getPackets().sendGameMessage(
					"You can't rest while you're poisoned.");
			return false;
		}
		if (player.getCombatDefinitions().isUnderCombat()) {
			player.getPackets().sendGameMessage(
					"You can't rest until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (player.getDetails().getRunEnergy() == 100)
            return 0;
        if (player.getDetails().getRunEnergy() > 98)
        	player.getDetails().setRunEnergy(100);
        else if (player.getDetails().getRunEnergy() <= 98)
        	player.getDetails().setRunEnergy(player.getDetails().getRunEnergy() + 1);
		return 2;
	}

	//this apparently gets ignored entirely.
	@Override
	public void stop(Player player) {
		Emote.setNextEmoteEnd(player);
		player.setResting((byte) 0);
		player.getInterfaceManager().sendRunButtonConfig();
		player.getAppearance().setRenderEmote((short) -1);
		player.setNextAnimation(new Animation(REST_DEFS[index][2]));
	}
}