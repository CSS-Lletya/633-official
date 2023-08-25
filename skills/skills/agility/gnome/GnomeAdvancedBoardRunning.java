package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ForceMovement;
import com.rs.utilities.Direction;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome advanced board running", stage = 7, object = 43581, duration = 4, levelRequired = 85, completionExperience = 25)
public class GnomeAdvancedBoardRunning implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		player.setNextAnimation(new Animation(2922));
		final WorldTile toTile = new WorldTile(2484, 3418, object.getPlane());
		player.getPackets().sendGameMessage("You skilfully run across the Board", true);
		player.setNextForceMovement(new ForceMovement(new WorldTile(2477, 3418, 3), 1, toTile, 3, Direction.SOUTHEAST));
		player.task(3, p -> player.setNextWorldTile(toTile));
	}

	@Override
	public void end(Player player, GameObject object) {
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}