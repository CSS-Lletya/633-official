package com.rs.game.player;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
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
		herbicideSettings = new boolean[17];
		holidayItems = new ItemsContainer<Item>(48, false);
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
	 * A collection of toggles for herbs to be incinerated
	 */
	private boolean[] herbicideSettings;
	
	/**
	 * Represents the last known npc interacted with. Useful for dialogues that aren't aligned normally. (Example: Pet puppy picking)
	 */
	private int lastNPCInteracted;
	
	/**
	 * Represents the last known skill menu opened, used to enter the full skill guide interface from the level up interface.
	 */
	private int lastSkillMenu;
	 
	/**
	 * Mutable values stored for specified uses It's more ideal to use this system
	 * for easy incrementing, such.
	 */
	private MutableNumber poisonImmunity = new MutableNumber(), skullId = new MutableNumber(),
			questPoints = new MutableNumber(), skullTimer = new MutableNumber(), teleBlockDelay = new MutableNumber(),
			prayerDelay = new MutableNumber(), essenceTeleporter = new MutableNumber(),
			gravestone = new MutableNumber(), censoredWordCount = new MutableNumber(), incubatorTimer = new MutableNumber(),
			strongholdLevels = new MutableNumber(), skullSceptreCharges = new MutableNumber(), coalBagSize = new MutableNumber(81), boneType = new MutableNumber(),
			playTime = new MutableNumber(), daysBanned = new MutableNumber(), daysMuted = new MutableNumber(), karambwanjiStock = new MutableNumber(),
			mediumRCPouchUses = new MutableNumber(45), largeRCPouchUses = new MutableNumber(29), giantRCPouchUses = new MutableNumber(11),
			ringOfForging = new MutableNumber(140), threadsUsed = new MutableNumber(5), gnomeAgilityStage = new MutableNumber(1);
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
	private SecondsTimer homeDelay = new SecondsTimer(), chargeDelay = new SecondsTimer(), recoverSpecialPotion = new SecondsTimer(), magicImbue = new SecondsTimer(), lunarHomeTeleport = new SecondsTimer();
	
	/**
	 * A collection of non save-based timers
	 */
	private transient SecondsTimer componentLockTimer = new SecondsTimer(), vengTimer = new SecondsTimer();
	
	private DynamicBoolean completedFightCaves = new DynamicBoolean(false), experienceLocked = new DynamicBoolean(false), ownsHouse = new DynamicBoolean(false),
			disableEquip = new DynamicBoolean(false), augPrayerUnlocked = new DynamicBoolean(false), rigourPrayerUnlocked = new DynamicBoolean(false), renewalPrayerUnlocked = new DynamicBoolean(false),
			bonesGrinded = new DynamicBoolean(false), disableDeathPopup = new DynamicBoolean(false), toggleLootShare = new DynamicBoolean(),
			allowChatEffects = new DynamicBoolean(), acceptAid = new DynamicBoolean(),
			mouseButtons = new DynamicBoolean(), profanityFilter = new DynamicBoolean(),
			questSort = new DynamicBoolean(), hideCompletedQuests = new DynamicBoolean(), filterGame = new DynamicBoolean(),
			forceNextMapLoadRefresh = new DynamicBoolean(), canPvp = new DynamicBoolean(), invulnerable = new DynamicBoolean(false)
	;
	
	/**
	 * A list of last known ips from the Player
	 */
	private ObjectArrayList<String> ipList = new ObjectArrayList<String>();
	
	public ItemsContainer<Item> holidayItems = new ItemsContainer<Item>(48, false);
	
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
}