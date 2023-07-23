package com.rs.game.map;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.rs.GameConstants;
import com.rs.Launcher;
import com.rs.cores.CoresManager;
import com.rs.game.Entity;
import com.rs.game.EntityList;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.LivingRockCavern;
import com.rs.game.task.Task;
import com.rs.game.task.TaskManager;
import com.rs.game.task.impl.BonusExperienceTimerTask;
import com.rs.game.task.impl.DailyCharacterBackupTask;
import com.rs.game.task.impl.GlobalImplingTask;
import com.rs.game.task.impl.RestoreHitpoints;
import com.rs.game.task.impl.RestoreRunEnergyTask;
import com.rs.game.task.impl.RestoreSkillTask;
import com.rs.game.task.impl.RestoreSpecialTask;
import com.rs.game.task.impl.ShopRestockTask;
import com.rs.game.task.impl.SummoningPassiveTask;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.AntiFlood;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

public class World {

	@Getter
	@Setter
	public short exiting_delay;
	
	@Getter
	@Setter
	public long exiting_start;
	
	private static final EntityList<Player> lobbyPlayers = new EntityList<Player>(GameConstants.PLAYERS_LIMIT, true);

	private static final Predicate<Player> VALID_PLAYER = (player) -> player != null && player.isStarted() && !player.isFinished();
	private static final Predicate<NPC> VALID_NPC = (npc) -> npc != null && !npc.isFinished();
	
	public static final EntityList<Player> players = new EntityList<Player>(GameConstants.PLAYERS_LIMIT, true);
	private static final EntityList<NPC> npcs = new EntityList<NPC>(GameConstants.NPCS_LIMIT, false);

	public static Stream<Entity> entities() {
		return Stream.concat(players(), npcs());
	}

	public static Stream<Player> players() {
		return players.stream().filter(VALID_PLAYER);
	}

	public static Stream<NPC> npcs() {
		return npcs.stream().filter(VALID_NPC);
	}
	
	public static final EntityList<Player> getLobbyPlayers() {
		return lobbyPlayers;
	}
	
	public static final void addNPC(NPC npc) {
		npcs.add(npc);
	}

	public static final void removeNPC(NPC npc) {
		npcs.remove(npc);
	}
	
	public static final EntityList<Player> getPlayers() {
		return players;
	}

	public static final EntityList<NPC> getNPCs() {
		return npcs;
	}
	
	@Getter
	private static Object2ObjectOpenHashMap<Integer, Region> regions = new Object2ObjectOpenHashMap<>();

	public static final void init() {
		World.get().submit(new RestoreRunEnergyTask());
		World.get().submit(new RestoreSpecialTask());
		World.get().submit(new SummoningPassiveTask());
		World.get().submit(new ShopRestockTask());
		World.get().submit(new RestoreSkillTask());
		World.get().submit(new RestoreHitpoints());
		World.get().submit(new BonusExperienceTimerTask());
		World.get().submit(new GlobalImplingTask());
		World.get().submit(new DailyCharacterBackupTask());
		LivingRockCavern.init();
	}

	public static final Region getRegion(int id) {
		return getRegion(id, false);
	}

	public static final Region getRegion(int id, boolean load) {
		Region region = regions.get(id);
		if (region == null) {
			region = new Region(id);
			regions.put(id, region);
		}
		if (load)
			region.checkLoadMap();
		return region;
	}

	public static final Player getLobbyPlayerByDisplayName(String username) {
	    String formattedUsername = Utility.formatPlayerNameForDisplay(username).toLowerCase();
	    Optional<Player> playerOptional = getLobbyPlayers().stream()
	            .filter(player -> player != null &&
	                    (player.getUsername().equalsIgnoreCase(formattedUsername) ||
	                            player.getDisplayName().equalsIgnoreCase(formattedUsername)))
	            .findFirst();
	    return playerOptional.orElse(null);
	}
	
	public static final boolean containsLobbyPlayer(String username) {
	    String formattedUsername = username.toLowerCase();
	    return getLobbyPlayers().stream()
	            .filter(player -> player != null && player.getUsername().equalsIgnoreCase(formattedUsername))
	            .findFirst()
	            .isPresent();
	}

	
	public static final void addPlayer(Player player) {
		players.add(player);
		if (World.containsLobbyPlayer(player.getUsername())) {
			World.removeLobbyPlayer(player);
			AntiFlood.remove(player.getSession().getIP());
		}
		AntiFlood.add(player.getSession().getIP());
	}
	
	public static final void addLobbyPlayer(Player player) {
		lobbyPlayers.add(player);
		AntiFlood.add(player.getSession().getIP());
	}

