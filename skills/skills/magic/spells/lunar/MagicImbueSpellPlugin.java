package skills.magic.spells.lunar;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;

import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

/**
 * TODO: Combination runes
 * @author Dennis
 *
 */
@PassiveSpellSignature(spellButton = 35, spellLevelRequirement = 82, spellbookId = PassiveSpellListener.LUNAR, experience = 86)
public class MagicImbueSpellPlugin implements PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		if (!player.getDetails().getMagicImbue().finished()) {
			player.getPackets().sendGameMessage("You already have this activated.");
			return false;
		}
		return player.getDetails().getMagicImbue().finished();
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock(2);
		player.getAudioManager().sendSound(Sounds.IMBUE_RUNES);
		player.setNextAnimation(Animations.MAGIC_IMBUE);
		player.setNextGraphics(Graphic.MAGIC_IMBUE);
		player.getAttributes().get(Attribute.MAGIC_IMBUED).set(true);
		player.getDetails().getMagicImbue().start(12);
		player.getPackets().sendGameMessage("You are charged to combine runes!");
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2),
				new Item(ItemNames.EARTH_RUNE_557, 2),
		};
	}
}