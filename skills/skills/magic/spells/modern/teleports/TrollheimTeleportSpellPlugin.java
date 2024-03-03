package skills.magic.spells.modern.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 69, spellLevelRequirement = 61, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class TrollheimTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return false;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 61, 68, new WorldTile(2888, 3674, 0), Magic.FIRE_RUNE, 2, Magic.LAW_RUNE, 2);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}