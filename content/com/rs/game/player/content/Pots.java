package com.rs.game.player.content;

import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Combat;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.type.CombatEffectType;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.Utility;

import skills.Skills;

public final class Pots {

	public static final int VIAL = 229;

	public static enum Pot {

		ATTACK_POTION(new int[] { 2428, 121, 123, 125 }, Effects.ATTACK_POTION),

		STRENGTH_POTION(new int[] { 113, 115, 117, 119 }, Effects.STRENGTH_POTION),

		DEFENCE_POTION(new int[] { 2432, 133, 135, 137 }, Effects.DEFENCE_POTION),

		RANGE_POTION(new int[] { 2444, 169, 171, 173 }, Effects.RANGE_POTION),

		MAGIC_POTION(new int[] { 3040, 3042, 3044, 3046 }, Effects.MAGIC_POTION),

		MAGIC_FLASK(new int[] { 23423, 23425, 23427, 23429, 23431, 23433 }, Effects.MAGIC_POTION),

		ANTI_POISION(new int[] { 2446, 175, 177, 179 }, Effects.ANTIPOISON),
		SUPER_ANTI_POISION(new int[] { 2448, 181, 183, 185 }, Effects.SUPER_ANTIPOISON),

		PRAYER_POTION(new int[] { 2434, 139, 141, 143 }, Effects.PRAYER_POTION),

		SUPER_ATT_POTION(new int[] { 2436, 145, 147, 149 }, Effects.SUPER_ATT_POTION),

		SUPER_STR_POTION(new int[] { 2440, 157, 159, 161 }, Effects.SUPER_STR_POTION),

		SUPER_DEF_POTION(new int[] { 2442, 163, 165, 167 }, Effects.SUPER_DEF_POTION),

		ENERGY_POTION(new int[] { 3008, 3010, 3012, 3014 }, Effects.ENERGY_POTION),

		SUPER_ENERGY(new int[] { 3016, 3018, 3020, 3022 }, Effects.SUPER_ENERGY),

		EXTREME_ATT_POTION(new int[] { 15308, 15309, 15310, 15311 }, Effects.EXTREME_ATT_POTION),

		EXTREME_STR_POTION(new int[] { 15312, 15313, 15314, 15315 }, Effects.EXTREME_STR_POTION),

		EXTREME_DEF_POTION(new int[] { 15316, 15317, 15318, 15319 }, Effects.EXTREME_DEF_POTION),

		EXTREME_MAGE_POTION(new int[] { 15320, 15321, 15322, 15323 }, Effects.EXTREME_MAG_POTION),

		EXTREME_RANGE_POTION(new int[] { 15324, 15325, 15326, 15327 }, Effects.EXTREME_RAN_POTION),

		SUPER_RESTORE_POTION(new int[] { 3024, 3026, 3028, 3030 }, Effects.SUPER_RESTORE),

		SARADOMIN_BREW(new int[] { 6685, 6687, 6689, 6691 }, Effects.SARADOMIN_BREW),

		RECOVER_SPECIAL(new int[] { 15300, 15301, 15302, 15303 }, Effects.RECOVER_SPECIAL),

		SUPER_PRAYER(new int[] { 15328, 15329, 15330, 15331 }, Effects.SUPER_PRAYER),

		OVERLOAD(new int[] { 15332, 15333, 15334, 15335 }, Effects.OVERLOAD),

		ANTI_FIRE(new int[] { 2452, 2454, 2456, 2458 }, Effects.ANTI_FIRE),

		SUMMONING_POTION(new int[] { 12140, 12142, 12144, 12146 }, Effects.SUMMONING_POT),

		SUMMONING_FLASK(new int[] { 23621, 23623, 23625, 23627, 23629, 23631 }, Effects.SUMMONING_POT),

		SANFEW_SERUM(new int[] { 10925, 10927, 10929, 10931 }, Effects.SANFEW_SERUM),

		ATTACK_FLASK(new int[] { 23195, 23197, 23199, 23201, 23203, 23205 }, Effects.ATTACK_POTION),

		STRENGTH_FLASK(new int[] { 23207, 23209, 23211, 23213, 23215, 23217 }, Effects.STRENGTH_POTION),

		RESTORE_FLASK(new int[] { 23219, 23221, 23223, 23225, 23227, 23229 }, Effects.RESTORE_POTION),

