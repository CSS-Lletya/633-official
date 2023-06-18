package com.rs.plugin.impl.inventory;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {7509}, itemNames = {  })
public class RockCakeItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		 player.setNextAnimation(Animations.CONSUMING_ITEM);
		 int currentHP = player.getHitpoints();
         int damage = currentHP - 1;
         if (player.getHitpoints() > 1)
             player.applyHit(new Hit(player, damage, HitLook.REGULAR_DAMAGE));
         player.getPackets().sendGameMessage("The taste of the rock cake almost kills you.");
         player.getInventory().deleteItem(item);
         player.getAudioManager().sendSound(Sounds.EAT_ROCKCAKE);
	}
}