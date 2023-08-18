package com.rs.plugin.impl.inventory;

import java.util.stream.IntStream;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;
import com.rs.utilities.RandomUtility;

@InventoryWrapper(itemId = { 2520,2521,2522,2523,2524,2525,2526, 4613, 12844 }, itemNames = {})
public class DiangoItemsItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		IntStream.of(2520,2521,2522,2523,2524,2525,2526).filter(horse -> horse == item.getId())
		.forEach(horse -> {
			if (option == 1) {
				//657 sound id if you want the horse to make a sound, but nty!
				player.getMovement().lock(2);
				int id = item.getId();
				int anim = id == 2524 ? 920 : id == 2526 ? 921 : id == 2522 ? 919 : 918;
				player.setNextAnimation(new Animation(anim));
				player.setNextForceTalk(new ForceTalk(RandomUtility.random(CHATS)));
			}
		});
		if (item.getId() == 4613) {
			if (RandomUtility.percentageChance(10)) {
				player.getAudioManager().sendSound(1, Sounds.PLATE_BREAKING);
				player.getInventory().deleteItem(new Item(4613));
				player.setNextAnimation(Animations.SPINNING_DIANGO_PLATE_FAIL);
				FloorItem.addGroundItem(new Item(4614), player, player, true, 60);
			} else {
				player.getAudioManager().sendSound(1, Sounds.PLATE_SPINNING);
				player.setNextAnimation(Animations.SPINNING_DIANGO_PLATE_SUCCESS);
			}
		}
		if (item.getId() == 12844) {
			player.setNextAnimation(Animations.FLY_TOY_KITE);
		}
	}
	
	private static final String CHATS[] = { "Come-on Dobbin, we can win the race!", "Hi-ho Silver, and away!", "Neaahhhyyy!" };
}