package com.rs.game.player.content;

import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

public enum ItemCombine {
	
	ORANGE_DYE(new Item(1769), new Item(1765), new Item(1763)),
	GREEN_DYE(new Item(1771), new Item(1765), new Item(1767)),
	PURPLE_DYE(new Item(1773), new Item(1767), new Item(1763)),
	
	ARMADYL_GS(new Item(11694), new Item(11690), new Item(11702)),
	BANDOS_GS(new Item(11696), new Item(11690), new Item(11704)),
	SARADOMIN_GS(new Item(11698), new Item(11690), new Item(11706)),
	ZAMORAK_GS(new Item(11700), new Item(11690), new Item(11708)),
	
	MITHRIL_GRAPPLE_HOOK(new Item(9419), new Item(9416), new Item(954)),
	
	DARKBOW_YELLOW(new Item(15701), new Item(11235), new Item(1765)),
	DARKBOW_BLUE(new Item(15702), new Item(11235), new Item(1767)),
	DARKBOW_WHITE(new Item(15703), new Item(11235), new Item(239)),
	DARKBOW_GREEN(new Item(15704), new Item(11235), new Item(1771)),
	CLEAN_DARKBOW_YELLOW(new Item(11235), new Item(15701), new Item(3188)),
	CLEAN_DARKBOW_BLUE(new Item(11235), new Item(15702), new Item(3188)),
	CLEAN_DARKBOW_WHITE(new Item(11235), new Item(15703), new Item(3188)),
	CLEAN_DARKBOW_GREEN(new Item(11235), new Item(15704), new Item(3188)),
	
	WHIP_YELLOW(new Item(15441), new Item(4151), new Item(1765)),
	WHIP_BLUE(new Item(15442), new Item(4151), new Item(1767)),
	WHIP_WHITE(new Item(15443), new Item(4151), new Item(239)),
	WHIP_GREEN(new Item(15444), new Item(4151), new Item(1771)),
	CLEAN_WHIP_YELLOW(new Item(4151), new Item(15441), new Item(3188)),
	CLEAN_WHIP_BLUE(new Item(4151), new Item(15442), new Item(3188)),
	CLEAN_WHIP_WHITE(new Item(4151), new Item(15443), new Item(3188)),
	CLEAN_WHIP_GREEN(new Item(4151), new Item(15444), new Item(3188));
	
	/**
	 * The product combining the ingredients.
	 */
	private final Item product;
	
	/**
	 * The ingredients for this process, removing them all and giving the {@link #product}.
	 */
	private final Item[] ingredients;
	
	/**
	 * The optional animation used on the combination.
	 */
	private final Optional<Animation> animation;
	
	/**
	 * The optional graphic used on the combination.
	 */
	private final Optional<Graphics> graphic;
	
	static final ImmutableSet<ItemCombine> VALUES = ImmutableSet.copyOf(values());
	
	/**
	 * Creates a new combination without any animation or graphic.
	 * @param product the product being created.
	 * @param ingredients the ingredients used.
	 */
	ItemCombine(Item product, Item... ingredients) {
		this.product = product;
		this.ingredients = ingredients;
		this.animation = Optional.empty();
		this.graphic = Optional.empty();
	}
	
	/**
	 * Creates a new item combination with a {@link Animation} and {@link Graphic}.
	 * @param product the product being created.
	 * @param anim the animation being handled on combination.
	 * @param graphic the graphic being handled on combination.
	 * @param ingredients the ingredients used.
	 */
	ItemCombine(Item product, Animation anim, Graphics graphic, Item... ingredients) {
		this.product = product;
		this.ingredients = ingredients;
		this.animation = Optional.of(anim);
		this.graphic = Optional.of(graphic);
	}
	
	public static boolean handle(Player player, Inventory container, Item use, Item used) {
		for(ItemCombine comb : VALUES) {
			boolean firstFound = false;
			boolean secondFound = false;
			for(Item item : comb.getIngredients()) {
				if(!firstFound && item.equals(use))
					firstFound = true;
				if(!secondFound && item.equals(used))
					secondFound = true;
				if(firstFound && secondFound) {
					comb.combine(player, container);
					return true;
				}
			}
		}
		return false;
	}
	
	public void combine(Player player, Inventory container) {
		if(!requirement(player))
			return;
		if(container.containsItems(ingredients)) {
			player.getAudioManager().sendSound(Sounds.ATTACH_SOMETHING);
			animation.ifPresent(player::setNextAnimation);
			graphic.ifPresent(player::setNextGraphics);
			container.removeItems(ingredients);
			container.addItem(new Item(product));
		} else {
			player.getPackets().sendGameMessage("You do not have all of the ingredients to do this.");
		}
	}
	
	/**
	 * An requirement check before combining the item.
	 * Checks for levels or anything else specific.
	 * @param player the player doing the action.
	 * @return {@code true} if the check passed, {@code false} otherwise.
	 */
	public boolean requirement(Player player) {
		return true;
	}
	
	/**
	 * Gets the {@link #product}
	 */
	public Item getProduct() {
		return product;
	}
	
	/**
	 * Gets the {@link #ingredients}
	 */
	public Item[] getIngredients() {
		return ingredients;
	}
	
	/**
	 * Gets the {@link #animation}
	 */
	public Optional<Animation> getAnimation() {
		return animation;
	}
	
	/**
	 * Gets the {@link #graphic}
	 */
	public Optional<Graphics> getGraphic() {
		return graphic;
	}
	
}
