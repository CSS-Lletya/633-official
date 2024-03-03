package skills.magic.spells.modern.teleports;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 43, spellLevelRequirement = 31, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class LumbridgeTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 25, 19, new WorldTile(3222, 3222, 0), Magic.FIRE_RUNE, 1, Magic.AIR_RUNE, 3, Magic.LAW_RUNE,
				1);
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.FIRE_RUNE_554, 1), 
				new Item(ItemNames.LAW_RUNE_563, 1),
				new Item(ItemNames.AIR_RUNE_556, 3),
		};
	}
}