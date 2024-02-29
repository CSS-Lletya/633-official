package skills.magic.spells.lunar.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 42, spellLevelRequirement = 69, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class MoonclanTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 69, 66, new WorldTile(2114, 3914, 0), Magic.ASTRAL_RUNE, 2, Magic.LAW_RUNE, 1,
				Magic.EARTH_RUNE, 2);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}