package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.impl.ItemFillingD;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.actions.FillAction;
import com.rs.game.player.actions.FillAction.Filler;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Waterpump", "Water pump", "Fountain", "Sink", "Well", "Pump" })
public class WaterFillingObjectPlugin extends ObjectListener {

	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		Filler fill = FillAction.isFillable(item);
		if (fill != null)
			player.dialogBlank(new ItemFillingD(player, fill));
	}
}