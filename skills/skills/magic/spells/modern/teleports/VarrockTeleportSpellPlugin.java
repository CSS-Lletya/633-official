package skills.magic.spells.modern.teleports;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 40, spellLevelRequirement = 25, spellbookId = PassiveSpellListener.MODERN, experience = 19)
public class VarrockTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(3212, 3429, 0));
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.AIR_RUNE_556, 3), 
				new Item(ItemNames.LAW_RUNE_563, 1),
				new Item(ItemNames.FIRE_RUNE_554, 1),
		};
	}
}