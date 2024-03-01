package skills.magic.spells.lunar.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 43, spellLevelRequirement = 87, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class CatherbyTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 87, 92, new WorldTile(2800, 3451, 0), Magic.ASTRAL_RUNE, 3, Magic.LAW_RUNE, 3,
				Magic.WATER_RUNE, 10);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}