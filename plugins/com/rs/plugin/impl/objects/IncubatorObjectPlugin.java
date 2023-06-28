package com.rs.plugin.impl.objects;

import java.util.stream.IntStream;

import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.task.impl.IncubatorTask;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.Skills;
import skills.summoning.IncubatorEgg;

@ObjectSignature(objectId = { 28336, 28359, 28352, 28550 }, name = { "Incubator" })
public class IncubatorObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		int eggId = player.getAttributes().get(Attribute.INCUBATOR_EGG).getInt();
		IncubatorEgg egg = IncubatorEgg.values()[eggId];
		if (optionId == 1) {
			if (player.getDetails().getIncubatorTimer().get() != egg.getInucbationTime()) {
				player.getPackets().sendGameMessage("The egg is still incubating.");
				return;
			}
			if (!player.getInventory().hasFreeSlots()) {
				player.getPackets().sendGameMessage("You don't have enough inventory space.");
				return;
			}
			if (egg != null) {
				String name = egg.getProduct().getName().toLowerCase();
				player.getPackets().sendGameMessage("You take your " + name + " out of the incubator.");
				player.getInventory().addItem(egg.getProduct());
				player.getAttributes().get(Attribute.INCUBATOR_EGG).set(-1);
				player.getVarsManager().sendVarBit(4221, 0).submitVarBitToMap(4221, 0);
			}
		}
		if (optionId == 2) {
			if (eggId != -1) {
				String name = IncubatorEgg.values()[eggId].getProduct().getName().toLowerCase();
				player.getPackets().sendGameMessage("There is a " + name + " incubating in there.");
			}
		}
	}

	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (IntStream.of(12483, 11694, 5077, 5076, 5078, 11965, 12494, 12477, 12480, 12478, 12479)
				.anyMatch(id -> item.getId() == id)) {
			final IncubatorEgg egg = IncubatorEgg.forItem(item);
			if (egg == null)
				return;
			if (player.getSkills().getLevel(Skills.SUMMONING) < egg.getLevel()) {
				player.dialogue(d -> d.item(egg.getEgg().getId(),
						"You need a Summoning level of at least " + egg.getLevel() + " in order to do this."));
				return;
			}
			World.get().submit(new IncubatorTask(player, egg.getInucbationTime(), 0, egg));
		}
	}
}