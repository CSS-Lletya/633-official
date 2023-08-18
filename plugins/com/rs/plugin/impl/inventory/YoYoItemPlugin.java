package com.rs.plugin.impl.inventory;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = 4079, itemNames = {})
public class YoYoItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		switch (option) {
		case 1:
			player.setNextAnimation(Animations.YO_YO_1);
			player.getAudioManager().sendSound(Sounds.YO_YO_ONE);
			break;
		case 2:
			player.setNextAnimation(Animations.YO_YO_2);
			player.getAudioManager().sendSound(Sounds.YO_YO_TWO);
			break;
		case 3:
			player.setNextAnimation(Animations.YO_YO_3);
			player.getAudioManager().sendSound(Sounds.YO_YO_THREE);
			break;
		case 6:
			player.setNextAnimation(Animations.YO_YO_4);
			player.getAudioManager().sendSound(Sounds.YO_YO_WIND);
			break;
		}
	}
}
