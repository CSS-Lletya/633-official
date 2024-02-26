package com.rs.plugin.impl.objects;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.magic.spells.modern.charge.ChargeAirOrb;
import skills.magic.spells.modern.charge.ChargeEarthOrb;
import skills.magic.spells.modern.charge.ChargeFireOrb;
import skills.magic.spells.modern.charge.ChargeWaterOrb;

@ObjectSignature(objectId = {}, name = {"obelisk of air", "obelisk of water", "obelisk of fire", "obelisk of earth"})
public class OrbChargingObeliskObjectPlugin extends ObjectListener {

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		int itemId = item.getId();
		if (itemId != 567) {
			player.getPackets().sendGameMessage("Nothing interesting happens.", true);
			return;
		}
		switch(object.getDefinitions().getName().toLowerCase()) {
		case "obelisk of air":
			player.getAction().setAction(new ChargeAirOrb());
			return;

		case "obelisk of water":
			player.getAction().setAction(new ChargeWaterOrb());
			return;

		case "obelisk of earth":
			player.getAction().setAction(new ChargeEarthOrb());
			return;

		case "obelisk of fire":
			player.getAction().setAction(new ChargeFireOrb());
			return;
		}
	}
}