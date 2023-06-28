package com.rs.game.player.content;

/**
 * @author Savions.
 */
public class GrandExchangeOffer {

	private final int slot;
	private final int stage;
	private final boolean remove;
	private final int itemId;
	private final int totalPrice;
	private final int currentPrice;
	private final int totalAmount;
	private final int currentAmount;

	public GrandExchangeOffer(int slot, int stage, boolean remove, int itemId, int totalPrice, int currentPrice, int totalAmount, int currentAmount) {
		this.slot = slot;
		this.stage = stage;
		this.remove = remove;
		this.itemId = itemId;
		this.totalPrice = totalPrice;
		this.currentPrice = currentPrice;
		this.totalAmount = totalAmount;
		this.currentAmount = currentAmount;
	}

	public int getSlot() {
		return slot;
	}

	public int getStage() {
		return stage;
	}

	public boolean isRemove() {
		return remove;
	}

	public int getItemId() {
		return itemId;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}
}
