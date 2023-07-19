package com.rs.plugin.impl.npcs;

import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.loaders.ShopsHandler;

@NPCSignature(name = { "Shopkeeper"}, npcId = {})
public class ShopkeeperNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		String key = ShopsHandler.getShopForNpc(npc.getId());
		if (key == null)
			return;
		ShopsHandler.openShop(player, key);
	}
}