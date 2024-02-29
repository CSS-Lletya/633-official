package skills.magic.spells.modern;

import java.util.Arrays;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import skills.Skills;
import skills.magic.spells.PassiveSpellListener;
import skills.magic.spells.PassiveSpellSignature;

@PassiveSpellSignature(spellButton = 65, spellLevelRequirement = 60, spellbookId = PassiveSpellListener.MODERN, experience = 35.5)
public class BonesToPeachesSpellPlugin extends PassiveSpellListener {

	private static final int[] BONES = new int[] {526, 532};
	
	@Override
	public boolean canExecute(Player player) {
		if (!hasBones(player)) {
			player.getPackets().sendGameMessage("You aren't holding any bones!");
			return false;
		}
		return hasBones(player);
	}

	@Override
	public void execute(Player player) {
		if (hasBones(player)) {
			player.getAudioManager().sendSound(Sounds.BONES_TO);
			player.setNextAnimation(Animations.ITEM_SPELL_CONVERTING);
			player.setNextGraphics(Graphic.BONES_TO_SPELL);
			player.getSkills().addExperience(Skills.MAGIC, 35.5);
		}
		Arrays.stream(BONES).filter(bone -> player.getInventory().containsAny(bone)).forEach(bone -> {
			int amount = player.getInventory().getAmountOf(bone);
			player.getInventory().deleteItem(new Item(bone, amount));
			player.getInventory().addItem(new Item(ItemNames.PEACH_6883, amount));
			player.getDetails().getStatistics()
			.addStatistic(ItemDefinitions.getItemDefinitions(bone).getName() + "_To_Peaches")
			.addStatistic("To_Peaches_Spell");
		});
	}

	@Override
	public Item[] runes() {
		return new Item[] {
				new Item(ItemNames.EARTH_RUNE_557, 4),
				new Item(ItemNames.WATER_RUNE_555, 4),
				new Item(ItemNames.NATURE_RUNE_561, 2)
		};
	}
	
	/**
	 * Checks if the player has bones.
	 * @param player the player.
	 * @return {@code True} if so.
	 */
	private boolean hasBones(Player player) {
		return Arrays.stream(BONES).anyMatch(bone -> player.getInventory().containsAny(bone));
	}
}