package skills.magic.spells.lunar.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 53, spellLevelRequirement = 71, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class OuraniaAltarTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 71, 69, new WorldTile(2467, 3247, 0), Magic.ASTRAL_RUNE, 2, Magic.LAW_RUNE, 1,
				Magic.EARTH_RUNE, 6);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}