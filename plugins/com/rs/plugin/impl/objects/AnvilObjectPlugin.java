package com.rs.plugin.impl.objects;

import java.util.stream.IntStream;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.smithing.DragonShieldCreation;
import skills.smithing.GodswordCreation;
import skills.smithing.SpiritShieldCreation;
import skills.smithing.StudCreation;

@ObjectSignature(objectId = {}, name = {"Anvil"})
public class AnvilObjectPlugin extends ObjectListener {

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (!player.getInventory().contains(new Item(ItemNames.HAMMER_2347))) {
            player.getPackets().sendGameMessage("You need a Hammer in order to do this. ");
            return;
        }
		if (item.getId() == ItemNames.STEEL_BAR_2353) {
			new StudCreation(player).start();
		}
		if (IntStream.of(11692, 11710, 11688, 11712, 11686, 11714).anyMatch(gs -> item.getId() == gs)) {
			GodswordCreation.craftBlades(player, item.getId());
		}
		if (IntStream.of(2366, 2368, 11286, 1540).anyMatch(gs -> item.getId() == gs)) {
			new DragonShieldCreation((item.getId() == 2366 || item.getId() == 2368 ? 1 : 2)).craftShield(player);
		}
		if (IntStream.of(13746, 13748,13750,13752).anyMatch(gs -> item.getId() == gs)) {
			SpiritShieldCreation.createSpiritShield(player, item);
		}
	}
}