package skills.magic.spells.lunar;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 30, spellLevelRequirement = 71, spellbookId = PassiveSpellListener.LUNAR, experience = 70)
public class HunterKitSpellPlugin implements PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		if (!player.getInventory().hasFreeSlots())
			player.getPackets().sendGameMessage(Inventory.INVENTORY_FULL_MESSAGE);
		return player.getInventory().hasFreeSlots();
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock(5);
		player.getAudioManager().sendSound(Sounds.HUNTER_KIT);
		player.setNextAnimation(Animations.HUNTER_KIT);
		player.setNextGraphics(Graphic.HUNTER_KIT);
		player.getInventory().addItem(new Item(ItemNames.HUNTER_KIT_11159));
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2),
				new Item(ItemNames.EARTH_RUNE_557, 2),
		};
	}
}