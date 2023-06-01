package com.rs.plugin.impl.inventory;

import java.util.Arrays;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

import skills.prayer.Bone;
import skills.prayer.PrayerBoneBury;

@InventoryWrapper(itemId = {526, 530, 3179, 2859, 532, 534, 536, 4834, 6729}, itemNames = {  })
public class BonesBuryItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		Arrays.stream(Bone.values()).filter(boneId -> boneId.getId() == item.getId()).forEach(bone -> {
			PrayerBoneBury buryAction = new PrayerBoneBury(player, item.getId(), bone);
			buryAction.start();
		});
	}
	
	
}