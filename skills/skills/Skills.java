package skills;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.constants.InterfaceVars;
import com.rs.game.player.Player;
import com.rs.utilities.Utility;

import lombok.Data;

/**
 * Represents a Player's Skill attributes
 * 
 * @author Dennis
 *
 */
@Data
public class Skills {

	/**
	 * The maximum amount of experience a Player can achieve in a Skill
	 */
	public static final int MAXIMUM_EXP = 200000000;
	
	public static int MAXIMUM_SKILL_COUNT = 25;

	/**
	 * Simple Skill name with value constants
	 */
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6,
			COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13,
			MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20,
			CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23, DUNGEONEERING = 24;

	/**
	 * Simple Skill names
	 */
	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Constitution", "Ranging", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
			"Summoning", "Dungeoneering" };

	/**
	 * An array of the levels the Player can level-up in
	 */
	private int level[];

	/**
	 * An array of xp that the player can gain in the skill
	 */
	private double xp[];

	/**
	 * Represents the XP Counter value itself
	 */
	private int xpCounter;

	/**
	 * Represents the Player
	 */
	private transient Player player;

	/**
	 * Constructs a new Skill load-out for new Players
	 */
	public Skills() {
		level = new int[25];
		xp = new double[25];
		IntStream.range(0, 25).forEach(skills -> {
			level[skills] = 1;
			xp[skills] = 0;
		});
		level[HITPOINTS] = 10;
		xp[HITPOINTS] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
		xpCounter = 1434;

		enabledSkillsTargets = new boolean[25];
		skillsTargetsUsingLevelMode = new boolean[25];
		skillsTargetsValues = new int[25];
	}

	/**
	 * Restores all the Skills to their original state
	 */
	public void restoreSkills() {
		IntStream.range(0, 25).forEach(skill -> {
			level[skill] = (byte) getLevelForXp(skill);
			refresh(skill);
		});
	}

	/**
	 * Gets the level of a Skill
	 * 
	 * @param skill
	 * @return level
	 */
	public int getLevel(int skill) {
		return level[skill];
	}

	/**
	 * Gets the xp of a Skill
	 * 
	 * @param skill
	 * @return xp
	 */
	public double getXp(int skill) {
		return xp[skill];
	}

	/**
	 * Sets the Skill level to a new one
	 * 
	 * @param skill
	 * @param newLevel
	 */
	public void set(int skill, int newLevel) {
		level[skill] = (byte) newLevel;
		refresh(skill);
	}

	/**
	 * Sets the xp of a Skill to a new one
	 * 
	 * @param skill
	 * @param exp
	 */
	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}

	/**
	 * Gets the combat level with summoning combat levels added
	 * 
	 * @return combat level
	 */
	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}

	/**
	 * Gets the summoning combat level itself
	 * 
	 * @return
	 */
	public int getSummoningCombatLevel() {
		return getLevelForXp(Skills.SUMMONING) / 8;
	}

	/**
	 * Calculates the Player's base combat level
	 * 
	 * @return
	 */
	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}

	/**
	 * Drains the Player's Level to specified amount
	 * 
	 * @param skill
	 * @param drain
	 * @return
	 */
	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	/**
	 * Drains the Player's Summoning level by specified amount
	 * 
	 * @param amt
	 */
	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	/**
	 * Gets the xp for the level
	 * 
	 * @param level
	 * @return xp
	 */
	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * Gets the level for the xp
	 * 
	 * @param level
	 * @return level
	 */
	public int getLevelForXp(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}

	/**
	 * Initializes the Player's Skills on login
	 */
	public void init() {
		IntStream.range(0, 25).forEach(level -> refresh(level));
		refreshXpCounter();
		refreshEnabledSkillsTargets();
        refreshUsingLevelTargets();
        refreshSkillsTargetsValues();
	}

	/**
	 * Refreshes the specified Skill
	 * 
	 * @param skill
	 */
	public void refresh(int skill) {
		player.getPackets().sendSkillLevel(skill);
	}

	/**
	 * Determines if your level is greater than or equal to {@code level}.
	 * 
	 * @param level the level to compare against this one.
	 * @return {@code true} if this level is greater than or equal to the other one,
	 *         {@code false} otherwise.
	 */
	public boolean reqLevel(int level) {
		return this.level[level] >= level;
	}

	/**
	 * Adds experience to the specified Skill
	 * 
	 * @param skill
	 * @param exp
	 * @return
	 */
	public double addXp(int skill, double exp) {
		int rate = skill == ATTACK || skill == STRENGTH || skill == DEFENCE || skill == HITPOINTS || skill == MAGIC
				|| skill == RANGE || skill == SUMMONING ? GameConstants.COMBAT_XP_RATE : GameConstants.XP_RATE;
		exp *= rate;
		return addXpNormal(skill, exp);
	}

	/**
	 * Adds normal xp rates based on interaction (typically combat)
	 * 
	 * @param skill
	 * @param exp
	 * @return xp
	 */
	public double addXpNormal(int skill, double exp) {
		if (player.getDetails().isXpLocked())
			return 0;
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		xpCounter += exp;
		refreshXpCounter();

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		gainedLevels = levelDiff;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			LevelUp.advanceLevel(player, skill, gainedLevels);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearance().generateAppearenceData();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 10);
				else if (skill == PRAYER)
					player.getPrayer().restorePrayer(levelDiff * 10);
			}
		}
		refresh(skill);
		return exp;
	}

	/**
	 * Adds xp via a experience lamp
	 * 
	 * @param skill
	 * @param exp
	 * @return xp
	 */
	public double addXpLamp(int skill, double exp) {
		if (player.getDetails().isXpLocked())
			return 0;
		exp *= GameConstants.LAMP_XP_RATE;
		int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		xpCounter += exp;
		refreshXpCounter();

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skill);
		int levelDiff = newLevel - oldLevel;
		gainedLevels = levelDiff;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			LevelUp.advanceLevel(player, skill, gainedLevels);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearance().generateAppearenceData();
				if (skill == HITPOINTS)
					player.heal(levelDiff * 10);
				else if (skill == PRAYER)
					player.getPrayer().restorePrayer(levelDiff * 10);
			}
		}
		refresh(skill);
		return exp;
	}

	/**
	 * Represents the amount of gained levels when leveling up In this case xp rate
	 * in-game is fast so we can see for example: gained 3 levels or 1 level message
	 */
	private int gainedLevels;

	/**
	 * Refreshes the xp counter value
	 */
	public void refreshXpCounter() {
		player.getVarsManager().sendVar(InterfaceVars.SKILLS_REFRESH_XP_COUNTER, (int) (xpCounter * 10D));
	}

	/**
	 * Resets the xp counter back to 0
	 */
	public void resetXpCounter() {
		xpCounter = 0;
		refreshXpCounter();
	}

	/**
	 * Levelup sounds 1 - 99
	 */
	public enum Musics {
		ATTACK(29, 30, 0), STRENGTH(65, 66, 2), DEFENCE(37, 38, 1), HITPOINTS(47, 48, 3), RANGE(57, 58, 4),
		MAGIC(51, 52, 6), PRAYER(55, 56, 5), AGILITY(28, 322, 16), HERBLORE(45, 46, 15), THIEVING(67, 68, 17),
		CRAFTING(35, 36, 12), RUNECRAFTING(59, 60, 20), MINING(53, 54, 14), SMITHING(63, 64, 13), FISHING(41, 42, 10),
		COOKING(33, 34, 7), FIREMAKING(39, 40, 11), WOODCUTTING(69, 70, 8), FLETCHING(43, 44, 9), SLAYER(61, 62, 18),
		FARMING(11, 10, 19), CONSTRUCTION(31, 32, 22), HUNTER(49, 50, 21), SUMMONING(300, 301, 23),
		DUNGEONEERING(416, 417, 24);

		private int id;
		private int id2;
		private int skill;

		private Musics(int id, int id2, int skill) {
			this.id = id;
			this.id2 = id2;
			this.skill = skill;
		}

		public int getId() {
			return id;
		}

		public int getId2() {
			return id2;
		}

		private static Map<Integer, Musics> musics = new HashMap<Integer, Musics>();

		public static Musics levelup(int skill) {
			return musics.get(skill);
		}

		static {
			for (Musics music : Musics.values()) {
				musics.put(music.skill, music);
			}
		}
	}

	/**
	 * Represents the total level of the player
	 */
	private transient int totallevel;

	/**
	 * Gets the total level of a Player note: might need to be tweaked
	 * 
	 * @param player
	 * @return level
	 */
	public int getTotalLevel(Player player) {
		IntStream.range(0, 25).forEach(level -> totallevel += player.getSkills().getLevelForXp(level));
		return totallevel;
	}

	private transient boolean[] leveledUp = new boolean[25];

	public int getTargetIdByComponentId(int componentId) {
		int[] mappings = {200, 11, 52, 93, 28, 193, 76, 19, 36, 60, 84, 110, 186, 179, 44, 68, 172, 165, 101, 118, 126, 134, 142, 150, 158};
		for (int i = 0; i < mappings.length; i++) {
			if (mappings[i] == componentId) {
				return i;
			}
		}
		return -1;
	}

	public int getSkillIdByTargetId(int targetId) {
		int[] mappings = {ATTACK, STRENGTH, RANGE, MAGIC, DEFENCE, HITPOINTS, PRAYER, AGILITY, HERBLORE, THIEVING, CRAFTING, RUNECRAFTING, MINING, SMITHING, FISHING, COOKING, FIREMAKING, WOODCUTTING, FLETCHING, SLAYER, FARMING, CONSTRUCTION, HUNTER, SUMMONING, DUNGEONEERING};
		if (targetId >= 0 && targetId < mappings.length) {
			return mappings[targetId];
		} else {
			return -1;
		}
	}
	
    public boolean[] enabledSkillsTargets;
    public boolean[] skillsTargetsUsingLevelMode;
    public int[] skillsTargetsValues;
    
    public void refreshEnabledSkillsTargets() {
        int value = Utility.get32BitValue(enabledSkillsTargets, true);
        player.getVarsManager().sendVar(InterfaceVars.SKILL_TARGETS, value);
    }

    public void refreshUsingLevelTargets() {
        int value = Utility.get32BitValue(skillsTargetsUsingLevelMode, true);
        player.getVarsManager().sendVar(InterfaceVars.SKILL_TARGET_LEVEL_MODE, value);
    }

    public void refreshSkillsTargetsValues() {
        for (int skill = 0; skill < 25; skill++) {
            player.getVarsManager().sendVar(InterfaceVars.SKILL_TARGET_VALUES + skill, skillsTargetsValues[skill]);
        }
    }

    public void setSkillTargetEnabled(int id, boolean enabled) {
        enabledSkillsTargets[id] = enabled;
        refreshEnabledSkillsTargets();
    }

    public void setSkillTargetUsingLevelMode(int id, boolean using) {
        skillsTargetsUsingLevelMode[id] = using;
        refreshUsingLevelTargets();
    }

    public void setSkillTargetValue(int skillId, int value) {
        skillsTargetsValues[skillId] = value;
        refreshSkillsTargetsValues();
    }

    public void setSkillTarget(boolean usingLevel, int skillId, int target) {
        setSkillTargetEnabled(skillId, true);
        setSkillTargetUsingLevelMode(skillId, usingLevel);
        setSkillTargetValue(skillId, target);
    }
    
	public void adjustStat(int baseMod, double mul, int... skills) {
		for (int i : skills)
			adjustStat(baseMod, mul, true, i);
	}

	public void adjustStat(int baseMod, double mul, boolean boost, int... skills) {
		for (int i : skills)
			adjustStat(baseMod, mul, boost, i);
	}

	public void adjustStat(int baseMod, double mul, boolean boost, int skill) {
		int realLevel = getLevelForXp(skill);
		int realBoost = (int) (baseMod + (getLevel(skill) * mul));
		if (realBoost < 0)
			realLevel = getLevel(skill);
		int maxBoost = (int) (realLevel + (baseMod + (realLevel * mul)));
		level[skill] = (short) Utility.clampI(level[skill] + realBoost, 0, boost ? maxBoost : (getLevel(skill) > realLevel ? getLevel(skill) : realLevel));
		refresh(skill);
	}
	
	public static int[] allExcept(int... exclude) {
		int[] skills = new int[1+SKILL_NAME.length-exclude.length];
		int idx = 0;
		for (int i = 0;i < SKILL_NAME.length;i++) {
			for (int ex : exclude)
				if (i == ex)
					continue;
			skills[idx++] = i;
		}
		return skills;
	}
}