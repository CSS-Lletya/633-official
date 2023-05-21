package com.rs.constants;

import com.rs.net.encoders.other.Graphics;

/**
 * A collection of useful type-safe Graphics designed to provide better
 * readability and code base flow.
 * 
 * TODO: Summoning, Skillcape, Combat/Magic Graphics (combat needs rework, so it can be left alone)
 * 
 * @author Dennis
 *
 */

public final class Graphic {
	
	public static final Graphics RESET_GRAPHICS = new Graphics(-1);

	/**
	 * Player Based Animations
	 */
	public static final Graphics OBELISK_SENDING = new Graphics(661);
	public static final Graphics HEALING_BARRIER = new Graphics(436);
	public static final Graphics LEVEL_UP_BASIC = new Graphics(199);//need to find 99 mastery
	public static final Graphics ANCIENT_TELEPORT = new Graphics(436);
	public static final Graphics LUNAR_TELEPORT = new Graphics(1685);
	public static final Graphics TELETAB_BREAKING_PORTAL = new Graphics(678);
	public static final Graphics MODERN_TELEPORTING = new Graphics(1576);
	public static final Graphics CLOUD_COVERING_PLAYER_RAPIDLY = new Graphics(606);
	public static final Graphics RED_WHITE_BEAMS_COVERING_PLAYER_RAPIDLY = new Graphics(2128);
	public static final Graphics SOULSPLIT = new Graphics(2264);
	public static final Graphics OVERLOAD_SHOCKING = new Graphics(560);
	public static final Graphics ECTOPHIAL_LIQUID = new Graphics(1688);
	public static final Graphics CURSES_PROTECT_ITEM = new Graphics(2213);
	public static final Graphics CURSES_BUFF = new Graphics(2266);
	public static final Graphics CURSES_TURMOIL = new Graphics(2226);
	public static final Graphics TELETAB_BREAKING_SPARKS = new Graphics(1680);
	public static final Graphics BLOW_KISS = new Graphics(1702);
	public static final Graphics ZOMBIE_HAND = new Graphics(1244);
	public static final Graphics AIR_GUITAR = new Graphics(1537);
	public static final Graphics SAFETY_FIRST = new Graphics(1553);
	public static final Graphics EXPLORE = new Graphics(1734);
	public static final Graphics TRICK = new Graphics(1864);
	public static final Graphics FREEZE = new Graphics(1973);
	public static final Graphics SMALL_CLOUD_COVERING_PLAYER = new Graphics(86);
	public static final Graphics AROUND_THE_WORLD_IN_EGGTY_DAYS = new Graphics(2037);
	public static final Graphics PUPPET_MASTER = new Graphics(2837);
	public static final Graphics TASK_MASTER = new Graphics(2930);
	public static final Graphics RUNECRAFTING = new Graphics(186);
}