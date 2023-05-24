package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"crate", "crates", "boxes", "bookcase", "drawers", "closed chest", "open chest"})
public class SearchablesObjectPlugin extends ObjectListener {

	GameObject interacting;
	
	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		interacting = object;
		object.doAction(optionId, object.getId(), "search", () -> player.getPackets().sendGameMessage(
                "You search the " + object.getDefinitions().getName().toLowerCase() + " but find nothing."));
		
		if (object.getDefinitions().getNameContaining("drawers")) {
			object.doAction(optionId, object.getId(), "open", () -> {
				interacting = new GameObject(getOpenId(object.getId()), object.getType(),
                        object.getRotation(), object.getX(), object.getY(), object.getPlane());
                player.faceObject(interacting);
                player.getMovement().lock(2);
                GameObject.spawnObjectTemporary(interacting, 60);
                player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
			object.doAction(optionId, object.getId(), "close", () -> {
				interacting = new GameObject(getCloseId(object.getId()), object.getType(), object.getRotation(),
						object.getX(), object.getY(), object.getPlane());
				player.faceObject(interacting);
                player.getMovement().lock(2);
				GameObject.removeObject(interacting);
				player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
		}
		if (object.getDefinitions().getNameContaining("closed chest") || object.getDefinitions().getNameContaining("open chest")) {
			object.doAction(optionId, object.getId(), "open", () -> {
				interacting = new GameObject(getOpenId(object.getId()), object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane());
                player.faceObject(interacting);
                player.getMovement().lock(2);
                GameObject.spawnObjectTemporary(interacting, 60);
                player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
			object.doAction(optionId, object.getId(), "shut", () -> {
				interacting = new GameObject(getCloseId(object.getId()), object.getType(), object.getRotation(),
						object.getX(), object.getY(), object.getPlane());
				player.faceObject(interacting);
                player.getMovement().lock(2);
				GameObject.removeObject(interacting);
				player.setNextAnimation(Animations.SIMPLE_GRAB);
			});
		}
	}
	
    private int getOpenId(int objectId) {
        return objectId + 1;
    }
    
    private int getCloseId(int objectId) {
        return objectId - 2;
    }
}