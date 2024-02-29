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
 * @author Dennis
 */
@PassiveSpellSignature(spellButton = 35, spellLevelRequirement = 82, spellbookId = PassiveSpellListener.LUNAR, experience = 86)
public class MagicImbueSpellPlugin extends PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		if (!player.getDetails().getMagicImbue().finished()) {
			player.getPackets().sendGameMessage("You already have this activated.");
			return false;
		}
		return true;
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock(2);
		player.getAudioManager().sendSound(Sounds.IMBUE_RUNES);
		player.setNextAnimation(Animations.LUNAR_MAGIC_IMBUE);
		player.setNextGraphics(Graphic.LUNAR_MAGIC_IMBUE);
		player.getAttributes().get(Attribute.MAGIC_IMBUED).set(true);
		player.getDetails().getMagicImbue().start(20);
		player.getPackets().sendGameMessage("You are charged to combine runes!");
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 2),
				new Item(ItemNames.FIRE_RUNE_554, 7),
				new Item(ItemNames.WATER_RUNE_555, 7),
		};
	}
}