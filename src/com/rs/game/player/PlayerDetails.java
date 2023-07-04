package com.rs.game.player;

import java.util.Optional;

import com.rs.game.player.type.impl.AntifireDetails;
import com.rs.utilities.DynamicBoolean;
import com.rs.utilities.MutableNumber;
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
		allowChatEffects = true;
		mouseButtons = true;
		profanityFilter = true;
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
	 * Represents if a Player is filtering out their chatbox messages
	 */
	private boolean filterGame;
	
	/**
	 * Represents if the Player has their experience locked
	 */
	private boolean xpLocked;
	
	/**
	 * The Run Engery amount a Player has
	 */
	private double runEnergy;

	/**
	 * Should the Player use Chat effects for overhead text
	 */
	private boolean allowChatEffects;
	
	/**
	 * Should the Player accept aid from other Players
	 */
	private boolean acceptAid;
	
	/**
	 * Should the Player play with 2 mouse button mode
	 */
	private boolean mouseButtons;
	
	/**
	 * Should the Player have their profanity filter on
	 */
	private boolean profanityFilter;
	
	/**
	 * Should the Player have their next map refreshed
	 */
	private boolean forceNextMapLoadRefresh;
	
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
	 * Represents toggle features for the Quest interface
	 */
	private boolean sort, hideDone;
	
	/**
	 * A collection of 'seen' Dungeoneering resource areas
	 */
	private boolean[] seenDungeon;
	
	/**
	 * Represents the state of loot sharing in a Clan Chat
	 */
	private boolean toogleLootShare;
	
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
	
	private DynamicBoolean completedFightCaves = new DynamicBoolean(false);
}