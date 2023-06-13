package skills.herblore;

/**
 * Represents the barbarian potion.
 * @author 'Vexia
 */
public enum BarbarianPotion {
	ATTACK_POTION(123, 4, 11429, 8, true),
	ANTI_POISION_POTION(177, 6, 11433, 12, true),
	RELIC(4846, 9, 11437, 14, true),
	STRENGTH_POTION(117, 14, 11443, 17, true),
	RESTORE_POTION(129, 24, 11449, 21, true),
	ENERGY_POTION(3012, 29, 11453, 23, false),
	DEFENCE_POTION(135, 33, 11457, 25, false),
	AGILITY_POTION(3036, 37, 11461, 27, false),
	COMBAT_POTION(9743, 40, 11445, 28, false),
	PRAYER_POTION(141, 42, 11465, 29, false),
	SUPER_ATTACK_POTION(147, 47, 11469, 33, false),
	SUPER_ANTIPOISION_POTION(183, 51, 11473, 35, false),
	FISHING_POTION(143, 53, 11477, 38, false),
	SUPER_ENERGY_POTION(3020, 59, 11481, 42, false),
	HUNTER_POTION(10002, 11517, 58, 40, false),
	SUPER_STRENGTH_POTION(159, 59, 11485, 42, false),
	SUPER_RESTORE(3028, 67, 11493, 48, false),
	SUPER_DEFENCE_POTION(165, 71, 11497, 50, false),
	ANTIFIRE_POTION(2456, 11505, 75, 53, false),
	RANGING_POTION(171, 80, 11509, 54, false),
	MAGIC_POTION(3044, 83, 11513, 57, false),
	ZAMORAK_BREW(191, 85, 11521, 58, false);
	
	/**
	 * Constructs a new {@code BarbarianPotion} {@Code Object}.
	 * @param item the item.	
	 * @param product the product.
	 * @param exp the exp.
	 * @param both if both can be added(roe, cavier).
	 */
	BarbarianPotion(int item, int level, int product, double exp, boolean both) {
		this.item = item;
		this.level = level;
		this.product = product;
		this.exp = exp;
		this.both = both;
	}
	
	public static void main(String[] args) {
		for (BarbarianPotion pots : BarbarianPotion.values()) {
			System.out.println(pots.item + ",");
		}
	}

	/**
	 * The item id.
	 */
	private int item;
	
	/**
	 * The product item id.
	 */
	private int product;
	
	/**
	 * The level required.
	 */
	private int level;
	
	/**
	 * the exp gained.
	 */
	private double exp;
	
	/**
	 * Represents if both <b>Roe</b> & <b>Cavier</b> can be added.
	 */
	private boolean both;

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public int getItem() {
		return item;
	}

	/**
	 * Gets the product.
	 * @return The product.
	 */
	public int getProduct() {
		return product;
	}

	/**
	 * Gets the level.
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the exp.
	 * @return The exp.
	 */
	public double getExp() {
		return exp;
	}

	/**
	 * Gets the both.
	 * @return The both.
	 */
	public boolean isBoth() {
		return both;
	}
	
	/**
	 * Gets the value from the id.
	 * @param id the id.
	 * @return the value.
	 */
	public static BarbarianPotion forId(int id) {
		for (BarbarianPotion pot : BarbarianPotion.values()) {
			if (pot.getItem() == id) {
				return pot;
			}
		}
		return null;
	}
}
