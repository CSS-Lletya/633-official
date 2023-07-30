package skills.magic;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.Sounds;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.HomeTeleporting;
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
			player.getAction().setAction(new HomeTeleporting());
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