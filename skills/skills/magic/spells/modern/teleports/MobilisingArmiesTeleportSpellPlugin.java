package skills.magic.spells.modern.teleports;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 37, spellLevelRequirement = 10, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class MobilisingArmiesTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		Magic.sendNormalTeleportSpell(player, 10, 19, new WorldTile(2413, 2848, 0), Magic.LAW_RUNE, 1, Magic.WATER_RUNE, 1, Magic.AIR_RUNE,
				1);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}