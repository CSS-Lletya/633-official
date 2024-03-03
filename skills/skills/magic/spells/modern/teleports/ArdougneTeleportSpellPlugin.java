package skills.magic.spells.modern.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 57, spellLevelRequirement = 51, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class ArdougneTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 51, 61, new WorldTile(2664, 3305, 0), Magic.WATER_RUNE, 2, Magic.LAW_RUNE, 2);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}