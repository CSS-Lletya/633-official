package com.rs.plugin.impl.npcs.region;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.plugin.listener.NPCListener;
import com.rs.plugin.wrapper.NPCSignature;
import com.rs.utilities.loaders.ShopsHandler;

import skills.magic.TeleportType;

@NPCSignature(name = {}, npcId = { 2259 })
public class WildernessRegionNPCPlugin extends NPCListener {

	@Override
	public void execute(Player player, NPC npc, int option) {
		if (option == 3) {
			player.getMovement().lock();
			World.get().submit(new Task(1) {
				int tick;
				@Override
				protected void execute() {
					switch (tick++) {
					case 1:
						npc.setNextForceTalk(new ForceTalk("Veniens! Sallakar! Rinnesset!"));
						npc.setNextAnimation(Animations.WILDY_ZAMORAK_MAGE_TELEPORT);
						npc.setNextGraphics(Graphic.WILDY_ZAMORAK_MAGE_TELEPORT);
						break;
					case 3:
						player.getMovement().move(false, new WorldTile(3030, 4856, 0), TeleportType.NORMAL);
						player.getPrayer().drainPrayer(player.getPrayer().getPoints());
						player.getMovement().unlock();
						cancel();
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