package com.rs.plugin.impl.npcs.region;

import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.net.encoders.other.Graphics;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.loaders.ShopsHandler;

@NPCSignature(name = {}, npcId = { 2257, 2258 })
public class WildernessRegionNPCPlugin implements NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 1) {
			player.getMovement().lock();
			//need to remove wildy controller
			World.get().submit(new Task(1) {
				int tick;
				@Override
				protected void execute() {
					switch (tick++) {
					case 1:
						npc.setNextForceTalk(new ForceTalk("Veniens! Sallakar! Rinnesset!"));
						npc.setNextAnimation(new Animation(1979));//add definitions V
						npc.setNextGraphics(new Graphics(4));
						break;
					case 3:
						Magic.sendItemTeleportSpell(player, true, 9603, 1684, 4, new WorldTile(3030, 4856, 0));
						player.getPrayer().drainPrayer(player.getPrayer().getPoints());
						player.getMovement().unlock();
						break;
					}
				}
			});
		}
		if (option == 2) {
			String key = ShopsHandler.getShopForNpc(npc.getId());
			if (key == null)
				return;
			ShopsHandler.openShop(player, key);
		}
	}
}