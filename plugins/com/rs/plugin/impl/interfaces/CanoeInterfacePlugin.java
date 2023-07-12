package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.traveling.Canoes;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 52,53 })
public class CanoeInterfacePlugin extends RSInterfaceListener {

	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (interfaceId == 53) {
			int selectedArea = -1;
            if (componentId == 47)
                selectedArea = 0;
            else if (componentId == 48)
                selectedArea = 1;
            else if (componentId == 3)
                selectedArea = 2;
            else if (componentId == 6)
                selectedArea = 3;
            else if (componentId == 49)
                selectedArea = 4;
            if (selectedArea != -1)
                Canoes.deportCanoeStation(player, selectedArea);
		} else 
		if (componentId >= 30 && componentId <= 34) {
            player.getAttributes().get(Attribute.CANOE_SELECTED).set(componentId - 30);
            Canoes.createShapedCanoe(player);
        }
	}
}