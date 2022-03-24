package com.rs.game.route;

public final class Flags {
    
    public static final int FLOOR_BLOCKSWALK = 0x200000;
    public static final int FLOORDECO_BLOCKSWALK = 0x40000;

    public static final int OBJ = 0x100;
    public static final int OBJ_BLOCKSFLY = 0x20000;
    public static final int OBJ_BLOCKSWALK_ALTERNATIVE = 0x40000000;
    
    public static final int WALLOBJ_NORTH = 0x2;
    public static final int WALLOBJ_EAST = 0x8;
    public static final int WALLOBJ_SOUTH = 0x20;
    public static final int WALLOBJ_WEST = 0x80;
    
    public static final int CORNEROBJ_NORTHWEST = 0x1;
    public static final int CORNEROBJ_NORTHEAST = 0x4;
    public static final int CORNEROBJ_SOUTHEAST = 0x10;
    public static final int CORNEROBJ_SOUTHWEST = 0x40;
    
    public static final int WALLOBJ_NORTH_BLOCKSFLY = 0x400;
    public static final int WALLOBJ_EAST_BLOCKSFLY = 0x1000;
    public static final int WALLOBJ_SOUTH_BLOCKSFLY = 0x4000;
    public static final int WALLOBJ_WEST_BLOCKSFLY = 0x10000;

    public static final int CORNEROBJ_NORTHWEST_BLOCKSFLY = 0x200;
    public static final int CORNEROBJ_NORTHEAST_BLOCKSFLY = 0x800;
    public static final int CORNEROBJ_SOUTHEAST_BLOCKSFLY = 0x2000;
    public static final int CORNEROBJ_SOUTHWEST_BLOCKSFLY = 0x8000;
    
    public static final int WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE = 0x800000;
    public static final int WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE = 0x2000000;
    public static final int WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE = 0x8000000;
    public static final int WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE = 0x20000000;

    public static final int CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE = 0x400000;
    public static final int CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE = 0x1000000;
    public static final int CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE = 0x4000000;
    public static final int CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE = 0x10000000;

}