	public static void removePlayer(Player player) {
	    Iterator<Player> iterator = players.iterator();
	    while (iterator.hasNext()) {
	        Player p = iterator.next();
	        if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
	            iterator.remove();
	        }
	    }
	    AntiFlood.remove(player.getSession().getIP());
	}
	
	public static void removeLobbyPlayer(Player player) {
	    Iterator<Player> iterator = lobbyPlayers.iterator();
	    while (iterator.hasNext()) {
	        Player p = iterator.next();
	        if (p.getUsername().equalsIgnoreCase(player.getUsername())) {
	            if (player.getCurrentFriendChat() != null) {
	                player.getCurrentFriendChat().leaveChat(player, true);
	            }
	            iterator.remove();
	        }
	    }
	    AntiFlood.remove(player.getSession().getIP());
	}
	
	/*
	 * checks clip
	 */
	public static boolean isRegionLoaded(int regionId) {
		Region region = getRegion(regionId);
		return region == null ? false : region.getLoadMapStage() == 2;
	}
	
	public static final Optional<Player> getPlayer(String username) {
		return players().filter(p -> p.getUsername().equalsIgnoreCase(username)).findFirst();
	}

	public static final Optional<Player> containsPlayer(String username) {
		return players().filter(p -> p.getUsername().equalsIgnoreCase(username)).findFirst();
	}

	public static final Player getPlayerByDisplayName(String username) {
		String formatedUsername = Utility.formatPlayerNameForDisplay(username);
		return players().filter(p -> p.getUsername().equalsIgnoreCase(formatedUsername) || p.getDisplayName().equalsIgnoreCase(formatedUsername)).findFirst().orElse(null);
	}

	@SneakyThrows(Throwable.class)
	public static final void safeShutdown(final boolean restart, short delay) {
		if (get().getExiting_start() != 0)
			return;
		get().setExiting_start(Utility.currentTimeMillis());
		get().setExiting_delay(delay);
		World.players().forEach(player -> player.getPackets().sendSystemUpdate(delay));
		CoresManager.schedule(() -> {
			World.players().forEach(player -> player.getSession().realFinish(player, true));
			Launcher.shutdown();
		}, delay);
	}

	public static final void sendGraphics(Graphics graphics, WorldTile tile) {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isStarted() || player.isFinished() || !player.withinDistance(tile))
				continue;
			player.getPackets().sendGraphics(graphics, tile);
		}
	}

	@SuppressWarnings("deprecation")
	public static final void sendProjectile(Entity shooter, WorldTile startTile, WorldTile receiver, int gfxId,
			int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Short> playersIndexes = getRegion(regionId).getPlayersIndexes();
			if (playersIndexes == null)
				continue;
			for (Short playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.isStarted() || player.isFinished()
						|| (!player.withinDistance(shooter) && !player.withinDistance(receiver)))
					continue;
				player.getPackets().sendProjectile(null, startTile, receiver, gfxId, startHeight, endHeight, speed,
						delay, curve, startDistanceOffset, shooter.getSize());
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static final void sendProjectile(Entity shooter, WorldTile receiver, int gfxId, int startHeight,
			int endHeight, int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Short> playersIndexes = getRegion(regionId).getPlayersIndexes();
			if (playersIndexes == null)
				continue;
			for (Short playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.isStarted() || player.isFinished()
						|| (!player.withinDistance(shooter) && !player.withinDistance(receiver)))
					continue;
				player.getPackets().sendProjectile(null, shooter, receiver, gfxId, startHeight, endHeight, speed, delay,
						curve, startDistanceOffset, shooter.getSize());
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static final void sendProjectile(Entity shooter, Entity receiver, int gfxId, int startHeight, int endHeight,
			int speed, int delay, int curve, int startDistanceOffset) {
		for (int regionId : shooter.getMapRegionsIds()) {
			List<Short> playersIndexes = getRegion(regionId).getPlayersIndexes();
			if (playersIndexes == null)
				continue;
			for (Short playerIndex : playersIndexes) {
				Player player = players.get(playerIndex);
				if (player == null || !player.isStarted() || player.isFinished()
						|| (!player.withinDistance(shooter) && !player.withinDistance(receiver)))
					continue;
				int size = shooter.getSize();
				player.getPackets().sendProjectile(receiver, shooter, receiver, gfxId, startHeight, endHeight, speed,
						delay, curve, startDistanceOffset, size);
			}
		}
	}

	public static void sendWorldMessage(String message) {
		players().forEach(player -> player.getPackets().sendGameMessage(message));
	}

	/**
	 * An implementation of the singleton pattern to prevent indirect
	 * instantiation of this class file.
	 */
	private static final World singleton = new World();
	
	/**
	 * Returns the singleton pattern implementation.
	 * @return The returned implementation.
	 */
	public static World get() {
		return singleton;
	}
	
	/**
	 * The manager for the queue of game tasks.
	 */
	@Getter
	public final TaskManager taskManager = new TaskManager();
	
	/**
	 * Represents a Tiles attribute (Mask, Clip type, such)
	 */
	@Getter
	public static final TileAttributes tileAttributes = new TileAttributes();
	
	/**
	 * Submits {@code t} to the backing {@link TaskManager}.
	 * @param task the task to submit to the queue.
	 */
	public void submit(Task task) {
		getTaskManager().submit(task);
	}
}