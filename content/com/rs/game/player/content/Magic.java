package com.rs.game.player.content;

import com.rs.GameConstants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.Entity;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import skills.Skills;
import skills.magic.TeleportType;

/*
 * content package used for static stuff
 */
public class Magic {

	public static final int MAGIC_TELEPORT = 0, ITEM_TELEPORT = 1, OBJECT_TELEPORT = 2;

	private static final int AIR_RUNE = 556, WATER_RUNE = 555;
	public static final int EARTH_RUNE = 557;
	private static final int FIRE_RUNE = 554;
	private static final int BODY_RUNE = 559;
	private static final int MIND_RUNE = 558;
	private static final int NATURE_RUNE = 561;
	private static final int CHAOS_RUNE = 562;
	public static final int DEATH_RUNE = 560;
	private static final int BLOOD_RUNE = 565;
	private static final int SOUL_RUNE = 566;
	public static final int ASTRAL_RUNE = 9075;
	private static final int LAW_RUNE = 563;
	@SuppressWarnings("unused")
	private static final int STEAM_RUNE = 4694;
	@SuppressWarnings("unused")
	private static final int MIST_RUNE = 4695;
	@SuppressWarnings("unused")
	private static final int DUST_RUNE = 4696;
	@SuppressWarnings("unused")
	private static final int SMOKE_RUNE = 4697;
	@SuppressWarnings("unused")
	private static final int MUD_RUNE = 4698;
	@SuppressWarnings("unused")
	private static final int LAVA_RUNE = 4699;
	private static final int ARMADYL_RUNE = 21773;
	private static final int ELEMENTAL_RUNE = 12850;
	private static final int CATALYTIC_RUNE = 12851;

	public static final boolean hasInfiniteRunes(int runeId, int weaponId, int shieldId) {
		if (runeId == AIR_RUNE) {
			if (weaponId == 23044 || weaponId == 1381 || weaponId == 1397 || weaponId == 1405 || weaponId == 21777
					|| weaponId == 11736 || weaponId == 11738) // air
				// staff
				return true;
		} else if (runeId == WATER_RUNE) {
			if (weaponId == 23045 || weaponId == 1383 || weaponId == 1395 || weaponId == 1403 || shieldId == 18346
					|| weaponId == 6563 || weaponId == 6726) // water
				// staff
				return true;
		} else if (runeId == EARTH_RUNE) {
			if (weaponId == 23046 || weaponId == 1385 || weaponId == 1399 || weaponId == 1407 || weaponId == 3053
					|| weaponId == 3054 || weaponId == 6563 || weaponId == 6726) // earth
				// staff
				return true;
		} else if (runeId == FIRE_RUNE) {
			if (weaponId == 23047 || weaponId == 1387 || weaponId == 1393 || weaponId == 1401 || weaponId == 3053
					|| weaponId == 3054 || weaponId == 11736 || weaponId == 11738) // fire
				// staff
				return true;
		}
		return false;
	}

	public static boolean hasSpecialRunes(Player player, int runeId, int amountRequired) {
		if (player.getInventory().containsItem(ELEMENTAL_RUNE, amountRequired)) {
			if (runeId == AIR_RUNE || runeId == WATER_RUNE || runeId == EARTH_RUNE || runeId == FIRE_RUNE)
				return true;
		}
		if (player.getInventory().containsItem(CATALYTIC_RUNE, amountRequired)) {
			if (runeId == ARMADYL_RUNE || runeId == MIND_RUNE || runeId == CHAOS_RUNE || runeId == DEATH_RUNE
					|| runeId == BLOOD_RUNE || runeId == BODY_RUNE || runeId == NATURE_RUNE || runeId == ASTRAL_RUNE
					|| runeId == SOUL_RUNE || runeId == LAW_RUNE)
				return true;
		}
		return false;
	}

