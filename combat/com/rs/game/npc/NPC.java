package com.rs.game.npc;

import java.util.Optional;
import java.util.stream.IntStream;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.constants.Graphic;
import com.rs.game.Entity;
import com.rs.game.EntityType;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.map.zone.impl.WildernessMapZone;
import com.rs.game.movement.route.RouteFinder;
import com.rs.game.movement.route.strategy.DumbRouteFinder;
import com.rs.game.movement.route.strategy.FixedTileStrategy;
import com.rs.game.npc.combat.NPCCombat;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.drops.Drop;
import com.rs.game.npc.drops.DropSets;
import com.rs.game.npc.drops.DropTable;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.global.GenericNPCDispatcher;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.task.Task;
import com.rs.utilities.Colors;
import com.rs.utilities.Direction;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.Utility;
import com.rs.utilities.loaders.CharmDrop;
import com.rs.utilities.loaders.NPCBonuses;
import com.rs.utilities.loaders.NPCCombatDefinitionsL;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;
import skills.herblore.HerbicideHandler;
import skills.prayer.BoneCrusher;
import skills.prayer.DungeoneeringNecklaces;
import skills.runecrafting.RunecraftingPouchDrop;

@Data
@EqualsAndHashCode(callSuper = false)
@NonNull
public class NPC extends Entity {

	public static byte NORMAL_WALK = 0x2, WATER_WALK = 0x4, FLY_WALK = 0x8;

	private int id;
	private WorldTile respawnTile;
	private boolean canBeAttackFromOutOfArea;
	private byte walkType;
	private double[] bonuses;
	private boolean spawned;
	private transient NPCCombat combat;
	public WorldTile forceWalk;
	
	private int combatLevel;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private short capDamage;
	private short lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private byte forceTargetDistance;
	private int forceAgressiveDistance;
	private boolean forceFollowClose;
	private boolean forceMultiAttacked;
	private boolean noDistanceCheck;
	private boolean intelligentRouteFinder;
	private transient GenericNPCDispatcher genericNPC;

	// npc masks
	private transient Transformation nextTransformation;

	public NPC(int id, WorldTile tile, Direction direction) {
		this(id, tile, direction, false, false);
	}
	
