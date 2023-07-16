package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = {1006})
public class HerbicideInterfacePlugin extends RSInterfaceListener {

    private final static String[] HERB_NAMES = new String[]{"Guam leave", "Marrentill", "Tarromin", "Harralander", "Ranarr", "Toadflax", "Spirit weed", "Irit", "Wergali", "Avantoe",
            "Kwuarm", "Snapdragon", "Cadantine", "Lantadyme", "Dwarf weed", "Fellstalk", "Torstol"};

    @Override
    public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
        if (componentId >= 32 && componentId <= 50) {
            int id = componentId == 50 ? 16 : (componentId - 32);
            player.getDetails().getHerbicideSettings()[id] = !player.getDetails().getHerbicideSettings()[id];
            player.getPackets().sendGameMessage(HERB_NAMES[id] + (player.getDetails().getHerbicideSettings()[id] ? "s are now being burnt for 2x cleaning experience." : "s are no longer being burnt for 2x cleaning experience."));
//            player.getPackets().sendIComponentSprite(1006, componentId, player.getDetails().herbicideSettings[id] ? 2548 : 2549);
            //TODO: function will work; but this'll be uncommented when the packet is fixed.
            return;
        }
    }
}