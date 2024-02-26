package skills.magic.spells;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import skills.Skills;

/**
 * @author Dennis
 * Since this is Player specific, need to create an instance of this class 
 * instead of static so it won't affect other players.
 */
public class PassiveSpellDispatcher {

	public static void execute(Player player, int spellButton) {
		Optional<PassiveSpellListener> spell = getVerifiedSpell(player, spellButton);
		spell.filter(caster -> canCast(player, spell, spellButton)).ifPresent(caster -> executeCast(player, spellButton));
	}

	private static boolean canCast(Player player, Optional<PassiveSpellListener> spell, int spellButton) {
	    if (!spell.isPresent()) {
	        return false;
	    }

	    PassiveSpellListener caster = spell.get();
	    Optional<PassiveSpellListener> verifiedSpell = getVerifiedSpell(player, spellButton);

	    if (!verifiedSpell.isPresent() || !verifiedSpell.get().canExecute(player)) {
	        return false;
	    }

	    int requiredMagicLevel = getSpellLevelRequirement(caster);
	    int playerMagicLevel = player.getSkills().getLevel(Skills.MAGIC);

	    if (playerMagicLevel < requiredMagicLevel) {
	        player.getPackets().sendGameMessage("You don't have the required Magic level to cast this spell.");
	        return false;
	    }

	    boolean hasRequiredRunes = Arrays.stream(caster.runes())
	            .allMatch(rune -> player.getInventory().containsItem(rune.getId(), rune.getAmount()));

	    if (!hasRequiredRunes) {
	        player.getPackets().sendGameMessage("You don't have the required amount of Runes to cast this spell.");
	        return false;
	    }

	    return true;
	}


	private static void executeCast(Player player, int spellButton) {
		getVerifiedSpell(player, spellButton).ifPresent(caster -> {
			Arrays.stream(caster.runes()).forEach(rune -> player.getInventory().deleteItem(new Item(rune.getId(), rune.getAmount())));
			player.getSkills().addExperience(Skills.MAGIC, getExperienceGiven(player, caster));
			caster.execute(player);
		});
	}

	private static int getSpellLevelRequirement(PassiveSpellListener spell) {
		PassiveSpellSignature signature = spell.getClass().getAnnotation(PassiveSpellSignature.class);
		return signature.spellLevelRequirement();
	}

	private static Optional<PassiveSpellListener> getVerifiedSpell(Player player, int id) {
	    return SPELLS.entrySet().stream()
	            .filter(entry -> isSpellButton(player, entry.getValue(), id))
	            .map(Map.Entry::getValue)
	            .findFirst();
	}

	private static boolean isSpellButton(Player player, PassiveSpellListener spell, int spellButton) {
		PassiveSpellSignature signature = spell.getClass().getAnnotation(PassiveSpellSignature.class);
		return signature.spellButton() == spellButton && player.getCombatDefinitions().spellBook == signature.spellbookId();
	}

	private static double getExperienceGiven(Player player, PassiveSpellListener spell) {
		PassiveSpellSignature signature = spell.getClass().getAnnotation(PassiveSpellSignature.class);
		return signature.experience();
	}
	
	public static void load() {
		List<PassiveSpellListener> modernSpellLoader = Utility.getClassesInDirectory("skills.magic.spells.modern").stream()
				.map(clazz -> (PassiveSpellListener) clazz).collect(Collectors.toList());
		modernSpellLoader.forEach(spell -> SPELLS.put(spell.getClass().getAnnotation(PassiveSpellSignature.class), spell));
		List<PassiveSpellListener> lunarSpellLoader = Utility.getClassesInDirectory("skills.magic.spells.lunar").stream()
				.map(clazz -> (PassiveSpellListener) clazz).collect(Collectors.toList());
		lunarSpellLoader.forEach(spell -> SPELLS.put(spell.getClass().getAnnotation(PassiveSpellSignature.class), spell));
	}

	private static final Object2ObjectOpenHashMap<PassiveSpellSignature, PassiveSpellListener> SPELLS = new Object2ObjectOpenHashMap<>();
	
	public void reload() {
		SPELLS.clear();
		load();
	}
}