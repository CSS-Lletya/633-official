package skills.magic.spells.modern.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 51, spellLevelRequirement = 45, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class CamelotTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 45, 55.5, new WorldTile(2757, 3478, 0), Magic.AIR_RUNE, 5, Magic.LAW_RUNE, 1);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}