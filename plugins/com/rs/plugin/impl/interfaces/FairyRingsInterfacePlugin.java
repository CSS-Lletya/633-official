package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.FairyRing;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {734, 735})
public class FairyRingsInterfacePlugin extends RSInterfaceListener {

    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
        if (interfaceId == 734) {
            if (componentId == 21)
                FairyRing.confirmRingHash(player);
            else
                FairyRing.handleDialButtons(player, componentId);
        } else if (interfaceId == 735) {
            if (componentId >= 14 && componentId <= 14 + 64)
                FairyRing.sendRingTeleport(player, componentId - 14);
        }
    }
}