package com.rs.game.dialogue.book;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;

import lombok.Getter;
import lombok.Setter;

public abstract class Book extends DialogueEventListener {

	@Getter
	protected String name;

	@Getter
	protected PageSet[] sets;

	@Getter
	@Setter
	protected int index = -1;
	
	public Book(final Player player, final String name, final PageSet...sets) {
		super(player);
		this.player = player;
		this.name = name;
		this.sets = sets;
	}

	@Override
	public void start() {
		open(player);
	}

	@Override
	public void listenToDialogueEvent(int buttonId) {
		switch(buttonId) {
		case 29:
			next();
			break;
		case 28:
			previous();
			break;
		}
	}

	public boolean open(final Player player) {
		next();
		return true;
	}
	
	public void next() {
		player.getMovement().lock();
		index++;
		final Page[] set = sets[index].getPages();
		display(set);
		player.getMovement().unlock();
	}
	
	public void previous() {
		player.getMovement().lock();
		index--;
		if (index < 0) {
			index = 0;
		}
		final Page[] set = sets[index].getPages();
		display(set);
		player.getMovement().unlock();
	}
	
	public void display(Page[] set) {}
	
	public void finish() {}
}