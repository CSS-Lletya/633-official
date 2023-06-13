package com.rs.game.item;

/**
 * Simple utility class to cleanup basic item on item checking
 * 
 * @author Dennis
 *
 */
public final class UseWith {

	/**
	 * The items that are being executed with
	 */
	private final Item firstItem, secondItem;

	/**
	 * Constructs a new Item on Item check
	 * 
	 * @param firstItem
	 * @param secondItem
	 * @param run
	 */
	public UseWith(Item firstItem, Item secondItem) {
		this.firstItem = firstItem;
		this.secondItem = secondItem;
	}

	/**
	 * Creates the Item on Item check
	 * 
	 * @param firstItem
	 * @param secondItem
	 * @param run
	 * @return
	 */
	public UseWith execute(Item firstItem, Item secondItem, Runnable run) {
		if (areItemsEqual(firstItem.getId(), secondItem.getId()))
			run.run();
		return this;
	}

	private boolean areItemsEqual(int itemId1, int itemId2) {
		return (firstItem.getId() == itemId1 && secondItem.getId() == itemId2)
				|| (secondItem.getId() == itemId1 && firstItem.getId() == itemId2);
	}
}