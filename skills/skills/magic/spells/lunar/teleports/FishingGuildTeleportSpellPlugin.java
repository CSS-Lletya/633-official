package skills.magic.spells.lunar.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 39, spellLevelRequirement = 85, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class FishingGuildTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendLunarTeleportSpell(player, 85, 89, new WorldTile(2612, 3383, 0), Magic.ASTRAL_RUNE, 3, Magic.LAW_RUNE, 3,
				Magic.WATER_RUNE, 8);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}