package skills.summoning;

import com.rs.game.item.Item;

/**
 * An egg to put in the incubator.
 * @author Vexia
 *
 */
public enum IncubatorEgg {
	PENGUIN(new Item(12483), 30, 30, new Item(12481)),
	RAVEN(new Item(11964), 50, 30, new Item(12484)),
	SARADOMIN_OWL(new Item(5077), 70, 60, new Item(12503)),
	ZAMORAK_HAWK(new Item(5076), 70, 60, new Item(12506)),
	GUTHIX_RAPTOR(new Item(5078), 70, 60, new Item(12509)),
	VULTURE(new Item(11965), 85, 60, new Item(12498)),
	CHAMELEON(new Item(12494), 90, 60, new Item(12492)),
	RED_DRAGON(new Item(12477), 99,  60, new Item(12469)),
	BLACK_DRAGON(new Item(12480), 99,  60, new Item(12475)),
	BLUE_DRAGON(new Item(12478), 99,  60, new Item(12471)),
	GREEN_DRAGON(new Item(12479), 99,  60, new Item(12473));
	
	/**
	 * The egg item.
	 */
	private final Item egg;
	
	/**
	 * The level.
	 */
	private final int level;
	
	/**
	 * The incubation time.
	 */
	private final int inucbationTime;
	
	/**
	 * The product item.
	 */
	private final Item product;

	/**
	 * Constructs a new {@Code IncubatorEgg} {@Code Object}
	 * @param egg the egg.
	 * @param level the level.
	 * @param inucbationTime the incubation time.
	 * @param product the product.
	 */
	private IncubatorEgg(Item egg, int level, int inucbationTime, Item product) {
		this.egg = egg;
		this.level = level;
		this.inucbationTime = inucbationTime;
		this.product = product;
	}
	
	/**
	 * Gets an egg by the item.
	 * @param item the item.
	 * @return the egg.
	 */
	public static IncubatorEgg forItem(Item item) {
		for (IncubatorEgg e : values()) {
			if (e.getEgg().getId() == item.getId()) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Gets the egg.
	 * @return the egg
	 */
	public Item getEgg() {
		return egg;
	}

	/**
	 * Gets the level.
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the inucbationTime.
	 * @return the inucbationTime
	 */
	public int getInucbationTime() {
		return inucbationTime;
	}

	/**
	 * Gets the product.
	 * @return the product
	 */
	public Item getProduct() {
		return product;
	}
	
}