		DEFENCE_FLASK(new int[] { 23231, 23233, 23235, 23237, 23239, 23241 }, Effects.DEFENCE_POTION),

		PRAYER_FLASK(new int[] { 23243, 23245, 23247, 23249, 23251, 23253 }, Effects.PRAYER_POTION),

		SUPER_ATTACK_FLASK(new int[] { 23255, 23257, 23259, 23261, 23263, 23265 }, Effects.SUPER_ATT_POTION),

		FISHING_FLASK(new int[] { 23267, 23269, 23271, 23273, 23275, 23277 }, Effects.FISHING_POTION),

		SUPER_STRENGTH_FLASK(new int[] { 23279, 23281, 23283, 23285, 23287, 23289 }, Effects.SUPER_STR_POTION),

		SUPER_DEFENCE_FLASK(new int[] { 23291, 23293, 23295, 23297, 23299, 23301 }, Effects.SUPER_DEF_POTION),

		RANGING_FLASK(new int[] { 23303, 23305, 23307, 23309, 23311, 23313 }, Effects.RANGE_POTION),

		ANTIPOISON_FLASK(new int[] { 23315, 23317, 23319, 23321, 23323, 23325 }, Effects.ANTIPOISON),

		SUPER_ANTIPOISON_FLASK(new int[] { 23327, 23329, 23331, 23333, 23335, 23337 }, Effects.SUPER_ANTIPOISON),

		/*
		 * ZAMORAK_BREW_FLASK(new int[] { 23339, 23341, 23343, 23345, 23347, 23349 },
		 * null),
		 */

		SARADOMIN_BREW_FLASK(new int[] { 23351, 23353, 23355, 23357, 23359, 23361 }, Effects.SARADOMIN_BREW),

		ANTIFIRE_FLASK(new int[] { 23363, 23365, 23367, 23369, 23371, 23373 }, Effects.ANTI_FIRE),

		ENERGY_FLASK(new int[] { 23375, 23377, 23379, 23381, 23383, 23385 }, Effects.ENERGY_POTION),

		SUPER_ENERGY_FLASK(new int[] { 23387, 23389, 23391, 23393, 23395, 23397 }, Effects.SUPER_ENERGY),

		SUPER_RESTORE_FLASK(new int[] { 23399, 23401, 23403, 23405, 23407, 23409 }, Effects.SUPER_RESTORE),

		/*
		 * AGILITY_FLASK(new int[] { 23411, 23413, 23415, 23417, 23419, 23421 }, null),
		 * 
		 * MAGIC_FLASK(new int[] { 23423, 23425, 23427, 23429, 23431, 23433 }, null),
		 * 
		 * HUNTER_FLASK(new int[] { 23435, 23437, 23439, 23441, 23443, 23445 }, null),
		 * 
		 * COMBAT_FLASK(new int[] { 23447, 23449, 23451, 23453, 23455, 23457 }, null),
		 * 
		 * CRAFTING_FLASK(new int[] { 23459, 23461, 23463, 23465, 23467, 23469 }, null),
		 * 
		 * FLETCHING_FLASK(new int[] { 23471, 23473, 23475, 23477, 23479, 23481 },
		 * null),
		 */

		RECOVER_SPECIAL_FLASK(new int[] { 23483, 23484, 23485, 23486, 23487, 23488 }, Effects.RECOVER_SPECIAL),

		/*
		 * SUPER_ANTIFIRE_FLASK(new int[] { 23489, 23490, 23491, 23492, 23493, 23494 },
		 * null),
		 */

		EXTREME_ATTACK_FLASK(new int[] { 23495, 23496, 23497, 23498, 23499, 23500 }, Effects.EXTREME_ATT_POTION),

		EXTREME_STRENGTH_FLASK(new int[] { 23501, 23502, 23503, 23504, 23505, 23506 }, Effects.EXTREME_STR_POTION),

		EXTREME_DEFENCE_FLASK(new int[] { 23507, 23508, 23509, 23510, 23511, 23512 }, Effects.EXTREME_DEF_POTION),

		EXTREME_MAGIC_FLASK(new int[] { 23513, 23514, 23515, 23516, 23517, 23518 }, Effects.EXTREME_MAG_POTION),

		EXTREME_RANGING_FLASK(new int[] { 23519, 23520, 23521, 23522, 23523, 23524 }, Effects.EXTREME_RAN_POTION),

