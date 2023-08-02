package com.rs.game.player.content;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.rs.game.player.Player;

public class GodswordDismantling {

	private static final int[] godswords = { 11694, 11696, 11698, 11700 };
	private static final int[] hilts = { 11702, 11704, 11706, 11708 };

	private static final int GODSWORD_BLADE = 11690;

	private static final String DISMANTLE_MESSAGE = "You dismantle the godsword blade from the hilt.";
	private static final String SPACE_MESSAGE = "You need at least 2 free inventory slots to dismantle your hilt from the godswords.";

	private static final List<Integer> godSwordsList = IntStream.of(godswords).boxed().collect(Collectors.toList());
	private static final List<Integer> hiltsList = IntStream.of(hilts).boxed().collect(Collectors.toList());

	public static int getHiltForGodsword(int itemId) {
		int index = godSwordsList.indexOf(itemId);
		return index >= 0 ? hiltsList.get(index) : -1;
	}

	public static void dismantleGodsword(Player p, int itemId) {
		if (p.getInventory().getFreeSlots() < 2) {
			p.getPackets().sendGameMessage(SPACE_MESSAGE);
			return;
		}
		IntStream.range(0, godswords.length).filter(i -> itemId == godswords[i] && p.getInventory().containsAny(itemId))
				.findFirst().ifPresent(i -> {
					p.getInventory().deleteItem(itemId, 1);
					p.getInventory().addItem(getHiltForGodsword(itemId), 1);
					p.getInventory().addItem(GODSWORD_BLADE, 1);
					p.getPackets().sendGameMessage(DISMANTLE_MESSAGE);
					p.getInventory().refresh();
				});
	}
}