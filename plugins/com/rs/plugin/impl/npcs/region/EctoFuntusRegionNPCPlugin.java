package com.rs.plugin.impl.npcs.region;

import com.rs.game.dialogue.Mood;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;

/**
 * Not adding tokens just yet so collection will give Ectophial instead
 * @author Dennis
 *
 */
@NPCSignature(name = {}, npcId = {1686})
public class EctoFuntusRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (npc.getId() == 1686 && option == 2) {
			if (!player.ownsItems(new Item(4251), new Item(4252)) && player.getInventory().addItem(new Item(4251))) {
				player.dialogue(1686, d -> d.npc(Mood.happy, "Here you are, your very own Ectophial"));
			}
		} 
	}
}