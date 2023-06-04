package com.rs.game.item;

/**
 * Simple utility class to cleanup basic item on item checking
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
	 * @param firstItem
	 * @param secondItem
	 * @param run
	 */
	public UseWith(Item firstItem, Item secondItem, Runnable run) {
		this.firstItem = firstItem;
		this.secondItem = secondItem;
		execute(firstItem, secondItem, run);
	}
	
	/**
	 * Creates the Item on Item check
	 * @param firstItem
	 * @param secondItem
	 * @param run
	 * @return
	 */
	private UseWith execute(Item firstItem, Item secondItem, Runnable run) {
		if (this.firstItem == firstItem || this.firstItem == secondItem && this.secondItem == secondItem || this.secondItem == firstItem)
			run.run();
		return this;
	}
}