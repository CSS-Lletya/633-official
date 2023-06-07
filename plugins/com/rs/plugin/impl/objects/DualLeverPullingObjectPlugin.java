package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.plugin.impl.objects.region.LumbridgeRegionObjectPlugin;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

/**
 * Needs to be classified, some places maybe use same object, like cockroaches (WIP)
 * @author Dennis
 *
 */
@ObjectSignature(objectId = {2718}, name = {})
public class DualLeverPullingObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getX() == 3166 && object.getY() == 3305) {
			object.doAction(optionId, 2718, "operate", () -> {
				player.getPackets().sendGameMessage("You pull the lever.");
				if (player.getAttributes().get(Attribute.WHEAT_DEPOSITED).getBoolean()) {
					player.getAttributes().get(Attribute.WHEAT_GRINDED).set(true);
					player.getPackets().sendGameMessage("You hear the grinding of stones and the wheat falls below.");
					LumbridgeRegionObjectPlugin.updateWheat(player);
				}
			});
		}
	}
}