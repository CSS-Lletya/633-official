package com.rs.plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.GameConstants;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.Utility;
import com.rs.utilities.LogUtility.LogType;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * @author Dennis
 */
public final class ObjectPluginDispatcher {

	/**
	 * The object map which contains all the Objects on the world.
	 */
	private static final Object2ObjectOpenHashMap<ObjectSignature, ObjectType> OBJECTS = new Object2ObjectOpenHashMap<>();

	/**
	 * Executes the specified Objects if it's registered.
	 * 
	 * @param player the player executing the Objects.
	 * @param parts  the string which represents a Objects.
	 */
	public static void execute(Player player, GameObject gamObject, int optionId) {
		getObject(gamObject, gamObject.getId()).ifPresent(object -> {
			Try.run(() -> object.execute(player, gamObject, optionId));
		});
	}
	
	/**
	 * Executes the specified Objects if it's registered.
	 * 
	 * @param player the player executing the Objects.
	 * @param parts  the string which represents a Objects.
	 */
	public static void executeItemOnObject(Player player, GameObject gamObject, Item item) {
		getObject(gamObject, gamObject.getId()).ifPresent(object -> {
			Try.run(() -> object.executeItemOnObject(player, gamObject, item));
		});
	}

	/**
	 * Gets a Objects which matches the {@code identifier}.
	 * 
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<ObjectType> getObject(GameObject object, int objectId) {
		for (Entry<ObjectSignature, ObjectType> objects : OBJECTS.entrySet()) {
			if (isObjetId(objects.getValue(), objectId) || isObjectNamed(objects.getValue(), object)) {
				return Optional.of(objects.getValue());
			}
		}
		return Optional.empty();
	}

	/**
	 * Checks if the the Object Id matches the signature
	 * 
	 * @param object
	 * @param objectId
	 * @return
	 */
	private static boolean isObjetId(ObjectType object, int objectId) {
		ObjectSignature signature = object.getClass().getAnnotation(ObjectSignature.class);
		return Arrays.stream(signature.objectId()).anyMatch(right -> objectId == right);
	}

	/**
	 * Checks if the the Object Name matches the signature
	 * 
	 * @param object
	 * @param objectId
	 * @return
	 */
	private static boolean isObjectNamed(ObjectType object, GameObject worldObject) {
		ObjectSignature signature = object.getClass().getAnnotation(ObjectSignature.class);
		return Arrays.stream(signature.name())
				.anyMatch(objectName -> worldObject.getDefinitions().getName().contains(objectName));
	}

	/**
	 * Loads all the Objects into the {@link #OBJECTS} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<ObjectType> objectTypes = Utility.getClassesInDirectory("com.rs.plugin.impl.objects").stream()
				.map(clazz -> (ObjectType) clazz).collect(Collectors.toList());
		objectTypes.forEach(objectType -> OBJECTS.put(objectType.getClass().getAnnotation(ObjectSignature.class), objectType));
		List<ObjectType> objectTypesRegion = Utility.getClassesInDirectory("com.rs.plugin.impl.objects.region").stream()
				.map(clazz -> (ObjectType) clazz).collect(Collectors.toList());
		objectTypesRegion.forEach(objectType -> OBJECTS.put(objectType.getClass().getAnnotation(ObjectSignature.class), objectType));
	}

	/**
	 * Reloads all the Objects into the {@link #OBJECTS} list.
	 * <p>
	 * </p>
	 * <b>This method can be invoked on run-time to clear all the commands in the
	 * list and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		OBJECTS.clear();
		load();
		LogUtility.log(LogType.INFO, "Reloaded Objects Plugins");
	}

	public static void handleItemOnObject(final Player player, final GameObject object, final int interfaceId, final Item item) {
		@SuppressWarnings("unused")
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setRouteEvent(new RouteEvent(object, () ->  {
			player.faceObject(object);
			if (GameConstants.DEBUG)
				System.out.println("Item on object: " + object.getId());
			executeItemOnObject(player, object, item);
		}, false));
	}
}