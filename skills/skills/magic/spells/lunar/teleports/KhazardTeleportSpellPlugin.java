package skills.magic.spells.lunar.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 40, spellLevelRequirement = 78, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class KhazardTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 78, 80, new WorldTile(2635, 3166, 0), Magic.ASTRAL_RUNE, 2, Magic.LAW_RUNE, 2,
				Magic.WATER_RUNE, 4);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}