package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {363})
public class PuzzleBoxInterfacePlugin extends RSInterfaceListener {

    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
        if (componentId == 4)
            player.getTreasureTrailsManager().movePuzzlePeice(slotId);
    }
}