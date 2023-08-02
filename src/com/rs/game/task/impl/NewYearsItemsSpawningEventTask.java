package com.rs.game.task.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rs.GameConstants;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtility;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A Rare Item spawning system that occurs every 8 hours!
 * Specifically for New Years in this case.
 * @author Dennis
 *
 */
public final class NewYearsItemsSpawningEventTask extends Task {

    /**
     * Creates a new {@link NewYearsItemsSpawningEventTask}.
     */
    public NewYearsItemsSpawningEventTask() {
        super(60 * 60 * 8, false);
    }

    @Override
    public void execute() {
    	if (!GameConstants.NEW_YEARS_EVENT)
    		return;
        int[] RARES = {962, 1038, 1040, 1042, 1044, 1040, 1046, 1048, 1050};
        Places random = Places.VALUES.get(RandomUtility.random(Places.values().length));
        FloorItem.addGroundItem(new Item(RandomUtility.random(RARES)), random.getLocation(), 60 * 10);
        World.sendWorldMessage("[New Years Event]Oh wow! a super rare item has appeared near " + random.getLocationName() + ", better be the first to go get it!");
   }

    @Override
    public void onCancel() {
        World.get().submit(new NewYearsItemsSpawningEventTask());
    }

    @AllArgsConstructor
    @Getter
    public enum Places {
        LUMBRIDGE(new WorldTile(3225, 3203, 0), "Lumbridge"),
        GE(new WorldTile(3169, 3491, 0), "Grand Exchange"),
        RED_DRAGONS(new WorldTile(3192, 3801, 0), "Red Dragons (PVP)"),
        MISC_ISLAND(new WorldTile(2514, 3872, 0), "Miscellania Island"),
        FISHING_PLATFORM(new WorldTile(2784, 3279, 1), "Fishing Platform"),
        WIZARD_TOWER(new WorldTile(3106, 3157, 2), "Wizards Tower"),
        SLAYER_TOWER(new WorldTile(3464, 3560, 0), "Slayer Tower"),
        LIGHTHOUSE(new WorldTile(2510, 3638, 2), "the Lighthouse"),
        SORC_GARDEN(new WorldTile(2917, 5467, 0), "Sorceress Garden"),
        CLAN_WARS(new WorldTile(2968, 9711, 0), "Clan Wars"),
        ZMI_ALTER(new WorldTile(3315, 4814, 0), "ZMI Alter"),
        ;
        private static final List<Places> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private WorldTile location;
        private String locationName;
    }
}