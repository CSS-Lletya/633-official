package com.rs.game;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.google.common.base.Preconditions;
import com.rs.GameConstants;
import com.rs.cache.loaders.AnimationDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.constants.Graphic;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.game.map.DynamicRegion;
import com.rs.game.map.GameObject;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.ForcedMovement;
import com.rs.game.movement.route.RouteFinder;
import com.rs.game.movement.route.strategy.EntityStrategy;
import com.rs.game.movement.route.strategy.ObjectStrategy;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Combat;
import com.rs.game.player.Hit;
import com.rs.game.player.LocalNPCUpdate;
import com.rs.game.player.LocalPlayerUpdate;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.AttributeMap;
import com.rs.game.player.content.TeleportType;
import com.rs.game.player.type.CombatEffectType;
import com.rs.game.player.type.Effect;
import com.rs.game.player.type.PoisonType;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.MutableNumber;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import skills.Skills;

@Getter
@Setter
public abstract class Entity extends WorldTile {

	private final static AtomicInteger hashCodeGenerator = new AtomicInteger();

	// transient stuff
	private transient int index;
	private transient short lastRegionId;
	private transient WorldTile lastLoadedMapRegionTile;
	private transient CopyOnWriteArrayList<Integer> mapRegionsIds;
	private transient int direction;
	private transient WorldTile lastWorldTile;
	private transient WorldTile nextWorldTile;
	private transient byte nextWalkDirection;
	private transient byte nextRunDirection;
	private transient WorldTile nextFaceWorldTile;
	private transient boolean teleported;
	// than 1thread so concurent
	private transient ObjectArrayFIFOQueue<Hit> receivedHits;
	private transient ConcurrentHashMap<Entity, Integer> receivedDamage;
	private transient boolean finished;
	
	// entity masks
	private transient Animation nextAnimation;
	private transient Graphics nextGraphics1;
	private transient Graphics nextGraphics2;
	private transient Graphics nextGraphics3;
	private transient Graphics nextGraphics4;
	private transient ObjectArrayList<Hit> nextHits;
	private transient ForcedMovement nextForceMovement;
	private transient ForceTalk nextForceTalk;
	private transient int nextFaceEntity;
	private transient int lastFaceEntity;
	private transient Entity attackedBy;
	private transient long attackedByDelay; 
	
	private transient boolean multiArea;
	private transient boolean isAtDynamicRegion;
	private transient long lastAnimationEnd;
	private transient boolean forceMultiArea;
	private transient long findTargetDelay;
	private transient short hashCode;
	private transient EntityMovement movement;
	/**
	 * An {@link AttributeMap} instance assigned to this {@code Actor}.
	 */
	protected AttributeMap attributes = new AttributeMap();
	private transient int mapSize;
	
	/**
	 * The amount of poison damage this entity has.
	 */
	private final MutableNumber poisonDamage = new MutableNumber();

	/**
	 * The type of poison that was previously applied.
	 */
	private PoisonType poisonType;
	
	private Map<Effect, Long> effects = new HashMap<>();
	
	private int hitpoints;
	private boolean run;

	// creates Entity and saved classes
	public Entity(WorldTile tile, EntityType type) {
		super(tile);
		this.type = requireNonNull(type);
	}

	@Override
	public int hashCode() {
		return getHashCode();
	}

	public final void initEntity() {
		setHashCode((short) hashCodeGenerator.getAndIncrement());
		setMapRegionsIds(new CopyOnWriteArrayList<Integer>());
		setReceivedHits(new ObjectArrayFIFOQueue<Hit>());
		setReceivedDamage(new ConcurrentHashMap<Entity, Integer>());
		setNextHits(new ObjectArrayList<Hit>());
		setNextWalkDirection((byte) (nextRunDirection - 1));
		setLastFaceEntity(-1);
		nextFaceEntity = -2;
		setMovement(new EntityMovement(this));
		attributes = new AttributeMap();
	}

	public int getClientIndex() {
		return getIndex() + (isPlayer() ? 32768 : 0);
	}

	public void applyHit(Hit hit) {
		if (isDead())
			return;
		getReceivedHits().enqueue(hit);
		handleIngoingHit(hit);
	}

	public abstract void handleIngoingHit(Hit hit);
	
	public abstract void sendDeath(Optional<Entity> source);

	public abstract void deregister();
	
	public void reset(boolean attributes) {
		setHitpoints(getMaxHitpoints());
		getReceivedHits().trim();
		resetCombat();
		getMovement().getWalkSteps().clear();
		resetReceivedDamage();
		setAttackedBy(null);
		setAttackedByDelay(0);
		if (attributes)
			getAttributes().attributes.clear();
		ifPlayer(player -> {
			player.getInterfaceManager().refreshHitPoints();
			player.getHintIconsManager().removeAll();
			player.getSkills().restoreSkills();
			player.getCombatDefinitions().resetSpecialAttack();
			player.getPrayer().reset();
			player.getCombatDefinitions().resetSpells(true);
			player.setResting((byte) 0);
			player.getDetails().getPoisonImmunity().set(0);
			player.getDetails().setAntifireDetails(Optional.empty());
			player.getDetails().setRunEnergy((byte) 100);
			player.getAppearance().generateAppearenceData();
		});
		ifNpc(npc -> {
			npc.setDirection(npc.getRespawnDirection());
			npc.getCombat().reset();
			npc.setForceWalk(null);
		});
	}

	public void reset() {
		reset(true);
	}

	public void resetCombat() {
		setAttackedBy(null);
		setAttackedByDelay(0);
	}

