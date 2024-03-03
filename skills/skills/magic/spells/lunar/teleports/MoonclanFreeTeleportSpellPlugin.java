package skills.magic.spells.lunar.teleports;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.magic.Magic;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 38, spellLevelRequirement = 1, spellbookId = PassiveSpellListener.LUNAR, experience = 0)
public class MoonclanFreeTeleportSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		if (!player.getDetails().getLunarHomeTeleport().finished()) {
			int minutes = player.getDetails().getLunarHomeTeleport().getMinutes();
			player.getPackets().sendGameMessage("You need to wait another " + minutes  + " " + (minutes == 1 ? "minute" : "minutes") + " to cast this spell.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player player) {
		player.getDetails().getLunarHomeTeleport().start(60 * 30);
		Magic.sendLunarTeleportSpell(player, 0, 0, new WorldTile(2114, 3914, 0));
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2), 
				new Item(ItemNames.LAW_RUNE_563, 1),
				new Item(ItemNames.EARTH_RUNE_557, 2),
		};
	}
}