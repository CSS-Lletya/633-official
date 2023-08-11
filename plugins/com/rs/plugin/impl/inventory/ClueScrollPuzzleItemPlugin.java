package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.trails.Puzzles;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;

@InventoryWrapper(itemId = {2798, 3565, 3576, 19042}, itemNames = {  })
public class ClueScrollPuzzleItemPlugin extends InventoryListener {

	@Override
	public void execute(Player player, Item item, int slotId, int option) {
		
		for (Puzzles puzzle : Puzzles.values()) {
			if (item.getId() == puzzle.getUnsolvedPuzzleId()) {
				if (player.getPuzzleBox() == null)
					player.setPuzzleBox(puzzle.getFirstTileId());
				player.getPuzzleBox().openPuzzle();
			} else if (item.getId() == puzzle.getSolvedPuzzleId()) {
				if (player.getPuzzleBox() != null)//just in case.
					player.getPuzzleBox().openPuzzle();
			}
				
		}
		
		player.getTreasureTrailsManager().openPuzzle(item.getId());
	}
}