	public void processReceivedHits() {
		ifPlayer(player -> {
			if (player.getNextEmoteEnd() >= Utility.currentTimeMillis())
				return;
		});
		ObjectArrayFIFOQueue<Hit> hit;
		int count = 0;
		while (!getReceivedHits().isEmpty() && (hit = getReceivedHits()) != null && count++ < 10)
			processHit(hit);
	}

	private void processHit(ObjectArrayFIFOQueue<Hit> hit) {
		if (isDead())
			return;
		while (!getReceivedHits().isEmpty()) {
			removeHitpoints(hit);
			getNextHits().add(hit.first());
			hit.dequeue();
		}
	}

	public void resetReceivedHits() {
		getNextHits().clear();
		getReceivedHits().clear();
	}

	public void removeHitpoints(ObjectArrayFIFOQueue<Hit> hit) {
		if (isDead())
			return;
		if (hit.first().getDamage() > getHitpoints())
			hit.first().setDamage(getHitpoints());
		addReceivedDamage(hit.first().getSource(), hit.first().getDamage());
		setHitpoints(getHitpoints() - hit.first().getDamage());
		if (getHitpoints() <= 0)
			sendDeath(Optional.of(hit.first().getSource()));

		ifPlayer(player -> {
			if (player.getPrayer().hasPrayersOn()) {
				if ((getHitpoints() < player.getMaxHitpoints() * 0.1) && player.getPrayer().usingPrayer(0, 23)) {
					setNextGraphics(Graphic.HEALING_BARRIER);
					setHitpoints((int) (getHitpoints() + player.getSkills().getLevelForXp(Skills.PRAYER) * 2.5));
					player.getSkills().set(Skills.PRAYER, 0);
					player.getPrayer().setPrayerpoints(0);
				} else if (player.getEquipment().getAmuletId() != 11090 && player.getEquipment().getRingId() == 11090
						&& player.getHitpoints() <= player.getMaxHitpoints() * 0.1) {
					player.getMovement().move(true, GameConstants.START_PLAYER_LOCATION, TeleportType.BLANK);
					player.getEquipment().deleteItem(11090, 1);
					player.getPackets()
							.sendGameMessage("Your ring of life saves you, but is destroyed in the process.");
				}
			}
			if (player.getEquipment().getAmuletId() == 11090
					&& player.getHitpoints() <= player.getMaxHitpoints() * 0.2) {
				player.heal((int) (player.getMaxHitpoints() * 0.3));
				player.getEquipment().deleteItem(11090, 1);
				player.getPackets()
						.sendGameMessage("Your pheonix necklace heals you, but is destroyed in the process.");
			}
			player.getInterfaceManager().refreshHitPoints();
		});
	}

	public void resetReceivedDamage() {
		getReceivedDamage().clear();
	}

	public void removeDamage(Entity entity) {
		getReceivedDamage().remove(entity);
	}

	public Player getMostDamageReceivedSourcePlayer() {
		Player player = null;
		int damage = -1;
		Iterator<Entry<Entity, Integer>> it$ = getReceivedDamage().entrySet().iterator();
		while (it$.hasNext()) {
			Entry<Entity, Integer> entry = it$.next();
			Entity source = entry.getKey();
			if (!source.isPlayer()) {
				continue;
			}
			Integer d = entry.getValue();
			if (d == null || source.isFinished()) {
				getReceivedDamage().remove(source);
				continue;
			}
			if (d > damage) {
				player = (Player) source;
				damage = d;
			}
		}
		return player;
	}

	public int getDamageReceived(Player source) {
		Integer d = getReceivedDamage().get(source);
		if (d == null || source.isFinished()) {
			getReceivedDamage().remove(source);
			return -1;
		}
		return d;
	}

	public void processReceivedDamage() {
		for (Entity source : getReceivedDamage().keySet()) {
			Integer damage = getReceivedDamage().get(source);
			if (damage == null || source.isFinished()) {
				getReceivedDamage().remove(source);
				continue;
			}
			damage--;
			if (damage == 0) {
				getReceivedDamage().remove(source);
				continue;
			}
			getReceivedDamage().put(source, damage);
		}
	}

	public void addReceivedDamage(Entity source, int amount) {
		if (source == null)
			return;
		Integer damage = getReceivedDamage().get(source);
		damage = damage == null ? amount : damage + amount;
		if (damage < 0)
			getReceivedDamage().remove(source);
		else
			getReceivedDamage().put(source, damage);
	}

	public void heal(int ammount) {
		heal(ammount, 0);
		ifPlayer(player -> player.getInterfaceManager().refreshHitPoints());
	}

	public void heal(int ammount, int extra) {
		int hp = (getHitpoints() + ammount) >= (getMaxHitpoints() + extra) ? (getMaxHitpoints() + extra)
				: (getHitpoints() + ammount);
		if (getHitpoints() > hp)
			return;
		setHitpoints((getHitpoints() + ammount) >= (getMaxHitpoints() + extra) ? (getMaxHitpoints() + extra)
				: (getHitpoints() + ammount));
	}

	public boolean hasWalkSteps() {
		return !getMovement().getWalkSteps().isEmpty();
	}

