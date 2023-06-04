package com.rs.game.player.content.trails;

import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtils;

public class PuzzleBox {

    public ItemsContainer<Item> currentPuzzle, completePuzzle;
    private int emptySlot, puzzleId;
    private boolean complete;
    private transient Player player;

    public PuzzleBox(Player player, int id) {
        this.player = player;
        this.puzzleId = id;
    }

    public void unlockPuzzleBox(Player player) {
        player.getPackets().sendUnlockIComponentOptionSlots(363, 4, 0, 25, 0);
    }

    public void openPuzzle() {
        if (currentPuzzle == null)
            generatePuzzle();
        unlockPuzzleBox(player);
        player.getPackets().sendItems(207, completePuzzle);
        player.getPackets().sendItems(140, currentPuzzle);
        player.getInterfaceManager().sendInterface(363);
    }

    private boolean canMove(int slotId) {
        if ((slotId + 1) == emptySlot || (slotId - 1) == emptySlot || (slotId + 5) == emptySlot || (slotId - 5) == emptySlot)
            return true;
        return false;
    }

    public void shiftPuzzle(int slotId) {
        if (complete) {
            player.getPackets().sendUpdateItems(140, currentPuzzle, slotId, slotId);
            return;
        }
        if (!canMove(slotId))
            return;
        Item movedTile = currentPuzzle.get(slotId);
        currentPuzzle.get(emptySlot).setId(movedTile.getId());
        currentPuzzle.get(slotId).setId(-1);
        player.getPackets().sendItems(140, currentPuzzle);
        emptySlot = slotId;
        player.getPackets().sendUpdateItems(140, currentPuzzle, slotId, emptySlot, slotId);
        checkCompletion();
    }

    public void checkCompletion() {
        for (int i = 0; i < 24; i++) {
            if (currentPuzzle.get(i).getId() != completePuzzle.get(i).getId())
                return;
        }
        complete = true;
        for (Puzzles puzzle : Puzzles.values()) {
            if (puzzle.getFirstTileId() == puzzleId) {
                player.getInventory().deleteItem(puzzle.getUnsolvedPuzzleId(), 1);
                player.getInventory().addItem(puzzle.getSolvedPuzzleId(), 1);
            }
        }
        player.getPackets().sendGameMessage("Congratulations! You've solved the puzzle!");
        return;
    }

    public void generatePuzzle() {
        ItemsContainer<Item> pieces = new ItemsContainer<Item>(24, false);
        completePuzzle = new ItemsContainer<Item>(24, false);
        currentPuzzle = new ItemsContainer<Item>(25, false);
        for (int i = 0; i < 24; i++) {
            completePuzzle.add(new Item(puzzleId + i, 1));
            pieces.add(new Item(puzzleId + i, 1));
        }
        emptySlot = RandomUtils.getRandom(24);
        for (int i = 0; i < 24; i++) {
            if (emptySlot == i)
                currentPuzzle.add(new Item(-1, 1));
            Item randomPiece = pieces.get(RandomUtils.random(pieces.getUsedSlots()));
            currentPuzzle.add(randomPiece);
            pieces.remove(randomPiece);
            pieces.shift();
        }
    }
}