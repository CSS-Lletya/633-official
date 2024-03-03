package skills.magic.spells.modern.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 62, spellLevelRequirement = 58, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class WatchtowerTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		//inccase you wish to add restrictions like RS.
		boolean shouldntBeLocked = true;
		return shouldntBeLocked;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 58, 68, new WorldTile(2547, 3113, 2), Magic.EARTH_RUNE, 2, Magic.LAW_RUNE, 2);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}