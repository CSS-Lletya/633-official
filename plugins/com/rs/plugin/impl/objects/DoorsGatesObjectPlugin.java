package com.rs.plugin.impl.objects;

import java.util.stream.IntStream;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.content.doors.Doors;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { 47, 48, 166, 167, 1551, 1552, 1553, 1556, 2050, 2051, 2306, 2313, 2320, 2344, 2261, 2262,
		2438, 2439, 3015, 3016, 3725, 3726, 4311, 4312, 7049, 7050, 7051, 7052, 15510, 15511, 15512, 15513, 15514,
		15515, 15516, 15517, 24560, 24561, 34777, 34778, 34779, 34780, 36912, 36913, 36914, 36915, 37351, 37352, 37353,
		37354, 45206, 45207, 45208, 45209, 45210, 45211, 45212, 45213, 33060, 31828 }, name = {"Door", "Gate" })
public class DoorsGatesObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (IntStream
				.of(47, 48, 166, 167, 1551, 1552, 1553, 1556, 2050, 2051, 2306, 2313, 2320, 2344, 2261, 2262, 2438,
						2439, 3015, 3016, 3725, 3726, 4311, 4312, 7049, 7050, 7051, 7052, 15510, 15511, 15512, 15513,
						15514, 15515, 15516, 15517, 24560, 24561, 34777, 34778, 34779, 34780, 36912, 36913, 36914,
						36915, 37351, 37352, 37353, 37354, 45206, 45207, 45208, 45209, 45210, 45211, 45212, 45213)
				.anyMatch(d -> object.getId() == d)) {
			Doors.handleGate(player, object);
		}
		if (object.getId() == 33060 || object.getId() == 31828) {
			Doors.handleDoor(player, object);
		}
		object.doAction(optionId, "Gate", "Open", () -> Doors.handleSmallGate(player, object));
		object.doAction(optionId, "Gate", "Close", () -> Doors.handleSmallGate(player, object));

		object.doAction(optionId, "Door", "Open", () -> Doors.handleDoor(player, object));
		object.doAction(optionId, "Door", "Close", () -> Doors.handleClosedDoor(player, object));

	}
}