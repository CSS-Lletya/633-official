package com.rs.game.npc;

import java.util.concurrent.ThreadLocalRandom;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.utilities.Chance;
import com.rs.utilities.RandomUtils;

import lombok.Data;

/**
 * A model representing an item within a rational item table that can be dropped.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
@Data
public class Drop {
	
	/**
	 * The identification of this {@code Drop}.
	 */
	public final int id;
	
	/**
	 * The minimum amount that will be dropped.
	 */
	public final int minimum;
	
	/**
	 * The maximum amount that will be dropped.
	 */
	public final int maximum;
	
	/**
	 * The chance of this item being dropped.
	 */
	private final Chance chance;
	
	/**
	 * Converts this {@code Drop} into an {@link Item} Object.
	 * @return the converted drop.
	 */
	public Item toItem() {
		return new Item(getId(), RandomUtils.inclusive(getMinimum(), getMaximum()));
	}
	
	/**
	 * Returns the condition if this item is rare.
	 * @return value.
	 */
	public boolean isRare() {
		return chance == Chance.RARE || chance == Chance.VERY_RARE || chance == Chance.EXTREMELY_RARE;
	}
	
	/**
	 * Gets the pricing value of this drop.
	 * @return value.
	 */
	public int value() {
		return ItemDefinitions.getItemDefinitions(id).getValue() * maximum;
	}
	
	/**
	 * Tries to roll this item.
	 * @param rand random gen.
	 * @return condition if successful.
	 */
	public boolean roll(ThreadLocalRandom rand) {
		return chance.getRoll() >= rand.nextDouble();
	}
}