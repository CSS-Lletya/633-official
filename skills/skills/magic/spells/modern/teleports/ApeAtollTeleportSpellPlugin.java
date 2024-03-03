package skills.magic.spells.modern.teleports;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 72, spellLevelRequirement = 64, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class ApeAtollTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return false;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 64, 76, new WorldTile(2776, 9103, 0), Magic.FIRE_RUNE, 2, Magic.WATER_RUNE, 2, Magic.LAW_RUNE,
				2, ItemNames.BANANA_1963, 1);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}