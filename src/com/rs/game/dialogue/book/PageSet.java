package com.rs.game.dialogue.book;

import lombok.Getter;

public class PageSet {

	@Getter
	private final Page[] pages;
	
	public PageSet(final Page...pages) {
		this.pages = pages;
	}
}