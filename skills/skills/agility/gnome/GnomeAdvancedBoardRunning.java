package skills.agility.gnome;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.movement.route.RouteEvent;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.ForceMovement;
import com.rs.utilities.Direction;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome advanced board running", stage = 5, object = 43581, duration = 4, levelRequired = 85, completionExperience = 25)
public class GnomeAdvancedBoardRunning implements Obstacle {

	final WorldTile toTile = new WorldTile(2484, 3418, 3);
	
	@Override
	public void start(Player player, GameObject object) {
		player.setNextAnimation(Animations.GNOME_BOARD_RUNNING);
		player.setRouteEvent(new RouteEvent(new WorldTile(2476, 3418, 3), () -> {
			player.getPackets().sendGameMessage("You skilfully run across the Board", true);
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, Direction.NORTH));
		}));
		
	}

	@Override
	public void end(Player player, GameObject object) {
		player.setNextWorldTile(toTile);
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}