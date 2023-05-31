package com.rs.game.dialogue.book;

import lombok.Getter;

public class Page {
	
	@Getter
	private final BookLine[] lines;
	
	public Page(BookLine...lines) {
		this.lines = lines;
	}
}