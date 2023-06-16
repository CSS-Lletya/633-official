package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {2492}, name = {})
public class SingleTilePortalObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		switch (player.getDetails().getEssenceTeleporter()) {
		case 553:
			player.setNextWorldTile(new WorldTile(3253, 3401, 0));
			break;
		case 300:
			player.setNextWorldTile(new WorldTile(3107, 9573, 0));
			break;
		case 2328:
			player.setNextWorldTile(new WorldTile(2682, 3323, 0));
			break;
		default:
			player.setNextWorldTile(new WorldTile(3253, 3401, 0));
			break;
		}
	}
}