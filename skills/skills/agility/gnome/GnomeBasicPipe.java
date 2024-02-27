package skills.agility.gnome;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Appearance;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.net.encoders.other.ForceMovement;
import com.rs.utilities.Direction;
import com.rs.utilities.MutableNumber;

import skills.Skills;
import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome basic pipe crawling", stage = 7, object = { 43543, 43544 }, duration = 5, levelRequired = 1, completionExperience = 7.5)
public class GnomeBasicPipe implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		int objectX = object.getX();
		int objectY = object.getY();
		if (objectY == 3435) {
			return;
		}
		player.addWalkSteps(objectX, objectY - 1, -1, false);
		player.getPackets().sendGameMessage("You pull yourself through the pipes.", true);
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(2, () -> player.setNextForceMovement(new ForceMovement(player, 1,
				new WorldTile(objectX, player.getY() + 3, 0), 4, Direction.NORTH)));
		seq.connect(1, () -> player.setNextAnimation(Animations.GNOME_PIPE_CRAWLING));
		seq.connect(2, () -> {
			player.getAppearance().transformIntoNPC(Appearance.SHADOW_NPC);
			player.setNextWorldTile(new WorldTile(objectX, player.getY() + 3, 0));
		});
		seq.connect(2, () -> {
			player.setNextForceMovement(new ForceMovement(player, 1,
					new WorldTile(objectX, player.getY() + 4, 0), 5, Direction.NORTH));
		});
		seq.connect(3, () -> {
			player.getAppearance().transformIntoNPC(Appearance.RESET_AS_NPC);
			player.setNextAnimation(Animations.GNOME_PIPE_CRAWLING);
			player.setNextWorldTile(new WorldTile(objectX, player.getY() + 4, 0));
		}).start();
	}

	@Override
	public void end(Player player, GameObject object) {
		player.getSkills().addExperience(Skills.AGILITY, 39);
		player.getDetails().getStatistics().addStatistic("Agility_Courses_Completed");
		player.getDetails().getStatistics().addStatistic("Agility_Gnome_Completed");
		player.getDetails().getGnomeAgilityStage().set(0);
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}