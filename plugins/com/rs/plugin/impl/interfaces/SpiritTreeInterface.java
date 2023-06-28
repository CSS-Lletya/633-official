package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.content.traveling.SpiritTree;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = 864)
public class SpiritTreeInterface extends RSInterfaceListener {
    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
        SpiritTree.handleSpiritTree(player, slotId);
    }
}