		SUPER_PRAYER_FLASK(new int[] { 23525, 23526, 23527, 23528, 23529, 23530 }, Effects.SUPER_PRAYER),

		OVERLOAD_FLASK(new int[] { 23531, 23532, 23533, 23534, 23535, 23536 }, Effects.OVERLOAD),

		BEER(new int[] { 1917, 1919 }, Effects.BEER),

		JUG(new int[] { 1993, 1935 }, Effects.WINE),

		/*
		 * RELICYMS_BALM_FLASK(new int[] { 23537, 23539, 23541, 23543, 23545, 23547 },
		 * null),
		 * 
		 * SERUM_207_FLASK(new int[] { 23549, 23550, 23551, 23552, 23553, 23554 },
		 * null),
		 * 
		 * GUTHIX_BALANCE_FLASK(new int[] { 23555, 23557, 23559, 23561, 23563, 23565 },
		 * null),
		 */

		SANFEW_SERUM_FLASK(new int[] { 23567, 23569, 23571, 23573, 23575, 23577 }, Effects.SANFEW_SERUM),

		/*
		 * ANTIPOISON_PLUS_FLASK(new int[] { 23579, 23581, 23583, 23585, 23587, 23589 },
		 * null),
		 * 
		 * ANTIPOISON_PLUS_PLUS_FLASK(new int[] { 23591, 23593, 23595, 23597, 23599,
		 * 23601 }, null),
		 * 
		 * SERUM_208_FLASK(new int[] { 23603, 23604, 23605, 23606, 23607, 23608 }, null)
		 * 
		 * /*MAGIC_ESSENCE_FLASK(new int[] { 23633, 23634, 23635, 23636, 23637, 23638 },
		 * null)
		 */;

		private int[] id;
		private Effects effect;

		private Pot(int[] id, Effects effect) {
			this.id = id;
			this.effect = effect;
		}

		public boolean isFlask() {
			return getMaxDoses() == 6;
		}

		public boolean isPotion() {
			return getMaxDoses() == 4;
		}

		public int getMaxDoses() {
			return id.length;
		}

		public int getIdForDoses(int doses) {
			return id[getMaxDoses() - doses];
		}
	}

	private enum Effects {
		ATTACK_POTION(Skills.ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		FISHING_POTION(Skills.FISHING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (level + 3);
			}
		},
		ZAMORAK_BREW(Skills.ATTACK) {
			int toAdd;

			@Override
			public void extra(Player player) {
				toAdd = (player.getSkills().getLevelForXp(Skills.ATTACK));
				player.getSkills().set(Skills.ATTACK, toAdd);
			}

		},
		SANFEW_SERUM(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGE, Skills.AGILITY,
				Skills.COOKING, Skills.CRAFTING, Skills.FARMING, Skills.FIREMAKING, Skills.FISHING, Skills.FLETCHING,
				Skills.HERBLORE, Skills.MINING, Skills.RUNECRAFTING, Skills.SLAYER, Skills.SMITHING, Skills.THIEVING,
				Skills.WOODCUTTING, Skills.SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel)
					return actualLevel;
				if (actualLevel + boost > realLevel)
					return realLevel;
				return actualLevel + boost;
			}

