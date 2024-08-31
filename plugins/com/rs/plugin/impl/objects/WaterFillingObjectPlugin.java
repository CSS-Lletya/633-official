package com.rs.plugin.impl.objects;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.actions.FillAction;
import com.rs.game.player.actions.FillAction.Filler;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.SkillsDialogue;

@ObjectSignature(objectId = {}, name = { "Pump and drain", "Waterpump", "Water pump", "Fountain", "Sink", "Well", "Pump" })
public class WaterFillingObjectPlugin extends ObjectListener {

	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		Filler.VALUES.stream().filter(i -> item.getId() == i.getEmpty().getId())
		.forEach(fillable -> {
			player.dialogue(d -> 
				d.skillsMenu((input) -> player.getAction().setAction(new FillAction(SkillsDialogue.getQuantity(player))), fillable.getFilled())
			);
		});
	}
}