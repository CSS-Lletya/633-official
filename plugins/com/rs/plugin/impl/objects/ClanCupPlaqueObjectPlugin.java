package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Clan Cup plaque"})
public class ClanCupPlaqueObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.getPackets().sendOpenURL("https://runescape.fandom.com/wiki/Clan_Cup_plaque");
	}
}