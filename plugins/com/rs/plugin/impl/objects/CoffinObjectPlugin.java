package com.rs.plugin.impl.objects;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtils;

@ObjectSignature(objectId = {}, name = {"Ogre Coffin", "Coffin"})
public class CoffinObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, "Ogre Coffin", "open", () -> {
			Item[] lootables = Item.toList(ItemNames.RUSTY_SWORD_686, ItemNames.BRONZE_HATCHET_1351, ItemNames.STEEL_HATCHET_1353,
		            ItemNames.MARBLE_BLOCK_8786, ItemNames.ADAMANT_SCIMITAR_1331, ItemNames.ADAMANT_2H_SWORD_1317, ItemNames.ADAMANT_BATTLEAXE_1371,
		            ItemNames.RUNE_BATTLEAXE_1373, ItemNames.ADAMANTITE_ORE_449, ItemNames.RUNITE_ORE_451, ItemNames.RANARR_SEED_5295, ItemNames.BLACK_NAILS_4821,
		           ItemNames.MITHRIL_PICKAXE_1273);
			Item[] bones = Item.toList(ItemNames.ZOGRE_BONES_4812, ItemNames.FAYRG_BONES_4830, ItemNames.RAURG_BONES_4832, ItemNames.OURG_BONES_4834);
			if (!player.getInventory().containsAny(ItemNames.OGRE_COFFIN_KEY_4850)) {
				player.getPackets().sendGameMessage("You need a ogre coffin key to unlock this.");
				return;
			}
			if (player.getInventory().getFreeSlots() < 2) {
				player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
				return;
			}
			Item bone = RandomUtils.random(bones);
			player.getInventory().deleteItem(new Item(ItemNames.OGRE_COFFIN_KEY_4850));
			player.getInventory().addItem(bone);
			player.getInventory().addItem(RandomUtils.random(lootables).getId(), RandomUtils.random(1,2));
			player.dialogue(d -> d.item(bone.getId(), "You find some " + bone.getName() + "."));
		});
	}
}