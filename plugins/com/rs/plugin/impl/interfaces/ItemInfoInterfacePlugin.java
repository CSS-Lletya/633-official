package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.Shop;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 449 })
public class ItemInfoInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		Shop shop = (Shop) player.getAttributes().get(Attribute.SHOP).get();
		shop.sendInventory(player);
	}
}