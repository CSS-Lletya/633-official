package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.Mood;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtility;

@ObjectSignature(objectId = {}, name = {"Hay bales", "Haystack"})
public class HaystackObjectPlugin extends ObjectListener {

	/**
	 * Represents the needle to give.
	 */
	private static final Item NEEDLE = new Item(1733);
	
	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		final int rand = RandomUtility.random(50);
		player.getMovement().lock(2);
		player.setNextAnimation(Animations.TOUCH_GROUND);
		player.getPackets().sendGameMessage("You search the " + object.getDefinitions().getName().toLowerCase() + "...");
		if (rand == 1 && player.getInventory().getFreeSlots() > 0 || player.getInventory().containsItem(NEEDLE)) {
			player.getInventory().addItem(NEEDLE);
			player.dialogue(new DialogueEventListener(player) {
				@Override
				public void start() {
					player(Mood.happy_plain_eyebrows_up, "Wow! A needle!", "Now what are the chances of finding that?");
				}
			});
			return;
		}
		player.getPackets().sendGameMessage("You find nothing of interest.");
	}
}