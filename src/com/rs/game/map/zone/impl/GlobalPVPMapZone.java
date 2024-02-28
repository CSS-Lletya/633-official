package com.rs.game.map.zone.impl;

import java.util.Optional;

import com.rs.constants.Sounds;
import com.rs.game.Entity;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.map.zone.MapZone;
import com.rs.game.npc.NPC;
import com.rs.game.player.Combat;
import com.rs.game.player.Player;
import com.rs.game.player.type.CombatEffectType;
import com.rs.utilities.Utility;

import skills.Skills;

public class GlobalPVPMapZone extends MapZone {

	public GlobalPVPMapZone() {
		super("Global PVP MapZone", MapZoneSafetyCondition.DANGEROUS, MapZoneType.NORMAL);
	}

	@Override
	public void start(Player player) {
		checkBoosts(player);
		sendInterfaces(player);
		moved(player);
	}

	@Override
	public boolean login(Player player) {
		start(player);
		return false;
	}

	@Override
	public boolean keepCombating(Player player,Entity target) {
		if (target.isNPC())
			return true;
		if (!canAttack(player, target))
			return false;
		if (target.getAttackedBy() != player && player.getAttackedBy() != target)
			Combat.effect(player, CombatEffectType.SKULL);
		if (player.getCombatDefinitions().getSpellId() <= 0
				&& Utility.inCircle(new WorldTile(3105, 3933, 0), target, 24)) {
			player.getPackets().sendGameMessage("You can only use magic in the arena.");
			return false;
		}
		return true;
	}

	@Override
	public boolean canAttack(Player player, Entity target) {
		if (target.isPlayer()) {
			Player p2 = (Player) target;
			if (player.getDetails().getCanPvp().isTrue() && p2.getDetails().getCanPvp().isFalse()) {
				player.getPackets().sendGameMessage("That player is not in the wilderness.");
				return false;
			}
			if (Math.abs(player.getSkills().getCombatLevel() - p2.getSkills().getCombatLevel()) > getWildLevel(player)) {
				player.getPackets().sendGameMessage("You can't attack " + p2.getDisplayName() + " - your level difference is too great.");
				return false;
			}
			if (canHit(player, target))
				return true;
			return false;
		}
		return true;
	}

	@Override
	public boolean canHit(Player player, Entity target) {
		if (target.isNPC())
			return true;
		Player p2 = (Player) target;
		if (Math.abs(player.getSkills().getCombatLevel() - p2.getSkills().getCombatLevel()) > getWildLevel(player))
			return false;
		return true;
	}

