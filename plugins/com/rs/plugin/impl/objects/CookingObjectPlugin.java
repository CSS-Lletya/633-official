package com.rs.plugin.impl.objects;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.SkillDialogueFeedback;

import skills.SkillsDialogue;
import skills.cooking.Cooking;
import skills.cooking.CookingData;
import skills.cooking.SodaAshBurning;
import skills.firemaking.FireLighter;

@ObjectSignature(objectId = {}, name = {"Fire", "Range", "Campfire", "Oven", "Cooking range", "Stove", "Clay oven", "Clay Oven", "Fireplace"})
public class CookingObjectPlugin extends ObjectListener {
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == ItemNames.SEAWEED_401) {
			new SodaAshBurning(player).start();
			return;
		}
		if (object.getDefinitions().getName().equalsIgnoreCase("Fire")){
			FireLighter.VALUES.stream().filter(i -> item.getId() == i.getItem())
			.forEach(fire -> {
				player.getInventory().deleteItem(new Item(fire.getItem()));
				object.setId(fire.getObjectId());
				World.getRegion(player.getRegionId()).refreshSpawnedObjects(player);
			});
			return;
		}
		CookingData.VALUES.stream().filter(raw -> raw.getRawId() == item.getId())
		.forEach(cookable -> {
			player.dialogue(d -> {
				d.skillsMenu(item);
				d.skillDialogue(new SkillDialogueFeedback() {
					@Override
					public void handle(int button) {
						new Cooking(player, object, cookable, true, SkillsDialogue.getQuantity(player)).start();
					}
				});
			});
		});
	}
}