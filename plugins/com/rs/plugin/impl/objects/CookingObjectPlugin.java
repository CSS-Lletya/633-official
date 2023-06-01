package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.impl.CookingD;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.cooking.CookingData;
import skills.firemaking.FireLighter;

@ObjectSignature(objectId = {}, name = { "Fire", "Cooking range" })
public class CookingObjectPlugin extends ObjectListener {
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
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
		.forEach(cookable -> player.dialogBlank(new CookingD(player, new CookingData[] {cookable}, object, item.getId())));
	}
}