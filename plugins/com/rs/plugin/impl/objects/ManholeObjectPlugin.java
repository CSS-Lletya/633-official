package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.TeleportType;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Manhole"})
public class ManholeObjectPlugin extends ObjectListener {

	private GameObject interacting;

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, object.getId(), "open", () -> {
			interacting = new GameObject(object.getId() + 1, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane());
			player.faceObject(interacting);
			GameObject.spawnObjectTemporary(interacting, 60);
		});
		object.doAction(optionId, object.getId(), "close", () -> {
			interacting = new GameObject(getCloseId(object.getId()), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane());
			player.faceObject(interacting);
			GameObject.removeObject(interacting);
		});
		
		object.doAction(optionId, 882, "climb-down", () -> {
			//varock
			if(object.matches(new WorldTile(3237, 3458, 0)))
				player.getMovement().move(false, new WorldTile(3237, 9858, 0), TeleportType.BLANK);
		});
	}

	private int getCloseId(int objectId) {
		return objectId - 1;
	}
} 