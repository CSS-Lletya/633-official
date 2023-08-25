package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ForceMovement;
import com.rs.utilities.Direction;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome advanced pole swinging", stage = 8, object = 43529, duration = 16, levelRequired = 85, completionExperience = 25)
public class GnomeAdvancedPoleSwinging implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		World.get().submit(new Task(1) {
			int stage;

			@Override
			protected void execute() {
				stage++;
				if (stage == 0)
					player.faceObject(object);
				else if (stage == 1) {
					player.setNextAnimation(new Animation(11784));
					player.setNextForceMovement(new ForceMovement(player.getTile(), 0,
							new WorldTile(player.getX(), 3421, 3), 1, Direction.NORTH));
				} else if (stage == 2) {
					player.setNextAnimation(new Animation(11785));
					player.setNextWorldTile(new WorldTile(player.getX(), 3421, 3));
					player.setNextForceMovement(new ForceMovement(new WorldTile(player.getX(), 3421, 3), 0,
							new WorldTile(player.getX(), 3425, 3), 1, Direction.NORTH));
				} else if (stage == 3) {
					player.setNextAnimation(new Animation(11789));
					player.setNextWorldTile(new WorldTile(player.getX(), 3425, 3));
				} else if (stage == 6)
					player.setNextForceMovement(new ForceMovement(new WorldTile(player.getX(), 3425, 3), 1,
							new WorldTile(player.getX(), 3429, 3), 2, Direction.NORTH));
				else if (stage == 10) {
					player.setNextWorldTile(new WorldTile(player.getX(), 3429, 3));
				} else if (stage == 11) {
					player.setNextForceMovement(new ForceMovement(new WorldTile(player.getX(), 3429, 3), 1,
							new WorldTile(player.getX(), 3432, 3), 2, Direction.NORTH));
				} else if (stage == 13) {
					player.setNextWorldTile(new WorldTile(player.getX(), 3432, 3));
					cancel();
				}
			}
		});
	}

	@Override
	public void end(Player player, GameObject object) {
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}