package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.traveling.Canoes;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {12163, 12164, 12165, 12166}, name = {"Canoe station"})
public class CanoeStationObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (player.getAttributes().get(Attribute.CANOE_SHAPED) != null
				&& player.getAttributes().get(Attribute.CANOE_SHAPED).getBoolean())
			Canoes.openTravelInterface(player, object.getId() - 12163);
		else if (player.getAttributes().get(Attribute.CANOE_CHOPPED) != null
				&& player.getAttributes().get(Attribute.CANOE_CHOPPED).getBoolean())
			Canoes.openSelectionInterface(player);
		else
			Canoes.chopCanoeTree(player, object.getId() - 12163);
	}
}