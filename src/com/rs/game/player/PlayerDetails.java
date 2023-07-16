package com.rs.game.player;

import java.util.Optional;

import com.rs.game.player.type.impl.AntifireDetails;
import com.rs.utilities.DynamicBoolean;
import com.rs.utilities.MutableNumber;
import com.rs.utilities.SecondsTimer;
import com.rs.utilities.Stopwatch;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;

/**
 * All personal variables of the Player shall be stored here for easier access.
 * 
 * @author Dennis
 *
 */
@Data
public final class PlayerDetails {

	/**
	 * Constructs a new Player's details
	 */
	public PlayerDetails() {
		runEnergy = 100;
		ipList = new ObjectArrayList<String>();
		seenDungeon = new boolean[16];
		statistics = new Statistics();
	}

	/**
	 * Represents a Players password for login
	 */
	private String password;
	
	/**
	 * The amount of authority this player has over others.
	 */
	private Rights rights = Rights.PLAYER;
	
	/**
	 * Represents the last known IP from the Player
	 */
	private String lastIP;
	
	/**
	 * The Run Engery amount a Player has
	 */
	private double runEnergy;
	
	// game bar status
	private byte publicStatus;
	private byte clanStatus;
	private byte tradeStatus;
	private byte assistStatus;

	/**
	 * Represents the Summoning quick-action selected from menu
	 */
	private int summoningLeftClickOption;

	/**
	 * Represents the current Friends chat owner
	 */
	private String currentFriendChatOwner;
	
	/**
	 * A collection of 'seen' Dungeoneering resource areas
	 */
	private boolean[] seenDungeon;
	
	/**
	 * Mutable values stored for specified uses It's more ideal to use this system
	 * for easy incrementing, such.
	 */
	private final MutableNumber poisonImmunity = new MutableNumber(), skullId = new MutableNumber(),
			questPoints = new MutableNumber(), skullTimer = new MutableNumber(), teleBlockDelay = new MutableNumber(),
			chargeDelay = new MutableNumber(), prayerDelay = new MutableNumber(), essenceTeleporter = new MutableNumber(),
			gravestone = new MutableNumber(), censoredWordCount = new MutableNumber(), incubatorTimer = new MutableNumber(),
			strongholdLevels = new MutableNumber(), skullSceptreCharges = new MutableNumber();

	/**
	 * Holds an optional wrapped inside the Anti-fire details.
	 */
	private Optional<AntifireDetails> antifireDetails = Optional.empty();
	
	/**
	 * A collection of stopwatch timers that create delays within certain events
	 */
	private Stopwatch boneBury = new Stopwatch(), thievingStun = new Stopwatch(), drinks = new Stopwatch(), food = new Stopwatch(), tolerance = new Stopwatch();

	/**
	 * A collection of Seconds timer
	 */
	private SecondsTimer homeDelay = new SecondsTimer();
	
	/**
	 * A list of last known ips from the Player
	 */
	private ObjectArrayList<String> ipList = new ObjectArrayList<String>();
	
	/**
	 * Represents a Static for the player
	 */
	private Statistics statistics;
	
	/**
	 * Total player weight.
	 * 
	 * @return the weight as a Double Integer.
	 */
	public double getWeight(Player player) {
		return player.getInventory().getInventoryWeight() + player.getEquipment().getEquipmentWeight();
	}
	
	private DynamicBoolean completedFightCaves = new DynamicBoolean(false), experienceLocked = new DynamicBoolean(false), ownsHouse = new DynamicBoolean(false),
			disableEquip = new DynamicBoolean(false),
			toggleLootShare = new DynamicBoolean(),
			allowChatEffects = new DynamicBoolean(), acceptAid = new DynamicBoolean(),
			mouseButtons = new DynamicBoolean(), profanityFilter = new DynamicBoolean(),
			questSort = new DynamicBoolean(), hideCompletedQuests = new DynamicBoolean(), filterGame = new DynamicBoolean(),
			forceNextMapLoadRefresh = new DynamicBoolean(), canPvp = new DynamicBoolean(), invulnerable = new DynamicBoolean();
}