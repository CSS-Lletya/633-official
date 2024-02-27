package com.rs.plugin.impl.objects.region;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.dialogue.impl.StairsLaddersDialogue;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.doors.Doors;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { 1749, 1750, 1759, 2554 }, name = {})
public class ArdougneRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		// Chaos Druid Tower
		object.doAction(optionId, 1750, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 1749, "climb-down",
				() -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 1759, "climb-down", () -> player.setNextWorldTile(new WorldTile(2562, 9755)));
		if (optionId == 1) {
			player.dialogue(d -> d.mes("The door is locked."));
			return;
		}
		object.doAction(optionId, 2554, "pick-lock", () -> {
			if (!player.getInventory().containsItem(ItemNames.LOCKPICK_1523, 1)) {
				player.dialogue(d -> d.mes("You're going to need something to pick the lock with."));
				return;
			}
			player.faceObject(object);
			LinkedTaskSequence seq = new LinkedTaskSequence();
			seq.connect(1, () -> player.setNextAnimation(Animations.LOCKPICKING));
			seq.connect(2, () -> Doors.handleClosedDoor(player, object)).start();
		});
	}
}