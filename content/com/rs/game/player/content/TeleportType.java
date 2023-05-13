package com.rs.game.player.content;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TeleportType {
	NORMAL(4, Optional.of(Animations.TELEPORT_NORMAL), Optional.of(Animations.TELEPORT_NORMAL_RETURN), Optional.of(new Graphics(1576)), Optional.of(new Graphics(1577))),
	ANCIENT(3, Optional.of(Animations.TELEPORT_ANCIENT), Optional.empty(), Optional.of(new Graphics(1681)), Optional.empty()),
	LUNAR(5, Optional.of(Animations.TELEPORT_LUNAR), Optional.empty(), Optional.of(new Graphics(1685)), Optional.empty()),
	TABLET(3, Optional.of(Animations.TELE_TAB_SINK_INWARDS), Optional.empty(), Optional.of(new Graphics(678)), Optional.empty()),
	LEVER(4, Optional.of(Animations.TELEPORT_NORMAL), Optional.of(Animations.TELEPORT_NORMAL_RETURN), Optional.of(new Graphics(1576)), Optional.of(new Graphics(1577))),
	LADDER(2, Optional.of(Animations.LADDER_CLIMB), Optional.empty(), Optional.empty(), Optional.empty()),
	DOOR(2, Optional.of(Animations.KNOCKING_ON_DOOR), Optional.empty(), Optional.empty(), Optional.empty()),
	//TODO: Fix animation.
	OBELISK(6, Optional.of(Animations.TELEPORT_NORMAL), Optional.of(Animations.TELEPORT_NORMAL_RETURN), Optional.of(new Graphics(661)), Optional.empty()),
	TRAINING_PORTAL(24, Optional.of(Animations.FADE_AWAY), Optional.of(Animations.FADE_BACK_IN), Optional.of(new Graphics(606)), Optional.empty()),
	BOSS_PORTAL(3, Optional.of(Animations.FADE_AWAY), Optional.of(Animations.FADE_BACK_IN), Optional.of(new Graphics(2128)), Optional.empty()),
	PVP_PORTAL(5, Optional.of(Animations.LEAP_INTO_AIR), Optional.of(Animations.FADE_BACK_IN), Optional.empty(), Optional.empty()),
	BLANK(1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
	
	/**
	 * The ending delay for this teleport.
	 */
	@Getter
	private final int endDelay;
	
	/**
	 * The start animation of this teleport.
	 */
	@Getter
	private final Optional<Animation> startAnimation;
	
	/**
	 * The end animation of this teleport.
	 */
	@Getter
	private final Optional<Animation> endAnimation;
	
	/**
	 * The start graphic of this teleport.
	 */
	@Getter
	private final Optional<Graphics> startGraphic;
	
	/**
	 * The end graphic of this teleport.
	 */
	@Getter
	private final Optional<Graphics> endGraphic;
}