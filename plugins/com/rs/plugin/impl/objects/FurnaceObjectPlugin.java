package com.rs.plugin.impl.objects;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.SkillDialogueFeedback;

import skills.SkillsDialogue;
import skills.crafting.MoltenGlassCrafting;
import skills.crafting.SilverItemCasting;
import skills.smithing.Smelting;
import skills.smithing.Smelting.SmeltingData;

@ObjectSignature(objectId = {45310}, name = {"Furnace"})
public class FurnaceObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.faceObject(object);
		player.dialogue(d -> {
			d.skillsMenu(2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363);
			d.skillDialogue(new SkillDialogueFeedback() {
				@Override
				public void handle(int button) {
					new Smelting(player, SmeltingData.values()[SkillsDialogue.getItemSlot(button)], false).start();
				}
			});
		});
	}
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (item.getId() == ItemNames.BUCKET_OF_SAND_1783 || item.getId() == ItemNames.SODA_ASH_1781) {
			new MoltenGlassCrafting(player).start();
		}
		if (item.getId() == ItemNames.SILVER_BAR_2355) {
			SilverItemCasting.sendInterface(player);
		}
	}
}