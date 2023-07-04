package com.rs.game.player;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import com.alex.utils.VarsManager;
import com.rs.GameConstants;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.content.mapzone.MapZone;
import com.rs.content.mapzone.MapZoneManager;
import com.rs.content.quests.QuestManager;
import com.rs.game.Entity;
import com.rs.game.EntityType;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.ScriptDialogueInterpreter;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.game.movement.route.CoordsEvent;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.other.Gravestone;
import com.rs.game.npc.other.Pet;
import com.rs.game.player.actions.ActionManager;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.DayOfWeekManager;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.MusicsManager;
import com.rs.game.player.content.Notes;
import com.rs.game.player.content.PriceCheckManager;
import com.rs.game.player.content.pet.PetManager;
import com.rs.game.player.content.trails.PuzzleBox;
import com.rs.game.player.content.trails.Puzzles;
import com.rs.game.player.content.trails.TreasureTrailsManager;
import com.rs.game.player.spells.passive.PassiveSpellDispatcher;
import com.rs.game.player.type.CombatEffect;
import com.rs.game.task.impl.CombatEffectTask;
import com.rs.game.task.impl.OverloadEffectTask;
import com.rs.game.task.impl.SkillActionTask;
import com.rs.net.IsaacKeyPair;
import com.rs.net.LogicPacket;
import com.rs.net.Session;
import com.rs.net.encoders.WorldPacketsEncoder;
import com.rs.net.encoders.other.HintIconsManager;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import skills.Skills;
import skills.prayer.book.PrayerManager;

