// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
package skills.herblore;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.InterfaceVars;
import com.rs.constants.Sounds;
import com.rs.content.mapzone.impl.WildernessMapZone;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Combat;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.player.type.CombatEffectType;
import com.rs.game.task.Task;
import com.rs.utilities.Utility;

import skills.Skills;


public class Potions {

	public static final int VIAL = 229;
	public static final int JUJU_VIAL = 19996;
	public static final int BEER_GLASS = 1919;
	public static final int EMPTY_KEG = 5769;

	public enum Potion {
		ATTACK_POTION(VIAL, new int[] { 2428, 121, 123, 125 }, p -> p.getSkills().adjustStat(3, 0.1, Skills.ATTACK)),
		ATTACK_MIX(VIAL, new int[] { 11429, 11431 }, p -> {
			p.getSkills().adjustStat(3, 0.1, Skills.ATTACK);
			p.heal(30);
		}),

		STRENGTH_POTION(VIAL, new int[] { 113, 115, 117, 119 }, p -> p.getSkills().adjustStat(3, 0.1, Skills.STRENGTH)),
		STRENGTH_MIX(VIAL, new int[] { 11443, 11441 }, p -> {
			p.getSkills().adjustStat(3, 0.1, Skills.STRENGTH);
			p.heal(30);
		}),

		DEFENCE_POTION(VIAL, new int[] { 2432, 133, 135, 137 }, p -> p.getSkills().adjustStat(3, 0.1, Skills.DEFENCE)),
		DEFENCE_MIX(VIAL, new int[] { 11457, 11459 }, p -> {
			p.getSkills().adjustStat(3, 0.1, Skills.DEFENCE);
			p.heal(30);
		}),

		JUG_OF_BAD_WINE(1935, 1991, p -> {
			p.getSkills().lowerStat(Skills.ATTACK, 3);
		}),
		JUG_OF_WINE(1935, 1993, p -> {
			p.heal(110, 0);
			p.getSkills().lowerStat(Skills.ATTACK, 2);
		}),
		
