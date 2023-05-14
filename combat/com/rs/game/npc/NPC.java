package com.rs.game.npc;

import java.util.Optional;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.constants.Graphic;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.game.Entity;
import com.rs.game.EntityType;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.combat.NPCCombat;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.global.GenericNPCDispatcher;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.route.RouteFinder;
import com.rs.game.route.strategy.FixedTileStrategy;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Utility;
import com.rs.utilities.loaders.NPCBonuses;
import com.rs.utilities.loaders.NPCCombatDefinitionsL;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;

@Data
@EqualsAndHashCode(callSuper = false)
@NonNull
public class NPC extends Entity {

	public static byte NORMAL_WALK = 0x2, WATER_WALK = 0x4, FLY_WALK = 0x8;

	private short id;
	private WorldTile respawnTile;
	private byte mapAreaNameHash;
	private boolean canBeAttackFromOutOfArea;
	private byte walkType;
	private short[] bonuses = NPCBonuses.getBonuses(id); // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab // def,
															// blahblah till 9
	private boolean spawned;
	private transient NPCCombat combat;
	public WorldTile forceWalk;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private short capDamage;
	private short lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private byte forceTargetDistance;
	private boolean forceFollowClose;
	private boolean forceMultiAttacked;
	private boolean noDistanceCheck;
	private boolean intelligentRouteFinder;
	private transient GenericNPCDispatcher genericNPC;

	// npc masks
	private transient Transformation nextTransformation;

	public NPC(short id, WorldTile tile, byte mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	public NPC(short id, WorldTile tile) {
		super(tile, EntityType.NPC);
		new NPC(id, tile, (byte) -1, false);
	}

	/*
	 * creates and adds npc
	 */
	public NPC(short id, WorldTile tile, byte mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile, EntityType.NPC);
		setId(id);
		setRespawnTile(new WorldTile(tile));
		setMapAreaNameHash(mapAreaNameHash);
		setCanBeAttackFromOutOfArea(canBeAttackFromOutOfArea);
		setSpawned(spawned);
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setWalkType(getDefinitions().getWalkMask());
		setCombat(new NPCCombat(this));
		setCapDamage((short) -1);
		setLureDelay((short) 12000);
		initEntity();
		World.addNPC(this);
		updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
		setGenericNPC(new GenericNPCDispatcher());
		getGenericNPC().setAttributes(this);
	}

	public void setNextNPCTransformation(short id) {
		setId(id);
		setNextTransformation(new Transformation(id));
	}

	public NPCDefinitions getDefinitions() {
		return NPCDefinitions.getNPCDefinitions(getId());
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NPCCombatDefinitionsL.getNPCCombatDefinitions(getId());
	}

	public void processNPC() {
		if (isDead() || getMovement().isLocked())
			return;
		if (!getCombat().process() || !isForceWalking() || !isCantInteract() || !checkAgressivity()) {
			if (getMovement().getFreezeDelay() < Utility.currentTimeMillis()) {
				if (!hasWalkSteps() && (getWalkType() & NORMAL_WALK) != 0) {
					boolean can = false;
					if (RandomUtils.inclusive(2) == 0) {
						can = RandomUtils.percentageChance(30);
					}
					if (can) {
						int moveX = (int) Math.round(Math.random() * 10.0 - 5.0);
						int moveY = (int) Math.round(Math.random() * 10.0 - 5.0);
						resetWalkSteps();
						if (getMapAreaNameHash() != -1) {
							addWalkSteps(getX() + moveX, getY() + moveY, 5, (getWalkType() & FLY_WALK) == 0);
						} else
							addWalkSteps(getRespawnTile().getX() + moveX, getRespawnTile().getY() + moveY, 5,
									(getWalkType() & FLY_WALK) == 0);
					}
				}
			}
		}
		if (isForceWalking()) {
			if (getMovement().getFreezeDelay() < Utility.currentTimeMillis()) {
				if (getX() != getForceWalk().getX() || getY() != getForceWalk().getY()) {
					if (!hasWalkSteps()) {
						int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, getX(), getY(), getPlane(),
								getSize(), new FixedTileStrategy(getForceWalk().getX(), getForceWalk().getY()), true);
						int[] bufferX = RouteFinder.getLastPathBufferX();
						int[] bufferY = RouteFinder.getLastPathBufferY();
						for (int i = steps - 1; i >= 0; i--) {
							if (!addWalkSteps(bufferX[i], bufferY[i], 25, true))
								break;
						}
					}
					if (!hasWalkSteps()) {
						safeForceMoveTile(new WorldTile(getForceWalk()));
						setForceWalk(null);
					}
				} else
					setForceWalk(null);
			}
		}
	}

	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
