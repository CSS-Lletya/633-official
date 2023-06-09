// This program is free software: you can redistribute it and/or modify
package com.rs.utilities.loaders;

public class ShopDef {

	private String name;
	private boolean generalStore;
	private boolean buyOnly;
	private int currency;
	private ShopItem[] items;
	private int[] npcIds;

	public String getName() {
		return name;
	}

	public ShopItem[] getItems() {
		return items;
	}

	public int getCurrency() {
		return currency;
	}

	public boolean isGeneralStore() {
		return generalStore;
	}

	public int[] getNpcIds() {
		return npcIds;
	}

	public boolean isBuyOnly() {
		return buyOnly;
	}
}