	@Override
	public boolean processMagicTeleport(Player player, WorldTile toTile) {
		if (getWildLevel(player) > 20) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getDetails().getTeleBlockDelay().get() > 0) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;

	}

	@Override
	public boolean processItemTeleport(Player player, WorldTile toTile) {
		if (getWildLevel(player) > 30) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getDetails().getTeleBlockDelay().get() > 0) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectTeleport(Player player, WorldTile toTile) {
		if (player.getDetails().getTeleBlockDelay().get() > 0) {
			player.getPackets().sendGameMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(Player player, final GameObject object) {
		if (object.getId() == 2557 || object.getId() == 65717) {
			player.getAudioManager().sendSound(Sounds.LOCKED);
			player.getPackets().sendGameMessage("It seems it is locked, maybe you should try something else.");
			return false;
		}
		if (player.getY() == 3962) {
			GateNorthOut(player, object);
			return true;
		} else if (player.getY() == 3958) {
			GateSouthOut(player, object);
			return true;
		} else if (player.getY() == 3963 || player.getY() == 3957) {
			player.getPackets().sendGameMessage("This gate is locked from the inside.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectClick2(Player player, GameObject object) {
		if (player.getSkills().getTrueLevel(Skills.THIEVING) < 60) {
			player.dialogue(d -> d.mes("You need atleast an level of 60 thieving to picklock this door."));
			return false;
		}
		if (!player.getInventory().containsItem(1523, 1)) {
			player.dialogue(d -> d.mes("You need an Lockpick to picklock this gate."));
			return false;
		}
		if (player.getY() == 3958 || player.getY() == 3962) {
			player.getPackets().sendGameMessage("You can't picklock from the inside.");
			return false;
		}
		return true;
	}
	@Override
	public void sendInterfaces(Player player) {
		if (isAtWild(player))
			showSkull(player);
	}

	@Override
	public void magicTeleported(Player player, int teleType) {
		if (!isAtWild(player.getNextWorldTile())) {
			player.getDetails().getCanPvp().setValue(false);
			removeIcon(player);
			player.setCurrentMapZone(Optional.empty());
		}
		player.getInterfaceManager().removeOverlay(false);
	}

	@Override
	public void moved(Player player) {
		boolean isAtWild = isAtWild(player);
		boolean isAtWildSafe = isAtWildSafe(player);
		if (!isAtWild(player) && !isAtWildSafe && !isAtWild && !showingSkull) {
			player.getDetails().getCanPvp().setValue(false);
			removeIcon(player);
			endMapZoneSession(player);
		} else if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.getDetails().getCanPvp().setValue(true);
			showSkull(player);
			player.getPackets().sendGlobalConfig(1000, player.getSkills().getCombatLevel() + player.getSkills().getSummoningCombatLevel());
			player.getAppearance().generateAppearenceData();
			checkBoosts(player);
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			removeIcon(player);
		} else if (!isAtWildSafe && !isAtWild) {
			player.getDetails().getCanPvp().setValue(false);
			removeIcon(player);
		}
	}
	
	@Override
	public boolean logout(Player player) {
		return false;
	}

	@Override
	public void forceClose(Player player) {
		removeIcon(player);
	}

	public static final boolean isAtWild(WorldTile tile) {
		return inPvP(tile) || (tile.getX() >= 3011 && tile.getX() <= 3132 && tile.getY() >= 10052 && tile.getY() <= 10175) // fortihrny
				// dungeon
				|| (tile.getX() >= 2940 && tile.getX() <= 3395 && tile.getY() >= 3525 && tile.getY() <= 4000)
				|| (tile.getX() >= 3264 && tile.getX() <= 3279 && tile.getY() >= 3279 && tile.getY() <= 3672)
				|| (tile.getX() >= 2756 && tile.getX() <= 2875 && tile.getY() >= 5512 && tile.getY() <= 5627)
				|| (tile.getX() >= 3158 && tile.getX() <= 3181 && tile.getY() >= 3679 && tile.getY() <= 3697)
				|| (tile.getX() >= 3280 && tile.getX() <= 3183 && tile.getY() >= 3885 && tile.getY() <= 3888)
				|| (tile.getX() >= 3012 && tile.getX() <= 3059 && tile.getY() >= 10303 && tile.getY() <= 10351);
	}

	public boolean isAtWildSafe(Player player) {
		player.getInterfaceManager().removeOverlay(false);
		return inSafeZone(player) || (player.getX() >= 2940 && player.getX() <= 3395 && player.getY() <= 3524 && player.getY() >= 3523);
	}

	public int getWildLevel(Player player) {
		if (player.getY() > 9900)
			return (player.getY() - 9912) / 8 + 1;
		return (player.getY() - 3520) / 8 + 1;
	}
	
	private boolean showingSkull;
	
	public static void checkBoosts(Player player) {
		boolean changed = false;
		int level = player.getSkills().getTrueLevel(Skills.ATTACK);
		int maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.ATTACK)) {
			player.getSkills().set(Skills.ATTACK, maxLevel);
			changed = true;
		}
		level = player.getSkills().getTrueLevel(Skills.STRENGTH);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.STRENGTH)) {
			player.getSkills().set(Skills.STRENGTH, maxLevel);
			changed = true;
		}
		level = player.getSkills().getTrueLevel(Skills.DEFENCE);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.DEFENCE)) {
			player.getSkills().set(Skills.DEFENCE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getTrueLevel(Skills.RANGE);
		maxLevel = (int) (level + 5 + (level * 0.1));
		if (maxLevel < player.getSkills().getLevel(Skills.RANGE)) {
			player.getSkills().set(Skills.RANGE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getTrueLevel(Skills.MAGIC);
		maxLevel = level + 5;
		if (maxLevel < player.getSkills().getLevel(Skills.MAGIC)) {
			player.getSkills().set(Skills.MAGIC, maxLevel);
			changed = true;
		}
		if (changed)
			player.getPackets().sendGameMessage("Your extreme potion bonus has been reduced.");
	}
	
	public void removeIcon(Player player) {
		if (showingSkull) {
			showingSkull = false;
			player.getDetails().getCanPvp().setValue(false);
			player.setCurrentMapZone(Optional.empty());
			player.getAppearance().generateAppearenceData();
			player.getEquipment().refresh(null);
		}
	}
	
	public void showSkull(Player player) {
		player.getInterfaceManager().sendWildyOverlay();
	}

	public static boolean isDitch(int id) {
		return id >= 1440 && id <= 1444 || id >= 65076 && id <= 65087;
	}

	@Override
	public void finish(Player player) {
		// TODO Auto-generated method stub
		
	}
	
	public static void GateNorthOut(final Player player, final GameObject object) {
		player.task(p -> {
			p.toPlayer().getMovement().lock(2);
			GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY() - 1, object.getPlane());
			if (GameObject.removeObjectTemporary(object, 2))
				GameObject.spawnObjectTemporary(openedDoor, 2);
			p.toPlayer().addWalkSteps(object.getX(), object.getY(), -1, false);
		});
	}

	public static void GateSouthOut(final Player player, final GameObject object) {
		player.task(p -> {
			p.toPlayer().getMovement().lock(2);
			GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY() + 1, object.getPlane());
			if (GameObject.removeObjectTemporary(object, 2))
				GameObject.spawnObjectTemporary(openedDoor, 2);
			p.toPlayer().addWalkSteps(object.getX(), object.getY(), -1, false);
		});
	}

	public static void GateNorth(final Player player, final GameObject object) {
		player.addWalkSteps(object.getX(), object.getY(), -1, false);
		player.task(p -> {
			p.toPlayer().getMovement().lock(2);
			GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY() - 1, object.getPlane());
			if (GameObject.removeObjectTemporary(object, 2))
				GameObject.spawnObjectTemporary(openedDoor, 2);
			p.toPlayer().addWalkSteps(object.getX(), object.getY() - 1, -1, false);
		});
	}

	public static void GateSouth(final Player player, final GameObject object) {
		player.addWalkSteps(object.getX(), object.getY(), -1, false);
		player.task(p -> {
			p.toPlayer().getMovement().lock(2);
			GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY() + 1, object.getPlane());
			if (GameObject.removeObjectTemporary(object, 2))
				GameObject.spawnObjectTemporary(openedDoor, 2);
			p.toPlayer().addWalkSteps(object.getX(), object.getY() + 1, -1, false);
		});
	}
	
	public static void LeaveEastDoor(final Player player, final GameObject object) {
		player.addWalkSteps(object.getX(), object.getY(), -1, false);
		player.task(p -> {
			p.toPlayer().getMovement().lock(2);
			GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX() + 1, object.getY(), object.getPlane());
			if (GameObject.removeObjectTemporary(object, 2))
				GameObject.spawnObjectTemporary(openedDoor, 2);
			p.toPlayer().addWalkSteps(object.getX() + 1, object.getY(), 1, false);
		});
	}

	public static void LeaveWestDoor(final Player player, final GameObject object) {
		player.addWalkSteps(object.getX(), object.getY(), -1, false);
		player.task(p -> {
			p.toPlayer().getMovement().lock(2);
			GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX() - 1, object.getY(), object.getPlane());
			if (GameObject.removeObjectTemporary(object, 2))
				GameObject.spawnObjectTemporary(openedDoor, 2);
			p.toPlayer().addWalkSteps(object.getX() - 1, object.getY(), 1, false);
		});
	}

	public static void LeaveNorthDoor(final Player player, final GameObject object) {
		player.addWalkSteps(object.getX(), object.getY(), -1, false);
		player.task(p -> {
			p.toPlayer().getMovement().lock(2);
			GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
					object.getX(), object.getY() + 1, object.getPlane());
			if (GameObject.removeObjectTemporary(object, 2))
				GameObject.spawnObjectTemporary(openedDoor, 2);
			p.toPlayer().addWalkSteps(object.getX(), object.getY() + 1, 1, false);
		});
	}

	public static void EnterEastDoor(final Player player, final GameObject object) {
		if (player.getX() == 3045) {
			player.task(p -> {
				p.toPlayer().getMovement().lock(2);
				GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
						object.getX() + 1, object.getY(), object.getPlane());
				if (GameObject.removeObjectTemporary(object, 2))
					GameObject.spawnObjectTemporary(openedDoor, 2);
				p.toPlayer().addWalkSteps(object.getX() - 1, object.getY(), 1, false);
			});
		}
	}

	public static void EnterWestDoor(final Player player, final GameObject object) {
		if (player.getX() == 3037) {
			player.task(p -> {
				p.toPlayer().getMovement().lock(2);
				GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
						object.getX() - 1, object.getY(), object.getPlane());// Coordinations
				if (GameObject.removeObjectTemporary(object, 2))
					GameObject.spawnObjectTemporary(openedDoor, 2);
				p.toPlayer().addWalkSteps(object.getX(), object.getY(), 1, false);
			});
		}
	}

	public static void EnterNorthDoor(final Player player, final GameObject object) {
		if (player.getY() == 3960) {
			player.task(p -> {
				p.toPlayer().getMovement().lock(2);
				GameObject openedDoor = new GameObject(object.getId(), object.getType(), object.getRotation() + 1,
						object.getX(), object.getY() + 1, object.getPlane());
				if (GameObject.removeObjectTemporary(object, 2))
					GameObject.spawnObjectTemporary(openedDoor, 2);
				p.toPlayer().addWalkSteps(object.getX(), object.getY(), 1, false);
			});
		}
	}
	
	/*
	 * Start of PvP Zones, Safe Zones, and Etc.
	 */
	
	public static boolean inPvP(WorldTile player) {
		return(!inSafeZone(player));
	}
	
	public static boolean inSafeZone(WorldTile player) {
		return(inEdgevilleBank(player) || inWestVarrockBank(player) || inEastVarrockBank(player) ||
		   inWestFaladorBank(player) || inEastFaladorBank(player) || inCamelotBank(player) ||
		   inCatherbyBank(player) || inNorthArdougneBank(player) ||inSouthArdougneBank(player) ||
		   grandExchange(player));
	}
	
	public static boolean inEdgevilleBank(WorldTile player) {
		return(player.getX() > 3090 && player.getX() < 3099 && player.getY() > 3487 && player.getY() < 3500);
	}
	
	public static boolean inWestVarrockBank(WorldTile player) {
		return (player.getX() > 3181 && player.getX() < 3200 && player.getY() > 3431 && player.getY() < 3447);
	} 
	
	public static boolean inEastVarrockBank(WorldTile player) {
		return(player.getX() > 3249 && player.getX() < 3258 && player.getY() > 3418 && player.getY() < 3425);
	}
	
	public static boolean inWestFaladorBank(WorldTile player) {
		return(player.getX() > 2941 && player.getX() < 2948 && player.getY() > 3367 && player.getY() < 3374 || player.getX() >= 2948 && player.getX() <= 2949 && player.getY() >= 3368 && player.getY() <= 3369);
	}
	
	public static boolean inEastFaladorBank(WorldTile player) {
		return(player.getX() > 3008 && player.getX() < 3019 && player.getY() > 3354 && player.getY() < 3359);
	}
	
	public static boolean inCamelotBank(WorldTile player) {
		return(player.getX() > 2720 && player.getX() < 2731 && player.getY() > 3489 && player.getY() < 3494 || player.getX() > 2723 && player.getX() < 2728 && player.getY() > 3486 && player.getY() < 3490);
	}
	
	public static boolean inCatherbyBank(WorldTile player) {
		return(player.getX() > 2805 && player.getX() < 2813 && player.getY() > 3437 && player.getY() < 3442);
	}
	
	public static boolean inNorthArdougneBank(WorldTile player) {
		return(player.getX() > 2611 && player.getX() < 2622 && player.getY() > 3329 && player.getY() <3336);
	}
	
	public static boolean inSouthArdougneBank(WorldTile player) {
		return(player.getX() > 2648 && player.getX() < 2657 && player.getY() > 3279 && player.getY() < 3288);
	}
	
	public static boolean grandExchange(WorldTile player) {
		return(player.getX() > 3153 && player.getX() < 3176 && player.getY() > 3478 && player.getY() < 3501);
	}
}