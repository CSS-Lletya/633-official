package com.rs.plugin.impl.inventory;

import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.herblore.Potions;
import skills.herblore.Potions.Potion;

@InventoryWrapper(itemId = { 2428, 121, 123, 125, 11429, 11431, 113, 115, 117, 119, 11441, 11443, 2432, 133, 135, 137,
		11457, 11459, 9739, 9741, 9743, 9745, 11445, 11447, 2436, 145, 147, 149, 18715, 18716, 18717, 18718, 11469,
		11471, 2440, 157, 159, 161, 18719, 18720, 18721, 18722, 11485, 11487, 2442, 163, 165, 167, 18723, 18724, 18725,
		18726, 11497, 11499, 2444, 169, 171, 173, 18731, 18732, 18733, 18734, 11509, 11511, 3040, 3042, 3044, 3046,
		18735, 18736, 18737, 18738, 11513, 11515, 2430, 127, 129, 131, 11449, 11451, 2434, 139, 141, 143, 11465, 11467,
		3024, 3026, 3028, 3030, 23399, 23401, 23403, 23405, 23407, 23409, 22379, 22380, 11493, 11495, 2446, 175, 177,
		179, 11433, 11435, 2448, 181, 183, 185, 11473, 11475, 5943, 5945, 5947, 5949, 11501, 11503, 5952, 5954, 5956,
		5958, 4842, 4844, 4846, 4848, 23537, 23539, 23541, 23543, 23545, 23547, 11437, 11439, 2450, 189, 191, 193,
		11521, 11523, 2452, 2454, 2456, 2458, 11505, 11507, 3008, 3010, 3012, 3014, 11453, 11455, 3016, 3018, 3020,
		3022, 18727, 18728, 18729, 18730, 11481, 11483, 4417, 4419, 4421, 4423, 6685, 6687, 6689, 6691, 22373, 22374,
		9021, 9022, 9023, 9024, 11489, 11491, 3032, 3034, 3036, 3038, 11461, 11463, 2438, 151, 153, 155, 11477, 11479,
		9998, 10000, 10002, 10004, 11517, 11519, 14838, 14840, 14842, 14844, 14846, 14848, 14850, 14852, 10925, 10927,
		10929, 10931, 12140, 12142, 12144, 12146, 15300, 15301, 15302, 15303, 15304, 15305, 15306, 15307, 15308, 15309,
		15310, 15311, 15312, 15313, 15314, 15315, 15316, 15317, 15318, 15319, 15320, 15321, 15322, 15323, 15324, 15325,
		15326, 15327, 15328, 15329, 15330, 15331, 22375, 22376, 15332, 15333, 15334, 15335, 17556, 17558, 17560, 17562,
		17574, 17576, 17578, 17580, 17582, 17584, 17586, 17588, 17598, 17600, 17602, 17604, 17606, 17608, 17610, 17612,
		17622, 17624, 17626, 17628, 17564, 17590, 17614, 17570, 17594, 17618, 197, 464, 431, 4627, 1915, 1917, 3803,
		7740, 1905, 7744, 5779, 5781, 5783, 5785, 5739, 5859, 5861, 5863, 5865, 1907, 5795, 5797, 5799, 5801, 5741,
		5875, 5877, 5879, 5881, 1909, 7746, 5787, 5789, 5791, 5793, 5743, 5867, 5869, 5871, 5873, 1911, 7748, 5803,
		5805, 5807, 5809, 5745, 5883, 5885, 5887, 5889, 1913, 5771, 5773, 5775, 5777, 5747, 5851, 5853, 5855, 5857,
		2955, 7750, 5811, 5813, 5815, 5817, 5749, 5891, 5893, 5895, 5897, 5751, 5819, 5821, 5823, 5825, 5753, 5899,
		5901, 5903, 5905, 5755, 7754, 5827, 5829, 5831, 5833, 5757, 5907, 5909, 5911, 5913, 5759, 5835, 5837, 5839,
		5841, 5761, 5915, 5917, 5919, 5921, 5763, 7752, 5843, 5845, 5847, 5849, 5765, 5923, 5925, 5927, 5929, 3408,
		3410, 3412, 3414, 3416, 3417, 3418, 3419, 3422, 3424, 3426, 3428, 3430, 3432, 3434, 3436}, itemNames = {})
public class PotionMixingItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item firstItem, Item secondItem, int fromSlot, int toSlot) {
		Item fromItem = firstItem;
		Item toItem = secondItem;
		if (fromItem.getId() == Potions.VIAL || toItem.getId() == Potions.VIAL) {
			Potion pot = Potion.forId(fromItem.getId() == Potions.VIAL ? toItem.getId() : fromItem.getId());
			if (pot == null || pot.emptyId == -1)
				return;
			int doses = Potions.getDoses(pot, fromItem.getId() == Potions.VIAL ? toItem : fromItem);
			if (doses == 1) {
				player.getInventory().switchItem(fromSlot, toSlot);
				player.getPackets().sendGameMessage("You combine the potions.", true);
				return;
			}
			int vialDoses = doses / 2;
			doses -= vialDoses;
			player.getInventory().getItems().set(fromItem.getId() == Potions.VIAL ? toSlot : fromSlot,
					new Item(pot.getIdForDoses(doses), 1));
			player.getInventory().getItems().set(fromItem.getId() == Potions.VIAL ? fromSlot : toSlot,
					new Item(pot.getIdForDoses(vialDoses), 1));
			player.getInventory().refresh(fromSlot);
			player.getInventory().refresh(toSlot);
			player.getAudioManager().sendSound(Sounds.VIAL_MIXING);
			player.getPackets().sendGameMessage("You split the potion between the two vials.", true);
			return;
		}
		Potion pot = Potion.forId(fromItem.getId());
		if (pot == null)
			return;
		int doses2 = Potions.getDoses(pot, toItem);
		if (doses2 == 0 || doses2 == pot.getMaxDoses())
			return;
		int doses1 = Potions.getDoses(pot, fromItem);
		doses2 += doses1;
		doses1 = doses2 > pot.getMaxDoses() ? doses2 - pot.getMaxDoses() : 0;
		doses2 -= doses1;
		if (doses1 == 0 && pot.emptyId == -1)
			player.getInventory().deleteItem(fromSlot, fromItem);
		else {
			player.getInventory().getItems().set(fromSlot,
					new Item(doses1 > 0 ? pot.getIdForDoses(doses1) : pot.emptyId, 1));
			player.getInventory().refresh(fromSlot);
		}
		player.getInventory().getItems().set(toSlot, new Item(pot.getIdForDoses(doses2), 1));
		player.getInventory().refresh(toSlot);
		player.getPackets().sendGameMessage("You pour from one container into the other"
				+ (pot.emptyId == -1 && doses1 == 0 ? " and the flask shatters to pieces." : "."));
	}
}