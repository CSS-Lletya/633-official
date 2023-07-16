package skills.magic;

import java.util.Optional;

import com.rs.GameConstants;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TeleportType {
	NORMAL(4, Optional.of(Animations.TELEPORT_NORMAL), Optional.of(Animations.TELEPORT_NORMAL_RETURN), Optional.of(Graphic.MODERN_TELEPORTING), Optional.of(new Graphics(1577)), Optional.empty()),
	ANCIENT(3, Optional.of(Animations.TELEPORT_ANCIENT), Optional.empty(), Optional.of(Graphic.ANCIENT_TELEPORT), Optional.empty(), Optional.empty()),
	LUNAR(5, Optional.of(Animations.TELEPORT_LUNAR), Optional.empty(), Optional.of(Graphic.LUNAR_TELEPORT), Optional.empty(), Optional.empty()),
	TABLET(-1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(SpecialEvent.TABLET)),
	LEVER(4, Optional.of(Animations.TELEPORT_NORMAL), Optional.of(Animations.TELEPORT_NORMAL_RETURN), Optional.of(Graphic.MODERN_TELEPORTING), Optional.of(new Graphics(1577)), Optional.empty()),
	LADDER(2, Optional.of(Animations.LADDER_CLIMB), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
	DOOR(2, Optional.of(Animations.KNOCKING_ON_DOOR), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
	//TODO: Fix animation.
	OBELISK(6, Optional.of(Animations.TELEPORT_NORMAL), Optional.of(Animations.TELEPORT_NORMAL_RETURN), Optional.of(Graphic.OBELISK_SENDING), Optional.empty(), Optional.empty()),
	TRAINING_PORTAL(24, Optional.of(Animations.FADE_AWAY), Optional.of(Animations.FADE_BACK_IN), Optional.of(Graphic.CLOUD_COVERING_PLAYER_RAPIDLY), Optional.empty(), Optional.empty()),
	BOSS_PORTAL(3, Optional.of(Animations.FADE_AWAY), Optional.of(Animations.FADE_BACK_IN), Optional.of(Graphic.RED_WHITE_BEAMS_COVERING_PLAYER_RAPIDLY), Optional.empty(), Optional.empty()),
	PVP_PORTAL(5, Optional.of(Animations.LEAP_INTO_AIR), Optional.of(Animations.FADE_BACK_IN), Optional.empty(), Optional.empty(), Optional.empty()),
	BLANK(1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
	MODERN_HOME(1, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(SpecialEvent.HOME)),
	;
	
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
	
	@Getter
	private final Optional<SpecialEvent> specialEvent;
	
	public void checkSpecialCondition(Player player, WorldTile destination) {
		specialEvent.ifPresent(p -> p.handleSpecialEvent(player, destination));
	}
}

/**
 * Executes a special event that contains specific time-based occurances within the single event
 * @author Dennis
 *
 */
enum SpecialEvent {
	TABLET {
		@Override
		protected boolean handleSpecialEvent(Player player, WorldTile destination) {
			World.get().submit(new Task(1) {
				int tick;
				@Override
				protected void execute() {
					switch(tick++) {
					case 0:
						player.setNextAnimation(Animations.BREAK_TELETAB);
						break;
					case 1:
						player.getAudioManager().sendSound(Sounds.TELETAB_BREAKING); 
						break;
					case 2:
						player.setNextAnimation(Animations.TELE_TAB_SINK_INWARDS);
						player.setNextGraphics(Graphic.TELETAB_BREAKING_PORTAL);
						break;
					case 3:
						player.setNextWorldTile(destination);
						break;
					case 4:
						player.setNextAnimation(Animations.RESET_ANIMATION);
						player.setNextGraphics(Graphic.RESET_GRAPHICS);
						cancel();
						break;
					}
				}
			});
			return true;
		}
	},
	HOME {
		@Override
		protected boolean handleSpecialEvent(Player player, WorldTile destination) {
			if (!player.getDetails().getHomeDelay().finished()) {
				int minutes = player.getDetails().getHomeDelay().getMinutes();
				player.getPackets().sendGameMessage("You need to wait another " + minutes  + " " + (minutes == 1 ? "minute" : "minutes") + " to cast this spell.");
				return false;
			}
			World.get().submit(new Task(1) {
				int ticks;
				private final int[] ANIMATIONS = { 1722, 1723, 1724, 1725, 2798, 2799, 2800, 3195, 4643, 4645, 4646, 4847, 4848,
						4849, 4850, 4851, 4852 };
				private int[] GRAPHICS = {
						800, 801, 802, 1703, 1704, 1705, 1706, 1707, 1708, 1709, 1710, 1711, 1712, 1713 };
				@Override
				protected void execute() {
					if (ticks++ == 0) {
						player.setNextAnimation(new Animation(ANIMATIONS[0]));
						player.setNextGraphics(new Graphics(GRAPHICS[0]));
					} else if (ticks == 1) {
						player.setNextGraphics(new Graphics(GRAPHICS[0]));
						player.setNextAnimation(new Animation(ANIMATIONS[1]));
					} else if (ticks == 2) {
						player.setNextGraphics(new Graphics(GRAPHICS[1]));
						player.setNextAnimation(new Animation(ANIMATIONS[2]));
					} else if (ticks == 3) {
						player.setNextGraphics(new Graphics(GRAPHICS[2]));
						player.setNextAnimation(new Animation(ANIMATIONS[3]));
					} else if (ticks == 4) {
						player.setNextGraphics(new Graphics(GRAPHICS[3]));
						player.setNextAnimation(new Animation(ANIMATIONS[4]));
					} else if (ticks == 5) {
						player.setNextGraphics(new Graphics(GRAPHICS[3]));
						player.setNextAnimation(new Animation(ANIMATIONS[5]));
					} else if (ticks == 6) {
						player.setNextGraphics(new Graphics(GRAPHICS[3]));
						player.setNextAnimation(new Animation(ANIMATIONS[6]));
					} else if (ticks == 7) {
						player.setNextGraphics(new Graphics(GRAPHICS[4]));
						player.setNextAnimation(new Animation(ANIMATIONS[7]));
					} else if (ticks == 8) {
						player.setNextGraphics(new Graphics(GRAPHICS[5]));
						player.setNextAnimation(new Animation(ANIMATIONS[8]));
					} else if (ticks == 9) {
						player.setNextGraphics(new Graphics(GRAPHICS[6]));
						player.setNextAnimation(new Animation(ANIMATIONS[9]));
					} else if (ticks == 10) {
						player.setNextGraphics(new Graphics(GRAPHICS[7]));
						player.setNextAnimation(new Animation(ANIMATIONS[10]));
					} else if (ticks == 11) {
						player.setNextGraphics(new Graphics(GRAPHICS[8]));
						player.setNextAnimation(new Animation(ANIMATIONS[11]));
					} else if (ticks == 12) {
						player.setNextGraphics(new Graphics(GRAPHICS[9]));
						player.setNextAnimation(new Animation(ANIMATIONS[12]));
					} else if (ticks == 13) {
						player.setNextGraphics(new Graphics(GRAPHICS[10]));
						player.setNextAnimation(new Animation(ANIMATIONS[13]));
					} else if (ticks == 14) {
						player.setNextGraphics(new Graphics(GRAPHICS[11]));
						player.setNextAnimation(new Animation(ANIMATIONS[14]));
					} else if (ticks == 15) {
						player.setNextGraphics(new Graphics(GRAPHICS[12]));
						player.setNextAnimation(new Animation(ANIMATIONS[15]));
					} else if (ticks == 15) {
						player.setNextGraphics(new Graphics(GRAPHICS[13]));
						player.setNextAnimation(new Animation(ANIMATIONS[16]));
					} else if (ticks == 16) {
						player.setNextWorldTile(GameConstants.START_PLAYER_LOCATION);
						player.setNextGraphics(Graphic.RESET_GRAPHICS);
						player.setNextAnimation(Animations.RESET_ANIMATION);
						player.getDetails().getHomeDelay().start(60 * 30);
						cancel();
					}
				}
			});
			return true;
		}
	}
	;
	
	/**
	 * Executes a special event that contains specific time-based occurances within the single event
	 * @param player
	 * @param destination
	 * @return
	 */
	protected boolean handleSpecialEvent(Player player, WorldTile destination) {
		return false;
	}
}