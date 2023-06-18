package com.rs.utilities;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import com.rs.GameConstants;
import com.rs.cache.Cache;
import com.rs.cores.WorldThread;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.utilities.LogUtility.LogType;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.SneakyThrows;
import lombok.Synchronized;
import skills.Skills;

public final class Utility {

	private static final Object ALGORITHM_LOCK = new Object();

	private static final long INIT_MILLIS = System.currentTimeMillis();
	private static final long INIT_NANOS = System.nanoTime();

	private static long millisSinceClassInit() {
		return (System.nanoTime() - INIT_NANOS) / 1000000;
	}

	public static long currentTimeMillis() {
		return INIT_MILLIS + millisSinceClassInit();
	}
	
	public static String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,###").format(amount);
	}

	public static String getFormattedNumber(double amount, char seperator) {
		String str = new DecimalFormat("#,###,###").format(amount);
		char[] rebuff = new char[str.length()];
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9')
				rebuff[i] = c;
			else
				rebuff[i] = seperator;
		}
		return new String(rebuff);
	}

	public static byte[] cryptRSA(byte[] data, BigInteger exponent, BigInteger modulus) {
		return new BigInteger(data).modPow(exponent, modulus).toByteArray();
	}

	@Synchronized("ALGORITHM_LOCK")
	@SneakyThrows(Throwable.class)
	public static final byte[] encryptUsingMD5(byte[] buffer) {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.update(buffer);
		byte[] digest = algorithm.digest();
		algorithm.reset();
		return digest;
	}

	public static boolean inCircle(WorldTile location, WorldTile center, int radius) {
		return getDistance(center, location) < radius;
	}

	@SuppressWarnings({ "rawtypes" })
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		ObjectArrayList<File> dirs = new ObjectArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile().replaceAll("%20", " ")));
		}
		ObjectArrayList<Class> classes = new ObjectArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	@SneakyThrows(Throwable.class)
	private static ObjectArrayList<Class> findClasses(File directory, String packageName) {
		ObjectArrayList<Class> classes = new ObjectArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class
						.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static final int getDistance(WorldTile t1, WorldTile t2) {
		return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
	}

	public static final int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	public static final int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0)
				return 5;
			else if (yOffset > 0)
				return 0;
			else
				return 3;
		} else if (xOffset > 0) {
			if (yOffset < 0)
				return 7;
			else if (yOffset > 0)
				return 2;
			else
				return 4;
		} else {
			if (yOffset < 0)
				return 6;
			else if (yOffset > 0)
				return 1;
			else
				return -1;
		}
	}

	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };

	public static int getNpcMoveDirection(int dd) {
		return dd < 0 ? -1 : getNpcMoveDirection(DIRECTION_DELTA_X[dd], DIRECTION_DELTA_Y[dd]);
	}

	public static int getNpcMoveDirection(int dx, int dy) {
		if (dx == 0 && dy > 0)
			return 0;
		if (dx > 0 && dy > 0)
			return 1;
		if (dx > 0 && dy == 0)
			return 2;
		if (dx > 0 && dy < 0)
			return 3;
		if (dx == 0 && dy < 0)
			return 4;
		if (dx < 0 && dy < 0)
			return 5;
		if (dx < 0 && dy == 0)
			return 6;
		if (dx < 0 && dy > 0)
			return 7;
		return -1;
	}

	public static final int[][] getCoordOffsetsNear(int size) {
		int[] xs = new int[4 + (4 * size)];
		int[] xy = new int[xs.length];
		xs[0] = -size;
		xy[0] = 1;
		xs[1] = 1;
		xy[1] = 1;
		xs[2] = -size;
		xy[2] = -size;
		xs[3] = 1;
		xy[2] = -size;
		for (int fakeSize = size; fakeSize > 0; fakeSize--) {
			xs[(4 + ((size - fakeSize) * 4))] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4))] = 1;
			xs[(4 + ((size - fakeSize) * 4)) + 1] = -size;
			xy[(4 + ((size - fakeSize) * 4)) + 1] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 2] = 1;
			xy[(4 + ((size - fakeSize) * 4)) + 2] = -fakeSize + 1;
			xs[(4 + ((size - fakeSize) * 4)) + 3] = -fakeSize + 1;
			xy[(4 + ((size - fakeSize) * 4)) + 3] = -size;
		}
		return new int[][] { xs, xy };
	}

	public static final int getFaceDirection(int xOffset, int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
	}

	public static final int getGraphicDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[21].getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndexes()[21].getValidFilesCount(lastArchiveId);
	}

	public static final int getAnimationDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[20].getLastArchiveId();
		return lastArchiveId * 128 + Cache.STORE.getIndexes()[20].getValidFilesCount(lastArchiveId);
	}

	public static final int getConfigDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[22].getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndexes()[22].getValidFilesCount(lastArchiveId);
	}

	public static final int getObjectDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[16].getLastArchiveId();
		return lastArchiveId * 256 + Cache.STORE.getIndexes()[16].getValidFilesCount(lastArchiveId);
	}

	public static final int getNPCDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[18].getLastArchiveId();
		return lastArchiveId * 128 + Cache.STORE.getIndexes()[18].getValidFilesCount(lastArchiveId);
	}

	// 22314

	public static final int getItemDefinitionsSize() {
		int lastArchiveId = Cache.STORE.getIndexes()[19].getLastArchiveId();
		return (lastArchiveId * 256 + Cache.STORE.getIndexes()[19].getValidFilesCount(lastArchiveId));
	}

	public static boolean itemExists(int id) {
		if (id >= getItemDefinitionsSize()) // setted because of custom items
			return false;
		return Cache.STORE.getIndexes()[19].fileExists(id >>> 8, 0xff & id);
	}

	public static final int getInterfaceDefinitionsSize() {
		return Cache.STORE.getIndexes()[3].getLastArchiveId() + 1;
	}

	public static final int getInterfaceDefinitionsComponentsSize(int interfaceId) {
		return Cache.STORE.getIndexes()[3].getLastFileId(interfaceId) + 1;
	}

	public static final boolean isQCValid(int id) {
		return Cache.STORE.getIndexes()[24].fileExists(1, id);
	}

	public static String formatPlayerNameForProtocol(String name) {
		if (name == null)
			return "";
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}

	public static String formatPlayerNameForDisplay(String name) {
		if (name == null)
			return "";
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				newName.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}

	public static final String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9' };

	public static boolean invalidAccountName(String name) {
		return name.length() < 2 || name.length() > 12 || name.startsWith("_") || name.endsWith("_")
				|| name.contains("__") || containsInvalidCharacter(name);
	}

	public static boolean invalidAuthId(String auth) {
		return auth.length() != 10 || auth.contains("_") || containsInvalidCharacter(auth);
	}

	public static boolean containsInvalidCharacter(char c) {
		for (char vc : VALID_CHARS) {
			if (vc == c)
				return false;
		}
		return true;
	}

	public static boolean containsInvalidCharacter(String name) {
		for (char c : name.toCharArray()) {
			if (containsInvalidCharacter(c))
				return true;
		}
		return false;
	}

	public static final long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static final int packGJString2(int position, byte[] buffer, String String) {
		int length = String.length();
		int offset = position;
		for (int index = 0; length > index; index++) {
			int character = String.charAt(index);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}

	public static final int calculateGJString2Length(String String) {
		int length = String.length();
		int gjStringLength = 0;
		for (int index = 0; length > index; index++) {
			char c = String.charAt(index);
			if (c > '\u007f') {
				if (c <= '\u07ff')
					gjStringLength += 2;
				else
					gjStringLength += 3;
			} else
				gjStringLength++;
		}
		return gjStringLength;
	}

	public static final int getNameHash(String name) {
		name = name.toLowerCase();
		int hash = 0;
		for (int index = 0; index < name.length(); index++)
			hash = TextUtils.method1258(name.charAt(index)) + ((hash << 5) - hash);
		return hash;
	}

	public static int getHashMapSize(int size) {
		size--;
		size |= size >>> -1810941663;
		size |= size >>> 2010624802;
		size |= size >>> 10996420;
		size |= size >>> 491045480;
		size |= size >>> 1388313616;
		return 1 + size;
	}

	/**
	 * Walk dirs 0 - South-West 1 - South 2 - South-East 3 - West 4 - East 5 -
	 * North-West 6 - North 7 - North-East
	 */
	public static int getPlayerWalkingDirection(int dx, int dy) {
		if (dx == -1 && dy == -1) {
			return 0;
		}
		if (dx == 0 && dy == -1) {
			return 1;
		}
		if (dx == 1 && dy == -1) {
			return 2;
		}
		if (dx == -1 && dy == 0) {
			return 3;
		}
		if (dx == 1 && dy == 0) {
			return 4;
		}
		if (dx == -1 && dy == 1) {
			return 5;
		}
		if (dx == 0 && dy == 1) {
			return 6;
		}
		if (dx == 1 && dy == 1) {
			return 7;
		}
		return -1;
	}

	public static int getPlayerRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2)
			return 0;
		if (dx == -1 && dy == -2)
			return 1;
		if (dx == 0 && dy == -2)
			return 2;
		if (dx == 1 && dy == -2)
			return 3;
		if (dx == 2 && dy == -2)
			return 4;
		if (dx == -2 && dy == -1)
			return 5;
		if (dx == 2 && dy == -1)
			return 6;
		if (dx == -2 && dy == 0)
			return 7;
		if (dx == 2 && dy == 0)
			return 8;
		if (dx == -2 && dy == 1)
			return 9;
		if (dx == 2 && dy == 1)
			return 10;
		if (dx == -2 && dy == 2)
			return 11;
		if (dx == -1 && dy == 2)
			return 12;
		if (dx == 0 && dy == 2)
			return 13;
		if (dx == 1 && dy == 2)
			return 14;
		if (dx == 2 && dy == 2)
			return 15;
		return -1;
	}

	public static byte[] completeQuickMessage(Player player, int fileId, byte[] data) {
		if (fileId == 1)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.AGILITY) };
		else if (fileId == 8)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.ATTACK) };
		else if (fileId == 13)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.CONSTRUCTION) };
		else if (fileId == 16)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.COOKING) };
		else if (fileId == 23)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.CRAFTING) };
		else if (fileId == 30)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.DEFENCE) };
		else if (fileId == 34)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FARMING) };
		else if (fileId == 41)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FIREMAKING) };
		else if (fileId == 47)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FISHING) };
		else if (fileId == 55)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.FLETCHING) };
		else if (fileId == 62)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.HERBLORE) };
		else if (fileId == 70)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.HITPOINTS) };
		else if (fileId == 74)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.HUNTER) };
		else if (fileId == 135)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.MAGIC) };
		else if (fileId == 127)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.MINING) };
		else if (fileId == 120)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.PRAYER) };
		else if (fileId == 116)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.RANGE) };
		else if (fileId == 111)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.RUNECRAFTING) };
		else if (fileId == 103)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.SLAYER) };
		else if (fileId == 96)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.SMITHING) };
		else if (fileId == 92)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.STRENGTH) };
		else if (fileId == 85)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.SUMMONING) };
		else if (fileId == 79)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.THIEVING) };
		else if (fileId == 142)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.WOODCUTTING) };
		else if (fileId == 990)
			data = new byte[] { (byte) player.getSkills().getLevelForXp(Skills.DUNGEONEERING) };