		COMBAT_POTION(VIAL, new int[] { 9739, 9741, 9743, 9745 }, p -> p.getSkills().adjustStat(3, 0.1, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE)),
		COMBAT_MIX(VIAL, new int[] { 11445, 11447 }, p -> {
			p.getSkills().adjustStat(3, 0.1, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(30);
		}),

		SUPER_ATTACK(VIAL, new int[] { 2436, 145, 147, 149 }, p -> p.getSkills().adjustStat(5, 0.15, Skills.ATTACK)),
		CW_SUPER_ATTACK_POTION(-1, new int[] { 18715, 18716, 18717, 18718 }, p -> p.getSkills().adjustStat(5, 0.15, Skills.ATTACK)),
		SUPER_ATTACK_MIX(VIAL, new int[] { 11469, 11471 }, p -> {
			p.getSkills().adjustStat(5, 0.15, Skills.ATTACK);
			p.heal(30);
		}),

		SUPER_STRENGTH(VIAL, new int[] { 2440, 157, 159, 161 }, p -> p.getSkills().adjustStat(5, 0.15, Skills.STRENGTH)),
		CW_SUPER_STRENGTH_POTION(-1, new int[] { 18719, 18720, 18721, 18722 }, p -> p.getSkills().adjustStat(5, 0.15, Skills.STRENGTH)),
		SUPER_STRENGTH_MIX(VIAL, new int[] { 11485, 11487 }, p -> {
			p.getSkills().adjustStat(5, 0.15, Skills.STRENGTH);
			p.heal(30);
		}),

		SUPER_DEFENCE(VIAL, new int[] { 2442, 163, 165, 167 }, p -> p.getSkills().adjustStat(5, 0.15, Skills.DEFENCE)),
		CW_SUPER_DEFENCE_POTION(-1, new int[] { 18723, 18724, 18725, 18726 }, p -> p.getSkills().adjustStat(5, 0.15, Skills.DEFENCE)),
		SUPER_DEFENCE_MIX(VIAL, new int[] { 11497, 11499 }, p -> {
			p.getSkills().adjustStat(5, 0.15, Skills.DEFENCE);
			p.heal(30);
		}),

		RANGING_POTION(VIAL, new int[] { 2444, 169, 171, 173 }, p -> p.getSkills().adjustStat(4, 0.10, Skills.RANGE)),
		CW_SUPER_RANGING_POTION(-1, new int[] { 18731, 18732, 18733, 18734 }, p -> p.getSkills().adjustStat(4, 0.10, Skills.RANGE)),
		RANGING_MIX(VIAL, new int[] { 11509, 11511 }, p -> {
			p.getSkills().adjustStat(4, 0.10, Skills.RANGE);
			p.heal(30);
		}),

		MAGIC_POTION(VIAL, new int[] { 3040, 3042, 3044, 3046 }, p -> p.getSkills().adjustStat(5, 0, Skills.MAGIC)),
		CW_SUPER_MAGIC_POTION(-1, new int[] { 18735, 18736, 18737, 18738 }, p -> p.getSkills().adjustStat(5, 0, Skills.MAGIC)),
		MAGIC_MIX(VIAL, new int[] { 11513, 11515 }, p -> {
			p.getSkills().adjustStat(5, 0, Skills.MAGIC);
			p.heal(30);
		}),

		RESTORE_POTION(VIAL, new int[] { 2430, 127, 129, 131 }, p -> p.getSkills().adjustStat(10, 0.3, false, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE, Skills.MAGIC)),
		RESTORE_MIX(VIAL, new int[] { 11449, 11451 }, p -> {
			p.getSkills().adjustStat(10, 0.3, false, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE, Skills.MAGIC);
			p.heal(30);
		}),

		PRAYER_POTION(VIAL, new int[] { 2434, 139, 141, 143 }, p -> p.getPrayer().restorePrayer(((int) (Math.floor(p.getSkills().getTrueLevel(Skills.PRAYER) * 2.5) + 70)))),
		PRAYER_MIX(VIAL, new int[] { 11465, 11467 }, p -> {
			p.getPrayer().restorePrayer(((int) (Math.floor(p.getSkills().getTrueLevel(Skills.PRAYER) * 2.5) + 70)));
			p.heal(30);
		}),

		SUPER_RESTORE(VIAL, new int[] { 3024, 3026, 3028, 3030 }, p -> {
			p.getSkills().adjustStat(8, 0.25, false, Utility.range(0, Skills.MAXIMUM_SKILL_COUNT));
			p.getPrayer().restorePrayer(((int) (p.getSkills().getTrueLevel(Skills.PRAYER) * 0.33 * 10)));
		}),
		SUPER_RESTORE_FLASK(-1, new int[] { 23399, 23401, 23403, 23405, 23407, 23409 }, p -> {
			p.getSkills().adjustStat(8, 0.25, false, Utility.range(0, Skills.MAXIMUM_SKILL_COUNT));
			p.getPrayer().restorePrayer(((int) (p.getSkills().getTrueLevel(Skills.PRAYER) * 0.33 * 10)));
		}),
		DOM_SUPER_RESTORE(-1, new int[] { 22379, 22380 }, p -> {
			p.getSkills().adjustStat(8, 0.25, false, Utility.range(0, Skills.MAXIMUM_SKILL_COUNT));
			p.getPrayer().restorePrayer(((int) (p.getSkills().getTrueLevel(Skills.PRAYER) * 0.33 * 10)));
		}),
		SUPER_RESTORE_MIX(VIAL, new int[] { 11493, 11495 }, p -> {
			p.getSkills().adjustStat(8, 0.25, false, Utility.range(0, Skills.MAXIMUM_SKILL_COUNT));
			p.getPrayer().restorePrayer(((int) (p.getSkills().getTrueLevel(Skills.PRAYER) * 0.33 * 10)));
			p.heal(30);
		}),
		ANTIPOISON(VIAL, new int[] { 2446, 175, 177, 179 }, p -> onAntiPoisonEffect(p, false, 100)),
		ANTIPOISON_MIX(VIAL, new int[] { 11433, 11435 }, p -> {
			onAntiPoisonEffect(p, false, 100);
			p.heal(30);
		}),

		SUPER_ANTIPOISON(VIAL, new int[] { 2448, 181, 183, 185 }, p -> onAntiPoisonEffect(p, false, 500)),
		ANTI_P_SUPERMIX(VIAL, new int[] { 11473, 11475 }, p -> {
			onAntiPoisonEffect(p, false, 500);
			p.heal(30);
		}),

		ANTIPOISONP(VIAL, new int[] { 5943, 5945, 5947, 5949 }, p -> onAntiPoisonEffect(p, false, 1200)),
		ANTIDOTEP_MIX(VIAL, new int[] { 11501, 11503 }, p -> {
			onAntiPoisonEffect(p, false, 1200);
			p.heal(30);
		}),

		ANTIPOISONPP(VIAL, new int[] { 5952, 5954, 5956, 5958 }, p -> onAntiPoisonEffect(p, false, 1200)),
		
		RELICYMS_BALM(VIAL, new int[] { 4842, 4844, 4846, 4848 }, p -> { /*TODO*/ }),
		RELICYMS_BALM_FLASK(-1, new int[] { 23537, 23539, 23541, 23543, 23545, 23547 }, p -> { /*TODO*/ }),
		RELICYMS_MIX(VIAL, new int[] { 11437, 11439 }, p -> {
			//TODO
			p.heal(30);
		}),

		ZAMORAK_BREW(VIAL, new int[] { 2450, 189, 191, 193 }, p -> {
			p.getSkills().adjustStat(2, 0.2, Skills.ATTACK);
			p.getSkills().adjustStat(2, 0.12, Skills.STRENGTH);
			p.getSkills().adjustStat(-2, -0.1, Skills.DEFENCE);
			p.applyHit(new Hit(null, (int) (p.getHitpoints()*0.12), HitLook.REGULAR_DAMAGE));
		}),
		ZAMORAK_MIX(VIAL, new int[] { 11521, 11523 }, p -> {
			p.heal(30);
		}),

		ANTIFIRE(VIAL, new int[] { 2452, 2454, 2456, 2458 }, p -> Combat.effect(p, CombatEffectType.ANTIFIRE_POTION)),
		ANTIFIRE_MIX(VIAL, new int[] { 11505, 11507 }, p -> {
			Combat.effect(p, CombatEffectType.ANTIFIRE_POTION);
			p.heal(30);
		}),

		ENERGY_POTION(VIAL, new int[] { 3008, 3010, 3012, 3014 }, p -> restoreRunEnergy(p, 20)),
		ENERGY_MIX(VIAL, new int[] { 11453, 11455 }, p -> {
			restoreRunEnergy(p, 20);
			p.heal(30);
		}),

		SUPER_ENERGY(VIAL, new int[] { 3016, 3018, 3020, 3022 }, p -> restoreRunEnergy(p, 40)),
		CW_SUPER_ENERGY_POTION(-1, new int[] { 18727, 18728, 18729, 18730 }, p -> restoreRunEnergy(p, 40)),
		SUPER_ENERGY_MIX(VIAL, new int[] { 11481, 11483 }, p -> {
			restoreRunEnergy(p, 40);
			p.heal(30);
		}),

		GUTHIX_REST(VIAL, new int[] { 4417, 4419, 4421, 4423 }, p -> {
			restoreRunEnergy(p, 5);
			p.heal(50, 50);
			p.getPoisonDamage().set(p.getPoisonDamage().get() - 10);
		}),

		SARADOMIN_BREW(VIAL, new int[] { 6685, 6687, 6689, 6691 }, p -> {
			int hpChange = (int) (p.getMaxHitpoints() * 0.15);
			p.heal(hpChange + 20, hpChange);
			p.getSkills().adjustStat(2, 0.2, Skills.DEFENCE);
			p.getSkills().adjustStat(-2, -0.1, Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE);
		}),
		
		DOM_SARADOMIN_BREW(-1, new int[] { 22373, 22374 }, p -> {
			int hpChange = (int) (p.getMaxHitpoints() * 0.15);
			p.heal(hpChange + 20, hpChange);
			p.getSkills().adjustStat(2, 0.2, Skills.DEFENCE);
			p.getSkills().adjustStat(-2, -0.1, Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE);
		}),

		MAGIC_ESSENCE(VIAL, new int[] { 9021, 9022, 9023, 9024 }, p -> p.getSkills().adjustStat(4, 0, Skills.MAGIC)),
		MAGIC_ESSENCE_MIX(VIAL, new int[] { 11489, 11491 }, p -> {
			p.getSkills().adjustStat(4, 0, Skills.MAGIC);
			p.heal(30);
		}),

		AGILITY_POTION(VIAL, new int[] { 3032, 3034, 3036, 3038 }, p -> p.getSkills().adjustStat(3, 0, Skills.AGILITY)),
		AGILITY_MIX(VIAL, new int[] { 11461, 11463 }, p -> {
			p.getSkills().adjustStat(3, 0, Skills.AGILITY);
			p.heal(30);
		}),

		FISHING_POTION(VIAL, new int[] { 2438, 151, 153, 155 }, p -> p.getSkills().adjustStat(3, 0, Skills.FISHING)),
		FISHING_MIX(VIAL, new int[] { 11477, 11479 }, p -> {
			p.getSkills().adjustStat(3, 0, Skills.FISHING);
			p.heal(30);
		}),

		HUNTER_POTION(VIAL, new int[] { 9998, 10000, 10002, 10004 }, p -> p.getSkills().adjustStat(3, 0, Skills.HUNTER)),
		HUNTING_MIX(VIAL, new int[] { 11517, 11519 }, p -> {
			p.getSkills().adjustStat(3, 0, Skills.HUNTER);
			p.heal(30);
		}),

		CRAFTING_POTION(VIAL, new int[] { 14838, 14840, 14842, 14844 }, p -> p.getSkills().adjustStat(3, 0, Skills.CRAFTING)),
		
		FLETCHING_POTION(VIAL, new int[] { 14846, 14848, 14850, 14852 }, p -> p.getSkills().adjustStat(3, 0, Skills.FLETCHING)),
		
		SANFEW_SERUM(VIAL, new int[] { 10925, 10927, 10929, 10931 }, p -> {
			p.getSkills().adjustStat(8, 0.25, false, Utility.range(0, Skills.MAXIMUM_SKILL_COUNT -1));
			p.getPrayer().restorePrayer(((int) (p.getSkills().getTrueLevel(Skills.PRAYER) * 0.33 * 10)));
			onAntiPoisonEffect(p, false, 500);
		}),

		SUMMONING_POTION(VIAL, new int[] { 12140, 12142, 12144, 12146 }, p -> {
			p.getSkills().adjustStat(7, 0.25, false, Skills.SUMMONING);
			Familiar familiar = p.getFamiliar();
			if (familiar != null)
				familiar.restoreSpecialAttack(15);
		}),

		RECOVER_SPECIAL(VIAL, new int[] { 15300, 15301, 15302, 15303 }, true, p -> {
			p.getDetails().getRecoverSpecialPotion().start(600 * 30);
			p.getCombatDefinitions().restoreSpecialAttack(25);
		}) {
			@Override
			public boolean canDrink(Player player) {
				if (!player.getDetails().getRecoverSpecialPotion().finished()) {
					player.getPackets().sendGameMessage("You may only use this pot every 30 seconds.");
					return false;
				}
				return true;
			}
		},

		SUPER_ANTIFIRE(VIAL, new int[] { 15304, 15305, 15306, 15307 }, true, p -> Combat.effect(p, CombatEffectType.ANTIFIRE_POTION)),
		
		EXTREME_ATTACK(VIAL, new int[] { 15308, 15309, 15310, 15311 }, true, p -> p.getSkills().adjustStat(5, 0.22, Skills.ATTACK)),
		
		EXTREME_STRENGTH(VIAL, new int[] { 15312, 15313, 15314, 15315 }, true, p -> p.getSkills().adjustStat(5, 0.22, Skills.STRENGTH)),
		
		EXTREME_DEFENCE(VIAL, new int[] { 15316, 15317, 15318, 15319 }, true, p -> p.getSkills().adjustStat(5, 0.22, Skills.DEFENCE)),
		
		EXTREME_MAGIC(VIAL, new int[] { 15320, 15321, 15322, 15323 }, true, p -> p.getSkills().adjustStat(7, 0, Skills.MAGIC)),
		
		EXTREME_RANGING(VIAL, new int[] { 15324, 15325, 15326, 15327 }, true, p -> p.getSkills().adjustStat(4, 0.2, Skills.RANGE)),
		
		SUPER_PRAYER(VIAL, new int[] { 15328, 15329, 15330, 15331 }, p -> p.getPrayer().restorePrayer(((int) (70 + (p.getSkills().getTrueLevel(Skills.PRAYER) * 3.43))))),
		DOM_SUPER_PRAYER(-1, new int[] { 22375, 22376 }),

		OVERLOAD(VIAL, new int[] { 15332, 15333, 15334, 15335 }, true, p -> p.getOverloadEffect().applyOverloadEffect()) {
			@Override
			public boolean canDrink(Player player) {
				if(WildernessMapZone.isAtWild(player)) {
					player.getPackets().sendGameMessage("You can't drink this potion in the wilderness.");
					return false;
				}
				if(player.getOverloadEffect() != null) {
					player.getPackets().sendGameMessage("You already have the effects of overload upon you.");
					return false;
				}
				if(player.getHitpoints() <= 500) {
					player.getPackets().sendGameMessage("You don't have enough health to drink this potion.");
					return false;
				}
				return true;
			}
		},
//		JUJU_MINING_POTION(JUJU_VIAL, new int[] { 20003, 20004, 20005, 20006 }, p -> p.addEffect(Effect.JUJU_MINING, 500)),
//		
//		JUJU_COOKING_POTION(JUJU_VIAL, new int[] { 20007, 20008, 20009, 20010 }),
//		
//		JUJU_FARMING_POTION(JUJU_VIAL, new int[] { 20011, 20012, 20013, 20014 }, p -> p.addEffect(Effect.JUJU_FARMING, 500)),
//		
//		JUJU_WOODCUTTING_POTION(JUJU_VIAL, new int[] { 20015, 20016, 20017, 20018 }, p -> p.addEffect(Effect.JUJU_WOODCUTTING, 500)),
//		
//		JUJU_FISHING_POTION(JUJU_VIAL, new int[] { 20019, 20020, 20021, 20022 }, p -> p.addEffect(Effect.JUJU_FISHING, 500)),
//		
//		JUJU_HUNTER_POTION(JUJU_VIAL, new int[] { 20023, 20024, 20025, 20026 }),
//		
//		SCENTLESS_POTION(JUJU_VIAL, new int[] { 20027, 20028, 20029, 20030 }, p -> p.addEffect(Effect.SCENTLESS, 500)),
//		
//		SARADOMINS_BLESSING(JUJU_VIAL, new int[] { 20031, 20032, 20033, 20034 }, p -> p.addEffect(Effect.SARA_BLESSING, 500)),
//		SARADOMINS_BLESSING_FLASK(-1, new int[] { 23173, 23174, 23175, 23176, 23177, 23178 }, p -> p.addEffect(Effect.SARA_BLESSING, 500)),
//
//		GUTHIXS_GIFT(JUJU_VIAL, new int[] { 20035, 20036, 20037, 20038 }, p -> p.addEffect(Effect.GUTHIX_GIFT, 500)),
//		
//		ZAMORAKS_FAVOUR(JUJU_VIAL, new int[] { 20039, 20040, 20041, 20042 }, p -> p.addEffect(Effect.ZAMMY_FAVOR, 500)),

		WEAK_MAGIC_POTION(17490, 17556, p -> p.getSkills().adjustStat(4, 0.1, Skills.MAGIC)),
		WEAK_RANGED_POTION(17490, 17558, p -> p.getSkills().adjustStat(4, 0.1, Skills.RANGE)),
		WEAK_MELEE_POTION(17490, 17560, p -> p.getSkills().adjustStat(4, 0.1, Skills.ATTACK, Skills.STRENGTH)),
		WEAK_DEFENCE_POTION(17490, 17562, p -> p.getSkills().adjustStat(4, 0.1, Skills.DEFENCE)),

		WEAK_GATHERERS_POTION(17490, 17574, p -> p.getSkills().adjustStat(3, 0.02, Skills.WOODCUTTING, Skills.MINING, Skills.FISHING)),
		WEAK_ARTISANS_POTION(17490, 17576, p -> p.getSkills().adjustStat(3, 0.02, Skills.SMITHING, Skills.CRAFTING, Skills.FLETCHING, Skills.CONSTRUCTION, Skills.FIREMAKING)),
		WEAK_NATURALISTS_POTION(17490, 17578, p -> p.getSkills().adjustStat(3, 0.02, Skills.COOKING, Skills.FARMING, Skills.HERBLORE, Skills.RUNECRAFTING)),
		WEAK_SURVIVALISTS_POTION(17490, 17580, p -> p.getSkills().adjustStat(3, 0.02, Skills.AGILITY, Skills.HUNTER, Skills.THIEVING, Skills.SLAYER)),

		MAGIC_POTION_D(17490, 17582, p -> p.getSkills().adjustStat(5, 0.14, Skills.MAGIC)),
		RANGED_POTION(17490, 17584, p -> p.getSkills().adjustStat(5, 0.14, Skills.RANGE)),
		MELEE_POTION(17490, 17586, p -> p.getSkills().adjustStat(5, 0.14, Skills.ATTACK, Skills.STRENGTH)),
		DEFENCE_POTION_D(17490, 17588, p -> p.getSkills().adjustStat(5, 0.14, Skills.DEFENCE)),

		GATHERERS_POTION(17490, 17598, p -> p.getSkills().adjustStat(4, 0.04, Skills.WOODCUTTING, Skills.MINING, Skills.FISHING)),
		ARTISANS_POTION(17490, 17600, p -> p.getSkills().adjustStat(4, 0.04, Skills.SMITHING, Skills.CRAFTING, Skills.FLETCHING, Skills.CONSTRUCTION, Skills.FIREMAKING)),
		NATURALISTS_POTION(17490, 17602, p -> p.getSkills().adjustStat(4, 0.04, Skills.COOKING, Skills.FARMING, Skills.HERBLORE, Skills.RUNECRAFTING)),
		SURVIVALISTS_POTION(17490, 17604, p -> p.getSkills().adjustStat(4, 0.04, Skills.AGILITY, Skills.HUNTER, Skills.THIEVING, Skills.SLAYER)),

		STRONG_MAGIC_POTION(17490, 17606, p -> p.getSkills().adjustStat(6, 0.2, Skills.MAGIC)),
		STRONG_RANGED_POTION(17490, 17608, p -> p.getSkills().adjustStat(6, 0.2, Skills.RANGE)),
		STRONG_MELEE_POTION(17490, 17610, p -> p.getSkills().adjustStat(6, 0.2, Skills.ATTACK, Skills.STRENGTH)),
		STRONG_DEFENCE_POTION(17490, 17612, p -> p.getSkills().adjustStat(6, 0.2, Skills.DEFENCE)),

		STRONG_GATHERERS_POTION(17490, 17622, p -> p.getSkills().adjustStat(6, 0.06, Skills.WOODCUTTING, Skills.MINING, Skills.FISHING)),
		STRONG_ARTISANS_POTION(17490, 17624, p -> p.getSkills().adjustStat(6, 0.06, Skills.SMITHING, Skills.CRAFTING, Skills.FLETCHING, Skills.CONSTRUCTION, Skills.FIREMAKING)),
		STRONG_NATURALISTS_POTION(17490, 17626, p -> p.getSkills().adjustStat(6, 0.06, Skills.COOKING, Skills.FARMING, Skills.HERBLORE, Skills.RUNECRAFTING)),
		STRONG_SURVIVALISTS_POTION(17490, 17628, p -> p.getSkills().adjustStat(6, 0.06, Skills.AGILITY, Skills.HUNTER, Skills.THIEVING, Skills.SLAYER)),

		WEAK_STAT_RESTORE_POTION(17490, 17564, p -> p.getSkills().adjustStat(5, 0.12, false, Skills.allExcept(Skills.PRAYER, Skills.SUMMONING))),
		STAT_RESTORE_POTION(17490, 17590, p -> p.getSkills().adjustStat(7, 0.17, false, Skills.allExcept(Skills.PRAYER, Skills.SUMMONING))),
		STRONG_STAT_RESTORE_POTION(17490, 17614, p -> p.getSkills().adjustStat(10, 0.24, false, Skills.allExcept(Skills.PRAYER, Skills.SUMMONING))),


		WEAK_REJUVENATION_POTION(17490, 17570, p -> {
			p.getSkills().adjustStat(5, 0.10, false, Skills.SUMMONING);
			p.getPrayer().restorePrayer(((int) (Math.floor(p.getSkills().getTrueLevel(Skills.PRAYER)) + 50)));
		}),
		REJUVENATION_POTION(17490, 17594, p -> {
			p.getSkills().adjustStat(7, 0.15, false, Skills.SUMMONING);
			p.getPrayer().restorePrayer(((int) (Math.floor(p.getSkills().getTrueLevel(Skills.PRAYER) * 1.5) + 70)));
		}),
		STRONG_REJUVENATION_POTION(17490, 17618, p -> {
			p.getSkills().adjustStat(10, 0.22, false, Skills.SUMMONING);
			p.getPrayer().restorePrayer(((int) (Math.floor(p.getSkills().getTrueLevel(Skills.PRAYER) * 2.2) + 100)));
		}),

		POISON_CHALICE(2026, 197, p -> {
			//TODO
		}),

		STRANGE_FRUIT(-1, 464, p -> restoreRunEnergy(p, 30)),


		KARAMJAN_RUM(-1, 431, p -> {
			p.getSkills().adjustStat(-4, 0, Skills.ATTACK);
			p.getSkills().adjustStat(-5, 0, Skills.STRENGTH);
			p.heal(50);
		}),
		BANDITS_BREW(BEER_GLASS, 4627, p -> {
			p.getSkills().adjustStat(1, 0, Skills.ATTACK, Skills.THIEVING);
			p.getSkills().adjustStat(-1, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(-6, 0, Skills.DEFENCE);
			p.heal(10);
		}),
		GROG(BEER_GLASS, 1915, p -> {
			p.getSkills().adjustStat(3, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(-6, 0, Skills.ATTACK);
			p.heal(30);
		}),
		BEER(BEER_GLASS, 1917, p -> {
			p.getSkills().adjustStat(0, 0.04, Skills.STRENGTH);
			p.getSkills().adjustStat(0, -0.07, Skills.ATTACK);
			p.heal(10);
		}),
		BEER_FREM(3805, 3803, p -> {
			p.getSkills().adjustStat(0, 0.04, Skills.STRENGTH);
			p.getSkills().adjustStat(0, -0.07, Skills.ATTACK);
			p.heal(10);
		}),
		BEER_POH(BEER_GLASS, 7740, p -> {
			p.getSkills().adjustStat(0, 0.04, Skills.STRENGTH);
			p.getSkills().adjustStat(0, -0.07, Skills.ATTACK);
			p.heal(10);
		}),

		ASGARNIAN_ALE(BEER_GLASS, 1905, p -> {
			p.getSkills().adjustStat(-2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(4, 0, Skills.ATTACK);
			p.heal(20);
		}),
		ASGARNIAN_ALE_POH(BEER_GLASS, 7744, p -> {
			p.getSkills().adjustStat(-2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(4, 0, Skills.ATTACK);
			p.heal(20);
		}),
		ASGARNIAN_ALE_KEG(EMPTY_KEG, new int[] { 5779, 5781, 5783, 5785 }, p -> {
			p.getSkills().adjustStat(-2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(4, 0, Skills.ATTACK);
			p.heal(20);
		}),
		ASGARNIAN_ALE_M(BEER_GLASS, 5739, p -> {
			p.getSkills().adjustStat(-3, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(6, 0, Skills.ATTACK);
			p.heal(20);
		}),
		ASGARNIAN_ALE_M_KEG(EMPTY_KEG, new int[] { 5859, 5861, 5863, 5865 }, p -> {
			p.getSkills().adjustStat(-3, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(6, 0, Skills.ATTACK);
			p.heal(20);
		}),

		MIND_BOMB(BEER_GLASS, 1907, p -> {
			p.getSkills().adjustStat(p.getSkills().getTrueLevel(Skills.MAGIC) >= 50 ? 3 : 2, 0, Skills.MAGIC);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK);
			p.getSkills().adjustStat(-4, 0, Skills.STRENGTH, Skills.DEFENCE);
		}),
		MIND_BOMB_KEG(EMPTY_KEG, new int[] { 5795, 5797, 5799, 5801 }, p -> {
			p.getSkills().adjustStat(p.getSkills().getTrueLevel(Skills.MAGIC) >= 50 ? 3 : 2, 0, Skills.MAGIC);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK);
			p.getSkills().adjustStat(-4, 0, Skills.STRENGTH, Skills.DEFENCE);
		}),
		MIND_BOMB_M(BEER_GLASS, 5741, p -> {
			p.getSkills().adjustStat(p.getSkills().getTrueLevel(Skills.MAGIC) >= 50 ? 4 : 3, 0, Skills.MAGIC);
			p.getSkills().adjustStat(-5, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),
		MIND_BOMB_M_KEG(EMPTY_KEG, new int[] { 5875, 5877, 5879, 5881 }, p -> {
			p.getSkills().adjustStat(p.getSkills().getTrueLevel(Skills.MAGIC) >= 50 ? 4 : 3, 0, Skills.MAGIC);
			p.getSkills().adjustStat(-5, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),

		GREENMANS_ALE(BEER_GLASS, 1909, p -> {
			p.getSkills().adjustStat(1, 0, Skills.HERBLORE);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),
		GREENMANS_ALE_POH(BEER_GLASS, 7746, p -> {
			p.getSkills().adjustStat(1, 0, Skills.HERBLORE);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),
		GREENMANS_ALE_KEG(EMPTY_KEG, new int[] { 5787, 5789, 5791, 5793 }, p -> {
			p.getSkills().adjustStat(1, 0, Skills.HERBLORE);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),
		GREENMANS_ALE_M(BEER_GLASS, 5743, p -> {
			p.getSkills().adjustStat(2, 0, Skills.HERBLORE);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),
		GREENMANS_ALE_M_KEG(EMPTY_KEG, new int[] { 5867, 5869, 5871, 5873 }, p -> {
			p.getSkills().adjustStat(2, 0, Skills.HERBLORE);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),

		DRAGON_BITTER(BEER_GLASS, 1911, p -> {
			p.getSkills().adjustStat(2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(-6, 0, Skills.ATTACK);
			p.heal(10);
		}),
		DRAGON_BITTER_POH(BEER_GLASS, 7748, p -> {
			p.getSkills().adjustStat(2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(-6, 0, Skills.ATTACK);
			p.heal(10);
		}),
		DRAGON_BITTER_KEG(EMPTY_KEG, new int[] { 5803, 5805, 5807, 5809 }, p -> {
			p.getSkills().adjustStat(2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(-6, 0, Skills.ATTACK);
			p.heal(10);
		}),
		DRAGON_BITTER_M(BEER_GLASS, 5745, p -> {
			p.getSkills().adjustStat(2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(-4, 0, Skills.ATTACK);
			p.heal(20);
		}),
		DRAGON_BITTER_M_KEG(EMPTY_KEG, new int[] { 5883, 5885, 5887, 5889 }, p -> {
			p.getSkills().adjustStat(2, 0, Skills.STRENGTH);
			p.getSkills().adjustStat(-4, 0, Skills.ATTACK);
			p.heal(20);
		}),
		DWARVEN_STOUT(BEER_GLASS, 1913, p -> {
			p.getSkills().adjustStat(1, 0, Skills.MINING, Skills.SMITHING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		DWARVEN_STOUT_KEG(EMPTY_KEG, new int[] { 5771, 5773, 5775, 5777 }, p -> {
			p.getSkills().adjustStat(1, 0, Skills.MINING, Skills.SMITHING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		DWARVEN_STOUT_M(BEER_GLASS, 5747, p -> {
			p.getSkills().adjustStat(2, 0, Skills.MINING, Skills.SMITHING);
			p.getSkills().adjustStat(-7, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),
		DWARVEN_STOUT_M_KEG(EMPTY_KEG, new int[] { 5851, 5853, 5855, 5857 }, p -> {
			p.getSkills().adjustStat(2, 0, Skills.MINING, Skills.SMITHING);
			p.getSkills().adjustStat(-7, 0, Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE);
			p.heal(10);
		}),

		MOONLIGHT_MEAD(BEER_GLASS, 2955, p -> p.heal(40)),
		MOONLIGHT_MEAD_POH(BEER_GLASS, 7750, p -> p.heal(40)),
		MOONLIGHT_MEAD_KEG(EMPTY_KEG, new int[] { 5811, 5813, 5815, 5817 }, p -> p.heal(40)),
		MOONLIGHT_MEAD_M(BEER_GLASS, 5749, p -> p.heal(60)),
		MOONLIGHT_MEAD_M_KEG(EMPTY_KEG, new int[] { 5891, 5893, 5895, 5897 }, p -> p.heal(60)),

		AXEMANS_FOLLY(BEER_GLASS, 5751, p -> {
			p.getSkills().adjustStat(1, 0, Skills.WOODCUTTING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		AXEMANS_FOLLY_KEG(EMPTY_KEG, new int[] { 5819, 5821, 5823, 5825 }, p -> {
			p.getSkills().adjustStat(1, 0, Skills.WOODCUTTING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		AXEMANS_FOLLY_M(BEER_GLASS, 5753, p -> {
			p.getSkills().adjustStat(2, 0, Skills.WOODCUTTING);
			p.getSkills().adjustStat(-4, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),
		AXEMANS_FOLLY_M_KEG(EMPTY_KEG, new int[] { 5899, 5901, 5903, 5905 }, p -> {
			p.getSkills().adjustStat(2, 0, Skills.WOODCUTTING);
			p.getSkills().adjustStat(-4, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),

		CHEFS_DELIGHT(BEER_GLASS, 5755, p -> {
			p.getSkills().adjustStat(1, 0.05, Skills.COOKING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		CHEFS_DELIGHT_POH(BEER_GLASS, 7754, p -> {
			p.getSkills().adjustStat(1, 0.05, Skills.COOKING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		CHEFS_DELIGHT_KEG(EMPTY_KEG, new int[] { 5827, 5829, 5831, 5833 }, p -> {
			p.getSkills().adjustStat(1, 0.05, Skills.COOKING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		CHEFS_DELIGHT_M(BEER_GLASS, 5757, p -> {
			p.getSkills().adjustStat(2, 0.05, Skills.COOKING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),
		CHEFS_DELIGHT_M_KEG(EMPTY_KEG, new int[] { 5907, 5909, 5911, 5913 }, p -> {
			p.getSkills().adjustStat(2, 0.05, Skills.COOKING);
			p.getSkills().adjustStat(-3, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),

		SLAYERS_RESPITE(BEER_GLASS, 5759, p -> {
			p.getSkills().adjustStat(2, 0, Skills.SLAYER);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		SLAYERS_RESPITE_KEG(EMPTY_KEG, new int[] { 5835, 5837, 5839, 5841 }, p -> {
			p.getSkills().adjustStat(2, 0, Skills.SLAYER);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		SLAYERS_RESPITE_M(BEER_GLASS, 5761, p -> {
			p.getSkills().adjustStat(4, 0, Skills.SLAYER);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),
		SLAYERS_RESPITE_M_KEG(EMPTY_KEG, new int[] { 5915, 5917, 5919, 5921 }, p -> {
			p.getSkills().adjustStat(4, 0, Skills.SLAYER);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(10);
		}),

		CIDER(BEER_GLASS, 5763, p -> {
			p.getSkills().adjustStat(1, 0, Skills.FARMING);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),
		CIDER_POH(BEER_GLASS, 7752, p -> {
			p.getSkills().adjustStat(1, 0, Skills.FARMING);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),
		CIDER_KEG(EMPTY_KEG, new int[] { 5843, 5845, 5847, 5849 }, p -> {
			p.getSkills().adjustStat(1, 0, Skills.FARMING);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),
		CIDER_M(BEER_GLASS, 5765, p -> {
			p.getSkills().adjustStat(2, 0, Skills.FARMING);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),
		CIDER_M_KEG(EMPTY_KEG, new int[] { 5923, 5925, 5927, 5929 }, p -> {
			p.getSkills().adjustStat(2, 0, Skills.FARMING);
			p.getSkills().adjustStat(-2, 0, Skills.ATTACK, Skills.STRENGTH);
			p.heal(20);
		}),

		SERUM_207(VIAL, new int[] { 3408, 3410, 3412, 3414 }),
		SERUM_208(VIAL, new int[] { 3416, 3417, 3418, 3419 }),
		OLIVE_OIL(VIAL, new int[] { 3422, 3424, 3426, 3428 }),
		SACRED_OIL(VIAL, new int[] { 3430, 3432, 3434, 3436 }),

		;

		private static Map<Integer, Potion> POTS = new HashMap<>();

		static {
			for (Potion pot : Potion.values())
				for (int id : pot.ids)
					POTS.put(id, pot);
		}

		public static Potion forId(int itemId) {
			return POTS.get(itemId);
		}

		public int emptyId;
		private Consumer<Player> effect;
		private int[] ids;

		private Potion(int emptyId, int[] ids, boolean isOP, Consumer<Player> effect) {
			this.emptyId = emptyId;
			this.ids = ids;
			this.effect = effect;
		}

		private Potion(int emptyId, int[] ids, Consumer<Player> effect) {
			this(emptyId, ids, false, effect);
		}

		private Potion(int emptyId, int id, Consumer<Player> effect) {
			this(emptyId, new int[] { id }, false, effect);
		}

		private Potion(int emptyId, int[] ids) {
			this(emptyId, ids, null);
		}

		public boolean canDrink(Player player) {
			return true;
		}

		public final void drink(Player player, int itemId, int slot) {
			Potion pot = forId(itemId);
			if (player.getInventory().getItem(slot) == null || player.getInventory().getItem(slot).getId() != itemId || !player.getDetails().getDrinks().elapsed(2 * 600) || player.getMapZoneManager().execute(zone -> !zone.canPot(player, pot)))
				return;
			if (effect == null) {
				player.getPackets().sendGameMessage("You wouldn't want to drink that.");
				return;
			}
			if (canDrink(player)) {
				int idIdx = -1;
				for (int i = 0;i < ids.length-1;i++)
					if (ids[i] == itemId) {
						idIdx = i;
						break;
					}
				int newId = idIdx == -1 ? emptyId : ids[idIdx+1];
				player.getInventory().getItems().set(slot, newId == -1 ? null : new Item(newId, 1));
				player.getInventory().refresh(slot);
				player.getDetails().getDrinks().reset();
				effect.accept(player);
				player.setNextAnimation(Animations.CONSUMING_ITEM);
				player.getAudioManager().sendSound(Sounds.DRINKING_POTION);
				player.getPackets().sendGameMessage("You drink some of your " + ItemDefinitions.getItemDefinitions(itemId).name.toLowerCase().replace(" (1)", "").replace(" (2)", "").replace(" (3)", "").replace(" (4)", "").replace(" (5)", "").replace(" (6)", "") + ".", true);
			}
		}

		public int[] getIds() {
			return ids;
		}

		public int getMaxDoses() {
			return ids.length;
		}

		public int getIdForDoses(int doses) {
			return ids[ids.length-doses];
		}

		public boolean isVial() {
			return emptyId == VIAL || emptyId == JUJU_VIAL;
		}
	}

	public static int getDoses(Potion pot, Item item) {
		for (int i = pot.ids.length - 1; i >= 0; i--)
			if (pot.ids[i] == item.getId())
				return pot.ids.length - i;
		return 0;
	}
	
	public static void applyOverLoadEffect(Player player) {
		if (WildernessMapZone.isAtWild(player)) {
			int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
			int realLevel = player.getSkills().getTrueLevel(Skills.ATTACK);
			int level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.ATTACK, (int) (level + 5 + (realLevel * 0.15)));

			actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
			realLevel = player.getSkills().getTrueLevel(Skills.STRENGTH);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.STRENGTH, (int) (level + 5 + (realLevel * 0.15)));

			actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
			realLevel = player.getSkills().getTrueLevel(Skills.DEFENCE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.DEFENCE, (int) (level + 5 + (realLevel * 0.15)));

			actualLevel = player.getSkills().getLevel(Skills.MAGIC);
			realLevel = player.getSkills().getTrueLevel(Skills.MAGIC);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.MAGIC, level + 5);

			actualLevel = player.getSkills().getLevel(Skills.RANGE);
			realLevel = player.getSkills().getTrueLevel(Skills.RANGE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.RANGE, (int) (level + 5 + (realLevel * 0.1)));
		} else {
			int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
			int realLevel = player.getSkills().getTrueLevel(Skills.ATTACK);
			int level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.ATTACK, (int) (level + 5 + (realLevel * 0.22)));

			actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
			realLevel = player.getSkills().getTrueLevel(Skills.STRENGTH);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.STRENGTH, (int) (level + 5 + (realLevel * 0.22)));

			actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
			realLevel = player.getSkills().getTrueLevel(Skills.DEFENCE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.DEFENCE, (int) (level + 5 + (realLevel * 0.22)));

			actualLevel = player.getSkills().getLevel(Skills.MAGIC);
			realLevel = player.getSkills().getTrueLevel(Skills.MAGIC);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.MAGIC, level + 7);

			actualLevel = player.getSkills().getLevel(Skills.RANGE);
			realLevel = player.getSkills().getTrueLevel(Skills.RANGE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.RANGE, (int) (level + 4 + (Math.floor(realLevel / 5.2))));
		}
	}
	
	/**
	 * The method that executes the anti-poison potion action.
	 * @param player the player to do this action for.
	 * @param superPotion {@code true} if this potion is a super potion, {@code false}
	 * otherwise.
	 * @param length the length that the effect lingers for.
	 */
	public static void onAntiPoisonEffect(Player player, boolean superPotion, int length) {
		if(player.isPoisoned()) {
			player.getPoisonDamage().set(0);
			player.getVarsManager().sendVar(InterfaceVars.POISIONED_HP_ORB, 0);
			player.getPackets().sendGameMessage("You have been cured of your poison!");
		}
		if(superPotion) {
			if(player.getDetails().getPoisonImmunity().get() <= 0) {
				player.getPackets().sendGameMessage("You have been granted immunity against poison.");
				player.getDetails().getPoisonImmunity().incrementAndGet(length);
				World.get().submit(new Task(50, false) {
					@Override
					public void execute() {
						if(player.getDetails().getPoisonImmunity().get() <= 0)
							this.cancel();
						if(player.getDetails().getPoisonImmunity().decrementAndGet(50) <= 50)
							player.getPackets().sendGameMessage("Your resistance to poison is about to wear off!");
						if(player.getDetails().getPoisonImmunity().get() <= 0)
							this.cancel();
					}
					
					@Override
					public void onCancel() {
						player.getPackets().sendGameMessage("Your resistance to poison has worn off!");
						player.getDetails().getPoisonImmunity().set(0);
					}
				}.attach(player));
			} else if(player.getDetails().getPoisonImmunity().get() > 0) {
				player.getPackets().sendGameMessage("Your immunity against poison has been restored!");
				player.getDetails().getPoisonImmunity().set(length);
			}
		}
	}
	
    public static void restoreRunEnergy(Player player, double energy) {
        if (player.getDetails().getRunEnergy() + energy > 100.0)
        	player.getDetails().setRunEnergy(100);
        else
        	player.getDetails().setRunEnergy(player.getDetails().getRunEnergy() + 1);
    }
}