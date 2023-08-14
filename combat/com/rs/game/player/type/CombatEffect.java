package com.rs.game.player.type;

import java.util.Collection;
import java.util.Collections;

import com.rs.game.Entity;
import com.rs.game.map.World;
import com.rs.game.task.impl.CombatEffectTask;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

/**
 * Some sort of temporary effect applied to a {@link Actor} during
 * combat. Combat effects include but are not limited to; being poisoned,
 * skulled, and teleblocked.
 * @author lare96 <http://github.org/lare96>
 */
public abstract class CombatEffect {
	
	/**
	 * The map of all of the combat effect types mapped to their respective
	 * listeners.
	 */
	public static final Object2ObjectArrayMap<CombatEffectType, CombatEffect> EFFECTS = new Object2ObjectArrayMap<>();
	
	/**
	 * The delay for this individual combat effect.
	 */
	private final int delay;
	
	/**
	 * Creates a new {@link CombatEffect}.
	 * @param delay the delay for this combat effect.
	 */
	public CombatEffect(int delay) {
		this.delay = delay;
	}
	
	/**
	 * The static initialization block that is used to populate the list with
	 * our combat effect listeners.
	 */
	static {
		CombatEffectType.TYPES.forEach($it -> EFFECTS.put($it, $it.getEffect()));
	}
	
	/**
	 * Starts this combat effect by scheduling a task utilizing the abstract
	 * methods in this class.
	 * @param c the character this combat effect is for.
	 */
	public final boolean start(Entity c) {
		if(apply(c)) {
			World.get().submit(new CombatEffectTask(c, this));
			return true;
		}
		return false;
	}
	
	/**
	 * Applies this effect to {@code c}.
	 * @param c the character this combat effect is for.
	 * @return {@code true} if the effect could be applied, {@code false}
	 * otherwise.
	 */
	public abstract boolean apply(Entity c);
	
	/**
	 * Removes this effect from {@code c} if needed.
	 * @param c the character this combat effect is for.
	 * @return {@code true} if this effect should be stopped, {@code false}
	 * otherwise.
	 */
	public abstract boolean removeOn(Entity c);
	
	/**
	 * Provides processing for this effect on {@code c}.
	 * @param c the character this combat effect is for.
	 */
	public abstract void process(Entity c);
	
	/**
	 * Executed on login, primarily used to re-apply the effect to {@code c}.
	 * @param c the character this combat effect is for.
	 * @return {@code true} if the effect should be re-applied, {@code false}
	 * otherwise.
	 */
	public abstract boolean onLogin(Entity c);
	
	/**
	 * Gets the delay for this individual combat effect.
	 * @return the delay for this effect.
	 */
	public final int getDelay() {
		return delay;
	}
	
	/**
	 * Returns an unmodifiable view of the combat effect listeners.
	 * @return the combat effect listeners.
	 */
	public static Collection<CombatEffect> values() {
		return Collections.unmodifiableCollection(EFFECTS.values());
	}
}
