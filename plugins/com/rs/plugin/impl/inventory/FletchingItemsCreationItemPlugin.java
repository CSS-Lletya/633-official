package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.fletching.ArrowCreation;
import skills.fletching.ArrowCreation.HeadlessArrowCreation;
import skills.fletching.BoltCreation;
import skills.fletching.BowStringing;
import skills.fletching.CrossbowLimbing;
import skills.fletching.CrossbowStringing;
import skills.fletching.DartCreation;

@InventoryWrapper(itemId = {
		//arrows
		39,882, 2865, 2866, 40,884,41,886, 42, 888, 13278, 4160,43,890,44,892,11237, 52,314, 12539, 52,
		//bolts
		9375, 314,877,45,9376,314,9139, 9187,9377,9140, 46,9382,9141,9188,877,47,9379,9142,9189,9142,9190,9380,9143,9191,9143,9192,9381,
		9144,9194,
		//bow stringing
		1777, 50, 48, 54, 56, 4825, 60, 58,64,62,68,66,72,70,
		//crossbow limbing
		9440, 9420,9442,9422,9444,9423, 9446,9425,9448,9427,9450,9429,9452,9431,
		//darts
		819,820,821,822,823, 824, 11232,
		//crossbow stringing
		9438, 9454,9456,9457,9459,9461,9463,9465,
}, itemNames = {  })
public class FletchingItemsCreationItemPlugin extends InventoryListener {
	
	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		if(ArrowCreation.fletch(player, firstItem, secondItem)) {
			return;
		}
		if(HeadlessArrowCreation.fletchArrowShaft(player, firstItem, secondItem)) {
			return;
		}
		if(HeadlessArrowCreation.fletchGrenwallSpikes(player, firstItem, secondItem)) {
			return;
		}
		if(BoltCreation.fletch(player, firstItem, secondItem)) {
			return;
		}
		if(BowStringing.string(player, firstItem, secondItem)) {
			return;
		}
		if(CrossbowLimbing.construct(player, firstItem, secondItem)) {
			return;
		}
		if (DartCreation.create(player, firstItem, secondItem)) {
			return;
		}
		if(CrossbowStringing.string(player, firstItem, secondItem)) {
			return;
		}
	}
}