	public NPC(int id, WorldTile tile, Direction direction, boolean canBeAttackFromOutOfArea) {
		this(id, tile, direction, canBeAttackFromOutOfArea, false);
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, WorldTile tile, Direction direction, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile, EntityType.NPC);
		setId(id);
		setRespawnTile(new WorldTile(tile));
		setCanBeAttackFromOutOfArea(canBeAttackFromOutOfArea);
		setSpawned(spawned);
		setHitpoints(getMaxHitpoints());
		setDirection(direction == null ? getRespawnDirection() : direction.getAngle());
		setWalkType((byte) (getDefinitions().walkMask & 0x2));
		setCombat(new NPCCombat(this));
		setCapDamage((short) -1);
		setLureDelay((short) 12000);
		setGenericNPC(new GenericNPCDispatcher());
		getGenericNPC().setAttributes(this);
		combatLevel = -1;
		setBonuses();
		initEntity();
		World.addNPC(this);
		updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}

	public void setNextNPCTransformation(int id) {
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
		if (!combat.process()) {
			if (!isForceWalking()) {
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getMovement().getFreezeDelay() < Utility.currentTimeMillis()) {
							if (!hasWalkSteps() && (getWalkType() & NORMAL_WALK) != 0) {
								boolean can = Math.random() > 0.9;
                                if (can) {
                                    int moveX = RandomUtility.random(getDefinitions().hasAttackOption() ? 4 : 2, getDefinitions().hasAttackOption() ? 8 : 4);
                                    int moveY = RandomUtility.random(getDefinitions().hasAttackOption() ? 4 : 2, getDefinitions().hasAttackOption() ? 8 : 4);
                                    if (RandomUtility.random(2) == 0)
                                        moveX = -moveX;
                                    if (RandomUtility.random(2) == 0)
                                        moveY = -moveY;
                                    resetWalkSteps();
                                    DumbRouteFinder.addDumbPathfinderSteps(this, respawnTile.transform(moveX, moveY, 0), getDefinitions().hasAttackOption() ? 7 : 3);
                                    if (Utility.getDistance(this, respawnTile) > 3 && !getDefinitions().hasAttackOption()) {
                                        DumbRouteFinder.addDumbPathfinderSteps(this, respawnTile, 12);
                                    }
                                }
							}
						}
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
						setNextWorldTile(new WorldTile(getForceWalk()));
						setForceWalk(null);
					}
				} else
					setForceWalk(null);
			}
		}
	}

	@Override
	public void processEntity() {
		processNPC();
		getGenericNPC().process(this);
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
			setNextWorldTile(getRespawnTile());
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
		Entity killer = getMostDamageReceivedSourcePlayer();
		getGenericNPC().sendDeath(killer.toPlayer(), Optional.of(this), () -> World.get().submit(new NPCDeath(this)));
	}

	@SneakyThrows(Exception.class)
	public void drop() {
		Player killer = getMostDamageReceivedSourcePlayer();
		if (killer == null)
			return;
		killer.toPlayer().getDetails().getStatistics().addStatistic("Total_NPC_Kills");
        killer.toPlayer().getDetails().getStatistics().addStatistic(getDefinitions().getName() + "_Kills");
		Item[] drops = DropTable.calculateDrops(killer, DropSets.getDropSet(id));

		if (killer.toPlayer().getDetails().getToggleLootShare().isFalse() || killer.toPlayer().getCurrentFriendChat() == null) {
			for (Item item : drops)
				sendDrop(killer, item);
		} else {
			ObjectArrayList<Player> players = FriendChatsManager.getLootSharingPeople(killer.toPlayer());
			Player luckyPlayer = players.get(RandomUtility.random(players.size()));
			if (luckyPlayer != null || players != null || players.size() > 0) {
				for (Item item : drops) {
					sendDrop(luckyPlayer, item);
					if (luckyPlayer == killer) {
						luckyPlayer.getPackets().sendGameMessage(Colors.color(Colors.red,
								"You received: " + item.getAmount() + " " + item.getName() + "."));
					}
					for (Player ccm : players) {
						if (ccm != luckyPlayer) {
							ccm.getPackets().sendGameMessage(luckyPlayer.getDisplayName() + " received "
									+ item.getAmount() + " " + item.getName() + ".");
							ccm.getPackets().sendGameMessage("Your chance of receiving loot has improved.");
							if (item.getDefinitions().getValue() >= 5000) {
								ccm.getPackets()
										.sendGameMessage("You recieved "
												+ (Utility.getFormattedNumber(
														item.getDefinitions().getValue() / players.size()))
												+ " gold as your split of this drop: " + item.getAmount() + " x "
												+ item.getName() + ".");
								FloorItem.updateGroundItem(
										new Item(995, item.getDefinitions().getValue() / players.size()),
										new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()),
										killer.toPlayer(), 60, 0);
							}
							break;
						}
						
					}
					
				}
			}
		}
		DropTable charm = CharmDrop.getCharmDrop(NPCDefinitions.getNPCDefinitions(getId()).getName().toLowerCase());
		if (charm != null)
			for (Drop d : charm.getDrops()) {
				killer.toPlayer().getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(d.getId()).getName() + "_Charms_Found").addStatistic("Charms_Found");
				sendDrop(killer, d.toItem());
			}
		
		Item[] clues = DropTable.calculateDrops(killer.toPlayer(), NPCClueDrops.rollClues(id));
        for (Item item : clues) {
        	killer.toPlayer().getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(item.getId()).getName() + "_Clues_Found").addStatistic("Clues_Found");
        	FloorItem.updateGroundItem(item, new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()), killer.toPlayer(), 60, 0);
        }
	}

	public void sendDrop(Player player, Item item) {
		DungeoneeringNecklaces.handleNecklaces(player, item.getId());
		int[] BRAWLERS = {13845, 13846, 13847, 13848, 13849, 13850, 13851, 13852, 13853, 13854, 13855, 13856, 13857};
		if (WildernessMapZone.isAtWild(player) && RandomUtility.random(300) == 0)
			FloorItem.addGroundItem(new Item(RandomUtility.random(BRAWLERS)), player, player, true, 60);
		
		if (id == 2263 || id == 2264 || id == 2265)
            RunecraftingPouchDrop.sendPouchDrop(player, this);
		if (BoneCrusher.handleDrop(player, item))
			return;
		 if (HerbicideHandler.handleDrop(player, item))
             return;
		if (IntStream.of(8832, 8833,8834).anyMatch(id -> getId() == id))
			FloorItem.addGroundItem(item, player, player, true, 60);
		else
			FloorItem.addGroundItem(item, new WorldTile(getCoordFaceX(getSize()), getCoordFaceY(getSize()), getPlane()), player, true, 60);
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
		getGenericNPC().setTarget(this, entity);
		getCombat().setTarget(entity);
		setLastAttackedByTarget(Utility.currentTimeMillis());
	}

	public void forceWalkRespawnTile() {
		setForceWalk(getRespawnTile());
	}

	public void setForceWalk(WorldTile tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	  public ObjectArrayList<Entity> getPossibleTargets(boolean checkNPCs, boolean checkPlayers) {
	        int size = getSize();
	        ObjectArrayList<Entity> possibleTarget = new ObjectArrayList<Entity>();
	        int attackStyle = getCombatDefinitions().getAttackStyle();
	        for (int regionId : getMapRegionsIds()) {
	            if (checkPlayers) {
	            	ObjectArrayList<Short> playerIndexes = World.getRegion(regionId).getPlayersIndexes();
	                if (playerIndexes != null) {
	                    for (int playerIndex : playerIndexes) {
	                        Player player = World.getPlayers().get(playerIndex);
	                        if (player == null || player.isDead() || player.isFinished() || !player.isRunning()
	                                || player.getAppearance().isHidePlayer()
	                                || !Utility.isOnRange(getX(), getY(), size, player.getX(), player.getY(),
	                                player.getSize(),
	                                forceAgressiveDistance != 0 ? forceAgressiveDistance
	                                        : isNoDistanceCheck() ? 64
	                                        : (attackStyle == NPCCombatDefinitions.MELEE
	                                        || attackStyle == NPCCombatDefinitions.SPECIAL2)
	                                        ? getSize()
	                                        : 8)
	                                || (!forceMultiAttacked && (!isMultiArea() || !player.isMultiArea())
	                                && (player.getAttackedBy() != this
	                                && (player.getAttackedByDelay() > Utility.currentTimeMillis())))
	                                || !clipedProjectile(player,
	                                (attackStyle == NPCCombatDefinitions.RANGE
	                                        || attackStyle == NPCCombatDefinitions.MAGE ? false : true))
	                                || !getDefinitions().hasAttackOption()
	                                || (!forceAgressive && !WildernessMapZone.isAtWild(this)
	                                && player.getSkills().getCombatLevelWithSummoning() >= getDefinitions().getCombatLevel() * 2)) {
	                            continue;
	                        }
	                        possibleTarget.add(player);
	                    }
	                }
	            }
	            if (checkNPCs) {
	            	ObjectArrayList<Short> npcsIndexes = World.getRegion(regionId).getNpcsIndexes();
	                if (npcsIndexes != null) {
	                    for (int npcIndex : npcsIndexes) {
	                        NPC npc = World.getNPCs().get(npcIndex);
	                        if (npc == null || npc == this || npc.isDead() || npc.isFinished()
	                                || !Utility.isOnRange(getX(), getY(), size, npc.getX(), npc.getY(), npc.getSize(),
	                                forceAgressiveDistance > 0 ? forceAgressiveDistance : getSize())
	                                || !npc.getDefinitions().hasAttackOption()
	                                || ((!isMultiArea() || !npc.isMultiArea()) && npc.getAttackedBy() != this
	                                && npc.getAttackedByDelay() > Utility.currentTimeMillis())
	                                || !clipedProjectile(npc, false)) {
	                            continue;
	                        }
	                        possibleTarget.add(npc);
	                    }
	                }
	            }
	        }
	        return possibleTarget;
	    }

	public ObjectArrayList<Entity> getPossibleTargets() {
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
			Entity target = possibleTarget.get(RandomUtility.inclusive(possibleTarget.size() - 1));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Utility.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract)
			getCombat().reset();
	}

	@Override
	public String toString() {
		return getDefinitions().getName() + " - " + id + " - " + getX() + " " + getY() + " " + getPlane();
	}

	public void transformIntoNPC(int id) {
		setId(id);
		setNextTransformation(new Transformation(id));
	}

	/**
	 * @param id
	 * @param tile
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 * @return
	 */
	public static final NPC spawnNPC(int id, WorldTile tile, Direction direction, boolean canBeAttackFromOutOfArea,
			boolean spawned) {

		NPC npcType = null;
		npcType = new NPC(id, tile, direction, canBeAttackFromOutOfArea, spawned);
		return npcType;
	}

	public static final NPC spawnNPC(int id, WorldTile tile, Direction direction, boolean canBeAttackFromOutOfArea) {
		return spawnNPC(id, tile, direction, canBeAttackFromOutOfArea, false);
	}
	
	/**
	 * A simple way of executing an action based on the option.
	 * can also force assign option actions as well.
	 * @param option
	 * @param searchedOption
	 * @param action
	 */
	public void doAction(int option, String searchedOption, Runnable action) {
		if (getDefinitions().options[option] != null && getDefinitions().options[option].toLowerCase().contains(searchedOption))
			action.run();
	}
	public void doAction(int option, int id, String searchedOption, Runnable action) {
		if (id == getDefinitions().getId() && getDefinitions().options[option] != null && getDefinitions().options[option].toLowerCase().contains(searchedOption))
			action.run();
	}

	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : getDefinitions().combatLevel;
	}
	
	public void setBonuses() {
		bonuses = getDefaultBonuses();
	}
	
	public double[] getDefaultBonuses() {
		double[] b = NPCBonuses.getBonuses(id);
		if (b == null) {
			b = new double[10];
			int level = getCombatLevel();
			for (int i = 0; i < b.length; i++) {
				b[i] = i >= 5 ?  level : (level / 2);
			}
		} else {
			b = b.clone();
		}
		return b;
	}
	
	
	public void setNPC(int id) {
		this.id = id;
		setBonuses();
	}
	
    public WorldTile getWorldTile() {
        return new WorldTile(getX(), getY(), getPlane());
    }
}