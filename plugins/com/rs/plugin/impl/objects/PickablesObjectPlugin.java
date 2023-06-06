package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Ticks;

@ObjectSignature(objectId = {}, name = {"Banana Tree", "Flax", "Onion", "Cabbage", "Wheat", "Potato", "Cadava bush", "Redberry bush"})
public class PickablesObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, "Banana Tree", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.BANANA_1963))) {
				player.setNextAnimation(Animations.TWO_HANDED_GRAB);
				player.getPackets().sendGameMessage("You pick a Banana from the Banana tree");
			}
		});
		object.doAction(optionId, "Flax", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.FLAX_1779))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				if (RandomUtils.percentageChance(18))
					GameObject.removeObjectTemporary(object, Ticks.fromSeconds(6));
			}
		});
		object.doAction(optionId, "Onion", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.ONION_1957))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
		object.doAction(optionId, "Cabbage", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.CABBAGE_1965))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
		object.doAction(optionId, "Wheat", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.GRAIN_1947))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
		object.doAction(optionId, "Potato", "Pick", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.POTATO_1942))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				GameObject.removeObjectTemporary(object, Ticks.fromSeconds(30));
			}
		});
		object.doAction(optionId, "Cadava bush", "Pick-from", () -> {
			if (object.getId() == 23627) {
				player.getPackets().sendGameMessage("There's nothing to pick from this bush.");
				return;
			}
			if (player.getInventory().addItem(new Item(ItemNames.CADAVA_BERRIES_753))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				if (RandomUtils.percentageChance(18))
					GameObject.spawnTempGroundObject(new GameObject(23627, 10, 0, object), Ticks.fromSeconds(6));
			}
		});
		object.doAction(optionId, "Cadava bush", "Pick-from", () -> {
			if (object.getId() == 23627) {
				player.getPackets().sendGameMessage("There's nothing to pick from this bush.");
				return;
			}
			if (player.getInventory().addItem(new Item(ItemNames.CADAVA_BERRIES_753))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				if (RandomUtils.percentageChance(18))
					GameObject.spawnTempGroundObject(new GameObject(23627, 10, 0, object), Ticks.fromSeconds(6));
			}
		});
		object.doAction(optionId, "Redberry bush", "Pick-from", () -> {
			if (object.getId() == 23630) {
				player.getPackets().sendGameMessage("There's nothing to pick from this bush.");
				return;
			}
			if (player.getInventory().addItem(new Item(ItemNames.REDBERRIES_1951))){
				player.setNextAnimation(Animations.TOUCH_GROUND);
				if (RandomUtils.percentageChance(18))
					GameObject.spawnTempGroundObject(new GameObject(23630, 10, 0, object), Ticks.fromSeconds(6));
			}
		});
	}
}