/**
 * Represents a Player & all of their attributes
 * @author Dennis
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Player extends Entity {

	/**
	 * The Player's Username
	 */
	private transient String username;
	
	/**
	 * The Player's Session
	 */
	private transient Session session;
	
	/**
	 * The Client Region Map Loading state
	 */
	private transient boolean clientLoadedMapRegion;
	
	/**
	 * Display Mode Type (Fixed, Resize, Fullscreen)
	 */
	private transient byte displayMode;
	
	/**
	 * Client Screen Width
	 */
	private transient short screenWidth;
	
	/**
	 * Client Screen Height
	 */
	private transient short screenHeight;
	
	/**
	 * Represents a Movement type
	 */
	private transient byte temporaryMovementType;
	
	/**
	 * Should we update the Movement state
	 */
	private transient boolean updateMovementType;
	
	/**
	 * Has the Player started their {@link #session}
	 */
	private transient boolean started;
	
	/**
	 * Is the Player's {@link #session} currently Running
	 */
	private transient boolean running;
	
	/**
	 * The type of Resting state of a Player
	 * Example: Not, Sitting, idle
	 */
	private transient byte resting;
	
	/**
	 * Can the Player engage in PVP combat
	 */
	private transient boolean canPvp;
	
	/**
	 * Does the Player have permission to Trade others
	 */
	private transient boolean cantTrade;
	
	/**
	 * Represents a Runnable event that takes place when closing an interface
	 */
	private transient Runnable closeInterfacesEvent;
	
	/**
	 * The last known time length of the last message a Player sends
	 */
	private transient long lastPublicMessage;
	
	/**
	 * The Item switching cache (Switches for PVE/PVP)
	 */
	private transient ObjectArrayList<Byte> switchItemCache;
	
	/**
	 * Does the Player have their Equipping/Removing disabled
	 */
	private transient boolean disableEquip;
	
	/**
	 * Does the Player become invulnerable to any damage.
	 */
	private transient boolean invulnerable;
	
	/**
	 * Is the Player finishing their {@link #session}
	 */
	private transient boolean finishing;
	
	/**
	 * Represents a Player's Interface management system
	 */
	private transient InterfaceManager interfaceManager;
	
	/**
	 * Represents a Player's Hint Icon management system
	 */
	private transient HintIconsManager hintIconsManager;
	
	/**
	 * Represents a Player's Price Checker's system
	 */
	private transient PriceCheckManager priceCheckManager;
	
	/**
	 * Represents a Player's Route (movement) management system
	 */
	private transient RouteEvent routeEvent;
	
	/**
	 * Represents a Player's current Friends Chat (FC)
	 */
	private transient FriendChatsManager currentFriendChat;
	
	/**
	 * Represents a Player's Trade system
	 */
	private transient Trade trade;
	
	/**
	 * Designed to help prevent Packet Injection
	 */
	private transient IsaacKeyPair isaacKeyPair;
	
	/**
	 * Represents a Player's Pet
	 */
	private transient Pet pet;
	
	/**
	 * Represents a Player's coordinate (movement) management system
	 */
	private transient CoordsEvent coordsEvent;
	
	/**
	 * Represents a Player's current Region
	 */
	private transient Region region;
	
	/**
	 * Represents a Player's last Emote delay (used for various things)
	 */
	private transient long nextEmoteEnd;
	
	/**
	 * Represents a Player's Passive Spell management system
	 */
	private transient PassiveSpellDispatcher spellDispatcher;
	
	/**
	 * Represents a Player's queue logic packets listing
	 */
	private transient ConcurrentLinkedQueue<LogicPacket> logicPackets;
	
	/**
	 * Represents a Action management system
	 */
	private transient ActionManager action = new ActionManager(this);
	
	/**
	 * Represents the Map Zone Manager
	 */
	private transient MapZoneManager mapZoneManager;
	
	/**
	 * The dialogue interpreter.
	 */
	private transient ScriptDialogueInterpreter dialogueInterpreter;
	
	/**
	 * Represents a transient Clue scroll rewards item container
	 */
	private transient ItemsContainer<Item> clueScrollRewards;

	/**
	 * Represents the Audio manager for sending Sounds
	 */
	private transient AudioManager audioManager;
	
	/**
	 * Represents an instance of the Overload effects task
	 */
	private transient OverloadEffectTask overloadEffect;
	
	/**
	 * Represents the Treasure Trails management
	 */
    private TreasureTrailsManager treasureTrailsManager;

    /**
	 * Represents the Treasure Trails Puzzle Box management
	 */
	private PuzzleBox puzzleBox;
	
	/**
	 * Personal details & information stored for a Player
	 */
	private PlayerDetails details;
	
	/**
	 * Represents a Player's Vars management system
	 */
	private VarsManager varsManager;
	
	/**
	 * The current skill action that is going on for this player.
	 */
	private Optional<SkillActionTask> skillAction = Optional.empty();
	
	/**
	 * Represents a Player's appearance management system
	 */
	private Appearance appearance;
	
	/**
	 * Represents a Player's inventory management system
	 */
	private Inventory inventory;
	
	/**
	 * Represents a Player's Equipment management system
	 */
	private Equipment equipment;
	
	/**
	 * Represents a Player's Skills management system
	 */
	private Skills skills;
	
	/**
	 * Represents a Player's Combat Definitions management system
	 */
	private CombatDefinitions combatDefinitions;
	
	/**
	 * Represents a Player's Prayer management system
	 */
	private PrayerManager prayer;
	
	/**
	 * Represents a Player's Bank management system
	 */
	private Bank bank;
	
	/**
	 * Represents a Player's Music management system
	 */
	private MusicsManager musicsManager;
	
	/**
	 * Represents a Player's Notes management system
	 */
	private Notes notes;
	
	/**
	 * Represents a Player's Friends Ignore management system
	 */
	private FriendsIgnores friendsIgnores;
	
	/**
	 * Represents a Player's Familiar (Summoning) management system
	 */
	private Familiar familiar;
	
	/**
	 * Represents a Player's Pet management system
	 */
	private PetManager petManager;
	
	/**
	 * The current Controller this Player is in.
	 */
	private Optional<MapZone> currentMapZone = Optional.empty();
	
	/**
	 * A collection of mapzone attributes
	 */
	private Object[] mapZoneAttributes;
	
	/**
	 * Represents a Quest Manager
	 */
	private QuestManager questManager;
	
	private DayOfWeekManager dayOfWeekManager;

	/**
	 * Constructs a new Player
	 * @param password
	 */
	public Player(String password) {
		super(GameConstants.START_PLAYER_LOCATION, EntityType.PLAYER);
		setHitpoints(100);
		setAppearance(new Appearance());
		setInventory(new Inventory());
		setEquipment(new Equipment());
		setSkills(new Skills());
		setCombatDefinitions(new CombatDefinitions());
		setPrayer(new PrayerManager());
		setBank(new Bank());
		setMusicsManager(new MusicsManager());
		setNotes(new Notes());
		setFriendsIgnores(new FriendsIgnores());
		setPetManager(new PetManager());
		setDetails(new PlayerDetails());
		setSpellDispatcher(new PassiveSpellDispatcher());
		getDetails().setPassword(password);
		setCurrentMapZone(Optional.empty());
		setVarsManager(new VarsManager());
		if (!getCurrentMapZone().isPresent())
			setCurrentMapZone(getCurrentMapZone());
		setQuestManager(new QuestManager());
		setDialogueInterpreter(new ScriptDialogueInterpreter(this));
		setTreasureTrailsManager(new TreasureTrailsManager());
		setClueScrollRewards(new ItemsContainer<Item>(10, true));
		Arrays.stream(Puzzles.values()).forEach(puzzle -> puzzleBox = new PuzzleBox(this, puzzle.getFirstTileId()));
		setAudioManager(new AudioManager(this));
		setDayOfWeekManager(new DayOfWeekManager());
	}
	
	/**
	 * Logs the Player into the lobby
	 * @param session
	 * @param user
	 * @param isaacKeyPair
	 */
	public void init(Session session, String user, IsaacKeyPair isaacKeyPair) {
		setUsername(user);
		setSession(session);
		setIsaacKeyPair(isaacKeyPair);
		World.addLobbyPlayer(this);
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "Initiated Lobby player: " + getUsername() + ", pass: " + getDetails().getPassword());
	}

	/**
	 * Logs In & creates a session with the game server
	 * @param session
	 * @param username
	 * @param displayMode
	 * @param screenWidth
	 * @param screenHeight
	 * @param isaacKeyPair
	 */
	public void init(Session session, String username, byte displayMode,
			short screenWidth, short screenHeight, IsaacKeyPair isaacKeyPair) {
		if (getDetails() == null)
			setDetails(new PlayerDetails());
		if (getPetManager() == null)
			setPetManager(new PetManager());
		if (getNotes() == null)
			setNotes(new Notes());
		setSession(session);
		setUsername(username);
		setDisplayMode(displayMode);
		setScreenWidth(screenWidth);
		setScreenHeight(screenHeight);
		setIsaacKeyPair(isaacKeyPair);
		setHintIconsManager(new HintIconsManager(this));
		setPriceCheckManager(new PriceCheckManager(this));
		setLocalPlayerUpdate(new LocalPlayerUpdate(this));
		setLocalNPCUpdate(new LocalNPCUpdate(this));
		setTrade(new Trade(this));
		if (getVarsManager() == null)
			setVarsManager(new VarsManager());
		getVarsManager().setPlayer(this);
		setSpellDispatcher(new PassiveSpellDispatcher());
		getAppearance().setPlayer(this);
		getInventory().setPlayer(this);
		getEquipment().setPlayer(this);
		getSkills().setPlayer(this);
		getCombatDefinitions().setPlayer(this);
		getPrayer().setPlayer(this);
		getBank().setPlayer(this);
		if (getClueScrollRewards() == null)
			setClueScrollRewards(new ItemsContainer<>(10, true));
		Arrays.stream(Puzzles.values()).forEach(puzzle -> puzzleBox = new PuzzleBox(this, puzzle.getFirstTileId()));
		if (getTreasureTrailsManager() == null)
			setTreasureTrailsManager(new TreasureTrailsManager());
        getTreasureTrailsManager().setPlayer(this);
		if (getQuestManager() == null)
			setQuestManager(new QuestManager());
		getMusicsManager().setPlayer(this);
		getNotes().setPlayer(this);
		getCombatDefinitions().setPlayer(this);
		getFriendsIgnores().setPlayer(this);
		getPetManager().setPlayer(this);
		setDirection((byte) Utility.getFaceDirection(0, -1));
		setTemporaryMovementType((byte) -1);
		setLogicPackets(new ConcurrentLinkedQueue<LogicPacket>());
		setSwitchItemCache(new ObjectArrayList<Byte>());
		if (getAction() == null)
			setAction(new ActionManager(this));
		if (getMapZoneManager() == null)
			setMapZoneManager(new MapZoneManager());
        getQuestManager().setPlayer(this);
        setInterfaceManager(new InterfaceManager(this));
        if (getDialogueInterpreter() == null)
        	setDialogueInterpreter(new ScriptDialogueInterpreter(this));
        if (getAudioManager() == null)
        	setAudioManager(new AudioManager(this));
        if (getDayOfWeekManager() == null)
        	setDayOfWeekManager(new DayOfWeekManager());
		initEntity();
		World.addPlayer(this);
		updateEntityRegion(this);
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "Initiated player: " + username + ", pass: "
					+ getDetails().getPassword());
		getSession().updateIPnPass(this);
	}
	
	/**
	 * Starts Lobby rendering, etc..
	 */
	public void startLobby(Player player) {
		getFriendsIgnores().setPlayer(this);
		getFriendsIgnores().init();
		if (getDetails().getCurrentFriendChatOwner() != null) {
			FriendChatsManager.joinChat(getUsername(), this);
			if (getCurrentFriendChat() == null)
				getDetails().setCurrentFriendChatOwner(null);
		}
		player.getPackets().sendFriendsChatChannel();
		getFriendsIgnores().sendFriendsMyStatus(true);
	}

	/**
	 * Starts ingame rendering, etc..
	 */
	public void start() {
		LogUtility.log(LogType.INFO, getDisplayName() + " has logged in from their IP " + getSession().getIP());
		loadMapRegions();
		setStarted(true);
		login();
		if (isDead())
			sendDeath(Optional.empty());
	}

	/**
	 * Processes the Entities state
	 */
	@Override
	public void processEntity() {
		if (isDead())
			return;
		getSession().processLogicPackets(this);
		if (getCoordsEvent() != null && getCoordsEvent().processEvent(this))
			setCoordsEvent(null);
		if (getRouteEvent() != null && getRouteEvent().processEvent(this))
			setRouteEvent(null);
		getAction().process();
		getPrayer().processPrayer();
		getMapZoneManager().executeVoid(this, zone -> zone.process(this));
		getDayOfWeekManager().process();
		if (getMusicsManager().musicEnded())
			getMusicsManager().replayMusic();
		if (getDetails().getChargeDelay().get() > 0) {
			if (getDetails().getChargeDelay().get() == 1)
				getAudioManager().sendSound(Sounds.CHARGE_SPELL_REMOVED);
			getDetails().getChargeDelay().getAndDecrement();
		}
	}

	/**
	 * Sends important information & data for login for the player to see
	 */
	public void login() {
		if (World.get().getExiting_start() != 0) {
			int delayPassed = (int) ((Utility.currentTimeMillis() - World.get().getExiting_start()) / 1000);
			getPackets().sendSystemUpdate(World.get().getExiting_delay() - delayPassed);
		}
		checkMultiArea();
		getDayOfWeekManager().init();
		Gravestone.login(this);
		getDetails().setLastIP(getSession().getIP());
		getAppearance().generateAppearenceData();
		getPackets().sendLocalPlayersUpdate();
		getInterfaceManager().sendInterfaces();
		getPackets().sendRunEnergy().sendGameBarStages().sendGameMessage("Welcome to " + GameConstants.SERVER_NAME + ".");
		CombatEffect.values().stream().filter(effects -> effects.onLogin(this)).forEach(effect -> World.get().submit(new CombatEffectTask(this, effect)));
		GameConstants.STAFF.entrySet().stream().filter(p -> getUsername().equalsIgnoreCase(p.getKey())).forEach(staff -> getDetails().setRights(staff.getValue()));
		getVarsManager().getVarMap().forEach((k, v) -> getVarsManager().sendVar(k, v));
		getVarsManager().getVarBitMap().forEach((k, v) -> getVarsManager().sendVarBit(k, v));
		getVarsManager().loadDefaultVars();
		if (getDetails().getCurrentFriendChatOwner() != null) {
			FriendChatsManager.joinChat(getUsername(), this);
			if (getCurrentFriendChat() == null)
				getDetails().setCurrentFriendChatOwner(null);;
		}
		getInventory().init();
		getEquipment().checkItems();
		getEquipment().init();
		getSkills().init();
		getCombatDefinitions().init();
		getPrayer().init();
		getFriendsIgnores().init();
		getPrayer().refreshPoints();
		getMusicsManager().init();
		getNotes().init();
		if (getFamiliar() != null)
			getFamiliar().respawnFamiliar(this);
		else
			getPetManager().init();
		setRunning(true);
		setUpdateMovementType(true);
		getMapZoneManager().execute(this, controller -> controller.login(this));
		if (HostManager.contains(getUsername(), HostListType.MUTED_IP)) {
			getPackets()
					.sendGameMessage("You have been temporarily muted due to breaking a rule.")
					.sendGameMessage("This mute will remain for a further X days.")
					.sendGameMessage("To prevent further mute please read the rules.");
			getInterfaceManager().sendInterface(801);
		}
		if (getDetails().getQuestPoints().get() != GameConstants.TOTAL_QUEST_POINTS
				&& getEquipment().containsAny(ItemNames.QUEST_POINT_CAPE_9813, ItemNames.QUEST_POINT_HOOD_9814)
				&& getDetails().getRights() != Rights.ADMINISTRATOR) {
			   getPackets().sendGameMessage(
                    "One or more Quests has been released, please complete them to continue wearing your Quest Cape.");
            getEquipment().getItems().set(Equipment.SLOT_CAPE, null);
            getEquipment().refresh(Equipment.SLOT_CAPE);
            getAppearance().generateAppearenceData();
            getInventory().addOrBank(new Item(9813));
        }
		if (!HostManager.contains(getUsername(), HostListType.STARTER_RECEIVED)) {
			GameConstants.STATER_KIT.forEach(getInventory()::addItem);
			HostManager.add(this, HostListType.STARTER_RECEIVED, true);
			World.sendWorldMessage("[New Player] " + getDisplayName() + " has just joined " + GameConstants.SERVER_NAME);
		}
	}

	/**
	 * Finishes the Player's Session
	 */
	@Override
	public void deregister() {
		getSession().finish(this, 0);
	}

	/**
	 * Gets the Player's Encoder Packets
	 * @return
	 */
	public WorldPacketsEncoder getPackets() {
		return getSession().getWorldPackets();

	}

	/**
	 * Handles an incoming Hit to the Player
	 */
	@Override
	public void handleIngoingHit(final Hit hit) {
		PlayerCombat.handleIncomingHit(this, hit);
	}
	
	/**
	 * Represents a Player's Death by various sources
	 * (Player, NPC, or neither)
	 */
	@Override
	public void sendDeath(Optional<Entity> source) {
		World.get().submit(new PlayerDeath(this));
	}

	/**
	 * Adds Logic Packets to a queue
	 * @param packet
	 */
	public void addLogicPacketToQueue(LogicPacket packet) {
		getLogicPackets().stream().filter(type -> type.getId() == packet.getId()).forEach(getLogicPackets()::remove);
		getLogicPackets().add(packet);
	}
	
	/**
	 * Formats the Player's username for a nicer display use
	 * @return
	 */
	public String getDisplayName() {
		return Utility.formatPlayerNameForDisplay(getUsername());
	}
	
	/**
	 * Submits & executes a Dialogue event
	 * @param listener
	 */
	public void dialog(DialogueEventListener listener){
		getAttributes().get(Attribute.DIALOGUE_EVENT).set(listener.begin());
		
	}
	
	/**
	 * Submits & executes a Dialogue event
	 * @param listener
	 */
	public void dialogBlank(DialogueEventListener listener){
		getAttributes().get(Attribute.BLANK_DIALOGUE_EVENT).set(listener.beginBlank());
		
	}
	
	/**
	 * A cleaner simpler way to do quick easy dialogues!
	 * @param listener
	 */
	public void dialogue(Consumer<DialogueEventListener> listener) {
		dialog(new DialogueEventListener(this) {
			@Override
			public void start() {
				listener.accept(this);
			}
		});
	}
	
	/**
	 * A cleaner simpler way to do quick easy dialogues!
	 * This method supports NPC dialogues
	 * @param listener
	 */
	public void dialogue(int npcId, Consumer<DialogueEventListener> listener) {
		dialog(new DialogueEventListener(this, Entity.findNPC(npcId)) {
			@Override
			public void start() {
				listener.accept(this);
			}
		});
	}

	/**
	 * Checks to see if the player owns the item (if exists in Bank, Inventory, or Equipment).
	 * @param items
	 * @return
	 */
    public boolean ownsItems(Item... items) {
        return Arrays.stream(items)
                .anyMatch(item -> getBank().getItem(item.getId()) != null
                        || getEquipment().containsAny(item.getId())
                        || getInventory().containsAny(item.getId()));
    }
    
    /**
	 * Checks to see if the player is carrying the item (if exists in Inventory, or Equipment).
	 * @param items
	 * @return
	 */
    public boolean carringItems(Item... items) {
        return Arrays.stream(items)
                .anyMatch(item -> getEquipment().containsAny(item.getId()) || getInventory().containsAny(item.getId()));
    }
}