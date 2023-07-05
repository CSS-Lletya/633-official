package com.rs.game.item;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Iterables;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a single item.
 * <p/>
 * 
 * @author Graham / edited by Dragonkk(Alex)
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class Item {

	private int id;
	protected int amount;
	private transient int slot;
	private Map<String, Object> metaData;

	@Override
	public Item clone() {
		return new Item(id, amount, Utility.cloneMap(metaData));
	}

	public Item(Item item) {
		this(item.getId(), item.getAmount(), Utility.cloneMap(item.getMetaData()));
	}

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int amount) {
		this(id, amount, false);
	}

	public Item(int id, int amount, Map<String, Object> metaData) {
		this(id, amount);
		this.metaData = metaData;
	}

	public Item(int id, int amount, boolean amt0) {
		this.id = (short) id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public Item setSlot(int slot) {
		this.slot = slot;
		return this;
	}

	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getItemDefinitions(id);
	}

	public String getName() {
		return getDefinitions().getName();
	}

	/**
	 * Converts an int array into an {@link Item} array.
	 * @param id the array to convert into an item array.
	 * @return the item array containing the values from the int array.
	 */
	public static final Item[] toList(int... id) {
		ObjectList<Item> items = new ObjectArrayList<>();
		for(int identifier : id) {
			items.add(new Item(identifier));
		}
		return Iterables.toArray(items, Item.class);
	}
	
	public Item addMetaData(String key, Object value) {
		if (metaData == null)
			metaData = new HashMap<String, Object>();
		metaData.put(key, value);
		return this;
	}

	public Map<String, Object> getMetaData() {
		return metaData;
	}

	public Object getMetaData(String key) {
		if (metaData != null)
			return metaData.get(key);
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T setMetaDataO(String name, Object value) {
		if (metaData == null)
			metaData = new HashMap<>();
		if (value == null) {
			Object old = metaData.remove(name);
			return old == null ? null : (T) old;
		}
		Object old = metaData.put(name, value);
		return old == null ? null : (T) old;
	}

	@SuppressWarnings("unchecked")
	public <T> T getMetaDataO(String name) {
		if (metaData == null)
			return null;
		if (metaData.get(name) == null)
			return null;
		return (T) metaData.get(name);
	}
	
	public int incMetaDataI(String key) {
		int val = getMetaDataI(key) + 1;
		addMetaData(key, val);
		return val;
	}
	
	public int decMetaDataI(String key) {
		int val = getMetaDataI(key) - 1;
		addMetaData(key, val);
		return val;
	}
	
	public int getMetaDataI(String key) {
		return getMetaDataI(key, -1);
	}
	
    public int getMetaDataI(String key, int defaultVal) {
        if (metaData != null && metaData.get(key) != null) {
            if (metaData.get(key) instanceof Integer)
                return (int) metaData.get(key);
            return (int) Math.floor(((double) metaData.get(key)));
        }
        return defaultVal;
    }

	public void deleteMetaData() {
		this.metaData = null;
	}
	
	@Override
	public String toString() {
		return "[" + ItemDefinitions.getItemDefinitions(id).name + " ("+id+"), " + amount + "]";
	}

	public boolean containsMetaData() {
		return metaData != null;
	}

	public double getMetaDataD(String key, double defaultVal) {
		if (metaData != null) {
			if (metaData.get(key) != null);
				return (double) metaData.get(key);
		}
		return defaultVal;
	}
	
	public double getMetaDataD(String key) {
		return getMetaDataD(key, 0);
	}
}