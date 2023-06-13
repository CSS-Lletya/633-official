package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.herblore.WeaponPoison;

@InventoryWrapper(itemId = {
		// poison vials
		187, 5937, 5940, 17572, 17596, 17620,
		// poisonable
		806, 807, 808, 809, 810, 811, 826, 827, 828, 829, 830, 863, 864, 865, 866, 867, 868, 869, 877, 882, 884, 886,
		888, 890, 892, 1203, 1205, 1207, 1209, 1211, 1213, 1215, 1217, 1237, 1239, 1241, 1243, 1245, 1247, 1249, 3093,
		4580, 6591, 8872, 9139, 9140, 9141, 9142, 9143, 9144, 9145, 10581, 11212, 11230, 11367, 11369, 11371, 11373,
		11375, 11377, 13083, 13879, 16427, 16432, 16437, 16442, 16447, 16452, 16457, 16462, 16467, 16472, 16477, 16757,
		16765, 16773, 16781, 16789, 16797, 16805, 16813, 16821, 16829, 16837, 17063, 17071, 17079, 17087, 17095, 17103,
		17111, 17119, 17127, 17135, 17143,

}, itemNames = {})
public class WeaponPoisoningItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int slot, int toSlot) {
		WeaponPoison.poison(player, firstItem, secondItem, false);
	}
}