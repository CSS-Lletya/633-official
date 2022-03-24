package com.rs.game.route;

/**
 * Class, controlling the exit point of a route.
 */
public abstract class RouteStrategy {

    public static final int BLOCK_FLAG_NORTH = 0x1;
    public static final int BLOCK_FLAG_EAST = 0x2;
    public static final int BLOCK_FLAG_SOUTH = 0x4;
    public static final int BLOCK_FLAG_WEST = 0x8;
    
    /**
     * Whether we can exit at specific x and y.
     */
    public abstract boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY);

    /**
     * Get's approximated destination position X.
     */
    public abstract int getApproxDestinationX();
    
    /**
     * Get's approximated destination position Y.
     */
    public abstract int getApproxDestinationY();
    
    /**
     * Get's approximated destination size X.
     */
    public abstract int getApproxDestinationSizeX();
    
    /**
     * Get's approximated destination size Y.
     */
    public abstract int getApproxDestinationSizeY();
    
    /**
     * Whether this strategy equals to other object.
     */
    public abstract boolean equals(Object other);
    
    
    /**
     * Check's if we can interact wall decoration from current position. 
     */
    protected static boolean checkWallDecorationInteract(int[][] clip, int currentX, int currentY, int sizeXY, int targetX, int targetY, int targetType, int targetRotation) {
	// TODO, include additional checks for size's bigger than 1.
	if (currentX == targetX && currentY == targetY)
	    return true;
	if (targetType == 6 || targetType == 7) {
	    if (targetType == 7)
		targetRotation = targetRotation + 2 & 0x3;
	    if (targetRotation == 0) {
		if (currentX == (targetX + 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALLOBJ_WEST) == 0)
		    return true;
		if (currentX == targetX && currentY == (targetY - 1) && (clip[currentX][currentY] & Flags.WALLOBJ_NORTH) == 0)
		    return true;
	    }
	    else if (targetRotation == 1) {
		if (currentX == (targetX - 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALLOBJ_EAST) == 0)
		    return true;
		if (currentX == targetX && currentY == (targetY - 1) && (clip[currentX][currentY] & Flags.WALLOBJ_NORTH) == 0)
		    return true;
	    } else if (targetRotation == 2) {
		if (currentX == (targetX - 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALLOBJ_EAST) == 0)
		    return true;
		if (currentX == targetX && currentY == (targetY + 1) && (clip[currentX][currentY] & Flags.WALLOBJ_SOUTH) == 0)
		    return true;
	    } else if (targetRotation == 3) {
		if (currentX == (targetX + 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALLOBJ_WEST) == 0)
		    return true;
		if (currentX == targetX && currentY == (targetY + 1) && (clip[currentX][currentY] & Flags.WALLOBJ_SOUTH) == 0)
		    return true;
	    }
	}
	else if (targetType == 8) {
	    if (currentX == targetX && currentY == (targetY + 1) && (clip[currentX][currentY] & Flags.WALLOBJ_SOUTH) == 0)
		return true;
	    if (currentX == targetX && currentY == (targetY - 1) && (clip[currentX][currentY] & Flags.WALLOBJ_NORTH) == 0)
		return true;
	    if (currentX == (targetX - 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALLOBJ_EAST) == 0)
		return true;
	    if (currentX == (targetX + 1) && currentY == targetY && (clip[currentX][currentY] & Flags.WALLOBJ_WEST) == 0)
		return true;
	}
	return false;
    }
    
    
    /**
     * Check's if we can interact wall object from current position.
     */
    protected static boolean checkWallInteract(int[][] clips, int currentX, int currentY, int sizeXY, int targetX, int targetY, int targetType, int targetRotation) {
	// TODO refactor
	if (sizeXY == 1) {
		if (currentX == targetX && currentY == targetY)
			return true; // we are inside the object
	} else if (targetX >= currentX && targetX <= currentX + sizeXY - 1 && targetY <= targetY + sizeXY - 1)
		return true; // we are inside the object bounds , though no y check?
	if (sizeXY == 1) {
		if (targetType == 0) {
			if (targetRotation == 0) {
				if (targetX - 1 == currentX && currentY == targetY)
					return true;
				if (currentX == targetX && targetY + 1 == currentY && (clips[currentX][currentY] & 0x2c0120) == 0)
					return true;
				if (targetX == currentX && currentY == targetY - 1 && (clips[currentX][currentY] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 1) {
				if (currentX == targetX && targetY + 1 == currentY)
					return true;
				if (currentX == targetX - 1 && targetY == currentY && (clips[currentX][currentY] & 0x2c0108) == 0)
					return true;
				if (targetX + 1 == currentX && currentY == targetY && (clips[currentX][currentY] & 0x2c0180) == 0)
					return true;
			} else if (targetRotation == 2) {
				if (targetX + 1 == currentX && currentY == targetY)
					return true;
				if (targetX == currentX && currentY == targetY + 1 && (clips[currentX][currentY] & 0x2c0120) == 0)
					return true;
				if (targetX == currentX && currentY == targetY - 1 && (clips[currentX][currentY] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 3) {
				if (currentX == targetX && targetY - 1 == currentY)
					return true;
				if (targetX - 1 == currentX && currentY == targetY && (clips[currentX][currentY] & 0x2c0108) == 0)
					return true;
				if (targetX + 1 == currentX && targetY == currentY && (clips[currentX][currentY] & 0x2c0180) == 0)
					return true;
			}
		}
		if (targetType == 2) {
			if (targetRotation == 0) {
				if (currentX == targetX - 1 && currentY == targetY)
					return true;
				if (targetX == currentX && targetY + 1 == currentY)
					return true;
				if (currentX == targetX + 1 && targetY == currentY && (clips[currentX][currentY] & 0x2c0180) == 0)
					return true;
				if (targetX == currentX && targetY - 1 == currentY && (clips[currentX][currentY] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 1) {
				if (targetX - 1 == currentX && targetY == currentY && (clips[currentX][currentY] & 0x2c0108) == 0)
					return true;
				if (targetX == currentX && targetY + 1 == currentY)
					return true;
				if (targetX + 1 == currentX && currentY == targetY)
					return true;
				if (targetX == currentX && currentY == targetY - 1 && (clips[currentX][currentY] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 2) {
				if (targetX - 1 == currentX && currentY == targetY && (clips[currentX][currentY] & 0x2c0108) == 0)
					return true;
				if (currentX == targetX && currentY == targetY + 1 && (clips[currentX][currentY] & 0x2c0120) == 0)
					return true;
				if (currentX == targetX + 1 && targetY == currentY)
					return true;
				if (currentX == targetX && targetY - 1 == currentY)
					return true;
			} else if (targetRotation == 3) {
				if (targetX - 1 == currentX && currentY == targetY)
					return true;
				if (targetX == currentX && targetY + 1 == currentY && (clips[currentX][currentY] & 0x2c0120) == 0)
					return true;
				if (currentX == targetX + 1 && targetY == currentY && (clips[currentX][currentY] & 0x2c0180) == 0)
					return true;
				if (currentX == targetX && targetY - 1 == currentY)
					return true;
			}
		}
		if (targetType == 9) {
			if (targetX == currentX && targetY + 1 == currentY && (clips[currentX][currentY] & 0x20) == 0)
				return true;
			if (currentX == targetX && targetY - 1 == currentY && (clips[currentX][currentY] & 0x2) == 0)
				return true;
			if (currentX == targetX - 1 && currentY == targetY && (clips[currentX][currentY] & 0x8) == 0)
				return true;
			if (currentX == targetX + 1 && currentY == targetY && (clips[currentX][currentY] & 0x80) == 0)
				return true;
		}
	} else {
		int i_66_ = currentX + sizeXY - 1;
		int i_67_ = sizeXY + currentY - 1;
		if (targetType == 0) {
			if (targetRotation == 0) {
				if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1 && (clips[targetX][currentY] & 0x2c0120) == 0)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY && (clips[targetX][i_67_] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 1) {
				if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY)
					return true;
				if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 0x2c0108) == 0)
					return true;
				if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 0x2c0180) == 0)
					return true;
			} else if (targetRotation == 2) {
				if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1 && (clips[targetX][currentY] & 0x2c0120) == 0)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY && (clips[targetX][i_67_] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 3) {
				if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY)
					return true;
				if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 0x2c0108) == 0)
					return true;
				if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 0x2c0180) == 0)
					return true;
			}
		}
		if (targetType == 2) {
			if (targetRotation == 0) {
				if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY)
					return true;
				if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 0x2c0180) == 0)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY && (clips[targetX][i_67_] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 1) {
				if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 0x2c0108) == 0)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1)
					return true;
				if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY && (clips[targetX][i_67_] & 0x2c0102) == 0)
					return true;
			} else if (targetRotation == 2) {
				if (currentX == targetX - sizeXY && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 0x2c0108) == 0)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY && (clips[targetX][currentY] & 0x2c0120) == 0)
					return true;
				if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY)
					return true;
			} else if (targetRotation == 3) {
				if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY + 1 && (clips[targetX][currentY] & 0x2c0120) == 0)
					return true;
				if (targetX + 1 == currentX && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 0x2c0180) == 0)
					return true;
				if (targetX >= currentX && targetX <= i_66_ && currentY == targetY - sizeXY)
					return true;
			}
		}
		if (targetType == 9) {
			if (targetX >= currentX && targetX <= i_66_ && targetY + 1 == currentY && (clips[targetX][currentY] & 0x2c0120) == 0)
				return true;
			if (targetX >= currentX && targetX <= i_66_ && targetY - sizeXY == currentY && (clips[targetX][i_67_] & 0x2c0102) == 0)
				return true;
			if (targetX - sizeXY == currentX && targetY >= currentY && targetY <= i_67_ && (clips[i_66_][targetY] & 0x2c0108) == 0)
				return true;
			if (currentX == targetX + 1 && targetY >= currentY && targetY <= i_67_ && (clips[currentX][targetY] & 0x2c0180) == 0)
				return true;
		}
	}
	return false;
    }
    
    /**
     * Check's if we can interact filled rectangular (Might be ground object or npc or player etc) from current position.
     */
    protected static boolean checkFilledRectangularInteract(int[][] clip, int currentX, int currentY, int sizeX, int sizeY, int targetX, int targetY, int targetSizeX, int targetSizeY, int accessBlockFlag) {
	// TODO refactor
	int srcEndX = currentX + sizeX;
	int srcEndY = currentY + sizeY;
	int destEndX = targetX + targetSizeX;
	int destEndY = targetY + targetSizeY;
	if (destEndX == currentX && (accessBlockFlag & 0x2) == 0) { // can we enter from east ?
		int i_12_ = currentY > targetY ? currentY : targetY;
		for (int i_13_ = srcEndY < destEndY ? srcEndY : destEndY; i_12_ < i_13_; i_12_++) {
			if (((clip[destEndX - 1][i_12_]) & 0x8) == 0)
				return true;
		}
	} else if (srcEndX == targetX && (accessBlockFlag & 0x8) == 0) { // can we enter from west ?
		int i_14_ = currentY > targetY ? currentY : targetY;
		for (int i_15_ = srcEndY < destEndY ? srcEndY : destEndY; i_14_ < i_15_; i_14_++) {
			if (((clip[targetX ][i_14_]) & 0x80) == 0)
				return true;
		}
	} else if (currentY == destEndY && (accessBlockFlag & 0x1) == 0) { // can we enter from north?
		int i_16_ = currentX > targetX ? currentX : targetX;
		for (int i_17_ = srcEndX < destEndX ? srcEndX : destEndX; i_16_ < i_17_; i_16_++) {
			if (((clip[i_16_][destEndY - 1]) & 0x2) == 0)
				return true;
		}
	} else if (targetY == srcEndY && (accessBlockFlag & 0x4) == 0) { // can we enter from south?
		int i_18_ = currentX > targetX ? currentX : targetX;
		for (int i_19_ = srcEndX < destEndX ? srcEndX : destEndX; i_18_ < i_19_; i_18_++) {
			if (((clip[i_18_][targetY]) & 0x20) == 0)
				return true;
		}
	}
	return false;
    }
    
}
