package com.rs.plugin.impl.objects.region;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {9584,9582}, name = {})
public class CraftingGuildRegionObjectPlugin extends ObjectListener{

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, 9584, "Climb-down", () -> player.setNextWorldTile(new WorldTile(2932, 3281, 0)));
		object.doAction(optionId, 9582, "Climb-up", () -> player.setNextWorldTile(new WorldTile(2933, 3282, 1)));
	}
}