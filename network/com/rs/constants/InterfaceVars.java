package com.rs.constants;

import com.rs.game.player.content.MusicsManager;

/**
 * A type-safe record of all known Interface Configs/Vars. Some data below may
 * not have completed contents and cannot be verified their uses, apologies in
 * advance!
 * 
 * Some contents may be excluded due to the nature of usages via for loops, such.
 * ({@link MusicsManager}
 * 
 * Thanks to Gregs open source repo, we have a list of varps to operate with
 * https://github.com/GregHib/void/blob/main/data/definitions/variables-player.yml
 * 
 * @author Dennis
 *
 */
public interface InterfaceVars {

	/**
	 * Used for the Level up interfaces & Skill icon flashing.
	 */
	int LEVEL_UP_AND_FLASH = 1179;

	/**
	 * Used to enable the poisoned orb effect on the Hitpoints Orb. This also
	 * unlocks the component function to instantly cure by clicking on the orb.
	 */
	int POISIONED_HP_ORB = 102;

	/**
	 * Though not confirmed due no existing Shop system created, it is assumed to be
	 * in relations to the Shop container itself.
	 */
	int SHOP_KEY = 1496;

	/**
	 * Though not confirmed due no existing Shop system created, it is assumed to be
	 * in relations to the Shop container itself.
	 */
	int SHOP_CURRENCY = 532;

	/**
	 * Though not confirmed due no existing Shop system created, it is assumed to be
	 * in relations to the Shop container itself.
	 */
	int SHOP_TRANSACTION = 2564;

	/**
	 * Though not confirmed due no existing Shop system created, it is assumed to be
	 * in relations to the Shop container itself.
	 */
	int SHOP_BUYING_STATE = 2565;

	/**
	 * Though not confirmed due no existing Shop system created, it is assumed to be
	 * in relations to the Shop container itself.
	 */
	int SHOP_RESET_SELECTED = 2563;

	/**
	 * Though not confirmed due no existing Shop system created, it is assumed to be
	 * in relations to the Shop container itself.
	 */
	int SHOP_ITEM_ID = 2562;

	/**
	 * Updates the Prayer bonuses % interface (optionally can hide this popup).
	 */
	int PRAYER_STAT_PERCENTAGE_BASE_VALUE = 6857;

	/**
	 * Unknown function due to lack of functionalities
	 */
	int CURSES_PRAYER_DISABLE = 1582;

	/**
	 * Unknown function due to lack of functionalities
	 */
	int MODERN_PRAYER_DISABLE = 1395;

	/**
	 * Updates the Slot id for a Curses Prayer being used.
	 */
	int UPDATE_CURSES_SLOT_PRAYER = 1582;

	/**
	 * Updates the Slot id for a Curses Quick Prayer being used.
	 */
	int UPDATE_QUICK_CURSES_SLOT_PRAYER = 1587;

	/**
	 * Updates the Slot id for a Curses Prayer being used.
	 */
	int UPDATE_MODERN_SLOT_PRAYER = 1395;

	/**
	 * Updates the Slot id for a Curses Quick Prayer being used.
	 */
	int UPDATE_QUICK_MODERN_SLOT_PRAYER = 1397;

	/**
	 * Refreshes the current Prayer book
	 */
	int REFRESH_PRAYER_BOOK = 1584;

	/**
	 * Refreshes the current Prayer points
	 */
	int REFRESH_PRAYER_POINTS = 2382;

	/**
	 * Sets the Primary color text of the Note being written
	 */
	int PRIMARY_NOTE_COLOR = 1440;

	/**
	 * Believed to set the background color of the text being written
	 */
	int SECONDARY_NOTE_COLOR = 1441;

	/**
	 * Believed to unlock the notes management buttons
	 */
	int UNLOCK_MANAGE_NOTES = 1437;

	/**
	 * An unknown Notes var/config Believed to set the note index (?)
	 */
	int SET_NOTE_INDEX = 1439;

	/**
	 * Sets the target destination waypoint flag in the World Map interface NOTE:
	 * Need to add Hint Icon system to waypoint coords to view from ingame itself
	 * (outside of world map interface)
	 */
	int WORLD_MAP_MARKER = 1159;
	
	/**
	 * Sets the Chat effects (wave1: text) to visable
	 */
	int SETTINGS_CHAT_EFFECTS = 171;
	
	/**
	 * Sets the force right click always function or not
	 */
	int SETTINGS_MOUSE_BUTTONS = 170;
	
	/**
	 * Sets the acceptance of other Players (Spells, such..)
	 */
	int SETTINGS_ACCEPT_AID = 427;
	
	/**
	 * Refreshes the Auto attack style of the magic spell
	 */
	int COMBAT_REFRESH_AUTO_CAST_SPELL = 108;
	
