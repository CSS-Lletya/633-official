package com.rs.plugin.impl.objects;

import com.rs.constants.Sounds;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.plugin.impl.objects.region.LumbridgeRegionObjectPlugin;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

/**
 * Needs to be classified, some places maybe use same object, like cockroaches
 * (WIP)
 * 
 * @author Dennis
 *
 */
@ObjectSignature(objectId = { 2718, 52552 }, name = {})
public class DualLeverPullingObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, 52552, "operate", () -> {
			player.getPackets().sendGameMessage("You pull the lever.");
			if (player.getAttributes().get(Attribute.WHEAT_DEPOSITED).getBoolean()) {
				player.getAttributes().get(Attribute.WHEAT_GRINDED).set(true);
				player.getAudioManager().sendSound(Sounds.HOPPER_LEVER_PULLING);
				player.getAudioManager().sendSound(Sounds.PIT_FALL);
				player.getPackets().sendGameMessage("You hear the grinding of stones and the wheat falls below.");
				LumbridgeRegionObjectPlugin.updateWheat(player);
			}
		});
		if (object.matches(new WorldTile(3166, 3305))) {
			object.doAction(optionId, 2718, "operate", () -> {
				player.getPackets().sendGameMessage("You pull the lever.");
				if (player.getAttributes().get(Attribute.WHEAT_DEPOSITED).getBoolean()) {
					player.getAttributes().get(Attribute.WHEAT_GRINDED).set(true);
					player.getAudioManager().sendSound(Sounds.HOPPER_LEVER_PULLING);
					player.getAudioManager().sendSound(Sounds.PIT_FALL);
					player.getPackets().sendGameMessage("You hear the grinding of stones and the wheat falls below.");
					LumbridgeRegionObjectPlugin.updateWheat(player);
				}
			});
		}
	}
}