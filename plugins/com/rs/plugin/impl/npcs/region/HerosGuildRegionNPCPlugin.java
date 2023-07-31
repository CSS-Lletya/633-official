package com.rs.plugin.impl.npcs.region;

import com.rs.game.dialogue.Mood;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.loaders.ShopsHandler;

@NPCSignature(name = {}, npcId = {797})
public class HerosGuildRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		player.dialogue(npc.getId(), d -> {
			d.npc(Mood.happy, "Welcome to the Heroes' Guild!");
			d.option("So do you sell anything here?", () -> {
				d.player(Mood.happy, "So do you sell anything good here?");
				d.npc(Mood.happy_plain, "Why yes! We DO run an exclusive shop for our members!");
				d.event(() -> ShopsHandler.openShop(player, ShopsHandler.getShopForNpc(npc.getId())));
			}, "So what can I do here?", () -> {
				d.player(Mood.happy, "So what can I do here?");
				d.npc(Mood.eyes_side2side_reading, "Look around... there are all sorts ","of things to keep our guild members entertained!");
			});
		});
	}
}