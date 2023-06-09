package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.Shop;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 620, 621 })
public class ShopInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		Shop shop = (Shop) player.getAttributes().get(Attribute.SHOP).get();
		if (shop == null)
            return;
		if (interfaceId == 620) {
			if (componentId == 25) {
				if (slotId > 0)
					slotId /= 6;
				switch(packetId) {
				case 11:
					shop.sendInfo(player, slotId, false);
					break;
				case 29:
					shop.buy(player, slotId, 1);
					break;
				case 31:
					shop.buy(player, slotId, 5);
					break;
				case 39:
					shop.buy(player, slotId, 10);
					break;
				case 32:
					shop.buy(player, slotId, 50);
					break;
				case 18:
					shop.buy(player, slotId, 500);
					break;
				case 12:
					shop.sendExamine(player, slotId);
					break;
				}
			}
		}
		if (interfaceId == 621) {
			switch(packetId) {
			case 11:
				shop.sendInfo(player, slotId, true);
				break;
			case 29:
				shop.sell(player, slotId, 1);
				break;
			case 31:
				shop.sell(player, slotId, 5);
				break;
			case 9:
				shop.sell(player, slotId, 10);
				break;
			case 32:
				shop.sell(player, slotId, 50);
				break;
			case 18:
				player.getInventory().sendExamine(slotId);
				break;
			}
		}
	}
}