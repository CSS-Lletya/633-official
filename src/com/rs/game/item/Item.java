package com.rs.game.item;

import com.google.common.collect.Iterables;
import com.rs.cache.loaders.ItemDefinitions;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.Data;

/**
 * Represents a single item.
 * <p/>
 * 
 * @author Graham / edited by Dragonkk(Alex)
 */
@Data
public class Item {

	private int id;
	protected int amount;

	@Override
	public Item clone() {
		return new Item(id, amount);
	}

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int amount) {
		this(id, amount, false);
	}

	public Item(int id, int amount, boolean amt0) {
		this.id = (short) id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public Item(Item item) {
		this.id = (short) item.getId();
		this.amount = item.getAmount();
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
	public static final Item[] convert(int... id) {
		ObjectList<Item> items = new ObjectArrayList<>();
		for(int identifier : id) {
			items.add(new Item(identifier));
		}
		return Iterables.toArray(items, Item.class);
	}
}