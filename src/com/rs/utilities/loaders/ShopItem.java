// This program is free software: you can redistribute it and/or modify
package com.rs.utilities.loaders;

import com.rs.game.item.Item;

public class ShopItem {
	private Item item;
	private transient int ticks;
	private int capStock;
	private int customPrice = -1;
	private int restockTicks = -1;

	public ShopItem(int id, int amount) {
		item = new Item(id, amount);
	}

	public void init() {
		capStock = item.getAmount();
	}

	public Item getItem() {
		return item;
	}

	public int getCustomPrice() {
		return customPrice;
	}

	public int getRestockTicks() {
		return restockTicks <= 0 ? 50 : restockTicks;
	}

	public void setCap(int cap) {
		capStock = cap;
	}

	public boolean tickRestock() {
		if (++ticks > getRestockTicks()) {
			ticks = 0;
			if (item.getAmount() > capStock) {
				item.setAmount(item.getAmount()-1);
				return true;
			}
			if (item.getAmount() < capStock) {
				item.setAmount(item.getAmount()+1);
				return true;
			}
		}
		return false;
	}
}
