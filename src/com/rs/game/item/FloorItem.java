package com.rs.game.item;

import com.rs.cores.CoresManager;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.utilities.Ticks;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FloorItem extends Item {

	public int type;
    private WorldTile tile;
    private String ownerName;
    private transient Player owner;
    private int tick;
    private boolean spawned;
    private boolean globalPicked;
    private String cantPickupBy;

    public FloorItem(int id) {
        super(id);
    }

    public FloorItem(Item item, WorldTile tile, Player owner, boolean underGrave, boolean invisible, String ironmanName) {
        super(item.getId(), item.getAmount());
        this.tile = tile;
        if (owner != null)
            this.ownerName = owner.getUsername();
        this.owner = owner;
        this.type = invisible ? 1 : 0;
        this.setCantPickupBy(ironmanName);
    }

    //global spawn & ironman friendly

    public FloorItem(Item item, WorldTile tile, Player owner, boolean underGrave, boolean invisible) {
        super(item.getId(), item.getAmount());
        this.tile = tile;
        if (owner != null)
            this.ownerName = owner.getUsername();
        this.owner = owner;
        this.type = invisible ? 1 : 0;
    }

    public FloorItem(Item item, WorldTile tile, Player owner, boolean invisible, int tick, boolean spawned) {
        super(item.getId(), item.getAmount());
        this.tile = tile;
        this.owner = owner;
        this.type = invisible ? 1 : 0;
        this.tick = tick;
        this.spawned = spawned;
    }


    public FloorItem(Item item, WorldTile tile, boolean appearforever, boolean ironPickup) {
        super(item.getId(), item.getAmount());
        this.tile = tile;
        this.type = appearforever ? 2 : 0;
        this.globalPicked = ironPickup;
    }

	public boolean isInvisible() {
		return type == 1;
	}

	public boolean isForever() {
		return type == 2;
	}

	public boolean hasOwner() {
		return owner != null;
	}

	public void setInvisible(boolean invisible) {
		type = invisible ? 1 : 0;
	}

	public static final FloorItem createGroundItem(final Item item, final WorldTile tile) {
		final FloorItem floorItem = new FloorItem(item, tile, null, false, false);
		final Region region = World.getRegion(tile.getRegionId());
		region.forceGetFloorItems().add(floorItem);
		int regionId = tile.getRegionId();
		World.players().filter(p -> tile.getPlane() == p.getPlane() && p.getMapRegionsIds().contains(regionId)).forEach(p -> p.getPackets().sendGroundItem(floorItem));
		return floorItem;
	}


    //Don't use for regular - isn't ironman safe
    public static void addGroundItem(final Item item, final WorldTile tile) {
        addGroundItem(item, tile, null, false, -1, 2, -1);
    }

    public static void addGroundItem(final Item item, final WorldTile tile, int publicTime) {
        addGroundItem(item, tile, null, false, -1, 2, publicTime);
    }

    public static void addGroundItem(final Item item, final WorldTile tile, final Player owner, boolean invisible,
                                     long hiddenTime) {
        addGroundItem(item, tile, owner, invisible, hiddenTime, 2, 60);
    }

    public static FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner,
                                          boolean invisible, long hiddenTime, int type) {
        return addGroundItem(item, tile, owner, invisible, hiddenTime, type, 60);
    }

    public static FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner,
                                          boolean invisible, long hiddenTime, int type, String ironmanName) {
        return addGroundItem(item, tile, owner, invisible, hiddenTime, type, 60, ironmanName);
    }

    public static FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner,
                                          boolean invisible, long hiddenTime, int type, final int publicTime, String ironmanName) {
        final FloorItem floorItem = new FloorItem(item, tile, owner, false, invisible, ironmanName);
        final Region region = World.getRegion(tile.getRegionId());
        if (type == 1) {
            if (ItemConstants.isTradeable(item)) {
                region.getGroundItemsSafe().add(floorItem);
            }
            if (invisible) {
                if (owner != null) {
                    if (ItemConstants.isTradeable(item)) {
                        owner.getPackets().sendGroundItem(floorItem);

                    }
                }
                if (hiddenTime != -1) {
                    CoresManager.schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                turnPublic(floorItem, publicTime);
                            } catch (Throwable e) {
                                
                            }
                        }
                    }, Ticks.fromSeconds((int) hiddenTime));
                }
            } else {
                int regionId = tile.getRegionId();
                for (Player player : World.players) {
                    if (player == null || !player.isStarted() || player.isFinished()
                            || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId))
                        continue;
                    player.getPackets().sendGroundItem(floorItem);
                }
                if (publicTime != -1)
                    removeGroundItem(floorItem, publicTime);
            }
        } else {
            region.getGroundItemsSafe().add(floorItem);
            if (invisible) {
                if (owner != null) {
                    owner.getPackets().sendGroundItem(floorItem);
                }
                if (hiddenTime != -1) {
                    CoresManager.schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                turnPublic(floorItem, publicTime);
                            } catch (Throwable e) {
                                
                            }
                        }
                    }, Ticks.fromSeconds((int) hiddenTime));
                }
            } else {
                int regionId = tile.getRegionId();
                for (Player player : World.players) {
                    if (player == null || !player.isStarted() || player.isFinished()
                            || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)
                            || !ItemConstants.isTradeable(item))
                        continue;
                    player.getPackets().sendGroundItem(floorItem);
                }
                if (publicTime != -1)
                    removeGroundItem(floorItem, publicTime);
            }
        }
        return floorItem;
    }

    /*
     * type 0 - gold if not tradeable type 1 - gold if destroyable type 2 - no gold
     */
    public static FloorItem addGroundItem(final Item item, final WorldTile tile, final Player owner,
                                          boolean invisible, long hiddenTime, int type, final int publicTime) {
        final FloorItem floorItem = new FloorItem(item, tile, owner, false, invisible);
        final Region region = World.getRegion(tile.getRegionId());
        floorItem.setGlobalPicked(false);
        if (type == 1) {
            if (ItemConstants.isTradeable(item)) {
                region.getGroundItemsSafe().add(floorItem);
            }
            if (invisible) {
                if (owner != null) {
                    if (ItemConstants.isTradeable(item)) {
                        owner.getPackets().sendGroundItem(floorItem);

                    }
                }
                if (hiddenTime != -1) {
                    CoresManager.schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                turnPublic(floorItem, publicTime);
                            } catch (Throwable e) {
                                
                            }
                        }
                    }, Ticks.fromSeconds((int) hiddenTime));
                }
            } else {
                int regionId = tile.getRegionId();
                for (Player player : World.players) {
                    if (player == null || !player.isStarted() || player.isFinished()
                            || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId))
                        continue;
                    player.getPackets().sendGroundItem(floorItem);
                }
                if (publicTime != -1)
                    removeGroundItem(floorItem, publicTime);
            }
        } else {
            region.getGroundItemsSafe().add(floorItem);
            if (invisible) {
                if (owner != null) {
                    owner.getPackets().sendGroundItem(floorItem);
                }
                if (hiddenTime != -1) {
                    CoresManager.schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                turnPublic(floorItem, publicTime);
                            } catch (Throwable e) {
                                
                            }
                        }
                    }, Ticks.fromSeconds((int) hiddenTime));
                }
            } else {
                int regionId = tile.getRegionId();
                for (Player player : World.players) {
                    if (player == null || !player.isStarted() || player.isFinished()
                            || player.getPlane() != tile.getPlane() || !player.getMapRegionsIds().contains(regionId)
                            || !ItemConstants.isTradeable(item))
                        continue;
                    player.getPackets().sendGroundItem(floorItem);
                }
                if (publicTime != -1)
                    removeGroundItem(floorItem, publicTime);
            }
        }
        return floorItem;
    }

    public static void updateGroundItem(Item item, final WorldTile tile, final Player owner, final int time,
                                        final int type) {
        final FloorItem floorItem = World.getRegion(tile.getRegionId()).getGroundItem(item.getId(), tile, owner);
        if (floorItem == null) {
            if (item.getAmount() != 1 && !item.getDefinitions().isStackable() && !item.getDefinitions().isNoted()) {
                for (int i = 0; i < item.getAmount(); i++) {
                    addGroundItem(new Item(item.getId(), 1), tile, owner, true, time, type);
                }
                return;
            } else {
                addGroundItem(item, tile, owner, true, time, type);
                return;
            }
        }
        if (floorItem.getDefinitions().isStackable() || floorItem.getDefinitions().isNoted()) {
            if (floorItem.getAmount() + item.getAmount() < 0
                    || floorItem.getAmount() + item.getAmount() > Integer.MAX_VALUE) {
                int totalAmount = Integer.MAX_VALUE - floorItem.getAmount();
                floorItem.setAmount(Integer.MAX_VALUE);
                item.setAmount(item.getAmount() - totalAmount);
                addGroundItem(item, tile, owner, true, time, type);
                owner.getPackets().sendRemoveGroundItem(floorItem);
                owner.getPackets().sendGroundItem(floorItem);
            } else
                floorItem.setAmount(floorItem.getAmount() + item.getAmount());
            owner.getPackets().sendRemoveGroundItem(floorItem);
            owner.getPackets().sendGroundItem(floorItem);
        } else {
            if (item.getAmount() != 1) {
                for (int i = 0; i < item.getAmount(); i++) {
                    addGroundItem(new Item(item.getId(), 1), tile, owner, true, time, type);
                }
                return;
            } else {
                addGroundItem(item, tile, owner, true, time, type);
                return;
            }
        }
        floorItem.setGlobalPicked(false);
    }

    public static void updateGroundItem(Item item, final WorldTile tile, final Player owner, final int time,
                                        final int type, String ironmanName) {
        final FloorItem floorItem = World.getRegion(tile.getRegionId()).getGroundItem(item.getId(), tile, owner);
        if (floorItem == null) {
            if (item.getAmount() != 1 && !item.getDefinitions().isStackable() && !item.getDefinitions().isNoted()) {
                for (int i = 0; i < item.getAmount(); i++) {
                    addGroundItem(new Item(item.getId(), 1), tile, owner, true, time, type, ironmanName);
                }
                return;
            } else {
                addGroundItem(item, tile, owner, true, time, type, ironmanName);
                return;
            }
        }
        if (floorItem.getDefinitions().isStackable() || floorItem.getDefinitions().isNoted()) {
            if (floorItem.getAmount() + item.getAmount() < 0
                    || floorItem.getAmount() + item.getAmount() > Integer.MAX_VALUE) {
                int totalAmount = Integer.MAX_VALUE - floorItem.getAmount();
                floorItem.setAmount(Integer.MAX_VALUE);
                item.setAmount(item.getAmount() - totalAmount);
                addGroundItem(item, tile, owner, true, time, type, ironmanName);
                owner.getPackets().sendRemoveGroundItem(floorItem);
                owner.getPackets().sendGroundItem(floorItem);
            } else
                floorItem.setAmount(floorItem.getAmount() + item.getAmount());
            owner.getPackets().sendRemoveGroundItem(floorItem);
            owner.getPackets().sendGroundItem(floorItem);
        } else {
            if (item.getAmount() != 1) {
                for (int i = 0; i < item.getAmount(); i++) {
                    addGroundItem(new Item(item.getId(), 1), tile, owner, true, time, type, ironmanName);
                }
                return;
            } else {
                addGroundItem(item, tile, owner, true, time, type, ironmanName);
                return;
            }
        }
    }

    public static void turnPublic(FloorItem floorItem, int publicTime) {
        if (!floorItem.isInvisible())
            return;
        int regionId = floorItem.getTile().getRegionId();
        final Region region = World.getRegion(regionId);
        if (!region.getGroundItemsSafe().contains(floorItem))
            return;
        Player realOwner = floorItem.hasOwner() ? World.getPlayer(floorItem.getOwnerName()).get() : null;
//		if (realOwner.isIronmanBased()) {
//			removeGroundItem(floorItem, 0);
//			return;
//		}
        floorItem.setInvisible(false);

        for (Player player : World.players) {
            if (player == null || player == realOwner || !player.isStarted() || player.isFinished()
                    || player.getPlane() != floorItem.getTile().getPlane()
                    || !player.getMapRegionsIds().contains(regionId) || !ItemConstants.isTradeable(floorItem))
                continue;
            player.getPackets().sendGroundItem(floorItem);
        }
        if (publicTime != -1)
            removeGroundItem(floorItem, publicTime);
    }

    public static void updateGroundItem(Item item, final WorldTile tile, final Player owner) {
        final FloorItem floorItem = World.getRegion(tile.getRegionId()).getGroundItem(item.getId(), tile, owner);
        if (floorItem == null) {
            addGroundItem(item, tile, owner, true, 60);
            return;
        }
        /*
         * owner.getPackets().sendGameMessage( "FloorItem " +
         * floorItem.getDefinitions().getName() + " set from " + floorItem.getAmount() +
         * " to " + (floorItem.getAmount() + item.getAmount()) + "");
         */
        floorItem.setAmount(floorItem.getAmount() + item.getAmount());
        /*
         * addGroundItem(floorItem, tile, owner, true, 60);
         */

    }

    private static void removeGroundItem(final FloorItem floorItem, long publicTime) {
        CoresManager.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    int regionId = floorItem.getTile().getRegionId();
                    Region region = World.getRegion(regionId);
                    if (!region.getGroundItemsSafe().contains(floorItem))
                        return;
                    region.getGroundItemsSafe().remove(floorItem);
                    for (Player player : World.players) {
                        if (player == null || !player.isStarted() || player.isFinished()
                                || player.getPlane() != floorItem.getTile().getPlane()
                                || !player.getMapRegionsIds().contains(regionId))
                            continue;
                        player.getPackets().sendRemoveGroundItem(floorItem);
                    }
                } catch (Throwable e) {
                    
                }
            }
        }, Ticks.fromSeconds((int) publicTime));
    }

    public static boolean removeGroundItem(Player player, FloorItem floorItem) {
        return removeGroundItem(player, floorItem, true);
    }

    public static void addGroundItemForever(Item item, final WorldTile tile) {
        int regionId = tile.getRegionId();
        final FloorItem floorItem = new FloorItem(item, tile, true, true);
        final Region region = World.getRegion(regionId);
        region.getGroundItemsSafe().add(floorItem);
        floorItem.type = 2;
        World.players().forEach(p -> {
            p.getPackets().sendGroundItem(floorItem);
            region.refreshSpawnedItems(p);
        });
    }

    public static boolean removeGroundItem(Player player, final FloorItem floorItem, boolean add) {
        int regionId = floorItem.getTile().getRegionId();
        Region region = World.getRegion(regionId);
        if (player == null) {
            region.getGroundItemsSafe().remove(floorItem);
            for (Player p2 : World.players) {
                if (p2 == null || !p2.isStarted() || p2.isFinished() || p2.getPlane() != floorItem.getTile().getPlane()
                        || !p2.getMapRegionsIds().contains(regionId))
                    continue;
                p2.getPackets().sendRemoveGroundItem(floorItem);
            }
            if (floorItem.isForever()) {
                CoresManager.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("first item spawn - keep this for testing");
                            addGroundItemForever(floorItem, floorItem.getTile());
                        } catch (Throwable e) {
                            
                        }
                    }
                }, Ticks.fromSeconds(10));
            }
            return false;
        }
        if (!region.getGroundItemsSafe().contains(floorItem))
            return false;
        if (floorItem.getId() == 2677
                && (player.getInventory().containsOneItem(2677) || player.getBank().getItem(2677) != null)) {
            player.getPackets().sendGameMessage("You can only have one easy clue scroll at a time.");
            return false;
        }
        if (floorItem.getId() == 2801
                && (player.getInventory().containsOneItem(2801) || player.getBank().getItem(2801) != null)) {
            player.getPackets().sendGameMessage("You can only have one medium clue scroll at a time.");
            return false;
        }
        if (floorItem.getId() == 2722
                && (player.getInventory().containsOneItem(2722) || player.getBank().getItem(2722) != null)) {
            player.getPackets().sendGameMessage("You can only have one hard clue scroll at a time.");
            return false;
        }
        if (floorItem.getId() == 19043
                && (player.getInventory().containsOneItem(19043) || player.getBank().getItem(19043) != null)) {
            player.getPackets().sendGameMessage("You can only have one elite clue scroll at a time.");
            return false;
        }
        int amount = floorItem.getAmount();
        int inventoryLeftOver = 0;
        if (player.getInventory().getNumberOf(floorItem.getId()) == Integer.MAX_VALUE) {
            player.getPackets().sendGameMessage("Not enough space in your inventory.");
            return false;
        }
        if (!player.getInventory().hasFreeSlots()
                && ((floorItem.getDefinitions().isStackable() || floorItem.getDefinitions().isNoted())
                && !player.getInventory().containsItem(floorItem.getId(), 1))) {
            player.getPackets().sendGameMessage("Not enough space in your inventory.");
            return false;
        } else if (!player.getInventory().hasFreeSlots()
                && (!floorItem.getDefinitions().isStackable() && !floorItem.getDefinitions().isNoted())) {
            player.getPackets().sendGameMessage("Not enough space in your inventory.");
            return false;
        }
        if (player.getInventory().getNumberOf(floorItem.getId()) + amount > Integer.MAX_VALUE
                || player.getInventory().getNumberOf(floorItem.getId()) + amount < 0) {
            inventoryLeftOver = Integer.MAX_VALUE - player.getInventory().getNumberOf(floorItem.getId());
            amount = amount - inventoryLeftOver;
            if (player.getInventory().getNumberOf(floorItem.getId()) != Integer.MAX_VALUE) {
                player.getInventory().deleteItem(floorItem.getId(), Integer.MAX_VALUE);
                player.getInventory().addItem(floorItem.getId(), Integer.MAX_VALUE);
            }
            floorItem.setAmount(amount);
            player.getPackets().sendRemoveGroundItem(floorItem);
            player.getPackets().sendGroundItem(floorItem);
            return false;
        }
        region.getGroundItemsSafe().remove(floorItem);
        if (add)
            player.getInventory()
                    .addItem(new Item(floorItem.getId() == 7957 ? 1005 : floorItem.getId(), floorItem.getAmount()));
        if (floorItem.isInvisible()) {
            player.getPackets().sendRemoveGroundItem(floorItem);
            return true;
        } else {
            World.players().filter(p2 -> p2.getPlane() == floorItem.getTile().getPlane()
                    || p2.getMapRegionsIds().contains(regionId)).forEach(p2 -> p2.getPackets().sendRemoveGroundItem(floorItem));
            if (floorItem.isForever()) {
                CoresManager.schedule(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addGroundItemForever(floorItem, floorItem.getTile());
                        } catch (Throwable e) {
                            
                        }
                    }
                }, Ticks.fromSeconds(60));
            }
        }
        return true;
    }
}