	/**
	 * Refreshes the scrollbar for the auto casting
	 * Note: haven't tested this proof of concept but assuming it is.
	 */
	int COMBAT_REFRESH_DEFENCE_SPELL_CAST_SCROLLBAR = 439;
	
	/**
	 * Refreshes the current combat spellbook
	 */
	int COMBAT_REFRESH_SPELLBOOK = 1376;
	
	/**
	 * Refreshes the current combat style state
	 */
	int COMBAT_REFRESH_COMBAT_STYLE = 43;
	
	/**
	 * Refreshes the current state of the special attack bar
	 */
	int COMBAT_REFRESH_USING_SPECIAL_ATTACK = 301;
	
	/**
	 * Refreshes the current state of the special attack bar value
	 */
	int COMBAT_REFRESH_SPECIAL_ATTACK_VALUE = 301;
	
	/**
	 * Refreshes the auto retaliation state
	 */
	int COMBAT_REFRESH_AUTO_RETALIATION = 172;
	
	/**
	 * Refreshes the last X value
	 */
	int BANK_LAST_X = 1249;
	
	/**
	 * Refreshes the last viewing tab of the bank
	 */
	int BANK_REFRESH_LAST_VIEWING_TAB = 4893;
	
	/**
	 * Refreshes the specified bank tab
	 */
	int BANK_REFRESH_SPECIFIED_TAB = 4885;
	
	/**
	 * Updates the state of insert mode for the bank
	 */
	int BANK_SWITCH_INSERT_MODES = 305;
	
	/**
	 * Refreshes the special energy used for familiars specials
	 */
	int SUMMONING_REFRESH_SPECIAL_ENGERY = 1177;
	
	/**
	 * Represents the time left of a familiars life
	 */
	int SUMMONING_TIME_REMAINING = 1176;
	
	/**
	 * Believed to be the item id for name display
	 */
	int SUMMONING_POUCH_ID = 448;
	
	/**
	 * Represents the familiars head animation
	 * NOTE: value of this config is bit weird, works though i guess
	 */
	int SUMMONING_HEAD_ANIMATION = 1160;
	
	/**
	 * Represents the special amount of a familiar that it can produce
	 */
	int SUMMONING_SPECIAL_AMOUNT = 1175;
	
	/**
	 * Summoning is not updated yet, unknown if functionalities are correct.
	 */
	int SUMMONING_SWITCH_ORB = 1174;
	
	/**
	 * Unable to verify the nature of these configs till summoning is updated.
	 * These are group together in the meantime as their usages are quite similar..
	 */
	int SUMMONING_LEFT_CLICK_OPTION = 1493;
	int SUMMONING_EXTRA_LEFT_CLICK_OPTION = 1494;
	
	/**
	 * Refreshes the XP counter value
	 */
	int SKILLS_REFRESH_XP_COUNTER = 1801;
	
	/**
	 * Refreshes the state of the clan status
	 */
	int GAME_BAR_STATUS_CLAN = 1054;
	
	/**
	 * Refreshes the state of the assist status
	 */
	int GAME_BAR_STATUS_ASSIST = 1055;
	
	/**
	 * Refreshes the state of the friends/ignore status
	 */
	int GAME_BAR_STATUS_FRIENDS_IGNORE = 2159;
	
	/**
	 * Believed to be the item id for name display
	 */
	int PET_ITEM_ID = 448;
	
	/**
	 * Represents the pet head animation
	 * NOTE: value of this config is bit weird, works though i guess
	 */
	int PET_HEAD_ANIMATION = 1160;
	
	/**
	 * Represents the run button config
	 */
	int RUN_BUTTON = 173;
	
	/**
	 * Represents the data being sent for the levl up interface and what you can now do on after unlocking the level.
	 */
	int SKILL_CONGRATULATIONS_LEVEL_UP_INFORMATION = 1230;
	
	/**
	 * Represents the global skill-based information (attack skill menu shows all weapons, such..)
	 */
	int SKILL_SKILL_GUIDE_DATA = 965;
	
	/**
	 * Represents an item being modified in a trade screen
	 */
	int TRADE_ITEM_MODIFIED = 1042;
	
	/**
	 * Represents the targets item being modified in a trade screen
	 */
	int TRADE_TARGET_ITEM_MODIFIED = 1043;
	
	/**
	 * This allows you to close your chatbox interface entirely as well as your 
	 * toolbelt slots (skill tab, such). This feature is disabled by default
	 * cause of the tutorial islands.
	 */
	int CLOSE_CHAT_TOOLBELT = 281;
	
	/**
	 * Enables Skill target Levels or XP tracking!
	 */
	int SKILL_TARGETS = 1966;
	
	/**
	 * Represents the state of using the Level mode specifically
	 */
	int SKILL_TARGET_LEVEL_MODE = 1968;
	
	/**
	 * Represents the tracking target values
	 */
	int SKILL_TARGET_VALUES = 1969;
}