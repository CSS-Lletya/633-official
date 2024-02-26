package skills.magic.spells.lunar;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 36, spellLevelRequirement = 94, spellbookId = PassiveSpellListener.LUNAR, experience = 112)
public class VengeanceSpellPlugin implements PassiveSpellListener {

	@Override
	public boolean canExecute(Player player) {
		if (!player.getDetails().getVengTimer().finished()) {
			player.getPackets().sendGameMessage("You can only cast vengeance spell every 30 seconds.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player player) {
		player.setNextAnimation(Animations.LUANR_VENG);
		player.setNextGraphics(Graphic.LUNAR_VENG);
		player.getAudioManager().sendSound(Sounds.VENGEANCE_SPELL);
		player.getDetails().getVengTimer().start(30);
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 4), 
				new Item(ItemNames.DEATH_RUNE_560, 2),
				new Item(ItemNames.EARTH_RUNE_557, 10),
		};
	}
}