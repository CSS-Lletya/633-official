package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"crate", "crates", "boxes", "bookcase", "drawers"})
public class SearchablesObjectPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getDefinitions().getNameContaining("drawers")) {
			if (object.getDefinitions().containsOption("open")) {
                GameObject openedDrawer = new GameObject(getOpenId(object.getId()), object.getType(),
                        object.getRotation(), object.getX(), object.getY(), object.getPlane());
                player.faceObject(openedDrawer);
                GameObject.spawnObjectTemporary(openedDrawer, 60);
                player.setNextAnimation(Animations.OPENING_INFRONT_OF_YOU);
            }
            if (object.getDefinitions().containsOption("search")) {
                player.getPackets().sendGameMessage("You search the drawers but find nothing.");
                return;
            }
		}
		if (object.getDefinitions().containsOption("search"))
			player.getPackets().sendGameMessage(
                    "You search the " + object.getDefinitions().getName().toLowerCase() + " but find nothing.");
	}
	
    private static int getOpenId(int objectId) {
        return objectId + 1;
    }
}