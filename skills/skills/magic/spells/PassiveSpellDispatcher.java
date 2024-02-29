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
import skills.magic.spells.lunar.PlankMakeSpellPlugin;

/**
 * @author Dennis
 * Since this is Player specific, need to create an instance of this class 
 * instead of static so it won't affect other players.
 */
public class PassiveSpellDispatcher {

	/**
	 * Executes a standard button click spell.
	 * @param player
	 * @param spellButton
	 */
	public static void execute(Player player, int spellButton) {
		Optional<PassiveSpellListener> spell = getVerifiedSpell(player, spellButton);
		spell.filter(caster -> canCast(player, spell, spellButton, null)).ifPresent(caster -> executeCast(player, spellButton));
	}

	/**
	 * Executes a Interface on Interface spell. (Magic interface on Inventory, like {@link PlankMakeSpellPlugin}).
	 * @param player
	 * @param spellButton
	 * @param item
	 * @param slot
	 */
	public static void executeSpellOnItem(Player player, int spellButton, Item item, int slot) {
		Optional<PassiveSpellListener> spell = getVerifiedSpell(player, spellButton);
		spell.filter(caster -> canCast(player, spell, spellButton, item)).ifPresent(caster -> executeCast(player, spellButton, item, slot));
	}
	
	private static boolean canCast(Player player, Optional<PassiveSpellListener> spell, int spellButton, Item item) {
	    if (!spell.isPresent()) {
	        return false;
	    }

	    PassiveSpellListener caster = spell.get();
	    Optional<PassiveSpellListener> verifiedSpell = getVerifiedSpell(player, spellButton);

	    if (!verifiedSpell.isPresent())
	    	return false;

	    int requiredMagicLevel = getSpellLevelRequirement(caster);
	    int playerMagicLevel = player.getSkills().getLevel(Skills.MAGIC);

	    if (player.getMovement().isLocked())
	    	return false;
	    
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
	    
	    if (!verifiedSpell.get().canExecute(player) && getSpellBook(player, caster) == PassiveSpellListener.MODERN) {
	    	return false;
	    }
	    if (!verifiedSpell.get().canExecute(player, item) && getSpellBook(player, caster) == PassiveSpellListener.LUNAR) {
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

	private static void executeCast(Player player, int spellButton, Item item, int slot) {
		getVerifiedSpell(player, spellButton).ifPresent(caster -> {
			Arrays.stream(caster.runes()).forEach(rune -> player.getInventory().deleteItem(new Item(rune.getId(), rune.getAmount())));
			player.getSkills().addExperience(Skills.MAGIC, getExperienceGiven(player, caster));
			caster.execute(player, item, slot);
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
	
	private static int getSpellBook(Player player, PassiveSpellListener spell) {
		PassiveSpellSignature signature = spell.getClass().getAnnotation(PassiveSpellSignature.class);
		return player.getCombatDefinitions().spellBook == signature.spellbookId() ? signature.spellbookId() : -1;
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