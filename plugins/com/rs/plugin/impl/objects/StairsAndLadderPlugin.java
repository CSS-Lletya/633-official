package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.TeleportType;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = {"Staircase", "Ladder"})
public class StairsAndLadderPlugin implements ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		//if special else do this
		if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb"))
			player.dialog(new DialogueEventListener(player) {
				@Override
				public void start() {
					option("Go-Up", () -> {
						player.getMovement().move(true,new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), TeleportType.BLANK);
					},
					"Go-Down", () -> {
						player.getMovement().move(true,new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), TeleportType.BLANK);
					});
				}
			});
		else if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-up"))
			player.getMovement().move(true, new WorldTile(player.getX(), player.getY(), player.getPlane() + 1), TeleportType.BLANK);
		else if (object.getDefinitions().getOption(optionId).equalsIgnoreCase("Climb-down"))
			player.getMovement().move(true, new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), TeleportType.BLANK);
	}
}