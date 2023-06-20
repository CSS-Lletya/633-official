package com.rs.plugin.impl.inventory;

import java.util.Arrays;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.herblore.FinishedPotion;
import skills.herblore.Grinding;
import skills.herblore.Herb;
import skills.herblore.Herb.GrimyHerb;
import skills.herblore.UnfinishedPotion;

@InventoryWrapper(itemId = {
		//unfinished potions
		227, 249, 253,251,255,257,2998,12172,259,14854,261,263,3000,265,2481,267,269,21624,
		//finished potions
		91,221,109,245,2483,3138,95,225,99,239,93, 235,97,223,1975,3002,2152,9736,231,12181,12109,14856,5004,101,1871,235,103,231,2970,10109,
		105,225,14856,11525,105,241,3004,223,3024, 235,1523,10937,107,2483,241,111,247,3002,6693,3018,5972,2454,4621,145,261,157,267,163,2481,
		3042,9594,169,12539,139,4255,269,15309,15313,15317,15321,15325,
		//grinding
		237,1973,5075,10109,243,14703,9735,6466,4698,592, 401,403, 530, 973, 341,7516,7418,11156,9079,3263,1550,4620,
		//tar creations - moved to: PestleAndMorterItemPlugin
		//herb cleaning
		199,1533,1525,201,203,205,207,3049,209,211,213,3051,215,2485,217,219
}, itemNames = {  })
public class HerbloreItemsCreationItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		if (option == 6 && item.getId() == 227) {
			player.getInventory().replaceItems(item, new Item(229));
			return;
		}
		Arrays.stream(GrimyHerb.values())
	    .filter(herb -> item.getId() == herb.grimy.getId())
	    .map(herb -> new Herb(player, herb))
	    .forEach(Herb::start);

	}
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		if(UnfinishedPotion.produce(player, firstItem, secondItem)) {
			return;
		}
		if(Grinding.produce(player, firstItem, secondItem)) {
			return;
		}
		if(FinishedPotion.produce(player, firstItem, secondItem)) {
			return;
		}
	}
}