package com.rs.game.route;

/**
 * Route finder, designed for single-threaded usage.
 * @author Mangis
 */
public class RouteFinder {

    /**
     * Standart walk route finder type.
     */
    public static final int WALK_ROUTEFINDER = 0;
    
    /**
     * Last routefinder that was used.
     */
    private static int lastUsed;
    
    /**
     * Find's route using given strategy.
     * Returns amount of steps found.
     * If steps > 0, route exists.
     * If steps = 0, route exists, but no need to move.
     * If steps < 0, route does not exist.
     */
    public static int findRoute(int type, int srcX, int srcY, int srcZ, int srcSizeXY, RouteStrategy strategy, boolean findAlternative) {
	switch (lastUsed = type) {
	    case WALK_ROUTEFINDER:
		return WalkRouteFinder.findRoute(srcX, srcY, srcZ, srcSizeXY, strategy, findAlternative);
	    default:
		throw new RuntimeException("Unknown routefinder type.");
	}
    }
    

    /**
     * Get's last path buffer x.
     * Modifying the buffer in any way is prohibited.
     */
    public static int[] getLastPathBufferX() {
	switch (lastUsed) {
	    case WALK_ROUTEFINDER:
		return WalkRouteFinder.getLastPathBufferX();
	    default:
		throw new RuntimeException("Unknown routefinder type.");
	}
    }
    
    /**
     * Get's last path buffer y.
     * Modifying the buffer in any way is prohibited.
     */
    public static int[] getLastPathBufferY() {
	switch (lastUsed) {
	    case WALK_ROUTEFINDER:
		return WalkRouteFinder.getLastPathBufferY();
	    default:
		throw new RuntimeException("Unknown routefinder type.");
	}
    }
    
    /**
     * Whether last path is only alternative path.
     */
    public static boolean lastIsAlternative() {
	switch (lastUsed) {
	    case WALK_ROUTEFINDER:
		return WalkRouteFinder.lastIsAlternative();
	    default:
		throw new RuntimeException("Unknown routefinder type.");
	}
    }

}