	public void processMovement() {
		setLastWorldTile(new WorldTile(this));
		if (getLastFaceEntity() >= 0) {
			Entity target = getLastFaceEntity() >= 32768 ? World.getPlayers().get(getLastFaceEntity() - 32768)
					: World.getNPCs().get(getLastFaceEntity());
			if (target != null)
				setDirection((byte) Utility.getFaceDirection(target.getCoordFaceX(target.getSize()) - getX(),
						target.getCoordFaceY(target.getSize()) - getY()));
		}
		setNextWalkDirection(nextRunDirection = -1);
		if (getNextWorldTile() != null) {
			int lastPlane = getPlane();
			setLocation(getNextWorldTile());
			setNextWorldTile(null);
			setTeleported(true);
			if (isPlayer() && ((Player) this).getTemporaryMovementType() == -1)
				((Player) this).setTemporaryMovementType(toPlayer().getMovement().getTELE_MOVE_TYPE());
			updateEntityRegion(this);
			if (needMapUpdate())
				loadMapRegions();
			else if (isPlayer() && lastPlane != getPlane())
				((Player) this).setClientLoadedMapRegion(false);
			resetWalkSteps();
			return;
		}
		setTeleported(false);
		if (getMovement().getWalkSteps().isEmpty())
			return;
		ifPlayer(player -> {
			if (player.getNextEmoteEnd() >= Utility.currentTimeMillis())
				return;
			if (player.getDetails().getRunEnergy() <= 0)
				setRun(false);
		});
		for (int stepCount = 0; stepCount < (isRun() ? 2 : 1); stepCount++) {
			Object[] nextStep = getNextWalkStep();
			if (nextStep == null) {
				if (stepCount == 1 && isPlayer())
					((Player) this).setTemporaryMovementType(getMovement().getWALK_MOVE_TYPE());
				break;
			}
			int dir = (int) nextStep[0];
			if (((boolean) nextStep[3] && !World.checkWalkStep(getPlane(), getX(), getY(), dir, getSize()))
					|| (isPlayer() && toPlayer().getMapZoneManager().execute((Player) this, controller -> !controller.canMove((Player) this, dir)))) {
				resetWalkSteps();
				break;
			}
			if (stepCount == 0) {
				setNextWalkDirection((byte) dir);
			} else {
				setNextRunDirection((byte) dir);
				ifPlayer(player -> player.getMovement().drainRunEnergy());
			}
			moveLocation(Utility.DIRECTION_DELTA_X[dir], Utility.DIRECTION_DELTA_Y[dir], 0);
		}
		updateEntityRegion(this);
		if (needMapUpdate())
			loadMapRegions();
	}

	@Override
	public void moveLocation(int xOffset, int yOffset, int planeOffset) {
		super.moveLocation(xOffset, yOffset, planeOffset);
		setDirection((byte) Utility.getFaceDirection(xOffset, yOffset));
	}

	private boolean needMapUpdate() {
		return needMapUpdate(getLastLoadedMapRegionTile());
	}

	public boolean needMapUpdate(WorldTile tile) {
		int lastMapRegionX = tile.getChunkX();
		int lastMapRegionY = tile.getChunkY();
		int regionX = getChunkX();
		int regionY = getChunkY();
		int size = ((GameConstants.MAP_SIZES[getMapSize()] >> 3) / 2) - 1;
		return Math.abs(lastMapRegionX - regionX) >= size || Math.abs(lastMapRegionY - regionY) >= size;
	}

	public boolean addWalkSteps(int destX, int destY) {
		return addWalkSteps(destX, destY, -1);
	}
	
    public void addWalkSteps(WorldTile toTile, int maxSteps, boolean clip) {
        addWalkSteps(toTile.getX(), toTile.getY(), maxSteps, clip);
    }

	public boolean clipedProjectile(WorldTile tile, boolean checkClose) {
		if (tile instanceof Entity) {
			Entity e = (Entity) tile;
			WorldTile me = this;
			if (((Entity) tile).isNPC()) {
				NPC n = (NPC) tile;
				tile = n.getMiddleWorldTile();
			} else if (isNPC()) {
				NPC n = (NPC) this;
				me = n.getMiddleWorldTile();
			}
			return clipedProjectile(tile, checkClose, 1) || e.clipedProjectile(me, checkClose, 1);
		}
		return clipedProjectile(tile, checkClose, 1);
	}

	public boolean clipedProjectile(WorldTile tile, boolean checkClose, int size) {
		int myX = getX();
		int myY = getY();
		if (isNPC()) {
			NPC n = (NPC) this;
			WorldTile thist = n.getMiddleWorldTile();
			myX = thist.getX();
			myY = thist.getY();
		}
		int destX = tile.getX();
		int destY = tile.getY();
		int lastTileX = myX;
		int lastTileY = myY;
		while (true) {
			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			int dir = Utility.getMoveDirection(myX - lastTileX, myY - lastTileY);
			if (dir == -1)
				return false;
			if (checkClose) {
				if (!World.checkWalkStep(getPlane(), lastTileX, lastTileY, dir, size))
					return false;
			} else if (!World.checkProjectileStep(getPlane(), lastTileX, lastTileY, dir, size))
				return false;
			lastTileX = myX;
			lastTileY = myY;
			if (lastTileX == destX && lastTileY == destY)
				return true;
		}
	}

	public boolean calcFollow(WorldTile target, boolean inteligent) {
		return calcFollow(target, -1, true, inteligent);
	}

	public boolean canWalkNPC(int toX, int toY) {
		int id = ((NPC) this).getId();
		// godwar npcs / corp can walk throught minions
		return id == 6260 || id == 6222 || id == 6203 || id == 6247 || id == 8133 || canWalkNPC(toX, toY, false);
	}

	private int getPreviewNextWalkStep() {
		Object[] step = getMovement().getWalkSteps().poll();
		if (step == null)
			return -1;
		return (int) step[0];
	}

