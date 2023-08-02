package com.rs.game.player.content;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtility;

public class ChrismasCracker {

	/**
	 * The party hat items.
	 */
	private static final Item[] HATS = new Item[] {new Item(1038), new Item(1040), new Item(1042), new Item(1044), new Item(1046), new Item(1048)};
	
	final static Item[] EXTRA_ITEMS = { new Item(1969, 1), new Item(2355, RandomUtility.random(1, 2)), new Item(1217, 1),
			new Item(1635, 1), new Item(441, 5), new Item(441, 10), new Item(1973, 1), new Item(1718, 1),
			new Item(950, 1), new Item(563, 1), new Item(1987, 1) };
	
	public static void splitCracker(Player player, Player target, Item cracker) {
		if (target.isFinished() || player.isFinished())
			return;
		if (cracker == null)
			return;
		if (target.getInventory().getFreeSlots() == 0) {
			player.getPackets().sendGameMessage("The other player doesn't have enough inventory space.");
			return;
		}
		if (player.getInventory().removeItems(cracker)) {
			player.faceEntity(target);
			target.faceEntity(player);
			player.getMovement().lock(3);
			player.setNextAnimation(Animations.SIMPLE_GRAB);//cant seem to find the real ID, nothing works better.
			target.setNextAnimation(Animations.SIMPLE_GRAB);
			player.setNextGraphics(new Graphics(176, 0, 95));
			player.getPackets().sendGameMessage("You pull a Christmas cracker...");
			boolean winner = RandomUtility.random(2) == 1;
			player.getPackets().sendGameMessage(winner ? "You get the prize from the cracker." : "The person you pull the cracker with gets the prize.");
			if (winner) {
				player.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
			} else {
				target.setNextForceTalk(new ForceTalk("Hey! I got the cracker!"));
			}
			Item hat = RandomUtility.random(HATS);
			if (winner) {
				player.getInventory().addItem(hat);
			} else {
				target.getInventory().addItem(hat);
			}
			if (!winner) {
				player.getInventory().addItem(RandomUtility.random(EXTRA_ITEMS));
			}
		}
	}
}