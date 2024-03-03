package skills.magic.spells.modern.teleports;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 69, spellLevelRequirement = 61, spellbookId = PassiveSpellListener.MODERN, experience = 68)
public class TrollheimTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return false;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2888, 3674, 0));
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.LAW_RUNE_563, 2),
				new Item(ItemNames.FIRE_RUNE_554, 2),
		};
	}
}