package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Ticks;

@ObjectSignature(objectId = {}, name = {"Banana Tree", "Flax", "Onion", "Cabbage", "Wheat", "Potato"})
public class PickablesObjectPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, "Banana Tree", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.BANANA_1963))) {
				player.setNextAnimation(Animations.GRABBING_INFRONT_OF_YOU);
				player.getPackets().sendGameMessage("You pick a Banana from the Banana tree");
			}
		});
		object.doAction(optionId, "Flax", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.FLAX_1779))){
				player.setNextAnimation(Animations.DIG);
				if (RandomUtils.percentageChance(18))
					GameObject.removeObjectTemporary(object, Ticks.fromSeconds(6));
			}
		});
		object.doAction(optionId, "Onion", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.ONION_1957))){
				player.setNextAnimation(Animations.DIG);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
		object.doAction(optionId, "Cabbage", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.CABBAGE_1965))){
				player.setNextAnimation(Animations.DIG);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
		object.doAction(optionId, "Wheat", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.GRAIN_1947))){
				player.setNextAnimation(Animations.DIG);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
		object.doAction(optionId, "Potato", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.POTATO_1942))){
				player.setNextAnimation(Animations.DIG);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
	}
}