package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtils;

@ObjectSignature(objectId = { 733, 1810, 11400, 33237 }, name = {})
public class WebSlashingObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		final boolean success = RandomUtils.random(2) == 1;
		player.getMovement().lock(2);
		player.setNextAnimation(Animations.KNIFE_SLASHING_WEB);
		if (success) {
			player.getPackets().sendGameMessage("You slash the web apart.");
			GameObject.spawnTempGroundObject(
					new GameObject((object.getId() == 27266 || object.getId() == 29354 ? 734 : object.getId() + 1),
							object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()),
					100);
		} else {
			player.getPackets().sendGameMessage("You fail to cut through it.");
		}
	}

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == 946) {
			player.setNextAnimation(Animations.KNIFE_SLASHING_WEB);
			final boolean success = RandomUtils.random(2) == 1;
			player.getMovement().lock(2);
			if (success) {
				player.getPackets().sendGameMessage("You slash the web apart.");
				GameObject.spawnTempGroundObject(new GameObject(
						(object.getId() == 27266 || object.getId() == 29354 ? 734 : object.getId() + 1),
						object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), 100);
			} else {
				player.getPackets().sendGameMessage("You fail to cut through it.");
			}
		}
	}
}