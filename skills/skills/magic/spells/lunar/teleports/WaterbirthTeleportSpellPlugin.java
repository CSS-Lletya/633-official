package skills.magic.spells.lunar.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 46, spellLevelRequirement = 72, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class WaterbirthTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 72, 71, new WorldTile(2546, 3758, 0), Magic.ASTRAL_RUNE, 2, Magic.LAW_RUNE, 1,
				Magic.WATER_RUNE, 1);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}