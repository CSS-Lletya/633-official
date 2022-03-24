package com.rs.game.npc.combat;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.impl.DefaultCombat;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.SneakyThrows;

/**
 * TODO: Redo Drops
 * @author Dennis
 */
public final class NPCCombatDispatcher {
	
	/**
	 * The object map which contains all the mob on the world.
	 */
	private static final Object2ObjectOpenHashMap<MobCombatSignature, MobCombatInterface> COMBATANTS = new Object2ObjectOpenHashMap<>();
	
	private int mobValue = 600;
	
	/**
	 * Executes the specified mob if it's registered.
	 * @param player the player executing the mob.
	 * @param parts the string which represents a mob.
	 * @throws Exception 
	 */
	@SneakyThrows(Exception.class)
	public int customDelay(Player player, NPC npc) {
		Optional<MobCombatInterface> mobCombat = getMobCombatant(npc);
		if (!mobCombat.isPresent()) {
			DefaultCombat defaultScript = new DefaultCombat();
			return defaultScript.execute(player, npc);
		}
		mobCombat.ifPresent(value -> {
			Try.run(() -> mobValue = value.execute(player, npc));
		});
		return mobValue;
	}

	/**
	 * Gets a mob which matches the {@code identifier}.
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private Optional<MobCombatInterface> getMobCombatant(NPC mob) {
		for(Entry<MobCombatSignature, MobCombatInterface> MobCombatInterface : COMBATANTS.entrySet()) {
			if (isMobId(MobCombatInterface.getValue(), mob.getId()) || isMobNamed(MobCombatInterface.getValue(), mob)) {
				return Optional.of(MobCombatInterface.getValue());
			}
		}
		return Optional.empty();
	}
	
	private boolean isMobId(MobCombatInterface MobCombatInterface, int mobId) {
		Annotation annotation = MobCombatInterface.getClass().getAnnotation(MobCombatSignature.class);
		MobCombatSignature signature = (MobCombatSignature) annotation;
		return Arrays.stream(signature.mobId()).anyMatch(id -> mobId == id);
	}
	
	/**
	 * Checks if the the NPC Name matches the signature
	 * @param mobType
	 * @param objectId
	 * @return
	 */
	private boolean isMobNamed(MobCombatInterface mobType, NPC mob) {
		Annotation annotation = mobType.getClass().getAnnotation(MobCombatSignature.class);
		MobCombatSignature signature = (MobCombatSignature) annotation;
		return Arrays.stream(signature.mobName()).anyMatch(mobName -> mob.getDefinitions().getName().equalsIgnoreCase(mobName));
	}
	
	/**
	 * Loads all the mob into the {@link #COMBATANTS} list.
	 * <p></p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<MobCombatInterface> interfaces = Utility.getClassesInDirectory("com.rs.game.npc.combat.impl").stream().map(clazz -> (MobCombatInterface) clazz).collect(Collectors.toList());
		
		for(MobCombatInterface MobCombatInterface : interfaces) {
			if(MobCombatInterface.getClass().getAnnotation(MobCombatSignature.class) == null) {
				throw new IncompleteAnnotationException(MobCombatSignature.class, MobCombatInterface.getClass().getName() + " has no annotation.");
			}
			COMBATANTS.put(MobCombatInterface.getClass().getAnnotation(MobCombatSignature.class), MobCombatInterface);
		}
	}
	
	/**
	 * Reloads all the mob into the {@link #COMBATANTS} list.
	 * <p></p>
	 * <b>This method can be invoked on run-time to clear all the commands in the list
	 * and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		COMBATANTS.clear();
		load();
	}
	
}