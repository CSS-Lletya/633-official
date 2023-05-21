package com.rs.game.item;

import com.rs.game.map.WorldTile;

import lombok.Data;

@Data
public class ItemSpawn {

    private final int itemId;
    private final int amount;
    private final WorldTile tile;
    private final String comment;

    public void spawn() {
        FloorItem.addGroundItemForever(new Item(itemId, amount), tile);
    }
}