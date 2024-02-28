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
	 * Player Based Graphics
	 */
	public static final Graphics OBELISK_SENDING = new Graphics(661);
	public static final Graphics HEALING_BARRIER = new Graphics(436);
	public static final Graphics LEVEL_UP = new Graphics(199);//need to find 99 mastery
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
	public static final Graphics RUNECRAFTING = new Graphics(186, 0, 95);
	public static final Graphics BONES_TO_SPELL = new Graphics(141,0, 96);
	public static final Graphics CHARGE_SPELL = new Graphics(6,0, 96);
	public static final Graphics SMOKE_APPEARING = new Graphics(74);
	public static final Graphics THREE_RING_TELEPORTING = new Graphics(110, 0, 100);
	public static final Graphics SORCERESSS_GARDEN_BROOMSTICK = new Graphics(1866);
	public static final Graphics HUMIDIFY_SPELL = new Graphics(1061);
	public static final Graphics HUNTER_KIT = new Graphics(1074);
	public static final Graphics DREAM = new Graphics(1056);
	public static final Graphics NPC_CONTACT = new Graphics(730, 0, 130);
	public static final Graphics SUPERGLASS_MAKE = new Graphics(729, 0, 120);
	public static final Graphics MAGIC_IMBUE = new Graphics(141, 0, 96);
	public static final Graphics LUNAR_CURE_ME = new Graphics(731, 0, 90);
	public static final Graphics LUNAR_VENG = new Graphics(726, 0, 96);
	public static final Graphics LUNAR_VENG_OTHER = new Graphics(725, 0, 96);
	public static final Graphics SPELLBOOK_SWAP = new Graphics(1062);
	public static final Graphics STUN_GRAPHIC = new Graphics(80, 100, 50);
	/**
	 * Object Based Graphics
	 */
	public static final Graphics BONE_ON_ALTER = new Graphics(186);
	
	/**
	 * NPC Based Graphics
	 */
	public static final Graphics SMALL_TELEPORTING_RINGS_VIA_HANDS = new Graphics(108);
	
	/**
	 * Other/Tile
	 */
	public static final Graphics TELEGRAB_SPELL = new Graphics(144);
	public static final Graphics WILDY_ZAMORAK_MAGE_TELEPORT = new Graphics(4);
}