//		getGenericNPC().process(this);
	}

	public byte getRespawnDirection() {
		NPCDefinitions definitions = getDefinitions();
		if (definitions.getAnInt853() << 32 != 0 && definitions.getRespawnDirection() > 0
				&& definitions.getRespawnDirection() <= 8)
			return (byte) ((4 + definitions.getRespawnDirection()) << 11);
		return 0;
	}

	public void sendSoulSplit(final Hit hit, final Entity user) {
		final NPC target = this;
		if (hit.getDamage() > 0)
			World.sendProjectile(user, this, 2263, 11, 11, 20, 5, 0, 0);
		user.heal(hit.getDamage() / 5);
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

	@Override
	public void handleIngoingHit(final Hit hit) {
		getCombatDefinitions().handleIngoingHit(this, hit);
		getGenericNPC().handleIngoingHit(this, hit);
	}

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		getCombat().reset();
		setForceWalk(null);
	}

	@Override
	public void deregister() {
		if (isFinished())
			return;
		setFinished(true);
		updateEntityRegion(this);
		World.removeNPC(this);
	}

	@SneakyThrows(Throwable.class)
	public void setRespawnTask() {
		getGenericNPC().setRespawnTask(this);
		if (!isFinished()) {
			reset();
			setLocation(getRespawnTile());
			deregister();
		}
		World.get().submit(new Task(getCombatDefinitions().getRespawnDelay()) {
			@Override
			protected void execute() {
				spawn();
				this.cancel();
			}
		});
	}

	public void spawn() {
		setFinished(false);
		World.addNPC(this);
		setLastRegionId((short) 0);
		updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}

	@Override
	public void sendDeath(Optional<Entity> source) {
		World.get().submit(new NPCDeath(this));
	}

	@SneakyThrows(Exception.class)
	public void drop() {
		Player killer = getMostDamageReceivedSourcePlayer();
		if (killer == null)
			return;
		DropManager.dropItems(killer, this);
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == getCombat().getTarget() && !(getCombat().getTarget() instanceof Familiar))
			setLastAttackedByTarget(Utility.currentTimeMillis());
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Utility.currentTimeMillis() - getLastAttackedByTarget() > getLureDelay();
	}

	public boolean isForceWalking() {
		return getForceWalk() != null;
	}

	public void setTarget(Entity entity) {
		if (isForceWalking())
			return;
		getCombat().setTarget(entity);
		setLastAttackedByTarget(Utility.currentTimeMillis());
	}

	public void forceWalkRespawnTile() {
		setForceWalk(getRespawnTile());
	}

	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		setNextWorldTile(tile);
	}

	public ObjectArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
		int size = getSize();
		int agroRatio = 32;
		ObjectArrayList<Entity> possibleTarget = new ObjectArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			if (checkPlayers) {
				ObjectArrayList<Short> playerIndexes = World.getRegion(regionId).getPlayersIndexes();
				if (playerIndexes != null) {
					for (int playerIndex : playerIndexes) {
						Player player = World.getPlayers().get(playerIndex);
						if (player.isDead() || player.isFinished() || !player.isRunning()
								|| player.getAppearance().isHidePlayer()
								|| !Utility.isOnRange(getX(), getY(), size, player.getX(), player.getY(),
										player.getSize(),
										getForceTargetDistance() > 0 ? getForceTargetDistance() : agroRatio)
								|| (!isForceMultiAttacked() && (!isMultiArea() || !player.isMultiArea())
										&& (player.getAttackedBy() != this
												&& (player.getAttackedByDelay() > Utility.currentTimeMillis()
														|| player.getFindTargetDelay() > Utility.currentTimeMillis())))
								|| !clipedProjectile(player, false)
								|| (!isForceAgressive() && !WildernessMapZone.isAtWild(this) && player.getSkills()
										.getCombatLevelWithSummoning() >= getDefinitions().getCombatLevel() * 2))
							continue;
						possibleTarget.add(player);
					}
				}
			}
			if (checkNPCs) {
				ObjectArrayList<Short> npcsIndexes = World.getRegion(regionId).getNpcsIndexes();
				if (npcsIndexes != null) {
					for (int npcIndex : npcsIndexes) {
						NPC npc = World.getNPCs().get(npcIndex);
						if (npc == this || npc.isDead() || npc.isFinished()
								|| !Utility.isOnRange(getX(), getY(), size, npc.getX(), npc.getY(), npc.getSize(),
										getForceTargetDistance() > 0 ? getForceTargetDistance() : agroRatio)
								|| !npc.getDefinitions().hasAttackOption()
								|| ((!isMultiArea() || !npc.isMultiArea()) && npc.getAttackedBy() != this
										&& npc.getAttackedByDelay() > Utility.currentTimeMillis())
								|| !clipedProjectile(npc, false))
							continue;
						possibleTarget.add(npc);
					}
				}
			}
		}
		return possibleTarget;
	}

	public ObjectArrayList<Entity> getPossibleTargets() {
		getGenericNPC().possibleTargets(this);
		return getPossibleTargets(false, true);
	}

	public boolean checkAgressivity() {
		if (getDefinitions().hasAttackOption()) {
			if (!isForceAgressive()) {
				NPCCombatDefinitions defs = getCombatDefinitions();
				if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
					return false;
			}
		}
		ObjectArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(RandomUtils.inclusive(possibleTarget.size() - 1));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utility.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public void setCantInteract(boolean cantInteract) {
		setCantInteract(cantInteract);
		if (cantInteract)
			getCombat().reset();
	}

	@Override
	public String toString() {
		return getDefinitions().getName() + " - " + id + " - " + getX() + " " + getY() + " " + getPlane();
	}

	public void transformIntoNPC(short id) {
		setId(id);
		setNextTransformation(new Transformation(id));
	}

	/**
	 * TODO: REDO ALL THIS TO THE NEW SYSTEM
	 * 
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 * @return
	 */
	public static final NPC spawnNPC(short id, WorldTile tile, byte mapAreaNameHash, boolean canBeAttackFromOutOfArea,
			boolean spawned) {

		NPC npcType = null;
		npcType = new NPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		npcType = new GenericNPCDispatcher().create(npcType);
		return npcType;
	}

	public static final NPC spawnNPC(short id, WorldTile tile, byte mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		return spawnNPC(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}
}