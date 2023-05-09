package com.rs.game.player;

import java.util.Optional;

import com.rs.game.player.content.ChargesManager;
import com.rs.game.player.type.impl.AntifireDetails;
import com.rs.utilities.MutableNumber;
import com.rs.utilities.Stopwatch;
import com.rs.utilities.Utility;

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
		pouches = new byte[4];
		creationDate = Utility.currentTimeMillis();
		runEnergy = 100;
		allowChatEffects = true;
		mouseButtons = true;
		profanityFilter = true;
		if (charges == null)
			charges = new ChargesManager();
		ownedObjectsManagerKeys = new ObjectArrayList<String>();
		passwordList = new ObjectArrayList<String>();
		ipList = new ObjectArrayList<String>();
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
	 * An array of Runecrafting pouches that possibly contain values.
	 */
	private byte[] pouches;

	/**
	 * The length of a Player being Muted (Unable to chat)
	 */
	private long muted;

	/**
	 * Length of the Player being Jailed (stuck in a remote area)
	 */
	private long jailed;
	
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
	 * An array of possible changes to the Max Cape customization
	 */
	private int[] maxedCapeCustomized;
	/**
	 * An array of possible changes to the Completionist Cape customization
	 */
	private int[] completionistCapeCustomized;

	/**
	 * Represents if the Player should be using older item models to display
	 */
	private boolean oldItemsLook;

	/**
	 * Represents the default Yell color for a Player
	 */
	private String yellColor = "ff0000";

	/**
	 * The creation date
	 */
	private long creationDate;

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
	
	/**
	 * The length of a Players ban
	 */
	private long banned;
	
	/**
	 * Is the Player permanently banned
	 */
	private boolean permBanned;
	
	/**
	 * Does the Player have their Yell turned off
	 */
	private boolean yellOff;
	
	// game bar status
	private byte publicStatus;
	private byte clanStatus;
	private byte tradeStatus;
	private byte assistStatus;

	private byte summoningLeftClickOption;
	
	// Used for storing recent ips and password
	private ObjectArrayList<String> passwordList = new ObjectArrayList<String>();
	private ObjectArrayList<String> ipList = new ObjectArrayList<String>();

	/**
	 * Represents an instance of the Players Charge handler
	 */
	private ChargesManager charges;

	private String currentFriendChatOwner;
	private String clanName;
	private boolean connectedClanChannel;
	
	/**
	 * The Skull ID displayed for PVP interactions
	 * 0, 1, 2
	 */
	private byte skullId;

	/**
	 * A list of Player owned Objects
	 */
	private ObjectArrayList<String> ownedObjectsManagerKeys;

	/**
	 * Mutable values stored for specified uses
	 */
	private final MutableNumber poisonImmunity = new MutableNumber(), skullTimer = new MutableNumber(), teleBlockDelay = new MutableNumber(), prayerDelay = new MutableNumber();

	/**
	 * Holds an optional wrapped inside the Antifire details.
	 */
	private Optional<AntifireDetails> antifireDetails = Optional.empty();
	
	/**
	 * A collection of stopwatch timers
	 */
	private Stopwatch boneBury = new Stopwatch(), thievingStun = new Stopwatch(), drinks = new Stopwatch(), food = new Stopwatch(), tolerance = new Stopwatch();
}