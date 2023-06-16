package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.ShipCharter;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.loaders.ShopsHandler;

@NPCSignature(name = { "Trader Crewmember", "Glum" }, npcId = { 376, 378, 377, 4652, 4655 })
public class ShipCharterNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 2) {// need to add shop
			String key = ShopsHandler.getShopForNpc(npc.getId());
			if (key == null)
				return;
			ShopsHandler.openShop(player, key);
			return;
		}
		ShipCharter.open(player);
	}
}