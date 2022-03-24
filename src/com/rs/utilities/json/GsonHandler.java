package com.rs.utilities.json;

import java.lang.reflect.InvocationTargetException;

import com.rs.utilities.json.impl.NPCAutoSpawn;
import com.rs.utilities.json.impl.ObjectSpawnLoader;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.SneakyThrows;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 1, 2014
 */
public class GsonHandler {

	/**
	 * Initializes all json loaders
	 */
	public static void initialize() {
		Try.run(() -> addJsonLoaders());
		CLASSES.forEach(classes -> classes.initialize());
		LOADED = Boolean.TRUE;
	}

	/**
	 * Waits for the json loaders to be loaded
	 */
	public static void waitForLoad() {
		while (!LOADED) {
			System.out.flush();
		}
	}

	/**
	 * Adds all json loaders to the map
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public static void addJsonLoaders() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		CLASSES.add(NPCAutoSpawn.class.newInstance());
		CLASSES.add(ObjectSpawnLoader.class.newInstance());
	}

	/**
	 * Gets a {@link #JsonLoader} by the class
	 * 
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	@SneakyThrows(Exception.class)
	public static <T> T getJsonLoader(Class<?> clazz) {
		GsonLoader<?> loader = CACHED_LOADERS.get(clazz.getSimpleName());
		if (loader != null) {
			return (T) loader;
		} else {
			for (GsonLoader<?> listLoader : CLASSES) {
				if (listLoader.getClass().getSimpleName().equals(clazz.getSimpleName())) {
					CACHED_LOADERS.put(listLoader.getClass().getSimpleName(), listLoader);
					return (T) listLoader;
				}
			}
		}
		return null;
	}

	/**
	 * If all loaders have loaded
	 */
	public static boolean LOADED = Boolean.FALSE;

	/** The cached loaders */
	private static Object2ObjectOpenHashMap<String, GsonLoader<?>> CACHED_LOADERS = new Object2ObjectOpenHashMap<String, GsonLoader<?>>();

	/**
	 * Adds all of the loaders to the map
	 */
	private static final ObjectArrayList<GsonLoader<?>> CLASSES = new ObjectArrayList<GsonLoader<?>>();
}
