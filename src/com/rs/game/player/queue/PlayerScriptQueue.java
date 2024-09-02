package com.rs.game.player.queue;

import com.rs.game.player.Player;

public class PlayerScriptQueue extends RSScriptQueue<Player> {

	public PlayerScriptQueue(Player owner) {
		super(owner);
	}

	@Override
	public boolean hasNonModalInterfaces() {
		return owner.getInterfaceManager().containsScreenInter();
	}

	@Override
	public void closeNonModalInterfaces() {
		owner.getInterfaceManager().closeInterfaces();//unsure
	}

}
