package com.rs.net.packets.outgoing.impl;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.cores.CoresManager;
import com.rs.game.item.FloorItem;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.PreciseDistanceInteraction;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.Ticks;
import com.rs.utilities.Utility;

import skills.Skills;

/**
 * @author Savions.
 */

@OutgoingPacketSignature(packetId = 66, description = "Represents an interface being used on a floor item")
public class InterfaceOnFloorItemPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		final int interfaceBitMap = stream.readIntV1();
		final int interfaceId = interfaceBitMap >> 16;
		final int buttonId = interfaceBitMap & 0XFF;
		final int slotId = stream.readUnsignedShort();
		final int xCoord = stream.readUnsignedShortLE();
		final int yCoord = stream.readUnsignedShort128();
		final int floorItemId = stream.readUnsignedShort128();
		final int componentItemId = stream.readUnsignedShortLE();
		final boolean forceRun = stream.readUnsignedByte() == 1;

		if (!player.isStarted() || !player.isClientLoadedMapRegion() || player.isDead()
                || player.getMovement().isLocked())
            return;
        long currentTime = Utility.currentTimeMillis();
        if (player.getMovement().getLockDelay() > currentTime)
            return;
        final WorldTile tile = new WorldTile(xCoord, yCoord, player.getPlane());
        final int regionId = tile.getRegionId();
        if (!player.getMapRegionsIds().contains(regionId))
            return;
        final FloorItem item = World.getRegion(regionId).getGroundItem(floorItemId, tile, player);
        if (item == null)
            return;
        player.getMovement().stopAll(false);
        if (interfaceId == 192) {
	        if (player.getSkills().getLevel(Skills.MAGIC) < 33) {
	            player.getPackets().sendGameMessage("You do not have the required level to cast this spell.");
	            return;
	        }
	        player.getAction().setAction(new PreciseDistanceInteraction(item.getTile(), (byte) 7, PreciseDistanceInteraction.Type.SMART, p -> {
					if (!player.getInventory().hasFreeSlots()) {
						player.getPackets().sendGameMessage("You don't have enough inventory space.");
						return;
					}
	                if (!player.getInventory().containsItem(563, 1) || (!wearingStaff(player, item.getId()) && !player.getInventory().containsItem(556, 1))) {
	                    player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
	                    return;
	                }
	                player.getAudioManager().sendSound(Sounds.TELE_GRAB_SPELL);
	                player.setNextAnimation(Animations.TELEGRAB_SPELL);
	                player.getSkills().addXp(Skills.MAGIC, 10);
	                World.sendProjectile(player, new WorldTile(xCoord, yCoord, player.getPlane()), 142, 18, 5, 20, 50, 0, 0);
	                CoresManager.schedule(() -> {
	                    World.sendGraphics(Graphic.TELEGRAB_SPELL, tile);
	                    if (!wearingStaff(player, item.getId())) {
	                    	player.getInventory().deleteItem(563, 1);
	                    }
	                    player.getInventory().deleteItem(556, 1);
	                    FloorItem.removeGroundItem(player, item);
	                }, Ticks.fromSeconds(2));
	            
	        }));
        }
        
		System.out.println("Unhandled Inter on floor item packet; component: " + interfaceId + ", " + buttonId + ", " + slotId + ", " + componentItemId +
				", -> item: " + floorItemId + ", coords[" + xCoord + ", " + yCoord + "], forceRun " + forceRun);
	}
	
	private boolean wearingStaff(Player player, int itemId) {
		return player.getEquipment().getWeaponId() == 1381 || player.getEquipment().getWeaponId() == 1397
                || player.getEquipment().getWeaponId() == 1405;
	}
}
