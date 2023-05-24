package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"crate", "crates", "boxes", "bookcase", "drawers", "closed chest", "open chest"})
public class SearchablesObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getDefinitions().getNameContaining("drawers")) {
			if (object.getDefinitions().containsOption("open")) {
                GameObject openedDrawer = new GameObject(getOpenId(object.getId()), object.getType(),
                        object.getRotation(), object.getX(), object.getY(), object.getPlane());
                player.faceObject(openedDrawer);
                player.getMovement().lock(2);
                GameObject.spawnObjectTemporary(openedDrawer, 60);
                player.setNextAnimation(Animations.OPENING_INFRONT_OF_YOU);
            }
            if (object.getDefinitions().containsOption("search")) {
                player.getPackets().sendGameMessage("You search the drawers but find nothing.");
                return;
            }
		}
		if (object.getDefinitions().getNameContaining("closed chest")) {
			if (object.getDefinitions().containsOption("open")) {
				GameObject openedChest = new GameObject(getOpenId(object.getId()), object.getType(),
                        object.getRotation(), object.getX(), object.getY(), object.getPlane());
                player.faceObject(openedChest);
                player.getMovement().lock(2);
                GameObject.spawnObjectTemporary(openedChest, 60);
                player.setNextAnimation(Animations.OPENING_INFRONT_OF_YOU);
			}
		}
		if (object.getDefinitions().containsOption("search"))
			player.getPackets().sendGameMessage(
                    "You search the " + object.getDefinitions().getName().toLowerCase() + " but find nothing.");
	}
	
    private int getOpenId(int objectId) {
        return objectId + 1;
    }
}