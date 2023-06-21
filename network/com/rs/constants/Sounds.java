package com.rs.constants;

/**
 * A collection of useful type-safe Sounds designed to provide better
 * readability and code base flow.
 * 
 * Note-worthy lists:
 * https://oldschool.runescape.wiki/w/List_of_in-game_sound_IDs
 * https://github.com/JesseGuerrero/DarkanTools/blob/windows/static/essentialIgnored/effectsSolved.txt
 * 
 * @author Dennis
 *
 */
public interface Sounds {

	/**
	 * Player Based Animations
	 */
	int BEING_TELEPORTED = 195;
	int CUTTING_GEMS = 2586;
	int CANCELING_PIN = 1042;
	int HIGH_ALCHEMY_SPELL = 97;
	int LOW_ALCHEMY_SPELL = 98;//invalid, use h.a instead
	int HUMIDIFY_SPELL = 3614;
	int MONSTER_EXAMINE_SPELL = 3621;
	int STAT_SPY_SPELL = 3620;
	int VENGEANCE_SPELL = 2908;
	int VENGEANCE_OTHER_SPELL = 2907;
	int SUPERHEAT_SPELL = 117;
	int BIRDS_NEST = 1997;
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
	int COOKING_SOUND = 65;
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
	int FINDING_GEM = 2655;
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