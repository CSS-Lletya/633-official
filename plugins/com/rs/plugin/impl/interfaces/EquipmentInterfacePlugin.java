package com.rs.plugin.impl.interfaces;

import java.util.stream.IntStream;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Graphics;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.ItemExamines;
import com.rs.utilities.loaders.ItemBonuses;

@RSInterfaceSignature(interfaceId = { 387 })
public class EquipmentInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (player.getInterfaceManager().containsInventoryInter())
			return;
		player.getMovement().stopAll();
		RSInterfacePluginDispatcher.executeEquipment(player, 387, componentId, packetId, slotId2);
		if (componentId == 45) {
			RSInterfacePluginDispatcher.openItemsKeptOnDeath(player);
		}
		if (componentId == 42) {
			if (player.getInterfaceManager().containsScreenInter() || player.getMovement().isLocked()) {
				player.getPackets()
						.sendGameMessage("Please finish what you're doing before opening the price checker.");
				return;
			}
			player.getMovement().stopAll();
			player.getPriceCheckManager().openPriceCheck();

		} else if (componentId == 39) {
			RSInterfacePluginDispatcher.openEquipmentBonuses(player, false);
		}
		if (IntStream.of(8, 11, 14, 17, 20, 23, 26, 29, 32, 35, 38).anyMatch(comp -> comp == componentId)
				&& packetId == 12) {
			player.getPackets().sendGameMessage(ItemExamines.getExamine(new Item(slotId2)));
		}
		if (packetId == 29 && componentId == 17) {
			if (slotId2 == 15484)
				player.getInterfaceManager().gazeOrbOfOculus();
		}
		if (packetId == 11) {
			if (componentId == 17)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_WEAPON);
			if (componentId == 8)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_HAT);
			if (componentId == 11)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_CAPE);
			if (componentId == 14)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_AMULET);
			if (componentId == 38)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_ARROWS);
			if (componentId == 20)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_CHEST);
			if (componentId == 26)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_LEGS);
			if (componentId == 29)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_HANDS);
			if (componentId == 32)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_FEET);
			if (componentId == 35)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_RING);
			if (componentId == 23)
				RSInterfacePluginDispatcher.sendRemove(player, Equipment.SLOT_SHIELD);
		}
	}
	
	@Override
	public void executeEquipment(Player player, Item item, int componentId, int packetId) {
		if (componentId == 17 && packetId == 29 && item.getId() == 2963) {
			if (player.getPrayer().getPoints() >= 6) {
                player.getPrayer().drainPrayer(6);
                player.getMovement().lock(2);
                player.setNextAnimation(Animations.CAST_BLOOM);
                for (GameObject object : World.getRegion(player.getRegionId()).getAllObjects()) {
                    if (object.withinDistance(player, 2)) {
                        switch (object.getId()) {
                            case 3512:
                            case 3510:
                            case 3508:
                            	GameObject.spawnTempGroundObject(new GameObject(object.getId() + 1,
                                        object.getType(), object.getRotation(), object.getX(),
                                        object.getY(), object.getPlane()), 30);
                                World.sendGraphics(new Graphics(263), object);
                                break;
                        }
                    }
                }
            }
            player.getPackets().sendGameMessage("You need more prayer points to do this.");
        }
	}

	public static void sendItemStats(final Player player, Item item) {
		StringBuilder statString = new StringBuilder();
		boolean hasBonuses = ItemBonuses.getItemBonuses(item.getId()) != null;
		for (int i = 0; i < 18; i++) {
			int bonus = hasBonuses ? ItemBonuses.getItemBonuses(item.getId())[i] : 0;
			String label = CombatDefinitions.BONUS_LABELS[i];
			String sign = bonus > 0 ? "+" : "";
			statString.append(label + ": " + (sign + bonus) + ((label == "Magic Damage" || label == "Absorb Melee"
					|| label == "Absorb Magic" || label == "Absorb Ranged") ? "%" : "") + "<br>");
		}
		player.getPackets().sendGlobalString(321, "Stats for " + item.getName());
		player.getPackets().sendGlobalString(324, statString.toString());
		player.getPackets().sendHideIComponent(667, 49, false);
		player.setCloseInterfacesEvent(() -> {
			player.getPackets().sendGlobalString(321, "");
			player.getPackets().sendGlobalString(324, "");
			player.getPackets().sendHideIComponent(667, 49, true);
		});
	}
}