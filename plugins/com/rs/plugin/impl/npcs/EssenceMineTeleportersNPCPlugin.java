package com.rs.plugin.impl.npcs;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.net.encoders.other.Graphics;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.loaders.ShopsHandler;

@NPCSignature(name = { "Brimstail", "Aubury", "Archmage Sedridor", "Wizard Distentor", "Wizard Cromperty",
		"Carwen Essencebinder"}, npcId = {300})
public class EssenceMineTeleportersNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		switch (option) {
		case 1:
			//dialogue
			break;
		case 2:
			if (npc.getId() == 300) {
				execute(player, npc, 3);
				return;
			}
			String key = ShopsHandler.getShopForNpc(npc.getId());
			if (key == null)
				return;
			ShopsHandler.openShop(player, key);
			break;
		case 3:
			player.getDetails().getEssenceTeleporter().set(npc.getId());
			npc.faceEntity(player);
			npc.setNextForceTalk(new ForceTalk("Senventior Disthine Molenko!"));
	        npc.setNextAnimation(Animations.WIZARD_ESSENCE_MINE_TELEPORT);
	        npc.setNextGraphics(Graphic.SMALL_TELEPORTING_RINGS_VIA_HANDS);
	        player.setNextGraphics(new Graphics(110));
	        player.task(3, p -> p.setNextWorldTile(RandomUtility.random(new WorldTile[]{new WorldTile(2901, 4816), new WorldTile(2888, 4845), new WorldTile(2926, 4842), new WorldTile(2921, 4811)})));
			break;
		}
	}
}