package com.rs.plugin.impl.inventory;

import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = 4079, itemNames = {})
public class YoYoItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		System.out.println(option);
		switch (option) {
		case 1:
			player.setNextAnimation(new Animation(1457));
			player.getAudioManager().sendSound(Sounds.YO_YO_ONE);
			break;
		case 2:
			player.setNextAnimation(new Animation(1458));
			player.getAudioManager().sendSound(Sounds.YO_YO_TWO);
			break;
		case 3:
			player.setNextAnimation(new Animation(1459));
			player.getAudioManager().sendSound(Sounds.YO_YO_THREE);
			break;
		case 6:
			player.setNextAnimation(new Animation(1460));
			player.getAudioManager().sendSound(Sounds.YO_YO_WIND);
			break;
		}
	}
}
