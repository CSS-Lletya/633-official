package com.rs.constants;

/**
 * A collection of useful type-safe Sounds designed to provide better
 * readability and code base flow. All the Sounds listed are in use, therefor not all newly found sounds exist on this list.
 * 
 * Note-worthy lists:
 * https://oldschool.runescape.wiki/w/List_of_in-game_sound_IDs
 * https://github.com/JesseGuerrero/DarkanTools/blob/windows/static/essentialIgnored/effectsSolved.txt
 * 
 * (slightly inaccurate)
 * https://rune-server.org/runescape-development/rs-503-client-and-server/configuration/672002-667-soundlist.html
 * @author Dennis
 *
 */
public interface Sounds {

	/**
	 * Player Based Animations
	 */
	int BEING_TELEPORTED = 195;
	int USING_CHISEL = 2586;
	int CANCELING_PIN = 1042;
	int HIGH_ALCHEMY_SPELL = 97;
	int LOW_ALCHEMY_SPELL = 98;//invalid, use h.a instead
	int HUMIDIFY_SPELL = 3614;
	int MONSTER_EXAMINE_SPELL = 3621;
	int STAT_SPY_SPELL = 3620;
	int VENGEANCE_SPELL = 2908;
	int VENGEANCE_OTHER_SPELL = 2907;
	int SUPERHEAT_SPELL = 117;
	int BIRDS_NEST = 1516; //1997 bird whistling
	int NPC_CONTACT_SPELL = 3618;
	int FLOUR_HOPPER_OPERATING = 3189;
	int WILDERNESS_OBELISK_TELEPORING = 204;
	int PICK_FLAX = 2581;
	int BANK_PIN_REPEAT_CONFRIMATION = 1040;
	int BANK_BAD_PIN = 1042;
	int BANK_PIN_OPEN_SETTINGS = 1040;
	int BANK_PIN_ID_SELECTED = 1041;
	int FALLING_FROM_HIGH = 4500;
	int ASSISTING_XP = 4010;
	int PRAYER_ACTIVATING = 2673;
	int PRAYER_OVERHEAD_ACTIVATING = 2662;
	int PRAYER_DISABLING = 2663;
	int TELEPORING_WARPING = 200;
	int TELEPORT_OTHER = 199;
	int TELEPORT_TAB_BREAKING = 979;
	int TELE_GRAB_SPELL = 192;//3008 is ending, 192 is full spell (seemingly)
	int TELE_GRAB_ON_IMPTACT = 3007;
	int WARRIORS_GUILD_ANIMATOR_HUMMING = 1909;//not working?
	int SKILL_RESTORING = 2674;
	int PLAYER_HEAVY_STUNNED_STATE = 3201;
	int PLAYER_STUNNED = 2727;
	int PRAYER_REDEMPTION = 2681;
	int PRAYER_RETRIBUTION = 159;
	int PRAYER_RUN_OUT_OF_POINTS = 2672;
	int SPIRIT_WOLF_SUMMONING = 4265;
	int UNICORN_STALION_SUMMONING = 4372;
	int DRINKING_POTION = 4580;
	int ITEM_DROPPING_TO_GROUND = 2739;
	int WEARING_ITEM = 2240;
	int WEARING_ITEM_2 = 2242;
	int GRAND_EXCHANGE_UPDATE = 4042;
	int EATING_FOOD = 2393;
	int WARRIORS_GUILD_CATAPULT_ROOM = 1911;
	int EXPERIENCE_LAMP_USED = 1270;
	int DESTOY_ITEM = 4500;
	int BURY_OR_PICK = 2738;
	int COOKING_SOUND = 5471; //evil bob un-cooking 2322
	int REMOVE_WORN_ITEM = 2238;
	int DOOR_CLOSING = 60;
	int DOOR_OPENING = 62;
	int GATE_CLOSING = 66;
	int GATE_OPENING = 67;
	int CHEST_OPEN = 51;
	int CHEST_CLOSED = 52;
	int DRAWER_CLOSED = 63;
	int DRAWER_OPEN = 64;
	int BOLT_ENCHANTING = 2921;
	int FALLING_TREE = 2734;
	int LIGHT_CANDLE = 3226;
	int EXTINGUISH_LIGHT_SOURCE = 1463;
	int GRAND_EXCHANGE_FINISH = 2115;
	int PICKING_LOCK = 1228;
	int PICKING_LOCK_MANY = 2407;
	int PICKAXE_LOST = 2306;
	int FINDING_TREASURE = 2655;
	int PROSPECTING = 2661;
	int PUZZLE_COMPLETE = 3283;
	int PUZZLE_SHIFT = 1859;
	int SAWMILL_PLANK_CONVERT = 1386;//custom (accurate enough) - saw_rise_and_spin
	int ONYX_ENCHANT = 144;
	int DIAMOND_ENCHANT = 138;
	int DRAGONSTONE_ENCHANT = 140;
	int EMERALD_ENCHANT = 142;
	int RUBY_ENCHANT = 146;
	int SAPPHIRE_ENCHANT = 147;
	int OFFER_BONES_ON_ALTAR = 958;
	int BONES_TO = 114;//cant find peaches, maybe same.
	int CHARGE_SPELL = 1651;
	int CHARGE_SPELL_REMOVED = 1650;
	int COINS_DROPPING_TO_GROUND = 1015;
	int ROCKCAKE_DROP_TO_GROUND = 1014;
	int FILL_FROM_WATER_SOURCE = 1004;
	int EAT_ROCKCAKE = 1018;
	int CHARGE_EARTH_ORB = 115;
	int CHARGE_AIR_ORB = 116;
	int CHARGE_FIRE_ORB = 117;
	int CHARGE_WATER_ORB = 118;
	int OPENING_TRAPDOOR = 89;
	int CLOSING_TRAPDOOR = 88;
	int FAIRY_RING_TELEPORING = 1098;
	int PIT_FALL = 1171;
	int BLOW_KISS = 1854;
	int YO_YO_ONE = 2261;
	int YO_YO_TWO = 2262;
	int YO_YO_THREE = 2263;
	int YO_YO_FOUR = 2264;
	int YO_YO_WIND = 2265;
	int FAILED_SPELL = 227;
	int BROKEN_AXE = 2514;
	int CHISELING = 2586;
	int SPINNING = 2590;
	int FILL_BUCKET_WITH_SAND = 2584;
	int EMPTY_ITEM = 2610;//is actually only used for vial, but we can use for other empty items.
	int MANHOLE_OPENING = 74;
	int MANHOLE_CLOSING = 75; //2969
	int PULLING_LEVER = 2400;
	int LOCKED = 2402; //not sure if it's related to objects being "locked", but indefinitely states "Lock"
	int HOPPER_LEVER_PULLING = 2575;
	int FANFARE = 2930; //funny TADA! sound effect. kek.
	int UNLOCK_AND_MOVE = 1473;
	int WATER_SPLASHING = 2496; //linked to JUMPING_INTO, or anything relevant too. 1658 is quieter
	int LOOM_WEAVING = 2587;
	int POTTERY = 2588;
	int POTTERY_OVEN = 2580;
	int GEM_SMASHING = 2589;
	int AMULET_STRINGING = 2593;
	int ANTIFIRE_EFFECTS_REMOVED = 2607;
	int ATTACH_SOMETHING = 2287;
	int VIAL_MIXING = 2611;
	int PESTLE_AND_MORTAR_GRINDING = 2608;
	int POISON_HITSPLAT = 2408;
	int JUMP_IN_WATER = 3832;
	int TELETAB_BREAKING = 979;
	int FILLING_UP_GLASS = 2395;
	int MASTERED_SKILL = 2396; //fireworks
	int PLATE_SPINNING = 2255;
	int PLATE_BREAKING = 2251;
	int GLASS_BLOWING = 2724; //not used, archived
	int WALLSAFE_CRACKING = 1243;
	int WALLSAFE_FAILURE = 3309;//1383 doesnt seem accurate
	int WALLSAFE_SUCCESS = 1238;
	int LUNAR_DREAMING = 3619;
	int BAKE_PIE_SPELL = 2879;
	int IMBUE_RUNES = 2888;
	int SUPERGLASS_MAKE = 2896;
	int LUNAR_STRING_JEWELRY = 2903;
	int LUNAR_CHANGE_SPELLBOOK = 3613;
	int HUMIDIFY = 3614;
	int HUNTER_KIT = 3615;
	int HUNTER_KIT_OPENING = 3616;
	int LUNAR_HEAL_ME = 2884;
	int RUNECRAFTING = 2710;
	int SMELTING = 2725;
	int STRONGHOLD_OF_SECURITY_DOOR_PASS = 2858;
	int TELEBLOCKED = 5376;
	int CREATED_FIRE = 130;
	int INTERFACE_CLICK = 2266;
	int HELLCAT_GROWING = 2293;
	int COW_INTERACT = 1170;
	int CASTING_BLOOM = 1493;
	int LOCKED_DOOR = 1631;
	int REMOVE_AXE = 3775;
	int BUILD_CANOE = 2729;
	int ROLL_CANOE = 2731;
	int SINK_CANOE = 2732;
	int LEVEL_UP = 4860;
	
	
	//combat specials, rest of combat will be defined after rework
	int ICE_CLEAVE_SPECIAL = 3846;
	int POWERSHOT_SPECIAL = 2536;
	int PUNCTURE_SPECIAL = 2537;
	int SEVER_SPECIAL = 2540;
	int SHOVE_SPECIAL = 2533;
	int SNAPSHOT_SPECIAL = 2545;
	
	
	//Sequential sounds being sent (All are used)
	int HOME_TELEPORT_1 = 193;//delay 1
	int HOME_TELEPORT_2 = 194;//delay 6
	int HOME_TELEPORT_3 = 195;//delay 12
	int HOME_TELEPORT_4 = 196;//delay 16
	
	/**
	 * NPC Based Animations
	 */
	//npc combat-based sounds will be added after combat rework
	int SHEEP_FAILED_SHEERING = 755;
	
}