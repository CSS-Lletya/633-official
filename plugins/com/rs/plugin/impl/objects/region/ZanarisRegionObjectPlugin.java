package com.rs.plugin.impl.objects.region;

import com.rs.GameConstants;
import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.FadingScreen;
import com.rs.game.player.content.doors.Doors;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { 24991, 12045, 52551, 52548, 33416, 50580, 52574}, name = {})
public class ZanarisRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, 12045, "open", () -> {
			if (!player.matches(new WorldTile(2465, 4433)) &&  !player.matches(new WorldTile(2470, 4437))) {
				if (player.getInventory().canRemove(ItemNames.DIAMOND_1601, 1)) {
					Doors.handleDoubleDoor(player, object);
				} else
					player.getPackets().sendGameMessage("You need a diamond to pass through.");// use real dialogue
																								// yourself kek
			} else
				Doors.handleDoubleDoor(player, object);
		});
		object.doAction(optionId, 24991, "enter",
				() -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		if (object.getId() == 52548) {
			if (player.getInventory().containsItem(1931, 1)) {
				if (player.getAttributes().get(Attribute.WHEAT_GRINDED).getBoolean()) {
					player.getAttributes().get(Attribute.WHEAT_GRINDED).set(false); 
					player.getAttributes().get(Attribute.WHEAT_DEPOSITED).set(false);
					player.getPackets().sendGameMessage("You take the ground flour.");
					player.getDetails().getStatistics().addStatistic("Flour_Taken");
					player.setNextAnimation(Animations.TWO_HANDED_GRAB);
					player.getInventory().deleteItem(1931, 1);
					player.getInventory().addItem(1933, 1);
					updateWheat(player);
				}
			} else
				player.getPackets().sendGameMessage("You need an empty pot to gather the flour.");
		}
		if (object.getId() == 33416)
			FadingScreen.fade(player, () -> player.setNextWorldTile(new WorldTile(2348, 4386)));
		if (object.getId() == 50580)
			FadingScreen.fade(player, () -> player.setNextWorldTile(new WorldTile(2400, 4379)));
		object.doAction(optionId, 52574, "smelt", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
	}
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (object.getId() == 52551 && item.getId() == 1947) {
			if (!player.getAttributes().get(Attribute.WHEAT_DEPOSITED).getBoolean()) {
				player.getInventory().deleteItem(1947, 1);
				player.setNextAnimation(new Animation(832));
				player.getPackets().sendGameMessage("You put the wheat in the hopper.");
				player.getAttributes().get(Attribute.WHEAT_DEPOSITED).set(true);
			} else
				player.getPackets().sendGameMessage("You already have wheat in deposited.");
		}
	}
	
	public void updateWheat(Player player) {
		player.getVarsManager().sendVar(695, (player.getAttributes().get(Attribute.WHEAT_GRINDED).getBoolean() ? 1 : 0)).submitVarToMap(695, (player.getAttributes().get(Attribute.WHEAT_GRINDED).getBoolean() ? 1 : 0));
	}
}