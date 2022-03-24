package com.rs.game.player.content;

import java.util.Optional;

import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import lombok.Getter;

public enum TeleportType {
	NORMAL(4, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphics(1576)), Optional.of(new Graphics(1577))),
	ANCIENT(3, Optional.of(new Animation(9599)), Optional.empty(), Optional.of(new Graphics(1681)), Optional.empty()),
	LUNAR(5, Optional.of(new Animation(9606)), Optional.empty(), Optional.of(new Graphics(1685)), Optional.empty()),
	TABLET(3, Optional.of(new Animation(4731)), Optional.empty(), Optional.of(new Graphics(678)), Optional.empty()),
	LEVER(4, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphics(1576)), Optional.of(new Graphics(1577))),
	LADDER(2, Optional.of(new Animation(828)), Optional.empty(), Optional.empty(), Optional.empty()),
	DOOR(2, Optional.of(new Animation(9105)), Optional.empty(), Optional.empty(), Optional.empty()),
	//TODO: Fix animation.
	OBELISK(6, Optional.of(new Animation(8939)), Optional.of(new Animation(8941)), Optional.of(new Graphics(661)), Optional.empty()),
	VOID_FAMILIAR(2, Optional.of(new Animation(8136)), Optional.of(new Animation(8137)), Optional.of(new Graphics(1503)), Optional.of(new Graphics(1502))),
	FREEZE(8, Optional.of(new Animation(11044)), Optional.empty(), Optional.of(new Graphics(1973)), Optional.empty()),
	TRAINING_PORTAL(24, Optional.of(new Animation(10100)), Optional.of(new Animation(9013)), Optional.of(new Graphics(606)), Optional.empty()),
	BOSS_PORTAL(3, Optional.of(new Animation(10100)), Optional.of(new Animation(9013)), Optional.of(new Graphics(2128)), Optional.empty()),
	PVP_PORTAL(5, Optional.of(new Animation(9602)), Optional.of(new Animation(9013)), Optional.empty(), Optional.empty()),
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
	
	/**
	 * Constructs a new {@link TeleportType}.
	 * @param startDelay {@link #startDelay}.
	 * @param endDelay {@link #endDelay}.
	 * @param startAnimation {@link #startAnimation}.
	 * @param endAnimation {@link #endAnimation}.
	 * @param startGraphic {@link #startGraphic}.
	 * @param endGraphic {@link #endGraphic}.
	 */
	private TeleportType(int endDelay, Optional<Animation> startAnimation, Optional<Animation> endAnimation, Optional<Graphics> startGraphic, Optional<Graphics> endGraphic) {
		this.endDelay = endDelay;
		this.startAnimation = startAnimation;
		this.endAnimation = endAnimation;
		this.startGraphic = startGraphic;
		this.endGraphic = endGraphic;
	}
}