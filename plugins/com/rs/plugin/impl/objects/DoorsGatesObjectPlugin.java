package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.content.doors.Doors;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { }, name = { "Door", "Gate" })
public class DoorsGatesObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, "Gate", "Open", () -> Doors.handleSmallGate(player, object));
		object.doAction(optionId, "Gate", "Close", () -> Doors.handleSmallGate(player, object));
		
		object.doAction(optionId, "Door", "Open", () -> Doors.handleDoor(player, object));
		object.doAction(optionId, "Door", "Close", () -> Doors.handleClosedDoor(player, object));

	}
}