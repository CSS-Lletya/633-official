package com.rs.game.player.attribute;

/**
 * A Type-safe declaration interface to simplify attribute declaring.
 * @author Dennis
 *
 */
public interface Attribute {
	

	/**
	 * Represents the Target Player to trade with
	 */
	public String TRADE_TARGET = "TradeTarget";
	
	/**
	 * Represents the Target Player to trade with
	 */
	public String TRADE_ITEM_X_SLOT = "TradeItemXSlot";
	/**
	 * Represents the Target Player to trade with
	 */
	public String TRADE_IS_REMOVE = "TtradeIsRemove";
	
	
	/**
	 * Represents the Familiar Special active state
	 */
	public String FAMILIAR_SPECIAL = "FamiliarSpec";
	
	/**
	 * Represents the Integer input value
	 */
	public String INTEGER_INPUT_ACTION = "IntegerInputAction";
	
	/**
	 * Represents the String input value
	 */
	public String STRING_INPUT_ACTION = "StringInputAction";
	
	/**
	 * Represents the temporary casted spell id
	 */
	public String TEMP_CAST_SPELL = "TempCastSpell";
	
	/**
	 * Represents the Miasmic immunity effects
	 */
	public String MIASMIC_IMMUNITY = "MiasmicImmunity";
	
	/**
	 * Represents the Miasmic effects
	 */
	public String MIASMIC_EFFECT = "MiasmicEffect";
	
	/**
	 * Represents the Shop instance
	 */
	public String SHOP_INSTANCE = "ShopInstance";
	
	/**
	 * Represents the World Hash
	 */
	public String WORLD_HASH = "WorldHash";
	
	/**
	 * Represents the Bank state
	 */
	public String IS_BANKING = "Banking";
	
	/**
	 * Represents the Skill menu state
	 */
	public String SKILL_GUIDE_MENU = "SkillGuideMenu";
	
	/**
	 * Represents the Dialogue event state
	 */
	public String DIALOGUE_EVENT = "DialogueEvent";
	
	/**
	 * Represents the Dialogue event state
	 */
	public String BLANK_DIALOGUE_EVENT = "BlankDialogueEvent";
	
	/**
	 * Represents the max skill quantity for skilling menu (fletching, such)
	 */
	public String SKILL_DIALOGUE_MAX_QUANTITY = "SkillsDialogueMaxQuantity";
	
	/**
	 * Represents the skill quantity for skilling menu (fletching, such)
	 */
	public String SKILL_DIALOGUE_QUANTITY = "SkillsDialogueQuantity";
	
	/**
	 * Represents the Miasmic effects
	 */
	public String RECOVER_SPECIAL_POT = "RecoverSpecialPot";
	
	/**
	 * Represents the current Note index
	 */
	public String CURRENT_NOTE = "CurrentNote";
	
	/**
	 * Represents the last Vengeance timer
	 */
	public String LAST_VENG = "LastVeng";
	
	/**
	 * Represents the state of the player infusing to summoning scrolls
	 */
	public String INFUSING_SCROLL = "InfusingScroll";
	
	/**
	 * Represents the state shop access, this system needs to be reworked
	 */
	public String SHOP = "Shop";
	
	/**
	 * Represents the current shop transaction amount placed
	 */
	public String SHOP_TRANSACTION = "ShopTransaction";
	
	/**
	 * Represents the Shop buying state
	 */
	public String IS_SHOP_BUYING = "IsShopBuying";
	
	/**
	 * Represents the Shop selected shop (item seleted in the Shop container)
	 */
	public String SHOP_SELECTED_SLOT = "ShopSelectedSlot";
	
	/**
	 * Represents the Shop selected item from inventory (to sell, such)
	 */
	public String SHOP_SELECTED_INVENTORY = "ShopSelectedInventory";
	
	/**
	 * Represents the X slot of the bank item.
	 * This attribute is currently not in use.
	 */
	public String BANK_ITEM_X_SLOT = "BankItemXSlot";
	
	/**
	 * Represents the item that's being destroyed.
	 * Destroying items can not be undone.
	 */
	public String DESTROY_ITEM_ID = "DestroyItem";
	
	/**
	 * Represents the set level mode of Skill Targets
	 */
	public String SET_LEVEL = "SetLevel";
	
	/**
	 * Represents the set xp mode of Skill Targets
	 */
	public String SET_XP = "SetXP";
	
	/**
	 * Represents the special recovery state
	 */
	public String SPECIAL_RECOVERY = "SpecRecovery";
	
	/**
	 * Represents the last 3 codes
	 */
	public String FAIRY_RING_LOCATION_ARRAY = "LocationArray";
	
	/**
	 * Represents the Wheat grind state
	 */
	public String WHEAT_GRINDED = "WheatGrinded";
	
	/**
	 * Represents the Wheat deposited state
	 */
	public String WHEAT_DEPOSITED = "WheatDeposited";
	
	//unused for now
	public String BOW_FLETCHING = "BowFletching";
	public String BOW_FLETCHING_CARVING = "BowFletchingCarving";
	public String CHAT_TYPE = "chatType";
}