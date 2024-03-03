package skills.magic.spells.modern.teleports;

import com.rs.GameConstants;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import skills.magic.TeleportType;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 24, spellLevelRequirement = 1, spellbookId = PassiveSpellListener.MODERN, experience = 0)
public class HomeTeleportFreeTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		return true;
	}

	@Override
	public void execute(Player player) {
		TeleportType.MODERN_HOME.checkSpecialCondition(player, GameConstants.START_PLAYER_LOCATION);
	}

	@Override
	public Item[] runes() {
		return new Item[] {};
	}
}