//		else if (fileId == 611) {
//			int value = player.getStealingCreationPoints();
//			data = new byte[] { (byte) (value >> 24), (byte) (value >> 16),
//					(byte) (value >> 8), (byte) value };
		else if (fileId == 965) {
			int value = player.getHitpoints();
			data = new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
		}

		else if (GameConstants.DEBUG)
			LogUtility.log(LogType.TRACE, "qc: " + fileId + ", " + (data == null ? 0 : data.length));
		return data;
	}

	public static boolean colides(int x1, int y1, int size1, int x2, int y2, int size2) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		return distanceX < size2 && distanceX > -size1 && distanceY < size2 && distanceY > -size1;
	}

	public static boolean isOnRange(int x1, int y1, int size1, int x2, int y2, int size2, int maxDistance) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		if (distanceX > size2 + maxDistance || distanceX < -size1 - maxDistance || distanceY > size2 + maxDistance
				|| distanceY < -size1 - maxDistance)
			return false;
		return true;
	}

	/*
	 * dont use this one
	 */
	public static boolean isOnRange(int x1, int y1, int x2, int y2, int sizeX, int sizeY) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		if (distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1)
			return false;
		return true;
	}

	/**
	 * Gets all of the classes in a directory
	 * 
	 * @param directory The directory to iterate through
	 * @return The list of classes
	 */
	public static List<Object> getClassesInDirectory(String directory) {
		List<Object> classes = new ArrayList<>();
		for (File file : new File("./bin/main/" + directory.replace(".", "/")).listFiles()) {
			if (file.getName().contains("$")) {
				continue;
			}
			Try.run(() -> {
				Object objectEvent;
				objectEvent = (Class.forName(directory + "." + file.getName().replace(".class", ""))
						.getConstructor().newInstance());
				classes.add(objectEvent);
			});
		}
		return classes;
	}
	
	public static final int getAngleTo(WorldTile fromTile, WorldTile toTile) {
		return getAngleTo(toTile.getX() - fromTile.getX(), toTile.getY() - fromTile.getY());
	}

	public static final int getAngleTo(int xOffset, int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
	}

	public static int get32BitValue(boolean[] array, boolean trueCondition) {
		int value = 0;
		for (int index = 1; index < array.length + 1; index++) {
			if (array[index - 1] == trueCondition) {
				value += 1 << index;
			}
		}
		return value;
	}
	

    public static Rational toRational(double number) {
        return toRational(number, 5);
    }

    public static long gcm(long a, long b) {
        return b == 0 ? a : gcm(b, a % b); // Not bad for one line of code :)
    }

    public static Rational toRational(double number, int largestRightOfDecimal) {

        long sign = 1;
        if (number < 0) {
            number = -number;
            sign = -1;
        }

        final long SECOND_MULTIPLIER_MAX = (long) Math.pow(10, largestRightOfDecimal - 1);
        final long FIRST_MULTIPLIER_MAX = SECOND_MULTIPLIER_MAX * 10L;
        final double ERROR = Math.pow(10, -largestRightOfDecimal - 1);
        long firstMultiplier = 1;
        long secondMultiplier = 1;
        boolean notIntOrIrrational = false;
        long truncatedNumber = (long) number;
        Rational rationalNumber = new Rational((long) (sign * number * FIRST_MULTIPLIER_MAX), FIRST_MULTIPLIER_MAX);

        double error = number - truncatedNumber;
        while ((error >= ERROR) && (firstMultiplier <= FIRST_MULTIPLIER_MAX)) {
            secondMultiplier = 1;
            firstMultiplier *= 10;
            while ((secondMultiplier <= SECOND_MULTIPLIER_MAX) && (secondMultiplier < firstMultiplier)) {
                double difference = (number * firstMultiplier) - (number * secondMultiplier);
                truncatedNumber = (long) difference;
                error = difference - truncatedNumber;
                if (error < ERROR) {
                    notIntOrIrrational = true;
                    break;
                }
                secondMultiplier *= 10;
            }
        }

        if (notIntOrIrrational) {
            rationalNumber = new Rational(sign * truncatedNumber, firstMultiplier - secondMultiplier);
        }
        return rationalNumber;
    }

    public static double randomD() {
        return RandomUtils.nextDouble();
    }

    public static double clampD(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
    
    public static String asFraction(long a, long b) {
        long gcm = gcm(a, b);
        return (a / gcm) + "/" + (b / gcm);
    }
    
    private double nothingRate = 0.0;
    
    public double getNothingRate() {
        return round(nothingRate, 10);
    }
    
    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int clampI(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int[] range(int min, int max) {
        int[] range = new int[max - min + 1];
        for (int i = min, j = 0; i <= max; i++, j++)
            range[j] = i;
        return range;
    }

    public static int[] range(int min, int max, int step) {
        int[] range = new int[(max - min) / step + 1];
        for (int i = min, j = 0; i <= max; i += step, j++)
            range[j] = i;
        return range;
    }

    private static final byte[][] ANGLE_DIRECTION_DELTA = { { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } };
    
    public static byte[] getDirection(int angle) {
		int v = angle >> 11;
		return ANGLE_DIRECTION_DELTA[v];
	}

    public static WorldTile getFreeTile(WorldTile center, int distance) {
        WorldTile tile = center;
        for (int i = 0; i < 10; i++) {
            tile = new WorldTile(center, distance);
            if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), 1))
                return tile;
        }
        return center;
    }

    public static String fixChatMessage(String message) {
        StringBuilder newText = new StringBuilder();
        boolean wasSpace = true;
        boolean exception = false;
        for (int i = 0; i < message.length(); i++) {
            if (!exception) {
                if (wasSpace) {
                    newText.append(("" + message.charAt(i)).toUpperCase());
                    if (!String.valueOf(message.charAt(i)).equals(" "))
                        wasSpace = false;
                } else {
                    newText.append(("" + message.charAt(i)).toLowerCase());
                }
            } else {
                newText.append(("" + message.charAt(i)));
            }
            if (String.valueOf(message.charAt(i)).contains(":"))
                exception = true;
            else if (String.valueOf(message.charAt(i)).contains(".") || String.valueOf(message.charAt(i)).contains("!")
                    || String.valueOf(message.charAt(i)).contains("?"))
                wasSpace = true;
        }
        return newText.toString();
    }
    
    public static Map<String, Object> cloneMap(Map<String, Object> from) {
		if (from == null)
			return null;
		Map<String, Object> newMap = new HashMap<String, Object>();

		for (Entry<String, Object> entry : from.entrySet()) {
			newMap.put(entry.getKey(), entry.getValue());
		}

		return newMap;
	}

    public static long currentWorldCycle() {
        return WorldThread.LAST_CYCLE_CTM;
    }
    
	public static int IPAddressToNumber(String ipAddress) {
		StringTokenizer st = new StringTokenizer(ipAddress, ".");
		int[] ip = new int[4];
		int i = 0;
		while (st.hasMoreTokens())
			ip[i++] = Integer.parseInt(st.nextToken());
		return ((ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | (ip[3]));
	}
	
	public static String decodeBase37(long value) {
		char[] chars = new char[12];
		int pos = 0;
		while (value != 0) {
			int remainder = (int) (value % 37);
			value /= 37;

			char c;
			if (remainder >= 1 && remainder <= 26) {
				c = (char) ('a' + remainder - 1);
			} else if (remainder >= 27 && remainder <= 36) {
				c = (char) ('0' + remainder - 27);
			} else {
				c = '_';
			}

			chars[chars.length - pos++ - 1] = c;
		}
		return new String(chars, chars.length - pos, pos);
	}

	public static final int[] DEFAULT_LOBBY_CONFIGS = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -476379337, -1634348113, 919975670, 100098016, -941649356, 33599227, 0, 0, 0, 2, 0, 10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 52776083, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 1, 1, 0, 0, 0, 0, 0, 130, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 557858, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 1000, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 272, 0, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 58720640, 0, 24596, 0, 0, 0, 0, 2048, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 269026962, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 1073742208, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 262177, 0, 256, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1076961290, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1073741824, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1409286144, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 99, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2147483648, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34079488, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 272, 0, 0, 0, 0, 0, 0, 0, 1073741824, 0, 0, 0, 0, 8192, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 407044218, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 536870912, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 134217730, 0, 2049, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 201326625, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4224, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 33554432, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4194304, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1073741824, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 134232576, 32768, 0, 0, 0, 0, 0, 4194304, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 262144, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2147483648, 0, 0, 0, -1, -1, -1, -1, -1, -1, 0, 0, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 256, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 268435456, 0, 0, 0, 0, 0, 0, 0, 0, 537133056, 2048, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 262273, 536877056, 0, 393216, 32, 0, 4096, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, 1073742848, 65536, 268435456, 0, 0, 0, 512, 0, 0, 0, 0, 0, 291912, 5672, 260092, 0, 60248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 508, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2048, -2130681854, 0, -1, -1, -1, -1, 0, 10, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 64, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, -1, 0, 0, 60, 0, 0, 470286336, 132096, 1, 1276, 0, 0, 0, 0, 0, 0, 0, 0, 0, 536870912, -1, 0, 54332549, 0, 0, 0, -1, 0, 67174408, -872415232, 256, 8388608, 0, 0, 0, 4353, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 590848, 0, 0, 0, 0, 0, 0, -1, 0, 1001, 0, 0, 0, 0, 82663, 1025, 0, 134217728, 14, 0, 0, 0, 0, 0, 557057, 1476461568, 16384, 4096, 3721, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 525312, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 22271787, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12582912, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -1, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 65536, 0, -1879048192, 0, 16384, 1, 16384, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, 0, -2147483648, 0, 0, -2147483648, 0, 0, 0, 0, 0, 0, 512, 0, 0, 0, 0, 0, 16777216, 0, 0, 0, 25165824, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67108864, 0, 0, 0, 268435456, 0, 136347657, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2144919536, 0, 0, -1, 65540, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 511305630, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1073744067, 0, 65536, 0, 0, 0, 0, 0, 0, 536870912, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65535, 65535, 65535, 1073741823, 2147483647, 1073741823, 1073741823, 1073741823, 1073741823, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 134218080, 0, 0, 0, 0, 545260028, 0, 0, 0, 0, 0, 0, 0, 0, 262144, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2080374784, 0, 0, 0, 0, 0, 0, -2147483648, 279019520, -2147483648, 134217728, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 751720, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -1, 268437504, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 4194304, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100673544, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 677729578, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 956430392, 25171979, 151589, 3, 4718466, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 393216, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8388608, 33554432, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67108864, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 41476, 0, 0, 0, 0, 32, 536870912, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 40632322, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 819320, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 4194304, 0, 0, 0, 0, 0, 0, 0, 0, 0, 256, 0, 0, 0, 0, 0, 0, 0, 1048559, 589721, -1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 589855, 0, 0, -1, 0, 0, 402655232, 98353, 0, 0, 0, -1137689604, 1073807344, 0, 0, 0, 10, 0, 0, 0, 8386561, 0, 0, -1, -1, -1, -1, -1, -1, -1, 1536, 8192, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 77, 1733, -16912800, 4594664, 5359015, 3721, 0, 0, 0, -2147483648, 1310720, -1, 0, 0, 0, 0, 0, 0, 0, 1342177408, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 16777216, 0, 0, 32289, -1, -1, 0, 0, 0, 77, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
	
}