package skills.magic.spells.lunar.teleports;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 22, spellLevelRequirement = 76, spellbookId = PassiveSpellListener.LUNAR, experience = 76)
public class BarbarianTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2543, 3569, 0));
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2), 
				new Item(ItemNames.LAW_RUNE_563, 1),
				new Item(ItemNames.FIRE_RUNE_554, 3),
		};
	}
}