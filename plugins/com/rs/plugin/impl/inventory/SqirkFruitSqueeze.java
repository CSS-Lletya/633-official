package com.rs.plugin.impl.inventory;

import com.rs.game.dialogue.Expression;
import com.rs.game.item.Item;
import com.rs.game.item.UseWith;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.Skills;
import skills.thieving.impl.sorceresssgarden.SqirkFruitSqueeze.SqirkFruit;

@InventoryWrapper(itemId = {233, 10844, 10845, 10846, 10847}, itemNames = {  })
public class SqirkFruitSqueeze extends InventoryListener {

	public static final int BEER_GLASS = 1919;
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
        for (SqirkFruit fruit : SqirkFruit.values()) {
        	new UseWith(new Item(233), new Item(fruit.getFruitId())).execute(firstItem, secondItem, () -> {
        		 if (!player.getInventory().containsAny(BEER_GLASS)) {
        			 	player.dialogue(d -> d.player(Expression.sad, "I should get an empty beer glass to hold the juice"," before I squeeze the fruit."));
        	        } else if (!player.getInventory().containsAny(233)) {
        	        	player.dialogue(d -> d.player(Expression.sad, "I should get a pestle and mortal before I ","squeeze the fruit."));
        	        } else if (!player.getInventory().containsItem(fruit.getFruitId(), fruit.getAmtRequired()))
        	        	player.dialogue(d -> d.player(Expression.sad, "I think I should wait until I have enough "," fruit to make a full glass."));
        	        else {
        	            player.getInventory().deleteItem(fruit.getFruitId(), fruit.getAmtRequired());
        	            player.getInventory().deleteItem(BEER_GLASS, 1);
        	            player.getInventory().addItem(fruit.getResultId(), 1);
        	            player.getSkills().addExperience(Skills.HERBLORE, 5);
        	            player.dialogue(d -> d.item(fruit.getResultId(), "You squeeze " + fruit.getAmtRequired() + " sq'irks into an empty glass."));
        	        }
        	});
        }
	}
}