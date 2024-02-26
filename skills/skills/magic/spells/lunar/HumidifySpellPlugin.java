package skills.magic.spells.lunar;

import java.util.Arrays;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.FillAction.Filler;

import skills.Skills;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 29, spellLevelRequirement = 68, spellbookId = PassiveSpellListener.LUNAR, experience = 65)
public class HumidifySpellPlugin implements PassiveSpellListener {
	
	@Override
	public boolean canExecute(Player player) {
		if (!hasEmpty(player)) {
			player.getPackets().sendGameMessage("You need something which holds water in order to do this spell.");
			return false;
		}
		return hasEmpty(player);
	}

	@Override
	public void execute(Player player) {
		player.getMovement().lock(5);
		player.getAudioManager().sendSound(Sounds.HUMIDIFY_SPELL);
		player.setNextAnimation(Animations.HUMIDIFY_SPELL);
		player.setNextGraphics(Graphic.HUMIDIFY_SPELL);
		player.getSkills().addExperience(Skills.MAGIC, 65);
		Arrays.stream(Filler.values()).filter(empty -> hasEmpty(player)).forEach(empty -> {
			int amount = player.getInventory().getAmountOf(empty.getEmpty().getId());
			player.getInventory().deleteItem(new Item(empty.getEmpty().getId(), amount));
			player.getInventory().addItem(new Item(empty.getFilled().getId(), amount));
		});
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.ASTRAL_RUNE_9075, 1),
				new Item(ItemNames.WATER_RUNE_555, 3),
				new Item(ItemNames.FIRE_RUNE_554, 1)
		};
	}
	
	/**
	 * Checks if the player has bones.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	private boolean hasEmpty(Player player) {
		return Arrays.stream(Filler.values()).anyMatch(empty -> player.getInventory().containsAny(empty.getEmpty().getId()));
	}
}