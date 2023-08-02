package com.rs.game.player.content;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * For a full video on the item interactions: https://www.youtube.com/watch?v=8uwM73CYU_Q
 * @author Dennis
 *
 */
public class DiangoHolidayItemRetrieval {

	private static List<Item> VALUES = Collections.unmodifiableList(Arrays.asList(new Item[] {
			new Item(ItemNames.BUNNY_EARS_1037), new Item(ItemNames.SCYTHE_1419), new Item(ItemNames.YO_YO_4079),
			new Item(ItemNames.RUBBER_CHICKEN_10732), new Item(ItemNames.ZOMBIE_HEAD_6722), new Item(ItemNames.EASTER_RING_7927),
			new Item(ItemNames.BOBBLE_HAT_6856), new Item(ItemNames.BOBBLE_SCARF_6857), new Item(ItemNames.JESTER_HAT_6858),
			new Item(ItemNames.JESTER_SCARF_6859), new Item(ItemNames.TRI_JESTER_HAT_6860), new Item(ItemNames.TRI_JESTER_SCARF_6861),
			new Item(ItemNames.WOOLLY_HAT_6862), new Item(ItemNames.WOOLLY_SCARF_6863),
			new Item(ItemNames.GREEN_MARIONETTE_6866), new Item(ItemNames.JACK_LANTERN_MASK_9920), new Item(ItemNames.SKELETON_BOOTS_9921),
			new Item(ItemNames.SKELETON_GLOVES_9922), new Item(ItemNames.SKELETON_LEGGINGS_9923), new Item(ItemNames.SKELETON_SHIRT_9924),
			new Item(ItemNames.SKELETON_MASK_9925), new Item(ItemNames.REINDEER_HAT_10507), new Item(ItemNames.WINTUMBER_TREE_10508),
			new Item(ItemNames.CHICKEN_FEET_11019), new Item(ItemNames.CHICKEN_LEGS_11018), new Item(ItemNames.CHICKEN_WINGS_11017),
			new Item(ItemNames.CHICKEN_HEAD_11015), new Item(ItemNames.GRIM_REAPER_HOOD_11789), new Item(ItemNames.SNOW_GLOBE_11949),
			new Item(ItemNames.CHOCATRICE_CAPE_12634), new Item(ItemNames.WARLOCK_TOP_14076), new Item(ItemNames.WARLOCK_LEGS_14077),
			new Item(ItemNames.WARLOCK_CLOAK_14081), new Item(ItemNames.SANTA_COSTUME_TOP_14595), new Item(ItemNames.SANTA_COSTUME_LEGS_14603),
			new Item(ItemNames.SANTA_COSTUME_GLOVES_14602), new Item(ItemNames.SANTA_COSTUME_BOOTS_14605), new Item(ItemNames.ICE_AMULET_14596),
			new Item(ItemNames.CORNUCOPIA_14537), new Item(ItemNames.EASTER_CARROT_14713), new Item(ItemNames.EEK_15353),
			new Item(ItemNames.WEB_CLOAK_15352), new Item(ItemNames.CANDY_CANE_15426), new Item(ItemNames.CHRISTMAS_GHOST_HOOD_15422),
			new Item(ItemNames.CHRISTMAS_GHOST_TOP_15423), new Item(ItemNames.CHRISTMAS_GHOST_BOTTOMS_15425), new Item(ItemNames.MAGNIFYING_GLASS_15374),
			new Item(ItemNames.SQUIRREL_EARS_15673)
	}));
	
	public static void sendInterface(Player player) {
		VALUES.stream().filter(item -> !player.ownsItems(item)).forEach(player.getDetails().getHolidayItems()::add);
		player.getPackets().sendItems(2, player.getDetails().getHolidayItems().getItemsCopy());
		player.getPackets().sendUpdateItems(2, player.getDetails().getHolidayItems().getItemsCopy(), 2);
		player.getPackets().sendInterSetItemsOptionsScript(468, 2, 2, 8, 6, "Take", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(468, 2, 0, 48, 0, 1, 2);
		player.getInterfaceManager().sendInterface(468);
		player.setCloseInterfacesEvent(player.getDetails().getHolidayItems()::clear);
	}
	
	public static void removeHolidayItem(Player player, int slot) {
		Item item = player.getDetails().getHolidayItems().get(slot);
		if (item == null)
			return;
		item = new Item(item.getId(), 1);
		player.getDetails().getHolidayItems().remove(slot, item);
		player.getDetails().getHolidayItems().shift();
		player.getInventory().addItem(item);
		for (int i = 0; i <= 48; i++)
			player.getPackets().sendUpdateItems(2, player.getDetails().getHolidayItems(), i);
	}
}