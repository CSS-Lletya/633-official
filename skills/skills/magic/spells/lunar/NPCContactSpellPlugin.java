package skills.magic.spells.lunar;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 26, spellLevelRequirement = 67, spellbookId = PassiveSpellListener.LUNAR, experience = 63)
public class NPCContactSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock(5);
		player.getDetails().getComponentLockTimer().start(5);
		player.getInterfaceManager().sendInterface(429);
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 1),
				new Item(ItemNames.AIR_RUNE_556, 2),
				new Item(ItemNames.COSMIC_RUNE_564, 1),
		};
	}
}