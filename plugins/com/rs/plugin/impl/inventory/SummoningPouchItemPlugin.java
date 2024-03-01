package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.summoning.Summoning;
import skills.summoning.Summoning.Pouch;

@InventoryWrapper(itemId = { 12047, 12043, 12059, 12019, 12009, 12778, 12049, 12055, 12808, 12067, 12063, 12091, 12800,
		12053, 12065, 12021, 12818, 12780, 12798, 12814, 12073, 12087, 12071, 12051, 12095, 12097, 12099, 12101, 12103,
		12105, 12107, 12075, 12816, 12041, 12061, 12007, 12035, 12027, 12077, 12531, 12812, 12784, 12810, 12023, 12085,
		12037, 12015, 12045, 12079, 12123, 12031, 12029, 12033, 12820, 12057, 14623, 12792, 12069, 12011, 12782, 12081,
		12794, 12013, 12802, 12804, 12806, 12025, 12017, 12788, 12776, 12083, 12039, 12786, 12089, 12796, 12822, 12093,
		12790, 14422, 14424, 14426, 14428, 14430}, itemNames = {})
public class SummoningPouchItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		if (item.getDefinitions().containsOption("Summon")) {
			Pouch pouch = Pouch.forId(item.getId());
			if (pouch != null)
				Summoning.spawnFamiliar(player, pouch);
		}
	}
}