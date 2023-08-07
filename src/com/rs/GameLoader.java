package com.rs;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

import com.rs.cache.Cache;
import com.rs.content.quests.QuestManager;
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
import com.rs.utilities.Utility;
import com.rs.utilities.loaders.Censor;
import com.rs.utilities.loaders.CharmDrop;
import com.rs.utilities.loaders.EquipData;
import com.rs.utilities.loaders.ItemBonuses;
import com.rs.utilities.loaders.ItemExamines;
import com.rs.utilities.loaders.ItemSpawns;
import com.rs.utilities.loaders.MapArchiveKeys;
import com.rs.utilities.loaders.MusicHints;
import com.rs.utilities.loaders.NPCCombatDefinitionsL;
import com.rs.utilities.loaders.NPCSpawns;
import com.rs.utilities.loaders.ShopsHandler;

import lombok.SneakyThrows;
import lombok.val;

/**
 * Represents the startup loading utilizing the {@link ForkJoinPool} system.
 * Best known startup benchmark:
 * 
 * 2023-08-06 11:54:19 [main] com.rs.utilities.LogUtility.log()
 * INFO: Server took 504 milli seconds to launch.
 * 
 * @author Dennis
 *
 */
public class GameLoader {
	
	@SneakyThrows(Exception.class)
	public static void load() {
			Cache.init();
			val pool = ForkJoinPool.commonPool();
			LogUtility.log(LogType.INFO, "Loading #633 Cache, Networking, and Core loaders.");
			pool.invokeAll(Arrays.asList(
					Utility.callable(World.get()::init), Utility.callable(CoresManager::init),
					Utility.callable(ServerChannelHandler::init), Utility.callable(Huffman::init),
					Utility.callable(MapArchiveKeys::init), Utility.callable(MapBuilder::init),
					Utility.callable(WorldList::init)
			));
			LogUtility.log(LogType.INFO, "Loading Plugin handlers.");
			pool.invokeAll(Arrays.asList(
					Utility.callable(RSInterfacePluginDispatcher::load), Utility.callable(InventoryPluginDispatcher::load),
					Utility.callable(ObjectPluginDispatcher::load), Utility.callable(CommandPluginDispatcher::load),
					Utility.callable(NPCPluginDispatcher::load), Utility.callable(NPCCombatDispatcher::load),
					Utility.callable(LogicPacketDispatcher::load), Utility.callable(OutgoingPacketDispatcher::load),
					Utility.callable(GenericNPCDispatcher::load), Utility.callable(PassiveSpellDispatcher::load),
					Utility.callable(RegionAttributePluginDispatcher::load)
			));
			if (GameConstants.SQL_ENABLED) {
				pool.invokeAll(Arrays.asList(Utility.callable(GameDatabase::initializeWebsiteDatabases),
						Utility.callable(PassiveDatabaseWorker::initialize)));
			}
			LogUtility.log(LogType.INFO, "Loading other files.");
			pool.submit(Utility.callable(ScriptManager::load));
			pool.submit(Utility.callable(AttributeKey::init));
			pool.submit(MusicHints::init);
			pool.submit(FriendChatsManager::init);
			pool.submit(QuestManager::load);
			pool.submit(ItemWeights::init);
			pool.submit(DoorPair::loadPairs);
			pool.submit(ShopsHandler::loadShops);
			pool.submit(CharmDrop::loadCharmDrops);
			pool.submit(DropSets::init);
			pool.submit(EquipData::init);
			pool.submit(ItemBonuses::init);
			pool.submit(ItemExamines::init);
			pool.submit(ItemSpawns::init);
			pool.submit(Censor::init);
			pool.submit(NPCCombatDefinitionsL::init);
			pool.submit(NPCSpawns::init);
			LogUtility.log(LogType.INFO, "Loading Player Conditions files.");
			pool.submit(() -> HostManager.deserialize(HostListType.STARTER_RECEIVED));
			pool.submit(() -> HostManager.deserialize(HostListType.BANNED_IP));
			pool.submit(() -> HostManager.deserialize(HostListType.MUTED_IP));
			pool.shutdown();
	}
}