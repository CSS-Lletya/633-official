package com.rs.plugin.impl.objects.region;

import com.rs.game.dialogue.impl.StairsLaddersDialogue;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.magic.TeleportType;
import skills.woodcutting.sawmill.Sawmill;

@ObjectSignature(objectId = {46034, 24367, 24359, 24360, 24350, 24352, 24353, 24361, 24362, 24349, 24354, 37116, 24355, 46307}, name = {})
public class VarrockRegionObjectPlugin extends ObjectListener{

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getId() == 46034) {
			if (player.getInventory().addItem(new Item(15287))) {
				player.getPackets().sendGameMessage("You take a leaflet from the table.");
				return;
			}
		}
		if (object.getId() == 24367) {
			if(object.getRotation() == 0)
				player.getMovement().move(false, new WorldTile(player.getX(), object.getY() + 3, player.getPlane() + 1), TeleportType.BLANK);
			else if (object.getRotation() == 1)
				player.getMovement().move(false, new WorldTile(player.getX() +4, object.getY(), player.getPlane() + 1), TeleportType.BLANK);
		}
		
		if (object.getId() == 24359 || object.getId() == 24360 || object.getId() == 35783) {
            if (object.matches(new WorldTile(3189, 3432))) {
            	player.getMovement().move(false,player.transform(2, 6400, 0), TeleportType.BLANK);
                return;
            }
            switch (object.getRotation()) {
                case 0:
                	player.getMovement().move(false, player.transform(0, -4, -1), TeleportType.BLANK);
                    break;
                case 1:
                	player.getMovement().move(false, player.transform(-4, 0, -1), TeleportType.BLANK);
                    break;
                case 2:
                	player.getMovement().move(false, player.transform(0, 4, -1), TeleportType.BLANK);
                    break;
                case 3:
                	player.getMovement().move(false, player.transform(4, 0, -1), TeleportType.BLANK);
                    break;
            }
        }
		if (object.getId() == 24350 || object.getId() == 24352 || object.getId() == 24353 || object.getId() == 24349) {
			new StairsLaddersDialogue(object).execute(player, optionId);
		}
		if (object.getId() == 24361) {
			player.getMovement().move(false, player.transform(2, 0, +1), TeleportType.BLANK);
		}
		if (object.getId() == 24362) {
			player.getMovement().move(false, player.transform(-2, 0, -1), TeleportType.BLANK);
		}
		if (object.getId() == 24354) {
			if (object.matches(new WorldTile(3268, 3379)))
				player.getMovement().move(false, player.transform(+2, 0, +1), TeleportType.BLANK);
			else if (object.matches(new WorldTile(3262, 3403)))
				player.getMovement().move(false, player.transform(0, 0, +1), TeleportType.BLANK);
			else if (object.matches(new WorldTile(3247, 3409)))
				player.getMovement().move(false, player.transform(+2, 0, +1), TeleportType.BLANK);
			else
				player.getMovement().move(false, player.transform(-2, 0, +1), TeleportType.BLANK);
		}
		if (object.getId() == 37116) {
			player.getMovement().move(false, player.transform(0, 0, -1), TeleportType.BLANK);
		}
		if (object.getId() == 24355) {
			player.getMovement().move(false, player.transform(0, 0, -1), TeleportType.BLANK);
		}
		if (object.getId() == 46307) {
			Sawmill.enter(player, object);
		}
	}
}