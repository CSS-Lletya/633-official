package com.rs.plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rs.game.Entity;
import com.rs.plugin.listener.RegionAttributeListener;
import com.rs.plugin.wrapper.RegionAttributeSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * Checks if an entity is in a region/coordinated area to set multi zoning Will
 * upgrade this system with further uses in future. (just basic for now)
 * 
 * @author Dennis
 */
public class RegionAttributePluginDispatcher {
	
	private static final Object2ObjectOpenHashMap<RegionAttributeSignature, RegionAttributeListener> REGION_ATTRIBUTES = new Object2ObjectOpenHashMap<>();

	public static boolean isMulti(Entity entity) {
		return getRegion(entity, entity.getRegionId()).isPresent();
	}

	private static Optional<RegionAttributeListener> getRegion(Entity entity, int region) {
		return REGION_ATTRIBUTES.values().stream().filter(regionId -> regionId.withinMultiZonedBoundaries(entity) || isRegion(regionId, region)).findFirst();
	}

	private static boolean isRegion(RegionAttributeListener command, int id) {
		RegionAttributeSignature sig = command.getClass().getAnnotation(RegionAttributeSignature.class);
		return Arrays.stream(sig.forceMultiRegions()).anyMatch(region -> region == id);
	}

	public static void load() {
		List<RegionAttributeListener> commands = Utility.getClassesInDirectory("com.rs.plugin.impl.regionalttributes")
				.stream().map(clazz -> (RegionAttributeListener) clazz).collect(Collectors.toList());
		commands.forEach(command -> REGION_ATTRIBUTES
				.put(command.getClass().getAnnotation(RegionAttributeSignature.class), command));
	}

	public static void reload() {
		REGION_ATTRIBUTES.clear();
		load();
		LogUtility.log(LogType.INFO, "Reloaded Region Attributes Plugins");
	}
}