	// checks collisions
	public boolean canWalkNPC(int toX, int toY, boolean checkUnder) {
		if (!isMultiArea() /*
							 * || (!checkUnder && !canWalkNPC(getX(), getY(), true))
							 */)
			return true;
		int size = getSize();
		for (int regionId : getMapRegionsIds()) {
			ObjectArrayList<Short> npcIndexes = World.getRegion(regionId).getNpcsIndexes();
			if (npcIndexes != null && npcIndexes.size() < 50) {
				for (int npcIndex : npcIndexes) {
					NPC target = World.getNPCs().get(npcIndex);
					if (target == null || target == this || target.isDead() || target.isFinished()
							|| target.getPlane() != getPlane() || !target.isMultiArea()
							|| (!(this instanceof Familiar) && target instanceof Familiar))
						continue;
					int targetSize = target.getSize();
					if (!checkUnder && target.getNextWalkDirection() == -1) { // means
						// the
						// walk
						// hasnt
						// been
						// processed
						// yet
						int previewDir = getPreviewNextWalkStep();
						if (previewDir != -1) {
							WorldTile tile = target.transform(Utility.DIRECTION_DELTA_X[previewDir],
									Utility.DIRECTION_DELTA_Y[previewDir], 0);
							if (Utility.colides(tile.getX(), tile.getY(), targetSize, getX(), getY(), size))
								continue;

							if (Utility.colides(tile.getX(), tile.getY(), targetSize, toX, toY, size))
								return false;
						}
					}
					if (Utility.colides(target.getX(), target.getY(), targetSize, getX(), getY(), size))
						continue;
					if (Utility.colides(target.getX(), target.getY(), targetSize, toX, toY, size))
						return false;
				}
			}
		}
		return true;
	}

	public WorldTile getMiddleWorldTile() {
		int size = getSize();
		return size == 1 ? this : new WorldTile(getCoordFaceX(size), getCoordFaceY(size), getPlane());
	}

	public static boolean findBasicRoute(Entity src, WorldTile dest, int maxStepsCount, boolean calculate) {
		int[] srcPos = src.getLastWalkTile();
		int[] destPos = { dest.getX(), dest.getY() };
		int srcSize = src.getSize();
		// set destSize to 0 to walk under it else follows
		int destSize = dest instanceof Entity ? ((Entity) dest).getSize() : 1;
		int[] destScenePos = { destPos[0] + destSize - 1, destPos[1] + destSize - 1 };// Arrays.copyOf(destPos,
																						// 2);//destSize == 1 ?
																						// Arrays.copyOf(destPos, 2) :
																						// new
																						// int[]
																						// {WorldTile.getCoordFaceX(destPos[0],
																						// destSize, destSize, -1),
																						// WorldTile.getCoordFaceY(destPos[1],
																						// destSize, destSize, -1)};
		while (maxStepsCount-- != 0) {
			int[] srcScenePos = { srcPos[0] + srcSize - 1, srcPos[1] + srcSize - 1 };// srcSize == 1 ?
																						// Arrays.copyOf(srcPos, 2) :
																						// new int[] {
																						// WorldTile.getCoordFaceX(srcPos[0],
																						// srcSize, srcSize, -1),
																						// WorldTile.getCoordFaceY(srcPos[1],
																						// srcSize, srcSize, -1)};
			if (!Utility.isOnRange(srcPos[0], srcPos[1], srcSize, destPos[0], destPos[1], destSize, 0)) {
				if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] < destScenePos[1]
						&& (!(src.isNPC()) || src.canWalkNPC(srcPos[0] + 1, srcPos[1] + 1))
						&& src.addWalkStep(srcPos[0] + 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] > destScenePos[1]
						&& (!(src.isNPC()) || src.canWalkNPC(srcPos[0] - 1, srcPos[1] - 1))
						&& src.addWalkStep(srcPos[0] - 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] > destScenePos[1]
						&& (!(src.isNPC()) || src.canWalkNPC(srcPos[0] + 1, srcPos[1] - 1))
						&& src.addWalkStep(srcPos[0] + 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					srcPos[1]--;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] < destScenePos[1]
						&& (!(src.isNPC()) || src.canWalkNPC(srcPos[0] - 1, srcPos[1] + 1))
						&& src.addWalkStep(srcPos[0] - 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[0] < destScenePos[0] && (!(src.isNPC()) || src.canWalkNPC(srcPos[0] + 1, srcPos[1]))
						&& src.addWalkStep(srcPos[0] + 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]++;
					continue;
				}
				if (srcScenePos[0] > destScenePos[0] && (!(src.isNPC()) || src.canWalkNPC(srcPos[0] - 1, srcPos[1]))
						&& src.addWalkStep(srcPos[0] - 1, srcPos[1], srcPos[0], srcPos[1], true)) {
					srcPos[0]--;
					continue;
				}
				if (srcScenePos[1] < destScenePos[1] && (!(src.isNPC()) || src.canWalkNPC(srcPos[0], srcPos[1] + 1))
						&& src.addWalkStep(srcPos[0], srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]++;
					continue;
				}
				if (srcScenePos[1] > destScenePos[1] && (!(src.isNPC()) || src.canWalkNPC(srcPos[0], srcPos[1] - 1))
						&& src.addWalkStep(srcPos[0], srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
					srcPos[1]--;
					continue;
				}
				return false;
			}
			break; // for now nothing between break and return
		}
		return true;
	}