	public static int getRuneForId(int runeId) {
		if (runeId == AIR_RUNE || runeId == WATER_RUNE || runeId == EARTH_RUNE || runeId == FIRE_RUNE)
			return ELEMENTAL_RUNE;
		else if (runeId == ARMADYL_RUNE || runeId == DEATH_RUNE || runeId == MIND_RUNE || runeId == CHAOS_RUNE
				|| runeId == BLOOD_RUNE || runeId == BODY_RUNE || runeId == NATURE_RUNE || runeId == ASTRAL_RUNE
				|| runeId == SOUL_RUNE || runeId == LAW_RUNE)
			return CATALYTIC_RUNE;
		return -1;
	}

	public static final boolean checkCombatSpell(Player player, int spellId, int set, boolean delete) {
		if (spellId == 65535)
			return true;
		switch (player.getCombatDefinitions().getSpellBook()) {
		case 193:
			switch (spellId) {
			case 28:
				if (!checkSpellRequirements(player, 50, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE,
						1))
					return false;
				break;
			case 32:
				if (!checkSpellRequirements(player, 52, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE,
						1))
					return false;
				break;
			case 24:
				if (!checkSpellRequirements(player, 56, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, BLOOD_RUNE, 1))
					return false;
				break;
			case 20:
				if (!checkSpellRequirements(player, 58, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, WATER_RUNE, 2))
					return false;
				break;
			case 30:
				if (!checkSpellRequirements(player, 62, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE,
						2))
					return false;
				break;
			case 34:
				if (!checkSpellRequirements(player, 64, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE,
						2))
					return false;
				break;
			case 26:
				if (!checkSpellRequirements(player, 68, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, BLOOD_RUNE, 2))
					return false;
				break;
			case 22:
				if (!checkSpellRequirements(player, 70, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, WATER_RUNE, 4))
					return false;
				break;
			case 29:
				if (!checkSpellRequirements(player, 74, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE,
						2))
					return false;
				break;
			case 33:
				if (!checkSpellRequirements(player, 76, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, AIR_RUNE, 2, SOUL_RUNE,
						2))
					return false;
				break;
			case 25:
				if (!checkSpellRequirements(player, 80, delete, DEATH_RUNE, 2, BLOOD_RUNE, 4))
					return false;
				break;
			case 21:
				if (!checkSpellRequirements(player, 82, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, WATER_RUNE, 3))
					return false;
				break;
			case 31:
				if (!checkSpellRequirements(player, 86, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, FIRE_RUNE, 4, AIR_RUNE,
						4))
					return false;
				break;
			case 35:
				if (!checkSpellRequirements(player, 88, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, AIR_RUNE, 4, SOUL_RUNE,
						3))
					return false;
				break;
			case 27:
				if (!checkSpellRequirements(player, 92, delete, DEATH_RUNE, 4, BLOOD_RUNE, 4, SOUL_RUNE, 1))
					return false;
				break;
			case 23:
				if (!checkSpellRequirements(player, 94, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, WATER_RUNE, 6))
					return false;
				break;
			case 36: // Miasmic rush.
				if (!checkSpellRequirements(player, 61, delete, CHAOS_RUNE, 2, EARTH_RUNE, 1, SOUL_RUNE, 1)) {
					return false;
				}
				int weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			case 38: // Miasmic burst.
				if (!checkSpellRequirements(player, 73, delete, CHAOS_RUNE, 4, EARTH_RUNE, 2, SOUL_RUNE, 2)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			case 37: // Miasmic blitz.
				if (!checkSpellRequirements(player, 85, delete, BLOOD_RUNE, 2, EARTH_RUNE, 3, SOUL_RUNE, 3)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			case 39: // Miasmic barrage.
				if (!checkSpellRequirements(player, 97, delete, BLOOD_RUNE, 4, EARTH_RUNE, 4, SOUL_RUNE, 4)) {
					return false;
				}
				weaponId = player.getEquipment().getWeaponId();
				if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
					player.getPackets().sendGameMessage("You need a Zuriel's staff to cast this spell.");
					return false;
				}
				break;
			default:
				return false;
			}
			break;
		case 192:
			switch (spellId) {
			case 25:
				if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 28:
				if (!checkSpellRequirements(player, 5, delete, WATER_RUNE, 1, AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 30:
				if (!checkSpellRequirements(player, 9, delete, EARTH_RUNE, 2, AIR_RUNE, 1, MIND_RUNE, 1))
					return false;
				break;
			case 32:
				if (!checkSpellRequirements(player, 13, delete, FIRE_RUNE, 3, AIR_RUNE, 2, MIND_RUNE, 1))
					return false;
				break;
			case 34: // air bolt
				if (!checkSpellRequirements(player, 17, delete, AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 36:// bind
				if (!checkSpellRequirements(player, 20, delete, EARTH_RUNE, 3, WATER_RUNE, 3, NATURE_RUNE, 2))
					return false;
				break;
			case 55: // snare
				if (!checkSpellRequirements(player, 50, delete, EARTH_RUNE, 4, WATER_RUNE, 4, NATURE_RUNE, 3))
					return false;
				break;
			case 81:// entangle
				if (!checkSpellRequirements(player, 79, delete, EARTH_RUNE, 5, WATER_RUNE, 5, NATURE_RUNE, 4))
					return false;
				break;
			case 39: // water bolt
				if (!checkSpellRequirements(player, 23, delete, WATER_RUNE, 2, AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 42: // earth bolt
				if (!checkSpellRequirements(player, 29, delete, EARTH_RUNE, 3, AIR_RUNE, 2, CHAOS_RUNE, 1))
					return false;
				break;
			case 45: // fire bolt
				if (!checkSpellRequirements(player, 35, delete, FIRE_RUNE, 4, AIR_RUNE, 3, CHAOS_RUNE, 1))
					return false;
				break;
			case 49: // air blast
				if (!checkSpellRequirements(player, 41, delete, AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 52: // water blast
				if (!checkSpellRequirements(player, 47, delete, WATER_RUNE, 3, AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 58: // earth blast
				if (!checkSpellRequirements(player, 53, delete, EARTH_RUNE, 4, AIR_RUNE, 3, DEATH_RUNE, 1))
					return false;
				break;
			case 63: // fire blast
				if (!checkSpellRequirements(player, 59, delete, FIRE_RUNE, 5, AIR_RUNE, 4, DEATH_RUNE, 1))
					return false;
				break;
			case 70: // air wave
				if (!checkSpellRequirements(player, 62, delete, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 73: // water wave
				if (!checkSpellRequirements(player, 65, delete, WATER_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 77: // earth wave
				if (!checkSpellRequirements(player, 70, delete, EARTH_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 80: // fire wave
				if (!checkSpellRequirements(player, 75, delete, FIRE_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1))
					return false;
				break;
			case 84:
				if (!checkSpellRequirements(player, 81, delete, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1))
					return false;
				break;
			case 87:
				if (!checkSpellRequirements(player, 85, delete, WATER_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE,
						1))
					return false;
				break;
			case 89:
				if (!checkSpellRequirements(player, 85, delete, EARTH_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE,
						1))
					return false;
				break;
			case 66: // Sara Strike
				if (player.getEquipment().getWeaponId() != 2415) {
					player.getPackets()
							.sendGameMessage("You need to be equipping a Saradomin staff to cast this spell.", true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2))
					return false;
				break;
			case 67: // Guthix Claws
				if (player.getEquipment().getWeaponId() != 2416) {
					player.getPackets().sendGameMessage(
							"You need to be equipping a Guthix Staff or Void Mace to cast this spell.", true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2))
					return false;
				break;
			case 68: // Flame of Zammy
				if (player.getEquipment().getWeaponId() != 2417) {
					player.getPackets().sendGameMessage("You need to be equipping a Zamorak Staff to cast this spell.",
							true);
					return false;
				}
				if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 4, BLOOD_RUNE, 2))
					return false;
				break;
			case 91:
				if (!checkSpellRequirements(player, 85, delete, FIRE_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE,
						1))
					return false;
				break;
			case 86: // teleblock
				if (!checkSpellRequirements(player, 85, delete, CHAOS_RUNE, 1, LAW_RUNE, 1, DEATH_RUNE, 1))
					return false;
				break;
			case 99: // Storm of Armadyl
				if (!checkSpellRequirements(player, 77, delete, ARMADYL_RUNE, 1))
					return false;
				break;
			default:
				return false;
			}
			break;
		default:
			return false;
		}
		if (set >= 0) {
			if (set == 0)
				player.getCombatDefinitions().setAutoCastSpell(spellId);
			else
				player.getAttributes().get(Attribute.TEMP_CAST_SPELL).set(spellId);
		}
		return true;
	}

	public static final void setCombatSpell(Player player, int spellId) {
		if (player.getCombatDefinitions().getAutoCastSpell() == spellId)
			player.getCombatDefinitions().resetSpells(true);
		else
			checkCombatSpell(player, spellId, 0, false);
	}

	public static final void processLunarSpell(Player player, int spellId, Entity target) {
		player.setNextFaceWorldTile(new WorldTile(target.getCoordFaceX(target.getSize()),
				target.getCoordFaceY(target.getSize()), target.getPlane()));
		// doesnt stop what u doing on rs
		switch (spellId) {
		case 42:
			if (!(target.isPlayer()))
				return;
			if (player.getSkills().getLevel(Skills.MAGIC) < 93) {
				player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
				return;
			}
			Long lastVeng = (Long) player.getAttributes().get(Attribute.LAST_VENG).get();
			if (lastVeng != null && lastVeng + 30000 > Utility.currentTimeMillis()) {
				player.getPackets().sendGameMessage("Players may only cast vengeance once every 30 seconds.");
				return;
			}
			if (((Player) target).getDetails().getAcceptAid().isFalse()) {
				player.getPackets().sendGameMessage(((Player) target).getDisplayName() + " is not accepting aid");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 3, DEATH_RUNE, 2, EARTH_RUNE, 10))
				return;
			player.setNextAnimation(new Animation(4411));
			player.getAttributes().get(Attribute.LAST_VENG).set(Utility.currentTimeMillis());
			player.getPackets().sendGameMessage("You cast a vengeance.");
			((Player) target).setNextGraphics(new Graphics(725, 0, 100));
//			((Player) target).setCastVeng(true);
			((Player) target).getPackets().sendGameMessage("You have the power of vengeance!");
			break;
		}
	}

	public static final void processLunarSpell(Player player, int spellId, int packetId) {
		switch (spellId) {
		case 37:
			if (player.getSkills().getLevel(Skills.MAGIC) < 94) {
				player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
				return;
			} else if (player.getSkills().getLevel(Skills.DEFENCE) < 40) {
				player.getPackets().sendGameMessage("You need a Defence level of 40 for this spell");
				return;
			}
			Long lastVeng = (Long) player.getAttributes().get(Attribute.LAST_VENG).get();
			if (lastVeng != null && lastVeng + 30000 > Utility.currentTimeMillis()) {
				player.getPackets().sendGameMessage("Players may only cast vengeance once every 30 seconds.");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 2, EARTH_RUNE, 10))
				return;
			player.setNextGraphics(new Graphics(726, 0, 100));
			player.setNextAnimation(new Animation(4410));
//			player.setCastVeng(true);
			player.getAttributes().get(Attribute.LAST_VENG).set(Utility.currentTimeMillis());
			player.getPackets().sendGameMessage("You cast a vengeance.");
			break;
		case 38:
			player.getMovement().move(false, GameConstants.START_PLAYER_LOCATION, TeleportType.LUNAR);
			break;
		case 74: // vegeance group
			if (player.getSkills().getLevel(Skills.MAGIC) < 95) {
				player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
				return;
			}
			lastVeng = (Long) player.getAttributes().get(Attribute.LAST_VENG).get();
			if (lastVeng != null && lastVeng + 30000 > Utility.currentTimeMillis()) {
				player.getPackets().sendGameMessage("Players may only cast vengeance once every 30 seconds.");
				return;
			}
			if (!checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 3, EARTH_RUNE, 11))
				return;
			player.getMapRegionsIds().parallelStream().filter(regionalPlayer -> regionalPlayer != null).forEach(regionalPlayer -> {
				ObjectArrayList<Short> playersIndexes = World.getRegion(regionalPlayer).getPlayersIndexes();
				playersIndexes.iterator().forEachRemaining(p -> {
					World.players().filter(
							playerIndex -> playerIndex.withinDistance(player, 4) || player.getMapZoneManager().execute(player, controller -> controller.canHit(player, playerIndex)))
							.forEach(worldPlayer -> {
								if (worldPlayer.getDetails().getAcceptAid().isFalse()) {
									player.getPackets().sendGameMessage(worldPlayer.getDisplayName() + " is not accepting aid");
									return;
								}
								worldPlayer.setNextGraphics(new Graphics(725, 0, 100));
//								p2.setCastVeng(true);
								worldPlayer.getPackets().sendGameMessage("You have the power of vengeance!");
							});
				});
			});
			player.setNextAnimation(new Animation(4411));
			player.getAttributes().get(Attribute.LAST_VENG).set(Utility.currentTimeMillis());
			break;
		case 43: // moonclan teleport
			sendLunarTeleportSpell(player, 69, 66, new WorldTile(2114, 3914, 0), ASTRAL_RUNE, 2, LAW_RUNE, 1,
					EARTH_RUNE, 2);
		case 54: // ourania teleport
			sendLunarTeleportSpell(player, 71, 69, new WorldTile(2467, 3247, 0), ASTRAL_RUNE, 2, LAW_RUNE, 1,
					EARTH_RUNE, 6);
		case 67: // south falador teleport
			sendLunarTeleportSpell(player, 72, 70, new WorldTile(3006, 3327, 0), ASTRAL_RUNE, 2, LAW_RUNE, 1, AIR_RUNE,
					2);
		case 47: // waterbirth teleport
			sendLunarTeleportSpell(player, 72, 71, new WorldTile(2546, 3758, 0), ASTRAL_RUNE, 2, LAW_RUNE, 1,
					WATER_RUNE, 1);
		case 22: // barbarian teleport
			sendLunarTeleportSpell(player, 75, 76, new WorldTile(2635, 3166, 0), ASTRAL_RUNE, 2, LAW_RUNE, 1, FIRE_RUNE,
					3);
		case 69: // North Ardroudge teleport
			sendLunarTeleportSpell(player, 76, 76, new WorldTile(2613, 3349, 0), ASTRAL_RUNE, 2, LAW_RUNE, 1,
					WATER_RUNE, 5);
		case 41: // Khazard teleport
			sendLunarTeleportSpell(player, 78, 80, new WorldTile(2635, 3166, 0), ASTRAL_RUNE, 2, LAW_RUNE, 2,
					WATER_RUNE, 4);
		case 40: // Fishing guild teleport
			sendLunarTeleportSpell(player, 85, 89, new WorldTile(2612, 3383, 0), ASTRAL_RUNE, 3, LAW_RUNE, 3,
					WATER_RUNE, 8);
		case 44: // Catherbay teleport
			sendLunarTeleportSpell(player, 87, 92, new WorldTile(2800, 3451, 0), ASTRAL_RUNE, 3, LAW_RUNE, 3,
					WATER_RUNE, 10);
		case 51: // Ice Plateau teleport
			sendLunarTeleportSpell(player, 89, 96, new WorldTile(2974, 3940, 0), ASTRAL_RUNE, 3, LAW_RUNE, 3,
					WATER_RUNE, 8);
		case 75: // Throheim teleport
			sendLunarTeleportSpell(player, 92, 101, new WorldTile(2814, 3680, 0), ASTRAL_RUNE, 3, LAW_RUNE, 3,
					WATER_RUNE, 10);
			break;
		}
	}

	public static final void processAncientSpell(Player player, int spellId, int packetId) {
		switch (spellId) {
		case 28:
		case 32:
		case 24:
		case 20:
		case 30:
		case 34:
		case 26:
		case 22:
		case 29:
		case 33:
		case 25:
		case 21:
		case 31:
		case 35:
		case 27:
		case 23:
		case 36:
		case 37:
		case 38:
		case 39:
			setCombatSpell(player, spellId);
			break;
		case 40:
			sendAncientTeleportSpell(player, 54, 64, new WorldTile(3099, 9882, 0), LAW_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE,
					1);
			break;
		case 41:
			sendAncientTeleportSpell(player, 60, 70, new WorldTile(3222, 3336, 0), LAW_RUNE, 2, SOUL_RUNE, 1);
			break;
		case 42:
			sendAncientTeleportSpell(player, 66, 76, new WorldTile(3492, 3471, 0), LAW_RUNE, 2, BLOOD_RUNE, 1);

			break;
		case 43:
			sendAncientTeleportSpell(player, 72, 82, new WorldTile(3006, 3471, 0), LAW_RUNE, 2, WATER_RUNE, 4);
			break;
		case 44:
			sendAncientTeleportSpell(player, 78, 88, new WorldTile(2990, 3696, 0), LAW_RUNE, 2, FIRE_RUNE, 3, AIR_RUNE,
					2);
			break;
		case 45:
			sendAncientTeleportSpell(player, 84, 94, new WorldTile(3217, 3677, 0), LAW_RUNE, 2, SOUL_RUNE, 2);
			break;
		case 46:
			sendAncientTeleportSpell(player, 90, 100, new WorldTile(3288, 3886, 0), LAW_RUNE, 2, BLOOD_RUNE, 2);
			break;
		case 47:
			sendAncientTeleportSpell(player, 96, 106, new WorldTile(2977, 3873, 0), LAW_RUNE, 2, WATER_RUNE, 8);
			break;
		case 48:
			player.getMovement().move(false, GameConstants.START_PLAYER_LOCATION, TeleportType.ANCIENT);
			break;
		}
	}

	public static void processNormalSpell(Player player, int spellId, int packetId) {
		System.out.println(packetId);
		switch (spellId) {
		case 25: // air strike
		case 28: // water strike
		case 30: // earth strike
		case 32: // fire strike
		case 34: // air bolt
		case 39: // water bolt
		case 42: // earth bolt
		case 45: // fire bolt
		case 49: // air blast
		case 52: // water blast
		case 58: // earth blast
		case 63: // fire blast
		case 70: // air wave
		case 73: // water wave
		case 77: // earth wave
		case 80: // fire wave
		case 99:
		case 84:
		case 87:
		case 89:
		case 91:
		case 36:
		case 55:
		case 81:
		case 66:
		case 67:
		case 68:
			setCombatSpell(player, spellId);
			break;
		case 27: // crossbow bolt enchant
			if (player.getSkills().getLevel(Skills.MAGIC) < 4) {
				player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
				return;
			}
			player.getMovement().stopAll();
			player.getInterfaceManager().sendInterface(432);
			break;
		case 24:
			TeleportType.MODERN_HOME.checkSpecialCondition(player, GameConstants.START_PLAYER_LOCATION);
			break;
		case 37: // mobi
			sendNormalTeleportSpell(player, 10, 19, new WorldTile(2413, 2848, 0), LAW_RUNE, 1, WATER_RUNE, 1, AIR_RUNE,
					1);
			break;
		case 40: // varrock
			sendNormalTeleportSpell(player, 25, 19, new WorldTile(3212, 3424, 0), FIRE_RUNE, 1, AIR_RUNE, 3, LAW_RUNE,
					1);
			break;
		case 43: // lumby
			player.getMovement().move(false, GameConstants.START_PLAYER_LOCATION, TeleportType.NORMAL);
			break;
		case 46: // fally
			sendNormalTeleportSpell(player, 37, 48, new WorldTile(2964, 3379, 0), WATER_RUNE, 1, AIR_RUNE, 3, LAW_RUNE,
					1);
			break;
		case 51: // camelot
			sendNormalTeleportSpell(player, 45, 55.5, new WorldTile(2757, 3478, 0), AIR_RUNE, 5, LAW_RUNE, 1);
			break;
		case 57: // ardy
			sendNormalTeleportSpell(player, 51, 61, new WorldTile(2664, 3305, 0), WATER_RUNE, 2, LAW_RUNE, 2);
			break;
		case 62: // watch
			sendNormalTeleportSpell(player, 58, 68, new WorldTile(2547, 3113, 2), EARTH_RUNE, 2, LAW_RUNE, 2);
			break;
		case 69: // troll
			sendNormalTeleportSpell(player, 61, 68, new WorldTile(2888, 3674, 0), FIRE_RUNE, 2, LAW_RUNE, 2);
			break;
		case 72: // ape
			sendNormalTeleportSpell(player, 64, 76, new WorldTile(2776, 9103, 0), FIRE_RUNE, 2, WATER_RUNE, 2, LAW_RUNE,
					2, 1963, 1);
			break;
		}
	}

	public static final boolean checkSpellRequirements(Player player, int level, boolean delete, int... runes) {
		if (!checkSpellLevel(player, level))
			return false;
		return checkRunes(player, delete, runes);
	}

	public static boolean checkSpellLevel(Player player, int level) {
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		return true;
	}

	public static boolean hasStaffOfLight(int weaponId) {
		if (weaponId == 15486 || weaponId == 22207 || weaponId == 22209 || weaponId == 22211 || weaponId == 22213)
			return true;
		return false;
	}

	public static final boolean checkRunes(Player player, boolean delete, int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getShieldId();
		int runesCount = 0;
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int amount = runes[runesCount++];
			if (hasInfiniteRunes(runeId, weaponId, shieldId))
				continue;
			else if (hasSpecialRunes(player, runeId, amount))
				continue;
			else if (hasStaffOfLight(weaponId) && RandomUtils.inclusive(8) == 0 && runeId != 21773)// 1
				// in
				// eight
				// chance
				// of
				// keeping
				// runes
				continue;
			if (!player.getInventory().containsItem(runeId, amount)) {
				player.getPackets()
						.sendGameMessage("You do not have enough "
								+ ItemDefinitions.getItemDefinitions(runeId).getName().replace("rune", "Rune")
								+ "s to cast this spell.");
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int amount = runes[runesCount++];
				if (hasInfiniteRunes(runeId, weaponId, shieldId))
					continue;
				if (hasSpecialRunes(player, runeId, amount))
					runeId = getRuneForId(runeId);
				player.getInventory().deleteItem(runeId, amount);
			}
		}
		return true;
	}

	public static final void sendAncientTeleportSpell(Player player, int level, double xp, WorldTile tile,
			int... runes) {
		sendTeleportSpell(player, 1979, -1, 1681, -1, level, xp, tile, 5, true, MAGIC_TELEPORT, runes);
	}

	public static final void sendLunarTeleportSpell(Player player, int level, double xp, WorldTile tile, int... runes) {
		sendTeleportSpell(player, 9606, -2, 1685, -1, level, xp, tile, 5, true, MAGIC_TELEPORT, runes);
	}

	public static final boolean sendNormalTeleportSpell(Player player, int level, double xp, WorldTile tile,
			int... runes) {
		return sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, tile, 3, true, MAGIC_TELEPORT, runes);
	}

	public static final boolean sendItemTeleportSpell(Player player, boolean randomize, int upEmoteId, int upGraphicId,
			int delay, WorldTile tile) {
		return sendTeleportSpell(player, upEmoteId, -2, upGraphicId, -1, 0, 0, tile, delay, randomize, ITEM_TELEPORT);
	}

	public static final void sendObjectTeleportSpell(Player player, boolean randomize, WorldTile tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, 3, randomize, OBJECT_TELEPORT);
	}

	public static final void sendDelayedObjectTeleportSpell(Player player, int delay, boolean randomize,
			WorldTile tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, delay, randomize, OBJECT_TELEPORT);
	}

	public static final boolean sendTeleportSpell(final Player player, int upEmoteId, final int downEmoteId,
			int upGraphicId, final int downGraphicId, int level, final double xp, final WorldTile tile, int delay,
			final boolean randomize, final int teleType, int... runes) {
		if (player.getMovement().isLocked())
			return false;
		if (player.getSkills().getLevel(Skills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		if (!checkRunes(player, false, runes))
			return false;
		if (teleType == MAGIC_TELEPORT) {
			if (player.getMapZoneManager().execute(player, controller -> !controller.processMagicTeleport(player, tile)))
				return false;
		} else if (teleType == ITEM_TELEPORT) {
			if (player.getMapZoneManager().execute(player, controller -> !controller.processItemTeleport(player, tile)))
				return false;
		} else if (teleType == OBJECT_TELEPORT) {
			if (player.getMapZoneManager().execute(player, controller -> !controller.processObjectTeleport(player, tile)))
				return false;
		}
		checkRunes(player, true, runes);
		player.getMovement().stopAll();
		if (upEmoteId != -1)
			player.setNextAnimation(new Animation(upEmoteId));
		if (upGraphicId != -1)
			player.setNextGraphics(new Graphics(upGraphicId));
		if (teleType == MAGIC_TELEPORT)
			player.getPackets().sendSound(5527, 0, 2);
		player.getMovement().lock(3 + delay);
		player.getDetails().getStatistics().addStatistic("Teleports_Completed");
		World.get().submit(new Task(delay) {
			boolean removeDamage;
			
			@Override
			protected void execute() {
				if (!removeDamage) {
					WorldTile teleTile = tile;
					if (randomize) {
						// attemps to randomize tile by 4x4 area
						for (int trycount = 0; trycount < 10; trycount++) {
							teleTile = new WorldTile(tile, 2);
							if (World.getTileAttributes().isTileFree(tile.getPlane(), teleTile.getX(), teleTile.getY(), player.getSize()))
								break;
							teleTile = tile;
						}
					}
					player.setNextWorldTile(teleTile);
					player.getMapZoneManager().executeVoid(player, controller -> controller.magicTeleported(player, teleType));
					if (!player.getCurrentMapZone().isPresent())
						teleControlersCheck(player, teleTile);
					if (xp != 0)
						player.getSkills().addExperience(Skills.MAGIC, xp);
					if (downEmoteId != -1)
						player.setNextAnimation(new Animation(downEmoteId == -2 ? -1 : downEmoteId));
					if (downGraphicId != -1)
						player.setNextGraphics(new Graphics(downGraphicId));
					if (teleType == MAGIC_TELEPORT) {
						player.getPackets().sendSound(5524, 0, 2);
						player.setNextFaceWorldTile(
								new WorldTile(teleTile.getX(), teleTile.getY() - 1, teleTile.getPlane()));
						player.setDirection((byte) 6);
						player.getAppearance().generateAppearenceData();
					}
					removeDamage = true;
				} else {
					player.resetReceivedHits();
					this.cancel();
				}
				this.cancel();
			}
		});
		return true;
	}

	public static void teleControlersCheck(Player player, WorldTile teleTile) {
//		if (WildernessController.isAtWild(teleTile))
//			new WildernessController().start(player);
	}

	public static void useEctoPhial(final Player player, Item item) {
		player.getInventory().deleteItem(item);
		player.setNextGraphics(Graphic.ECTOPHIAL_LIQUID);
		player.setNextAnimation(Animations.EMPTY_ECTOPHIAL);
		World.get().submit(new Task(6) {
			@Override
			protected void execute() {
				sendTeleportSpell(player, 8939, 8941, 1678, 1679, 0, 0, new WorldTile(3662, 3518, 0), 4, true,
						ITEM_TELEPORT);
				this.cancel();
			}
		});
	}
	
	 public static void resourcesTeleport(final Player player, final int x, final int y, final int h) {
	        player.getMovement().lock();
	        World.get().submit(new Task(1) {
	        	 int loop;
				@Override
				protected void execute() {
					 if (loop == 0) {
		                    player.setNextAnimation(new Animation(13288));
		                    player.setNextGraphics(new Graphics(2516));
		                } else if (loop == 2) {
		                    player.setNextWorldTile(new WorldTile(x, y, h));
		                    player.setNextAnimation(new Animation(13285));
		                    player.setNextGraphics(new Graphics(2517));
		                    player.getMovement().unlock();
		                    cancel();
		                }
		                loop++;
				}
			});
	    }
}
