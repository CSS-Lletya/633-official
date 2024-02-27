package skills.agility.gnome;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.net.encoders.other.ForceMovement;
import com.rs.utilities.Direction;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome advanced pole swinging", stage = 6, object = 43529, duration = 16, levelRequired = 85, completionExperience = 25)
public class GnomeAdvancedPoleSwinging implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		LinkedTaskSequence seq = new LinkedTaskSequence();
		System.out.println("?");
		seq.connect(1, () -> {
			player.setNextAnimation(Animations.GNOME_RUNNING_IN_PLACE);
			player.faceObject(object);
		});
		seq.connect(1, () -> player.setNextForceMovement(new ForceMovement(player.getTile(), 0,
				new WorldTile(player.getX(), 3421, 3), 1, Direction.NORTH)));
		seq.connect(2, () -> {
			player.setNextAnimation(Animations.GNOME_JUMP_TO_PIPE_A);
			player.setNextWorldTile(new WorldTile(player.getX(), 3421, 3));
			player.setNextForceMovement(new ForceMovement(new WorldTile(player.getX(), 3421, 3), 0,
					new WorldTile(player.getX(), 3425, 3), 1, Direction.NORTH));
		});
		seq.connect(1, () -> {
			player.setNextAnimation(Animations.GNOME_SWINING_ON_PIPE_A);
			player.setNextWorldTile(new WorldTile(player.getX(), 3425, 3));
		});
		seq.connect(3, () -> {
			player.setNextForceMovement(new ForceMovement(new WorldTile(player.getX(), 3425, 3), 1,
					new WorldTile(player.getX(), 3429, 3), 2, Direction.NORTH));
		});
		seq.connect(4, () -> player.setNextWorldTile(new WorldTile(player.getX(), 3429, 3)));
		seq.connect(1, () -> {
			player.setNextForceMovement(new ForceMovement(new WorldTile(player.getX(), 3429, 3), 1,
					new WorldTile(player.getX(), 3432, 3), 2, Direction.NORTH));
		});
		seq.connect(2, () -> player.setNextWorldTile(new WorldTile(player.getX(), 3432, 3)))
		.start();
	}

	@Override
	public void end(Player player, GameObject object) {
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}