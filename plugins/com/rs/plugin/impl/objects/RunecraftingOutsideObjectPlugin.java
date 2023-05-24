package com.rs.plugin.impl.objects;

import java.util.Arrays;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.content.TeleportType;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.runecrafting.Altar;

@ObjectSignature(objectId = { 2452, 2453, 2454, 2455, 2456, 2457, 2458, 2459, 2460, 2461, 2462, 2464}, name = {})
public class RunecraftingOutsideObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		Arrays.stream(Altar.values()).filter(altar -> altar.getOutisdeObject() == object.getId())
		.forEach(altar -> {
			player.getMovement().move(true, altar.getWorldTile(), TeleportType.BLANK);
			player.getPackets().sendGameMessage("A mysterious force grabs hold of you.");
		});
	}
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		Arrays.stream(Altar.values()).filter(altar -> altar.getTalisman() == item.getId() && altar.getOutisdeObject() == object.getId())
		.forEach(altar -> {
			player.getMovement().move(true, altar.getWorldTile(), TeleportType.BLANK);
			player.getPackets().sendGameMessage("A mysterious force grabs hold of you.");
		});
	}
}