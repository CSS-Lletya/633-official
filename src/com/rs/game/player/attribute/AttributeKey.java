package com.rs.game.player.attribute;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.IdentityHashMap;

import com.google.common.base.MoreObjects;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

/**
 * A {@link String} wrapper also known as an alias that defines behavior and functionality for attributes. {@code String}
 * keys are forcibly interned and aliased on startup into an {@link IdentityHashMap} to be easily accessible as well as high
 * performing.
 * <p>
 * <p>
 * The naming convention for all {@code String} keys is {@code lower_underscore}. Spaces and uppercase letters are not
 * allowed.
 * @param <T> The {@link Object} type represented by this key.
 * @author lare96 <http://github.org/lare96>
 * @author Dennis
 */
public final class AttributeKey<T> {
	
	/**
	 * An {@link IdentityHashMap} of {@link String} keys mapped to their {@code AttributeKey} aliases. All {@code String}s
	 * added to this tool.mapviewer are forcibly interned so we can compare them by their identity for faster performance.
	 */
	@SuppressWarnings("rawtypes")
	public static final Object2ObjectArrayMap<String, AttributeKey> ALIASES = new Object2ObjectArrayMap<>();
	
	/**
	 * Aliases all attributes that will be used while Main is online. This is called eagerly on startup.
	 */
	public static void init() {
//		AttributeKey.forPersistent("", false);//saves
//		AttributeKey.forTransient("", false);//doesn't save
		
		AttributeKey.forTransient(Attribute.TRADE_TARGET, false);
		AttributeKey.forTransient(Attribute.TRADE_IS_REMOVE, false);
		AttributeKey.forTransient(Attribute.TRADE_ITEM_X_SLOT, -1);
		AttributeKey.forTransient(Attribute.FAMILIAR_SPECIAL, 100);
		AttributeKey.forTransient(Attribute.INTEGER_INPUT_ACTION, -1);
		AttributeKey.forTransient(Attribute.STRING_INPUT_ACTION, "");
		AttributeKey.forTransient(Attribute.TEMP_CAST_SPELL, -1);
		AttributeKey.forTransient(Attribute.MIASMIC_IMMUNITY, false);
		AttributeKey.forTransient(Attribute.MIASMIC_EFFECT, false);
		AttributeKey.forTransient(Attribute.WORLD_HASH, -1);
		AttributeKey.forTransient(Attribute.IS_BANKING, false);
		AttributeKey.forTransient(Attribute.SKILL_GUIDE_MENU, 0);
		AttributeKey.forTransient(Attribute.DIALOGUE_EVENT, false);
		AttributeKey.forTransient(Attribute.BLANK_DIALOGUE_EVENT, false);
		AttributeKey.forTransient(Attribute.SKILL_DIALOGUE_MAX_QUANTITY, 28);
		AttributeKey.forTransient(Attribute.SKILL_DIALOGUE_QUANTITY, 1);
		AttributeKey.forTransient(Attribute.RECOVER_SPECIAL_POT, -1);
		AttributeKey.forTransient(Attribute.CURRENT_NOTE, -1);
		AttributeKey.forTransient(Attribute.LAST_VENG, -1);
		AttributeKey.forTransient(Attribute.INFUSING_SCROLL, false);
		AttributeKey.forTransient(Attribute.SHOP, false);
		AttributeKey.forTransient(Attribute.SHOP_TRANSACTION, 1);
		AttributeKey.forTransient(Attribute.IS_SHOP_BUYING, false);
		AttributeKey.forTransient(Attribute.SHOP_SELECTED_SLOT, -1);
		AttributeKey.forTransient(Attribute.SHOP_SELECTED_INVENTORY, false);
		AttributeKey.forTransient(Attribute.BANK_ITEM_X_SLOT, -1);
		AttributeKey.forTransient(Attribute.DESTROY_ITEM_ID, -1);
		AttributeKey.forTransient(Attribute.SET_LEVEL, -1);
		AttributeKey.forTransient(Attribute.SET_XP, -1);
		AttributeKey.forTransient(Attribute.SPECIAL_RECOVERY, -1);
		AttributeKey.forTransient(Attribute.FAIRY_RING_LOCATION_ARRAY, new int[] {});
		AttributeKey.forPersistent(Attribute.WHEAT_GRINDED, false);
		AttributeKey.forPersistent(Attribute.WHEAT_DEPOSITED, false);
		AttributeKey.forPersistent(Attribute.BOW_FLETCHING, false);
		AttributeKey.forPersistent(Attribute.BOW_FLETCHING_CARVING, false);
	}
	
	/**
	 * Aliases {@code name} with an initial value of {@code initialValue} that will be written to and read from the character
	 * file.
	 * @param name The name of this key.
	 * @param initialValue The initial value of this key.
	 */
	public static <T> void forPersistent(String name, T initialValue) {
		ALIASES.put(name, new AttributeKey<>(name, initialValue, true));
	}
	
	/**
	 * Aliases {@code name} with an initial value of {@code initialValue}.
	 * @param name The name of this key.
	 * @param initialValue The initial value of this key.
	 */
	public static <T> void forTransient(String name, T initialValue) {
		ALIASES.put(name, new AttributeKey<>(name, initialValue, false));
	}
	
	/**
	 * The name of this alias.
	 */
	private final String name;
	
	/**
	 * The initial value of this alias.
	 */
	private final T initialValue;
	
	/**
	 * If the value of this alias should be serialized.
	 */
	private final boolean isPersistent;
	
	/**
	 * The fully-qualified class name of this attribute type.
	 */
	private final String typeName;
	
	/**
	 * Creates a new {@link AttributeKey}.
	 * @param name The name of this alias.
	 * @param initialValue The initial value of this alias.
	 * @param isPersistent If the value of this alias should be serialized.
	 */
	private AttributeKey(String name, T initialValue, boolean isPersistent) {
		checkArgument(!name.isEmpty(), "attribute name length <= 0");
		checkState(!ALIASES.containsKey(name.intern()), "attribute already aliased");
		
		this.name = name.intern();
		this.initialValue = initialValue;
		this.isPersistent = isPersistent;
		typeName = initialValue.getClass().getName();
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", name).add("persistent", isPersistent).add("type", typeName).toString();
	}
	
	/**
	 * @return The name of this alias.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The initial value of this alias.
	 */
	public T getInitialValue() {
		return initialValue;
	}
	
	/**
	 * @return {@code true} if the value of this alias should be serialized, {@code false} otherwise.
	 */
	public boolean isPersistent() {
		return isPersistent;
	}
	
	/**
	 * @return The fully-qualified class name of this attribute type.
	 */
	public String getTypeName() {
		return typeName;
	}
}