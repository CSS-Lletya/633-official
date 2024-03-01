package com.rs.game.map;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.rs.GameConstants;
import com.rs.cores.CoresManager;
import com.rs.game.Entity;
import com.rs.game.EntityList;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.game.task.TaskManager;
import com.rs.game.task.impl.BonusExperienceTimerTask;
import com.rs.game.task.impl.DailyCharacterBackupTask;
import com.rs.game.task.impl.GlobalImplingTask;
import com.rs.game.task.impl.GlobalPlayerSavingTask;
import com.rs.game.task.impl.NewYearsItemsSpawningEventTask;
import com.rs.game.task.impl.RestoreHitpoints;
import com.rs.game.task.impl.RestoreRunEnergyTask;
import com.rs.game.task.impl.RestoreSkillTask;
import com.rs.game.task.impl.RestoreSpecialTask;
import com.rs.game.task.impl.ShopRestockTask;
import com.rs.game.task.impl.SummoningPassiveTask;
import com.rs.net.ServerChannelHandler;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.AntiFlood;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import skills.mining.LivingRockCavern;

public class World {

	@Getter
	@Setter
	private short exiting_delay;
	
	@Getter
	@Setter
	private long exiting_start;
	
	private static final EntityList<Player> lobbyPlayers = new EntityList<Player>(GameConstants.PLAYERS_LIMIT, true);

	private static final Predicate<Player> VALID_PLAYER = (player) -> player != null && player.isStarted() && !player.isFinished();
	private static final Predicate<NPC> VALID_NPC = (npc) -> npc != null && !npc.isFinished();
	
	private static final EntityList<Player> players = new EntityList<Player>(GameConstants.PLAYERS_LIMIT, true);
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
	private static Short2ObjectOpenHashMap<Region> regions = new Short2ObjectOpenHashMap<>();

	public final void init() {
		submit(new RestoreRunEnergyTask(), new RestoreSpecialTask(), new SummoningPassiveTask(), new ShopRestockTask(),
				new RestoreSkillTask(), new RestoreHitpoints(), new BonusExperienceTimerTask(), new GlobalImplingTask(),
				new DailyCharacterBackupTask(), new NewYearsItemsSpawningEventTask(), new GlobalPlayerSavingTask());
		LivingRockCavern.init();
	}

	public static final Region getRegion(int id) {
		return getRegion(id, false);
	}

	public static final Region getRegion(int id, boolean load) {
		Region region = regions.get((short) id);
		if (region == null) {
			region = new Region(id);
			regions.put((short) id, region);
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
			ServerChannelHandler.shutdown();
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
			ObjectArrayList<Short> playersIndexes = getRegion(regionId).getPlayersIndexes();
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
			ObjectArrayList<Short> playersIndexes = getRegion(regionId).getPlayersIndexes();
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
			ObjectArrayList<Short> playersIndexes = getRegion(regionId).getPlayersIndexes();
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
	
	/**
	 * Submits a collection of tasks to the backing {@link TaskManager}.
	 * @param task the task to submit to the queue.
	 */
	public void submit(Task... task) {
		Arrays.stream(task).forEach(getTaskManager()::submit);
	}

    public static final Int2ObjectAVLTreeMap<Player> USED_PIDS = new Int2ObjectAVLTreeMap<Player>();
    public static final IntArrayList AVAILABLE_PIDS = new IntArrayList(IntStream.rangeClosed(0, 2000).boxed().collect(Collectors.toList()));
    
    public static final void shufflePids() {
        final int n = 2000;
        for (int i = 0; i < n; i++) {
            final int change = i + RandomUtility.nextInt(n - i);
            swap(i, change);
        }
    }
    
    private static final void swap(final int i, final int change) {
        val a = World.USED_PIDS.get(i);
        val b = World.USED_PIDS.get(change);
        if ((a == null) && (b == null)) {
            return;
        }

        val pidA = a == null ? -1 : a.getPid();
        val pidB = b == null ? -1 : b.getPid();
        if (pidA != -1 && pidB != -1) {
            World.USED_PIDS.put(pidB, a);
            World.USED_PIDS.put(pidA, b);
            b.setPid(pidA);
            a.setPid(pidB);
        } else if (pidA != -1) {
            val pid = World.AVAILABLE_PIDS.removeInt(RandomUtility.random(World.AVAILABLE_PIDS.size() - 1));
            a.setPid(pid);
            World.USED_PIDS.put(pid, a);
            World.USED_PIDS.remove(pidA);
            World.AVAILABLE_PIDS.add(pidA);
        } else if (pidB != -1) {
            val pid = World.AVAILABLE_PIDS.removeInt(RandomUtility.random(World.AVAILABLE_PIDS.size() - 1));
            b.setPid(pid);
            World.USED_PIDS.put(pid, b);
            World.USED_PIDS.remove(pidB);
            World.AVAILABLE_PIDS.add(pidB);
        }
    }

    public static final boolean containsObjectWithId(WorldTile tile, int id) {
        return getRegion(tile.getRegionId()).containsObjectWithId(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion(), id);
    }
}