package com.rs.game.player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

import com.alex.utils.VarsManager;
import com.rs.GameConstants;
import com.rs.constants.Sounds;
import com.rs.content.quests.QuestManager;
import com.rs.game.Entity;
import com.rs.game.EntityType;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.dialogue.ScriptDialogueInterpreter;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.item.ItemsContainer;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.game.map.zone.MapZone;
import com.rs.game.map.zone.MapZoneManager;
import com.rs.game.map.zone.impl.GlobalPVPMapZone;
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
import com.rs.game.player.queue.PlayerScriptQueue;
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
import com.rs.net.updating.LocalNPCUpdate;
import com.rs.net.updating.LocalPlayerUpdate;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
	private transient ObjectList<Byte> switchItemCache;
	
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
	 * Represents a Player's queue logic packets listing
	 */
	private transient Queue<LogicPacket> logicPackets;
	
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
	 * Represents the players current PID
	 */
	private transient int pid;
	
	/**
	 * The current skill action that is going on for this player.
	 */
	private transient Optional<SkillActionTask> skillAction = Optional.empty();
	
	/**
	 * Personal details & information stored for a Player
	 */
	private PlayerDetails details;
	
	/**
	 * Represents the Treasure Trails management
	 */
    private TreasureTrailsManager treasureTrailsManager;

    /**
	 * Represents the Treasure Trails Puzzle Box management
	 */
	private PuzzleBox puzzleBox;
	
	/**
	 * Represents a Player's Vars management system
	 */
	private VarsManager varsManager;
	
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
	private Optional<MapZone> currentMapZone;
	
	/**
	 * A collection of mapzone attributes
	 */
	private Object[] mapZoneAttributes;
	
	/**
	 * Represents a Quest Manager
	 */
	private QuestManager questManager;
	
	/**
	 * Represents Days of the Week management
	 */
	private DayOfWeekManager dayOfWeekManager;

	@Getter
	private transient PlayerScriptQueue scripts;
	
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
		getDetails().setPassword(password);
		setVarsManager(new VarsManager());
		setQuestManager(new QuestManager());
		setDialogueInterpreter(new ScriptDialogueInterpreter(this));
		setTreasureTrailsManager(new TreasureTrailsManager());
		setClueScrollRewards(new ItemsContainer<Item>(10, true));
		Arrays.stream(Puzzles.values()).forEach(puzzle -> puzzleBox = new PuzzleBox(this, puzzle.getFirstTileId()));
		setAudioManager(new AudioManager(this));
		setDayOfWeekManager(new DayOfWeekManager());
		setMapZoneManager(new MapZoneManager());
		setCurrentMapZone(Optional.empty());
		this.scripts = new PlayerScriptQueue(this);
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
		getAppearance().setPlayer(this);
		getEquipment().setPlayer(this);
		getSkills().setPlayer(this);
		getInventory().setPlayer(this);
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
		setLogicPackets(new LinkedList<LogicPacket>());
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
        getDayOfWeekManager().setPlayer(this);
        getMapZoneManager().setPlayer(this);
        this.scripts = new PlayerScriptQueue(this);
		initEntity();
		World.addPlayer(this);
		updateEntityRegion(this);
		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "Initiated player: " + username + ", pass: "
					+ getDetails().getPassword());
		getSession().updateIPnPass(this);
	}

	/**
	 * Processes the Entities state
	 */
	@Override
	public void processEntity() {
		getSession().processLogicPackets(this);
		
		getDetails().getPlayTime().getAndIncrement();
		getDayOfWeekManager().process();
		if (isDead())
			return;
		getScripts().process();
		if (getCoordsEvent() != null && getCoordsEvent().processEvent(this))
			setCoordsEvent(null);
		if (getRouteEvent() != null && getRouteEvent().processEvent(this))
			setRouteEvent(null);
		getAction().process();
		getPrayer().processPrayer();
		getMapZoneManager().executeVoid(zone -> zone.process(this));
		if (getMusicsManager().musicEnded())
			getMusicsManager().replayMusic();
		if (getDetails().getChargeDelay().ticksRemaining() == 1)
			getAudioManager().sendSound(Sounds.CHARGE_SPELL_REMOVED);
		if (getDetails().getMagicImbue().ticksRemaining() == 6) {
			getPackets().sendGameMessage("Magic Imbue spell charge is running out...");
		}
		if (getDetails().getMagicImbue().ticksRemaining() == 1) {
			getPackets().sendGameMessage("Magic Imbue charge has ended.");
			getAttributes().get(Attribute.MAGIC_IMBUED).set(false);
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
		getAppearance().generateAppearenceData();
		getPackets().sendLocalPlayersUpdate();
		getInterfaceManager().sendInterfaces();
		checkMultiArea();
		Gravestone.login(this);
		getDetails().setLastIP(getSession().getIP());
		if (GameConstants.isPVPWorld()) {
			getMapZoneManager().submitMapZone(new GlobalPVPMapZone());
			dialogue(d -> d.item(11784, "Welcome to " + GameConstants.SERVER_NAME + "'s PVP World. Good luck!"));
		} else {
			if (getMapZoneManager().isValidInstance(GlobalPVPMapZone.class) && !GameConstants.isPVPWorld()) {
				getMapZoneManager().endMapZoneSession(this);
			} else
				getCurrentMapZone().ifPresent(getMapZoneManager()::submitMapZone);
		}
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
		getMapZoneManager().execute(controller -> controller.login(this));
		if (!HostManager.contains(getUsername(), HostListType.STARTER_RECEIVED)) {
			getBank().addItem(new Item(995, 25), false);
			ItemConstants.STATER_KIT.forEach(getInventory()::addItem);
			HostManager.add(this, HostListType.STARTER_RECEIVED);
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
		getLogicPackets().removeIf(logicPacket -> logicPacket.getId() == packet.getId());
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
	public void dialogue(DialogueEventListener listener){
		getAttributes().get(Attribute.DIALOGUE_EVENT).set(listener.begin());
		
	}
	
	/**
	 * A cleaner simpler way to do quick easy dialogues!
	 * @param listener
	 */
	public void dialogue(Consumer<DialogueEventListener> listener) {
		dialogue(new DialogueEventListener(this) {
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
		dialogue(new DialogueEventListener(this, npcId) {
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
    public boolean carryingItems(Item... items) {
        return Arrays.stream(items)
                .anyMatch(item -> getEquipment().containsAny(item.getId()) || getInventory().containsAny(item.getId()));
    }
    
    public void setPuzzleBox(int puzzleId) {
		this.puzzleBox = new PuzzleBox(this, puzzleId);
	}
}