package com.rs.game.player.content.trails;

public enum Puzzles {

    TROLL_PUZZLE(3643, 13021, 13033),
    CASTLE_PUZZLE(2749, 13017, 13029),
    TREE_PUZZLE(3619, 3565, 3578),
    BRIDGE_PUZZLE(18913, 13011, 13023),
    BANDOS_PUZZLE(18865, 3576, 3569),
    CORPOREAL_BEAST_PUZZLE(18889, 2795, 3571),
    KING_BLACK_DRAGON_PUZZLE(18841, 13013, 13019);

    private int firstTileId, unsolvedItemId, solvedItemId;

    Puzzles(int firstTileId, int unsolvedItemId, int solvedItemId) {
        this.firstTileId = firstTileId;
        this.unsolvedItemId = unsolvedItemId;
        this.solvedItemId = solvedItemId;
    }

    public int getFirstTileId() {
        return firstTileId;
    }

    public int getUnsolvedPuzzleId() {
        return unsolvedItemId;
    }

    public int getSolvedPuzzleId() {
        return solvedItemId;
    }

}