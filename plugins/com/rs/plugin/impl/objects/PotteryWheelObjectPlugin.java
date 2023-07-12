package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.impl.PotteryWheelD;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.crafting.PotteryWheel.PotteryWheelData;

@ObjectSignature(objectId = {}, name = { "Potter's Wheel" })
public class PotteryWheelObjectPlugin extends ObjectListener {
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == 1761) {
			player.faceObject(object);
			player.dialogueBlank(new PotteryWheelD(player, PotteryWheelData.values()));
		}
	}
}