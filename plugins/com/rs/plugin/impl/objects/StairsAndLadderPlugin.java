package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.TeleportType;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

/**
 * For anything specific we can just declare the respective object ids and
 * create a event check.
 * 
 * @author Dennis
 *
 */
@ObjectSignature(objectId = {}, name = { "Staircase", "Ladder" })
public class StairsAndLadderPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return;
			player.dialog(new DialogueEventListener(player) {
				@Override
				public void start() {
					option("Go-Up", () -> {
						if (player.getPlane() == 3) {
							return;
						}
						player.getMovement().move(true,
								new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), TeleportType.BLANK);
					}, "Go-Down", () -> {
						player.getMovement().move(true,
								new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), TeleportType.BLANK);
					});
				}
			});
		} else if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return;
			player.getMovement().move(true, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), TeleportType.BLANK);
		} else if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return;
			player.getMovement().move(true, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), TeleportType.BLANK);
		}
	}
}