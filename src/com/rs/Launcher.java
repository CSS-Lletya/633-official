package com.rs;

import java.util.Arrays;
import java.util.Objects;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.map.MapBuilder;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.net.ServerChannelHandler;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import io.vavr.control.Try;

/**
 * The Runnable source of open633
 * This is where we start our start-up services, etc.. to build our game.
 * The Client then connects and communicates specific data with the server concurrently
 * @author Dennis
 *
 */
public class Launcher {

	/**
	 * The Runnable, we're nothing without you <3
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GameProperties.getGameProperties().load();
		
		long currentTime = Utility.currentTimeMillis();
		
		GameLoader.getLOADER().getBackgroundLoader().waitForPendingTasks().shutdown();
		  
		LogUtility.log(LogType.INFO, "Server took "
				+ (Utility.currentTimeMillis() - currentTime)
				+ " milli seconds to launch.");
		
		addCleanMemoryTask();
	}

	/**
	 * A Simple memory cleaning event that takes place is the maximum memory is exceeded.
	 * This'll help the server become more stable in a sense of not using too much power 
	 * for the host service (PC/VPS/Dedicated Server)
	 */
	private static void addCleanMemoryTask() {
		CoresManager.schedule(() -> {
			cleanMemory(Runtime.getRuntime().freeMemory() < GameConstants.MIN_FREE_MEM_ALLOWED);
		}, 10);
	}

	/**
	 * The memory cleaning event contents. Here you can see what's being done specifically.
	 * @param force
	 */
	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			for (Region region : World.getRegions().values()) {
				for (int regionId : MapBuilder.FORCE_LOAD_REGIONS)
					if (regionId == region.getRegionId())
						continue;
				region.unloadMap();
			}
		}
		Arrays.stream(Cache.STORE.getIndexes()).filter(Objects::nonNull).forEach(index -> index.resetCachedFiles());
		System.gc();
		LogUtility.log(LogType.INFO, "Game Server memory has been cleaned " + (force ? "force: true:" : "force: false"));
	}

	/**
	 * The shutdown hook fore the Network, then finally terminating the Application itself.
	 */
	public static void shutdown() {
		Try.runRunnable(() -> {
			ServerChannelHandler.shutdown();
			CoresManager.shutdown();
		}).andFinally(() -> System.exit(0));
	}
}