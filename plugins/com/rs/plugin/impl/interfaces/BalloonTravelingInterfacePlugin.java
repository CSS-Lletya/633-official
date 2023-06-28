package com.rs.plugin.impl.interfaces;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.FadingScreen;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

import lombok.AllArgsConstructor;

//TODO: send var configs event with blackout minimap, for now using basic movement
@RSInterfaceSignature(interfaceId = { 469 })
public class BalloonTravelingInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		player.getInterfaceManager().closeInterfaces();
		FlightData.VALUES.stream().filter(id -> componentId == id.componentId)
		.forEach(flight -> {
			if (!player.getInventory().containsAny(flight.logId)) {
				//not real message but idc
				player.getPackets().sendGameMessage("You need to bring " + ItemDefinitions.getItemDefinitions(flight.logId).getName() + " in order to make the distance.");
				return;
			}
			player.getInventory().deleteItem(new Item(flight.logId));
			FadingScreen.fade(player, () -> player.setNextWorldTile(flight.desination));
		});
	}
	
	@AllArgsConstructor
	public enum FlightData{
		VARROCK(19, ItemNames.WILLOW_LOGS_1519, new WorldTile(3299, 3483)),
		TAVERLY(18, ItemNames.LOGS_1511, new WorldTile(2938, 3425)),
		ENTRANA(17, ItemNames.LOGS_1511, new WorldTile(2807, 3353)),
		CRAFTING_GUILD(16, ItemNames.OAK_LOGS_1521, new WorldTile(2925, 3299)),
		GRAND_TREE(15, ItemNames.MAGIC_LOGS_1513, new WorldTile(2479, 3456)),
		CASTLE_WARS(14, ItemNames.YEW_LOGS_1515, new WorldTile(2462, 3109))
		;
		private final int componentId, logId;
		private final WorldTile desination;
		
		public static final ImmutableSet<FlightData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(FlightData.class));
	}
}