	// used for normal npc follow int maxStepsCount, boolean calculate used to
	// save mem on normal path
	public boolean calcFollow(WorldTile target, int maxStepsCount, boolean calculate, boolean inteligent) {
		if (inteligent) {
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, getX(), getY(), getPlane(), getSize(),
					target instanceof GameObject ? new ObjectStrategy((GameObject) target)
							: new EntityStrategy((Entity) target),
					true);
			if (steps == -1)
				return false;
			if (steps == 0)
				return true;
			int[] bufferX = RouteFinder.getLastPathBufferX();
			int[] bufferY = RouteFinder.getLastPathBufferY();
			for (int step = steps - 1; step >= 0; step--) {
				if (!addWalkSteps(bufferX[step], bufferY[step], 25, true))
					break;
			}
			return true;
		}
		return findBasicRoute(this, (Entity) target, maxStepsCount, true);
	}

	// used for normal npc follow
	private int[] calculatedStep(int myX, int myY, int destX, int destY, int lastX, int lastY, int sizeX, int sizeY) {
		if (myX < destX) {
			myX++;
			if ((isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true))
				myX--;
			else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myX > destX) {
			myX--;
			if ((isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true))
				myX++;
			else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myY < destY) {
			myY++;
			if ((isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true))
				myY--;
			else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		} else if (myY > destY) {
			myY--;
			if ((isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastX, lastY, true)) {
				myY++;
			} else if (!(myX - destX > sizeX || myX - destX < -1 || myY - destY > sizeY || myY - destY < -1)) {
				if (myX == lastX || myY == lastY)
					return null;
				return new int[] { myX, myY };
			}
		}
		if (myX == lastX || myY == lastY)
			return null;
		return new int[] { myX, myY };
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY, int maxStepsCount) {
		return addWalkSteps(destX, destY, -1, true);
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkSteps(final int destX, final int destY, int maxStepsCount, boolean check) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check))
				return false;
			if (stepCount == maxStepsCount)
				return true;
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY)
				return true;
		}
	}

	private int[] getLastWalkTile() {
		Object[] objects = getMovement().getWalkSteps().toArray();
		if (objects.length == 0)
			return new int[] { getX(), getY() };
		Object step[] = (Object[]) objects[objects.length - 1];
		return new int[] { (int) step[1], (int) step[2] };
	}

	public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
		int dir = Utility.getMoveDirection(nextX - lastX, nextY - lastY);
		if (dir == -1)
			return false;
		if (check && !World.checkWalkStep(getPlane(), lastX, lastY, dir, getSize()))
			return false;
		ifPlayer(player -> {
			if (player.getMapZoneManager().execute((Player) this, controller -> !controller.checkWalkStep((Player) this, lastX, lastY, nextX, nextY)))
				return;
		});
		getMovement().getWalkSteps().add(new Object[] { dir, nextX, nextY, check });
		return true;
	}

	public void resetWalkSteps() {
		ifPlayer(p -> p.getPackets().sendResetMinimapFlag());
		getMovement().getWalkSteps().clear();
	}

	private Object[] getNextWalkStep() {
		Object[] step = getMovement().getWalkSteps().poll();
		if (step == null)
			return null;
		return step;
	}

	public boolean restoreHitPoints() {
		int maxHp = getMaxHitpoints();
		if (getHitpoints() > maxHp) {
			ifPlayer(player -> {
				if (player.getPrayer().usingPrayer(1, 5) && RandomUtils.inclusive(100) <= 15)
					return;
			});
			setHitpoints(getHitpoints() - 1);
			return true;
		} else if (getHitpoints() < maxHp) {
			setHitpoints(getHitpoints() + 1);
			ifPlayer(player -> {
				if (true) {
					if (player.getPrayer().usingPrayer(0, 9))
						restoreHitPoints();
					if (player.getResting() != 0)
						restoreHitPoints();
					player.getInterfaceManager().refreshHitPoints();
				}
				if (player.getPrayer().usingPrayer(0, 9) && getHitpoints() < maxHp)
					setHitpoints(getHitpoints() + 1);
				else if (player.getPrayer().usingPrayer(0, 26) && getHitpoints() < maxHp)
					setHitpoints(getHitpoints() + (getHitpoints() + 4 > maxHp ? maxHp - getHitpoints() : 4));
			});
			return true;
		}
		return false;
	}

	public boolean needMasksUpdate() {
		if (isPlayer())
			return (toPlayer().getTemporaryMovementType() != -1)
					|| (toPlayer().isUpdateMovementType()) || nextFaceEntity != -2 || nextAnimation != null || nextGraphics1 != null || nextGraphics2 != null
							|| nextGraphics3 != null || nextGraphics4 != null
							|| (nextWalkDirection == -1 && nextFaceWorldTile != null) || !nextHits.isEmpty()
							|| nextForceMovement != null || nextForceTalk != null;
		if (isNPC())
			return (toNPC().getNextTransformation() != null)|| nextFaceEntity != -2 || nextAnimation != null || nextGraphics1 != null || nextGraphics2 != null
					|| nextGraphics3 != null || nextGraphics4 != null
					|| (nextWalkDirection == -1 && nextFaceWorldTile != null) || !nextHits.isEmpty()
					|| nextForceMovement != null || nextForceTalk != null;
		return false;
	}

	/**
	 * The flag determining if this entity is dead.
	 */
	private transient boolean dead;

	public void resetMasks() {
		nextAnimation = null;
		nextGraphics1 = null;
		nextGraphics2 = null;
		nextGraphics3 = null;
		nextGraphics4 = null;
		if (nextWalkDirection == -1)
			nextFaceWorldTile = null;
		nextForceMovement = null;
		nextForceTalk = null;
		nextFaceEntity = -2;
		nextHits.clear();
		ifPlayer(player -> {
			player.setTemporaryMovementType((byte) -1);
			player.setUpdateMovementType(false);
			if (!player.isClientLoadedMapRegion()) {
				player.setClientLoadedMapRegion(true);
				World.getRegion(getRegionId()).refreshSpawnedObjects(player);
				World.getRegion(getRegionId()).refreshSpawnedItems(player);
			}
		});
		ifNpc(npc -> npc.setNextTransformation(null));
	}

	public int getMaxHitpoints() {
		return isNPC() ? toNPC().getCombatDefinitions().getHitpoints() : toPlayer().getSkills().getLevel(Skills.HITPOINTS) * 10;
	}

	public void processEntityUpdate() {
		processMovement();
		processReceivedHits();
		processReceivedDamage();
	}

	public void processEntity() { }

	public void loadMapRegions() {
		getMapRegionsIds().clear();
		setAtDynamicRegion(false);
		int chunkX = getChunkX();
		int chunkY = getChunkY();
		int mapHash = GameConstants.MAP_SIZES[getMapSize()] >> 4;
		int minRegionX = (chunkX - mapHash) / 8;
		int minRegionY = (chunkY - mapHash) / 8;
		for (int xCalc = minRegionX < 0 ? 0 : minRegionX; xCalc <= ((chunkX + mapHash) / 8); xCalc++)
			for (int yCalc = minRegionY < 0 ? 0 : minRegionY; yCalc <= ((chunkY + mapHash) / 8); yCalc++) {
				int regionId = yCalc + (xCalc << 8);
				if (World.getRegion(regionId, isPlayer()) instanceof DynamicRegion)
					setAtDynamicRegion(true);
				getMapRegionsIds().add(regionId);
			}
		setLastLoadedMapRegionTile(new WorldTile(this));
		ifPlayer(player -> {
			boolean wasAtDynamicRegion = isAtDynamicRegion();
			player.setClientLoadedMapRegion(false);
			if (isAtDynamicRegion()) {
				player.getPackets().sendDynamicGameScene(!player.isStarted());
				if (!wasAtDynamicRegion)
					player.getLocalNPCUpdate().reset();
			} else {
				player.getPackets().sendGameScene(!player.isStarted());
				if (wasAtDynamicRegion)
					player.getLocalNPCUpdate().reset();
			}
			player.getDetails().setForceNextMapLoadRefresh(false);
		});
	}

	public void setMapSize(int size) {
		setMapSize(size);
		loadMapRegions();
	}

	public void setNextAnimation(Animation nextAnimation) {
		if (nextAnimation != null && nextAnimation.getIds()[0] >= 0)
			lastAnimationEnd = Utility.currentTimeMillis()
					+ AnimationDefinitions.getAnimationDefinitions(nextAnimation.getIds()[0]).getEmoteTime();
		this.nextAnimation = nextAnimation;
	}

	public void setNextAnimationNoPriority(Animation nextAnimation) {
		if (getLastAnimationEnd() > Utility.currentTimeMillis())
			return;
		setNextAnimation(nextAnimation);
	}

	public void setNextGraphics(Graphics nextGraphics) {
		if (nextGraphics == null) {
			if (getNextGraphics4() != null)
				setNextGraphics4(null);
			else if (getNextGraphics3() != null)
				setNextGraphics3(null);
			else if (getNextGraphics2() != null)
				setNextGraphics2(null);
			else
				setNextGraphics1(null);
		} else {
			if (nextGraphics.equals(getNextGraphics1()) || nextGraphics.equals(getNextGraphics2())
					|| nextGraphics.equals(getNextGraphics3()) || nextGraphics.equals(getNextGraphics4()))
				return;
			if (getNextGraphics1() == null)
				setNextGraphics1(nextGraphics);
			else if (getNextGraphics2() == null)
				setNextGraphics2(nextGraphics);
			else if (getNextGraphics3() == null)
				setNextGraphics3(nextGraphics);
			else
				setNextGraphics4(nextGraphics);
		}
	}

	public void setNextFaceWorldTile(WorldTile nextFaceWorldTile) {
		if (nextFaceWorldTile.getX() == getX()
				&& nextFaceWorldTile.getY() == getY())
			return;
		this.nextFaceWorldTile = nextFaceWorldTile;
		if (nextWorldTile != null)
			direction = Utility.getFaceDirection(nextFaceWorldTile.getX()
					- nextWorldTile.getX(), nextFaceWorldTile.getY()
					- nextWorldTile.getY());
		else
			direction = Utility.getFaceDirection(nextFaceWorldTile.getX()
					- getX(), nextFaceWorldTile.getY() - getY());
	}

	public int getSize() {
		return isNPC() ? toNPC().getDefinitions().getSize() : toPlayer().getAppearance().getSize();
	}

	public void cancelFaceEntityNoCheck() {
		setLastFaceEntity(-1);
		nextFaceEntity = -2;
	}

	public void setNextFaceEntity(Entity entity) {
		if (entity == null) {
			nextFaceEntity = -1;
			setLastFaceEntity(-1);
		} else {
			nextFaceEntity = entity.getClientIndex();
			setLastFaceEntity(getNextFaceEntity());
		}
	}

	public double getMagePrayerMultiplier() {
		return isPlayer() ? 0.6 : 0;
	}

	public double getRangePrayerMultiplier() {
		return isPlayer() ? 0.6 : 0;
	}

	public double getMeleePrayerMultiplier() {
		return isPlayer() ? 0.6 : 0;
	}

	public void checkMultiArea() {
		setMultiArea(isForceMultiArea() ? true : World.isMultiArea(this));
		ifPlayer(player -> {
			if (!player.isStarted())
				return;
			boolean isAtMultiArea = player.isForceMultiArea() ? true : World.isMultiArea(player);
			if (isAtMultiArea && !player.isMultiArea()) {
				player.setMultiArea(isAtMultiArea);
				player.getPackets().sendGlobalConfig(616, 1);
			} else if (!isAtMultiArea && player.isMultiArea()) {
				player.setMultiArea(isAtMultiArea);
				player.getPackets().sendGlobalConfig(616, 0);
			}
		});
	}
	
	public void faceObject(GameObject object) {
		ObjectDefinitions objectDef = object.getDefinitions();
		setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(
				objectDef.getSizeX(), objectDef.getSizeY(),
				object.getRotation()), object.getCoordFaceY(
				objectDef.getSizeX(), objectDef.getSizeY(),
				object.getRotation()), object.getPlane()));
	}

	public void setForceMultiArea(boolean forceMultiArea) {
		setForceMultiArea(forceMultiArea);
		checkMultiArea();
	}

	public void playSound(int soundId, int type) {
		for (int regionId : getMapRegionsIds()) {
			ObjectArrayList<Short> playerIndexes = World.getRegion(regionId).getPlayersIndexes();
			if (playerIndexes != null) {
				World.players().filter(p -> !withinDistance(p))
						.forEach(p -> p.getPackets().sendSound(soundId, 0, type));
			}
		}
	}

	public boolean addWalkStepsInteract(int destX, int destY, int maxStepsCount, int size, boolean calculate) {
		return addWalkStepsInteract(destX, destY, maxStepsCount, size, size, calculate);
	}

	/*
	 * return added all steps
	 */
	public boolean addWalkStepsInteract(final int destX, final int destY, int maxStepsCount, int sizeX, int sizeY,
			boolean calculate) {
		int[] lastTile = getLastWalkTile();
		int myX = lastTile[0];
		int myY = lastTile[1];
		int stepCount = 0;
		while (true) {
			stepCount++;
			int myRealX = myX;
			int myRealY = myY;

			if (myX < destX)
				myX++;
			else if (myX > destX)
				myX--;
			if (myY < destY)
				myY++;
			else if (myY > destY)
				myY--;
			if ((isNPC() && !canWalkNPC(myX, myY)) || !addWalkStep(myX, myY, lastTile[0], lastTile[1], true)) {
				if (!calculate)
					return false;
				myX = myRealX;
				myY = myRealY;
				int[] myT = calculatedStep(myRealX, myRealY, destX, destY, lastTile[0], lastTile[1], sizeX, sizeY);
				if (myT == null)
					return false;
				myX = myT[0];
				myY = myT[1];
			}
			int distanceX = myX - destX;
			int distanceY = myY - destY;
			if (!(distanceX > sizeX || distanceX < -1 || distanceY > sizeY || distanceY < -1))
				return true;
			if (stepCount == maxStepsCount)
				return true;
			lastTile[0] = myX;
			lastTile[1] = myY;
			if (lastTile[0] == destX && lastTile[1] == destY)
				return true;
		}
	}

	/**
	 * Determines if this entity is poisoned.
	 * 
	 * @return {@code true} if this entity is poisoned, {@code false} otherwise.
	 */
	public final boolean isPoisoned() {
		return poisonDamage.get() > 0;
	}

	/**
	 * Applies poison with an intensity of {@code type} to the entity.
	 * 
	 * @param type the poison type to apply.
	 */
	public void poison(PoisonType type) {
		poisonType = type;
		if (isPlayer()) {
			Player player = (Player) this;
			player.getPackets().sendGameMessage("You have been poisoned!");
		}
		Combat.effect(this, CombatEffectType.POISON);
	}

	public final void updateEntityRegion(Entity entity) {
		if (entity.isFinished()) {
			if (isPlayer())
				World.getRegion(entity.getLastRegionId()).removePlayerIndex((short) entity.getIndex());
			else
				World.getRegion(entity.getLastRegionId()).removeNPCIndex(entity.getIndex());
			return;
		}
		short regionId = (short) entity.getRegionId();
		if (entity.getLastRegionId() != regionId) {
			ifPlayer(player -> {
				if (entity.getLastRegionId() > 0)
					World.getRegion(entity.getLastRegionId()).removePlayerIndex((short) entity.getIndex());
				Region region = World.getRegion(regionId);
				region.addPlayerIndex((short) entity.getIndex());
				int musicId = region.getRandomMusicId();
				if (musicId != -1)
					player.getMusicsManager().checkMusic(musicId);
				player.getMapZoneManager().executeVoid(player, controller -> controller.moved(player));
				if (player.isStarted())
					checkControlersAtMove(player);
			});
			ifNpc(npc -> {
				if (entity.getLastRegionId() > 0)
					World.getRegion(entity.getLastRegionId()).removeNPCIndex(entity.getIndex());
				World.getRegion(regionId).addNPCIndex((short) entity.getIndex());
			});
			entity.checkMultiArea();
			entity.setLastRegionId(regionId);
		} else {
			ifPlayer(player -> {
				if (player.isStarted())
					checkControlersAtMove(player);
			});
			entity.checkMultiArea();
		}
	}
	
	private static void checkControlersAtMove(Player player) {
		if (WildernessMapZone.isAtWild(player) && !player.getMapZoneManager().getMapZone(player).isPresent())
			player.getMapZoneManager().submitMapZone(player, new WildernessMapZone());
		else
			player.getCurrentMapZone().ifPresent(zone -> zone.moved(player));
	}

	public final boolean isPvpArea(WorldTile tile) {
		return WildernessMapZone.isAtWild(tile);
	}

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final Player target = (Player) this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
		target.getPrayer().drainPrayer(hit.getDamage() / 5);
		World.get().submit(new Task(1) {
			@Override
			protected void execute() {
				setNextGraphics(Graphic.SOULSPLIT);
				if (hit.getDamage() > 0)
					World.sendProjectile(target, user, 2263, 11, 11, 20, 5, 0, 0);
				this.cancel();
			}
		});
	}

	public void safeForceMoveTile(WorldTile desination) {
		if (World.isFloorFree(desination.getPlane(), desination.getX(), desination.getY()))
			IntStream.range(0, 3).forEach(count -> setNextWorldTile(new WorldTile(desination, count)));
	}

	/**
	 * The type of node that this node is.
	 */
	private final EntityType type;

	/**
	 * Determines if this entity is a {@link Player}.
	 * 
	 * @return {@code true} if this entity is a {@link Player}, {@code false}
	 *         otherwise.
	 */
	public final boolean isPlayer() {
		return getType() == EntityType.PLAYER;
	}

	/**
	 * Executes the specified action if the underlying node is a player.
	 * 
	 * @param action the action to execute.
	 */
	public final void ifPlayer(Consumer<Player> action) {
		if (!this.isPlayer()) {
			return;
		}
		action.accept(this.toPlayer());
	}

	/**
	 * Casts the {@link Actor} to a {@link Player}.
	 * 
	 * @return an instance of this {@link Actor} as a {@link Player}.
	 */
	public final Player toPlayer() {
		Preconditions.checkArgument(isPlayer(), "Cannot cast this entity to player.");
		return (Player) this;
	}

	/**
	 * Determines if this entity is a {@link Mob}.
	 * 
	 * @return {@code true} if this entity is a {@link Mob}, {@code false}
	 *         otherwise.
	 */
	public final boolean isNPC() {
		return getType() == EntityType.NPC;
	}

	/**
	 * Executes the specified action if the underlying node is a player.
	 * 
	 * @param action the action to execute.
	 */
	public final void ifNpc(Consumer<NPC> action) {
		if (!this.isNPC())
			return;
		action.accept(this.toNPC());
	}

	/**
	 * Casts the {@link Actor} to a {@link Mob}.
	 * 
	 * @return an instance of this {@link Actor} as a {@link Mob}.
	 */
	public final NPC toNPC() {
		Preconditions.checkArgument(isNPC(), "Cannot cast this entity to npc.");
		return (NPC) this;
	}

	// used for update
	private transient LocalPlayerUpdate localPlayerUpdate;
	private transient LocalNPCUpdate localNPCUpdate;
	
	/**
	 * Sends a queued Task with Consumer action support
	 * @param delay
	 * @param entity
	 */
	public void task(int delay, Consumer<Entity> entity) {
		Entity consumer = this;
		new Task(delay, false) {
			@Override
			protected void execute() {
				entity.accept(consumer);
				cancel();
			}
		}.submit();
	}
	
	/**
	 * Sends a queued Task with Consumer action support
	 * @param delay
	 * @param entity
	 */
	public void task(Consumer<Entity> entity) {
		Entity consumer = this;
		new Task(0, true) {
			@Override
			protected void execute() {
				entity.accept(consumer);
				cancel();
			}
		}.submit();
	}
	
	/**
	 * Updates a Player's Run (movement) state 
	 */
	public void setRunState(boolean run) {
		setRun(run);
		ifPlayer(player -> {
			player.setUpdateMovementType(true);
			if (run != isRun()) {
				player.getInterfaceManager().sendRunButtonConfig();
			}
		});
	}
	
	public void clearEffects() {
		effects = new HashMap<>();
	}

	public boolean hasEffect(Effect effect) {
		return effects != null && effects.containsKey(effect);
	}

	public void addEffect(Effect effect, long ticks) {
		if (effects == null)
			effects = new HashMap<>();
		effects.put(effect, ticks);
		effect.apply(this);
		effect.tick(this, ticks);
	}

	public void removeEffect(Effect effect) {
		ifPlayer(p -> {
			if (effect.sendWarnings())
				p.getPackets().sendGameMessage(effect.getExpiryMessage());
		});
		effects.remove(effect);
		effect.expire(this);
	}

	public void removeEffects(Effect... effects) {
		for (Effect e : effects)
			removeEffect(e);
	}

	protected void processEffects() {
		if (effects == null)
			return;
		Set<Effect> expired = new HashSet<>();
		for (Effect effect : effects.keySet()) {
			long time = effects.get(effect);
			time--;
			effect.tick(this, time);
			if (isPlayer()) {
				if (time == 50 && effect.sendWarnings())
					toPlayer().getPackets().sendGameMessage(effect.get30SecWarning());
			}
			
			if (time <= 0) {
				if (isPlayer()) {
					if (effect.sendWarnings())
						toPlayer().getPackets().sendGameMessage(effect.getExpiryMessage());
				}
				expired.add(effect);
			} else
				effects.put(effect, time);
		}
		for (Effect e : expired) {
			effects.remove(e);
			e.expire(this);
		}
	}
}