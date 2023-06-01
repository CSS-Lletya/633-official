package skills.firemaking;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

/**
 * The enumerated type whose elements represent the data for lighting logs.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public enum LogType {
	LOG(1511, 1, 30, 40),
	ACHEY(2862, 1, 48, 40),
	OAK(1521, 15, 56, 60),
	WILLOW(1519, 30, 66, 90),
	TEAK(6333, 35, 79, 105),
	ARCTIC_PINE(10810, 42, 84, 125),
	MAPLE(1517, 45, 95, 135),
	MAHOGANY(6332, 50, 103, 157.5),
	EUCALYPTUS(12581, 58, 114, 193.5),
	YEW(1515, 60, 126, 202.5),
	MAGIC(1513, 75, 140, 303.8);
	
	/**
	 * Caches our enum values.
	 */
	private static final ImmutableSet<LogType> VALUES = Sets.immutableEnumSet(EnumSet.allOf(LogType.class));
	
	/**
	 * The identifier for the log.
	 */
	private final Item log;
	
	/**
	 * The requirement required for lighting this log.
	 */
	private final int requirement;
	
	/**
	 * The identifier which identifies for how long this log will burn.
	 */
	private final int timer;
	
	/**
	 * The experience received for lighting this log.
	 */
	private final double experience;
	
	/**
	 * Constructs a new {@link LogType}.
	 * @param logId {@link #log}.
	 * @param requirement {@link #requirement}.
	 * @param timer {@link #timer}.
	 * @param experience {@link #experience}.
	 */
	LogType(int logId, int requirement, int timer, double experience) {
		this.log = new Item(logId);
		this.requirement = requirement;
		this.timer = timer;
		this.experience = experience;
	}
	
	public static Optional<LogType> getDefinition(int id) {
		return VALUES.stream().filter(def -> def.log.getId() == id).findAny();
	}
	
	public static Optional<LogType> getDefinition(Player player) {
		return VALUES.stream().filter(i -> player.getInventory().containsItem(i.getLog())).findAny();
	}
	
	/**
	 * Gets the definition for this log type.
	 * @param id the identifier to check for matches.
	 * @param secondId the second identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	protected static Optional<LogType> getDefinition(int id, int secondId) {
		return VALUES.stream().filter(def -> def.log.getId() == id || def.log.getId() == secondId).findAny();
	}
	
	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}
	
	/**
	 * @return {@link #log}.
	 */
	public Item getLog() {
		return log;
	}
	
	/**
	 * @return {@link #requirement}.
	 */
	public int getRequirement() {
		return requirement;
	}
	
	/**
	 * @return {@link #timer}.
	 */
	public int getTimer() {
		return timer;
	}
	
	/**
	 * @return {@link #experience}.
	 */
	public double getExperience() {
		return experience;
	}
}