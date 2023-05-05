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
	 * Represents the Familiar Special active state
	 */
	public String FAMILIAR_SPECIAL = "FamiliarSpec";
	
	/**
	 * Represents the Integer input value
	 */
	public String INTEGER_INPUT_ACTION = "integer_input_action";
	
	/**
	 * Represents the String input value
	 */
	public String STRING_INPUT_ACTION = "string_input_action";
	
	/**
	 * Represents the temporary casted spell id
	 */
	public String TEMP_CAST_SPELL = "tempCastSpell";
	
	/**
	 * Represents the Miasmic immunity effects
	 */
	public String MIASMIC_IMMUNITY = "miasmic_immunity";
	
	/**
	 * Represents the Miasmic effects
	 */
	public String MIASMIC_EFFECT = "miasmic_effect";
	
	/**
	 * Represents the Shop instance
	 */
	public String SHOP_INSTANCE = "shop_instance";
	
	/**
	 * Represents the World Hash
	 */
	public String WORLD_HASH = "worldHash";
	
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
	public String DIALOGUE_EVENT = "dialogue_event";
	
	/**
	 * Represents the max skill quantity for skilling menu (fletching, such)
	 * TODO: Implement proper system
	 */
	public String SKILL_DIALOGUE_MAX_QUANTITY = "SkillsDialogueMaxQuantity";
	
	/**
	 * Represents the Miasmic effects
	 */
	public String RECOVER_SPECIAL_POT = "Recover_Special_Pot";
	
	/**
	 * Represents the current Note index
	 */
	public String CURRENT_NOTE = "Current_Note";
	
	/**
	 * Represents the last Vengeance timer
	 */
	public String LAST_VENG = "LastVeng";
	
	/**
	 * Represents the state of the player infusing to summoning scrolls
	 */
	public String INFUSING_SCROLL = "infusing_scroll";
	
	/**
	 * Represents the state shop access, this system needs to be reworked
	 */
	public String SHOP = "Shop";
	
	/**
	 * Represents the current shop transaction amount placed
	 */
	public String SHOP_TRANSACTION = "shop_transaction";
	
	/**
	 * Represents the Shop buying state
	 */
	public String IS_SHOP_BUYING = "isShopBuying";
	
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
	public String BANK_ITEM_X_SLOT = "bank_item_X_Slot";
	
	/**
	 * Represents the X slot of the price checked item.
	 * This attribute is currently not in use.
	 */
	public String PC_ITEM_X_SLOT = "pc_item_X_Slot";
}