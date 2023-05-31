package com.rs.game.dialogue.book;

import java.util.stream.IntStream;

import com.rs.game.player.Player;

public class DemoBook extends Book {

	private static PageSet[] PAGES = new PageSet[] {
			new PageSet(new Page(
					new BookLine("First Page side one", 30), new BookLine("First Page side two", 41),
					new BookLine("Second line", 31), new BookLine("Second line", 42))),
			new PageSet(new Page(
							new BookLine("Second Page side one", 30), new BookLine("Second Page side two", 41),
							new BookLine("Second line", 31), new BookLine("Second line", 42))),
			new PageSet(new Page(
							new BookLine("Third Page side one", 30), new BookLine("Third Page side two", 41),
							new BookLine("Second line", 31), new BookLine("Second line", 42)))
	};

	public DemoBook(final Player player) {
		super(player, "My Book", PAGES);
	}
	
	@Override
	public void display(Page[] set) {
		player.getMovement().lock();
		player.getInterfaceManager().sendInterface(959);
		IntStream.rangeClosed(0, 53).forEach(line -> player.getPackets().sendIComponentText(959, line, ""));
		player.getPackets().sendIComponentText(959, 5, getName());
		for (Page page : set) {
			for (BookLine line : page.getLines()) {
				player.getPackets().sendIComponentText(959, line.getChild(), line.getMessage());
			}
		}
		boolean lastPage = index == sets.length-1;
		player.getPackets().sendHideIComponent(959, 28, (index == 0));
		player.getPackets().sendHideIComponent(959, 29, lastPage);
		if (lastPage)
			finish();
		player.getMovement().unlock();
	}
	
	@Override
	public void finish() {
		player.getPackets().sendGameMessage("Do something special at the end?");
	}
}