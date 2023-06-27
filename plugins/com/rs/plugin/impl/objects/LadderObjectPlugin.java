package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.magic.TeleportType;

@ObjectSignature(objectId = {29355, 29358, 55404, 26518, 32015, 36687, 1756, 52547, 52546}, name = {"Ladder"})
public class LadderObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, 36687, "climb-down", () -> player.getMovement().move(true, new WorldTile(3208, 9616, 0), TeleportType.BLANK));
		if (object.getId() == 29355) {
			if (object.matches(new WorldTile(3088, 9971))) //to wildy
				player.getMovement().move(true, new WorldTile(3087, 3571, 0), TeleportType.BLANK);
			if (object.matches(new WorldTile(3209, 9616)))
				player.getMovement().move(true, new WorldTile(3210, 3216, 0), TeleportType.BLANK);
			if (object.matches(new WorldTile(3097, 9867)))
				player.getMovement().move(true, new WorldTile(3096, 3468, 0), TeleportType.BLANK);
			if (object.matches(new WorldTile(3116, 9852)))
				player.getMovement().move(true, new WorldTile(3117, 3452, 0), TeleportType.BLANK);
		}
		if (object.getId() == 29358) {
			if (object.matches(new WorldTile(3088, 3571))) {//leave wildy
				//fix controllers on login to further test this.
				player.getMapZoneManager().executeVoid(player, zone -> zone.finish(player));
				player.getMovement().move(true, new WorldTile(3089, 9971, 0), TeleportType.BLANK);
			}
		}
		if (object.getId() == 55404) {//tav
			if (object.matches(new WorldTile(2884, 3397)))
				player.getMovement().move(true, new WorldTile(2884, 9798, 0), TeleportType.BLANK);
		}
		if (object.getId() == 26518 || object.getId() == 32015) {
			if(object.matches(new WorldTile(3084, 9672, 0)))//north
				player.getMovement().move(false, new WorldTile(3083, 3272, 0), TeleportType.LADDER);
			if(object.matches(new WorldTile(3118, 9643, 0)))//north
				player.getMovement().move(false, new WorldTile(3118, 3245, 0), TeleportType.LADDER);
			if(object.matches(new WorldTile(3084, 9672, 0)))//south
				player.getMovement().move(false, new WorldTile(3084, 3273, 0), TeleportType.LADDER);
			if(object.matches(new WorldTile(3103, 9576, 0)))//north
				player.getMovement().move(false, new WorldTile(3105, 3162, 0), TeleportType.LADDER);
			if (object.matches(new WorldTile(2884, 9797)))
				player.getMovement().move(true, new WorldTile(2884, 3398, 0), TeleportType.BLANK);
			if (object.matches(new WorldTile(2842, 9824)))
				player.getMovement().move(true, new WorldTile(2842, 3425, 0), TeleportType.BLANK);
		}
		if (object.getId() == 1756) {//camelot water obelisk
			if (object.matches(new WorldTile(2842, 3424)))
				player.getMovement().move(true, new WorldTile(2842, 9825, 0), TeleportType.BLANK);
		}
		if (object.getId() == 52546) {//tav
			player.getMovement().move(true, new WorldTile(2403, 4457, 1), TeleportType.LADDER);
		}
		if (object.getId() == 52547) {//tav
			player.getMovement().move(true, new WorldTile(2403, 4459, 0), TeleportType.BLANK);
		}
	}
}