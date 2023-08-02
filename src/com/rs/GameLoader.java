package com.rs;

import java.io.IOException;
import java.util.concurrent.Executors;

import com.google.gson.JsonIOException;
import com.rs.cache.Cache;
import com.rs.content.quests.QuestManager;
import com.rs.cores.BlockingExecutorService;
import com.rs.cores.CoresManager;
import com.rs.game.item.ItemWeights;
import com.rs.game.map.MapBuilder;
import com.rs.game.map.World;
import com.rs.game.npc.combat.NPCCombatDispatcher;
import com.rs.game.npc.drops.DropSets;
import com.rs.game.npc.global.GenericNPCDispatcher;
import com.rs.game.player.attribute.AttributeKey;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.doors.DoorPair;
import com.rs.game.player.spells.passive.PassiveSpellDispatcher;
import com.rs.game.system.scripts.ScriptManager;
import com.rs.net.Huffman;
import com.rs.net.ServerChannelHandler;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;
import com.rs.net.packets.logic.LogicPacketDispatcher;
import com.rs.net.packets.outgoing.OutgoingPacketDispatcher;
import com.rs.net.wordlist.WorldList;
import com.rs.network.sql.GameDatabase;
import com.rs.network.sql.PassiveDatabaseWorker;
import com.rs.plugin.CommandPluginDispatcher;
import com.rs.plugin.InventoryPluginDispatcher;
import com.rs.plugin.NPCPluginDispatcher;
import com.rs.plugin.ObjectPluginDispatcher;
import com.rs.plugin.RSInterfacePluginDispatcher;
import com.rs.plugin.RegionAttributePluginDispatcher;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.loaders.Censor;
import com.rs.utilities.loaders.CharmDrop;
import com.rs.utilities.loaders.EquipData;
import com.rs.utilities.loaders.ItemBonuses;
import com.rs.utilities.loaders.ItemExamines;
import com.rs.utilities.loaders.ItemSpawns;
import com.rs.utilities.loaders.MapArchiveKeys;
import com.rs.utilities.loaders.MusicHints;
import com.rs.utilities.loaders.NPCBonuses;
import com.rs.utilities.loaders.NPCCombatDefinitionsL;
import com.rs.utilities.loaders.NPCSpawns;
import com.rs.utilities.loaders.ShopsHandler;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @author Dennis
 * @since Feb 27, 2014
 */
public class GameLoader {

	public GameLoader() {
		load();
	}

	/**
	 * The instance of the loader
	 */
	@Getter
	private static final GameLoader LOADER = new GameLoader();

	/**
	 * An executor service which handles background loading tasks.
	 */
	@Getter
	private final BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newCachedThreadPool());

	/**
	 * Loads everything here
	 *
	 * @throws IOException
	 */
	@SneakyThrows({IOException.class, JsonIOException.class})
	public void load() {
		LogUtility.log(LogType.INFO, "Loading #633 Cache");
		Cache.init();
		getBackgroundLoader().submit(() -> {
			LogUtility.log(LogType.INFO, "Loading Game World & Service Network.");
			CoresManager.init();
			World.get().init();
			ServerChannelHandler.init();
			Huffman.init();
			MapArchiveKeys.init();
			MapBuilder.init();
			WorldList.init();
		});
		getBackgroundLoader().submit(() -> {
			LogUtility.log(LogType.INFO, "Loading Bonuses.");
			ItemBonuses.init();
			EquipData.init();
			ItemExamines.init();
			ItemSpawns.init();
			Censor.init();
			NPCCombatDefinitionsL.init();
			NPCBonuses.init();
			NPCSpawns.init();
		});
		getBackgroundLoader().submit(() -> {
			LogUtility.log(LogType.INFO, "Loading Miscellaneous Files.");
			MusicHints.init();
			FriendChatsManager.init();
			AttributeKey.init();
			QuestManager.load();
			ItemWeights.init();
			DoorPair.loadPairs();
			ShopsHandler.loadShops();
		});
		getBackgroundLoader().submit(() -> {
			LogUtility.log(LogType.INFO, "Loading Host files.");
			HostManager.deserialize(HostListType.STARTER_RECEIVED);
			HostManager.deserialize(HostListType.BANNED_IP);
			HostManager.deserialize(HostListType.MUTED_IP);
		});
		getBackgroundLoader().submit(() -> {
			LogUtility.log(LogType.INFO, "Loading Plugin handlers.");
			RSInterfacePluginDispatcher.load();
			InventoryPluginDispatcher.load();
			ObjectPluginDispatcher.load();
			CommandPluginDispatcher.load();
			NPCPluginDispatcher.load();
			NPCCombatDispatcher.load();
			LogicPacketDispatcher.load();
			OutgoingPacketDispatcher.load();
			GenericNPCDispatcher.load();
			PassiveSpellDispatcher.load();
			RegionAttributePluginDispatcher.load();
		});
		getBackgroundLoader().submit(() -> {
        	if (GameConstants.SQL_ENABLED) {
    			GameDatabase.initializeWebsiteDatabases();
    			PassiveDatabaseWorker.initialize();
    		}
        });
		getBackgroundLoader().submit(() -> {
        	CharmDrop.loadCharmDrops();
        	DropSets.init();
        	ScriptManager.load();
        });
	}
}