			@Override
			public void extra(Player player) {
				player.getPrayer()
						.restorePrayer((int) ((int) (player.getSkills().getLevelForXp(Skills.PRAYER) * 0.33 * 10)));
//				player.getDetails().addPoisonImmune(180000);
				// TODO DISEASE HEALING
			}

		},
		SUMMONING_POT(Skills.SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int restore = (int) (Math.floor(player.getSkills().getLevelForXp(Skills.SUMMONING) * 0.25) + 7);
				if (actualLevel + restore > realLevel)
					return realLevel;
				return actualLevel + restore;
			}

			@Override
			public void extra(Player player) {
				Familiar familiar = player.getFamiliar();
				if (familiar != null)
					familiar.restoreSpecialAttack(15);
			}
		},
		ANTIPOISON() {
			@Override
			public void extra(Player player) {
				onAntiPoisonEffect(player, false, 250);
			}
		},
		SUPER_ANTIPOISON() {
			@Override
			public void extra(Player player) {
				onAntiPoisonEffect(player, true, 500);
			}
		},
		ENERGY_POTION() {
			@Override
			public void extra(Player player) {
				double restoredEnergy = player.getDetails().getRunEnergy() + 20;
				player.getMovement().setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
			}
		},
		SUPER_ENERGY() {
			@Override
			public void extra(Player player) {
				double restoredEnergy = (player.getDetails().getRunEnergy() + 40);
				player.getMovement().setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
			}
		},
		ANTI_FIRE() {
			@Override
			public void extra(final Player player) {
				onAntiFireEffect(player, false);
			}
		},
		STRENGTH_POTION(Skills.STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		DEFENCE_POTION(Skills.DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		RANGE_POTION(Skills.RANGE) {

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.1));
			}
		},
		MAGIC_POTION(Skills.MAGIC) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return level + 5;
			}
		},
		PRAYER_POTION() {
			@Override
			public void extra(Player player) {
				player.getPrayer().restorePrayer(
						(int) ((int) (Math.floor(player.getSkills().getLevelForXp(Skills.PRAYER) * 2.5) + 70)));
			}
		},
		SUPER_STR_POTION(Skills.STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SUPER_DEF_POTION(Skills.DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SUPER_ATT_POTION(Skills.ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		EXTREME_STR_POTION(Skills.STRENGTH) {

			@Override
			public boolean canDrink(Player player) {
//				if (player.getControllerManager().getController() instanceof Wilderness) {
//					player.getPackets().sendGameMessage("You cannot drink this potion here.");
//					return false;
//				}
				return true;
			}

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_DEF_POTION(Skills.DEFENCE) {

			@Override
			public boolean canDrink(Player player) {
//				if (player.getControllerManager().getController() instanceof Wilderness) {
//					player.getPackets().sendGameMessage("You cannot drink this potion here.");
//					return false;
//				}
				return true;
			}

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_ATT_POTION(Skills.ATTACK) {

			@Override
			public boolean canDrink(Player player) {
//				if (player.getControllerManager().getController() instanceof Wilderness) {
//					player.getPackets().sendGameMessage("You cannot drink this potion here.");
//					return false;
//				}
				return true;
			}

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_RAN_POTION(Skills.RANGE) {

			@Override
			public boolean canDrink(Player player) {
//				if (player.getControllerManager().getController() instanceof Wilderness) {
//					player.getPackets().sendGameMessage("You cannot drink this potion here.");
//					return false;
//				}
				return true;
			}

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 4 + (Math.floor(realLevel / 5.2)));
			}
		},
		EXTREME_MAG_POTION(Skills.MAGIC) {

			@Override
			public boolean canDrink(Player player) {
//				if (player.getControllerManager().getController() instanceof Wilderness) {
//					player.getPackets().sendGameMessage("You cannot drink this potion here.");
//					return false;
//				}
				return true;
			}

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return level + 7;
			}
		},
		RECOVER_SPECIAL() {

			@Override
			public boolean canDrink(Player player) {
//				if (player.getControllerManager().getController() instanceof Wilderness) {
//					player.getPackets().sendGameMessage("You cannot drink this potion here.");
//					return false;
//				}
				Long time = (Long) player.getAttributes().get(Attribute.RECOVER_SPECIAL_POT).get();
				if (time != null && Utility.currentTimeMillis() - time < 30000) {
					player.getPackets().sendGameMessage("You may only use this pot every 30 seconds.");
					return false;
				}
				return true;
			}

			@Override
			public void extra(Player player) {
				player.getAttributes().get(Attribute.RECOVER_SPECIAL_POT).set(Utility.currentTimeMillis());
				player.getCombatDefinitions().restoreSpecialAttack(25);
			}
		},
		SARADOMIN_BREW("You drink some of the foul liquid.", Skills.ATTACK, Skills.DEFENCE, Skills.STRENGTH,
				Skills.MAGIC, Skills.RANGE) {

			@Override
			public boolean canDrink(Player player) {
				return true;
			}

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				if (skillId == Skills.DEFENCE) {
					int boost = (int) (realLevel * 0.25);
					int level = actualLevel > realLevel ? realLevel : actualLevel;
					return level + boost;
				} else {
					return (int) (actualLevel * 0.90);
				}
			}

			@Override
			public void extra(Player player) {
				int hitpointsModification = (int) (player.getMaxHitpoints() * 0.15);
				player.heal(hitpointsModification + 20, hitpointsModification);
			}
		},

		OVERLOAD() {

			@Override
			public boolean canDrink(Player player) {
//				if (player.getControllerManager().getController() instanceof Wilderness) {
//					player.getPackets().sendGameMessage("You cannot drink this potion here.");
//					return false;
//				}
				/*
				 * if (player.getOverloadDelay() > 0) { player.getPackets().sendGameMessage(
				 * "You may only use this potion every five minutes."); return false; }
				 */
				if (player.getHitpoints() <= 500 /* || player.getOverloadDelay() > 480 */) {
					player.getPackets()
							.sendGameMessage("You need more than 500 life points to survive the power of overload.");
					return false;
				}
				return true;
			}

			@Override
			public void extra(final Player player) {
				//		player.setOverloadDelay(501);
				World.get().submit(new Task(2) {
					int count = 4;
					@Override
					protected void execute() {
						if (count == 0)
							this.cancel();
						player.setNextAnimation(new Animation(3170));
						player.setNextGraphics(new Graphics(560));
						player.applyHit(new Hit(player, 100, HitLook.REGULAR_DAMAGE, 0));
						count--;
						this.cancel();
					}
				});
			}
		},
		SUPER_PRAYER() {
			@Override
			public void extra(Player player) {
				player.getPrayer()
						.setPrayerpoints((int) ((int) (70 + (player.getSkills().getLevelForXp(Skills.PRAYER) * 3.43))));
			}
		},
		SUPER_RESTORE(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.MAGIC, Skills.RANGE, Skills.AGILITY,
				Skills.COOKING, Skills.CRAFTING, Skills.FARMING, Skills.FIREMAKING, Skills.FISHING, Skills.FLETCHING,
				Skills.HERBLORE, Skills.MINING, Skills.RUNECRAFTING, Skills.SLAYER, Skills.SMITHING, Skills.THIEVING,
				Skills.WOODCUTTING, Skills.SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel)
					return actualLevel;
				if (actualLevel + boost > realLevel)
					return realLevel;
				return actualLevel + boost;
			}

			@Override
			public void extra(Player player) {
				player.getPrayer()
						.restorePrayer((int) ((int) (player.getSkills().getLevelForXp(Skills.PRAYER) * 0.33 * 10)));
			}

		},
		RESTORE_POTION(Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE, Skills.PRAYER) {

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel)
					return actualLevel;
				if (actualLevel + boost > realLevel)
					return realLevel;
				return actualLevel + boost;
			}
		},
		BEER(Skills.ATTACK, Skills.STRENGTH) {

			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				if (skillId == Skills.STRENGTH) {
					int boost = (int) (realLevel * 0.07);
					if (actualLevel > realLevel)
						return actualLevel;
					if (actualLevel + boost > realLevel)
						return realLevel;
					return actualLevel + boost;
				} else {
					return (int) (actualLevel * 0.90);
				}
			}

			@Override
			public void extra(Player player) {
				player.getPackets().sendGameMessage("You drink the beer. You feel slightly reinvigorated...");
				player.getPackets().sendGameMessage("...and slightly dizzy too.");
			}
		},
		WINE(Skills.ATTACK) {
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				return (int) (actualLevel * 0.90);
			}

			@Override
			public void extra(Player player) {
				player.getPackets().sendGameMessage("You drink the wine. You feel slightly reinvigorated...");
				player.getPackets().sendGameMessage("...and slightly dizzy too.");
				player.heal(70);
			}
		};

		private int[] affectedSkills;
		private String drinkMessage;

		private Effects(int... affectedSkills) {
			this(null, affectedSkills);
		}

		private Effects(String drinkMessage, int... affectedSkills) {
			this.drinkMessage = drinkMessage;
			this.affectedSkills = affectedSkills;
		}

		public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
			return actualLevel;
		}

		public boolean canDrink(Player player) {
			// usualy unused
			return true;
		}

		public void extra(Player player) {
			// usualy unused
		}
	}

	public static Pot getPot(int id) {
		for (Pot pot : Pot.values())
			for (int potionId : pot.id) {
				if (id == potionId)
					return pot;
			}
		return null;
	}

	public static int getDoses(Pot pot, Item item) {
		for (int i = pot.id.length - 1; i >= 0; i--) {
			if (pot.id[i] == item.getId())
				return pot.id.length - i;
		}
		return 0;
	}

	public static boolean mixPot(Player player, Item fromItem, Item toItem, int fromSlot, int toSlot) {
		if (fromItem.getId() == VIAL || toItem.getId() == VIAL) {
			Pot pot = getPot(fromItem.getId() == VIAL ? toItem.getId() : fromItem.getId());
			if (pot == null || pot.isFlask())
				return false;
			int doses = getDoses(pot, fromItem.getId() == VIAL ? toItem : fromItem);
			if (doses == 1) {
				player.getInventory().switchItem(fromSlot, toSlot);
				player.getPackets().sendGameMessage("You pour from one container into the other.", true);
				return true;
			}
			int vialDoses = doses / 2;
			doses -= vialDoses;
			player.getInventory().getItems().set(fromItem.getId() == VIAL ? toSlot : fromSlot,
					new Item(pot.getIdForDoses(doses), 1));
			player.getInventory().getItems().set(fromItem.getId() == VIAL ? fromSlot : toSlot,
					new Item(pot.getIdForDoses(vialDoses), 1));
			player.getInventory().refresh(fromSlot);
			player.getInventory().refresh(toSlot);
			player.getPackets().sendGameMessage("You split the potion between the two vials.", true);
			return true;
		}
		Pot pot = getPot(fromItem.getId());
		if (pot == null)
			return false;
		int doses2 = getDoses(pot, toItem);
		if (doses2 == 0 || doses2 == pot.getMaxDoses()) // not same pot type or
			// full already
			return false;
		int doses1 = getDoses(pot, fromItem);
		doses2 += doses1;
		doses1 = doses2 > pot.getMaxDoses() ? doses2 - pot.getMaxDoses() : 0;
		doses2 -= doses1;
		if (doses1 == 0 && pot.isFlask())
			player.getInventory().deleteItem(fromSlot, fromItem);
		else {
			player.getInventory().getItems().set(fromSlot, new Item(doses1 > 0 ? pot.getIdForDoses(doses1) : VIAL, 1));
			player.getInventory().refresh(fromSlot);
		}
		player.getInventory().getItems().set(toSlot, new Item(pot.getIdForDoses(doses2), 1));
		player.getInventory().refresh(toSlot);
		player.getPackets().sendGameMessage("You pour from one container into the other"
				+ (pot.isFlask() && doses1 == 0 ? " and the glass shatters to pieces." : "."));
		return true;
	}

	public static boolean emptyPot(Player player, Item item, int slot) {
		Pot pot = getPot(item.getId());
		if (pot == null || pot.isFlask())
			return false;
		item.setId(VIAL);
		player.getInventory().refresh(slot);
		player.getPackets().sendGameMessage("You empty the vial.", true);
		return true;
	}

	public static boolean pot(Player player, Item item, int slot) {
		Pot pot = getPot(item.getId());
		if (pot == null)
			return false;
		if (!player.getDetails().getDrinks().elapsed(1800)) {
			return false;
		}
		if (player.getMapZoneManager().execute(player, controller -> !controller.canPot(player, pot))) {
			return false;
		}
		if (!pot.effect.canDrink(player))
			return true;
		player.getDetails().getDrinks().reset();
		pot.effect.extra(player);
		int dosesLeft = getDoses(pot, item) - 1;
		if (dosesLeft == 0 && pot.isFlask())
			player.getInventory().deleteItem(slot, item);
		else {
			player.getInventory().getItems().set(slot, new Item(
					dosesLeft > 0 ? pot.getIdForDoses(dosesLeft) : pot.isPotion() ? VIAL : getReplacedId(pot), 1));
			player.getInventory().refresh(slot);
		}
		for (int skillId : pot.effect.affectedSkills)
			player.getSkills().set(skillId, pot.effect.getAffectedSkill(player, skillId,
					player.getSkills().getLevel(skillId), player.getSkills().getLevelForXp(skillId)));
		player.setNextAnimationNoPriority(new Animation(829));
		player.getPackets().sendSound(4580, 0, 1);
		if (pot.isFlask() || pot.isPotion()) {
			player.getPackets().sendGameMessage(pot.effect.drinkMessage != null ? pot.effect.drinkMessage
					: "You drink some of your "
							+ item.getDefinitions().getName().toLowerCase().replace(" (1)", "").replace(" (2)", "")
									.replace(" (3)", "").replace(" (4)", "").replace(" (5)", "").replace(" (6)", "")
							+ ".",
					true);
			player.getPackets()
					.sendGameMessage(dosesLeft == 0
							? "You have finished your " + (pot.isFlask() ? "flask and the glass shatters to pieces."
									: pot.isPotion() ? "potion." : item.getName().toLowerCase() + ".")
							: "You have " + dosesLeft + " dose of potion left.", true);
		}
		return true;
	}

	@SuppressWarnings("incomplete-switch")
	private static int getReplacedId(Pot pot) {
		switch (pot) {
		case JUG:
			return 1935;
		case BEER:
			return 1919;
		}
		return 0;
	}

	public static void resetOverLoadEffect(Player player) {
		if (!player.isDead()) {
			int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
			int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
			if (actualLevel > realLevel)
				player.getSkills().set(Skills.ATTACK, realLevel);
			actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
			realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
			if (actualLevel > realLevel)
				player.getSkills().set(Skills.STRENGTH, realLevel);
			actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
			realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
			if (actualLevel > realLevel)
				player.getSkills().set(Skills.DEFENCE, realLevel);
			actualLevel = player.getSkills().getLevel(Skills.MAGIC);
			realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
			if (actualLevel > realLevel)
				player.getSkills().set(Skills.MAGIC, realLevel);
			actualLevel = player.getSkills().getLevel(Skills.RANGE);
			realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
			if (actualLevel > realLevel)
				player.getSkills().set(Skills.RANGE, realLevel);
			player.heal(500);
		}
//		player.setOverloadDelay(0);
		player.getPackets()
				.sendGameMessage("<col=480000>The effects of overload have worn off and you feel normal again.");
	}

	public static void applyOverLoadEffect(Player player) {
//		if (player.getControllerManager().getController() instanceof Wilderness) {
//			int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
//			int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
//			int level = actualLevel > realLevel ? realLevel : actualLevel;
//			player.getSkills().set(Skills.ATTACK, (int) (level + 5 + (realLevel * 0.15)));
//
//			actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
//			realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
//			level = actualLevel > realLevel ? realLevel : actualLevel;
//			player.getSkills().set(Skills.STRENGTH, (int) (level + 5 + (realLevel * 0.15)));
//
//			actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
//			realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
//			level = actualLevel > realLevel ? realLevel : actualLevel;
//			player.getSkills().set(Skills.DEFENCE, (int) (level + 5 + (realLevel * 0.15)));
//
//			actualLevel = player.getSkills().getLevel(Skills.MAGIC);
//			realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
//			level = actualLevel > realLevel ? realLevel : actualLevel;
//			player.getSkills().set(Skills.MAGIC, level + 5);
//
//			actualLevel = player.getSkills().getLevel(Skills.RANGE);
//			realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
//			level = actualLevel > realLevel ? realLevel : actualLevel;
//			player.getSkills().set(Skills.RANGE, (int) (level + 5 + (realLevel * 0.1)));
//		} else {
			int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
			int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
			int level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.ATTACK, (int) (level + 5 + (realLevel * 0.22)));

			actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
			realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.STRENGTH, (int) (level + 5 + (realLevel * 0.22)));

			actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
			realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.DEFENCE, (int) (level + 5 + (realLevel * 0.22)));

			actualLevel = player.getSkills().getLevel(Skills.MAGIC);
			realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.MAGIC, level + 7);

			actualLevel = player.getSkills().getLevel(Skills.RANGE);
			realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(Skills.RANGE, (int) (level + 4 + (Math.floor(realLevel / 5.2))));
//		}
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
			player.getPackets().sendGlobalConfig(102, 0); //isn't right method to call orb change
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
	
	/**
	 * The method that executes the anti-fire potion action.
	 * @param player the player to do this action for.
	 * @param superVariant determines if this potion is the super variant.
	 */
	private static void onAntiFireEffect(Player player, boolean superVariant) {
		player.getPackets().sendGameMessage("You take a sip of the" + (superVariant ? " super" : "") + " antifire potion.");
		Combat.effect(player, superVariant ? CombatEffectType.SUPER_ANTIFIRE_POTION : CombatEffectType.ANTIFIRE_POTION);
	}
}