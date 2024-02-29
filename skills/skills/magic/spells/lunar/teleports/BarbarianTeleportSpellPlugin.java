package skills.magic.spells.lunar.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 22, spellLevelRequirement = 76, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class BarbarianTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 75, 76, new WorldTile(2543, 3569, 0), Magic.ASTRAL_RUNE, 2, Magic.LAW_RUNE, 1, Magic.FIRE_RUNE,
				3);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}