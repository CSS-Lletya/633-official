package com.rs.plugin.impl.objects;

import com.rs.GameConstants;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Small obelisk"})
public class SmallSummoningObeliskObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT);
	}
}