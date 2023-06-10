package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Fungi on log" })
public class FungiLogsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (player.getInventory().addItem(new Item(2970))) {
			player.getMovement().lock(2);
			player.getPackets().sendGameMessage("You carefully pick the Fungi off of the log.");
			player.getAudioManager().sendSound(Sounds.BURY_OR_PICK);
			player.setNextAnimation(Animations.TOUCH_GROUND);
		}
	}
}