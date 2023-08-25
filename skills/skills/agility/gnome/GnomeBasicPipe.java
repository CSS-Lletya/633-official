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
		World.get().submit(new Task(1) {
			int ticks;
			@Override
			protected void execute() {
				ticks++;
				if (ticks == 2) {
					player.setNextForceMovement(new ForceMovement(player, 1,
							new WorldTile(objectX, player.getY() + 3, 0), 4, Direction.NORTH));
				} else if (ticks == 3) {
					player.setNextAnimation(new Animation(10580));
				} else if (ticks == 5) {
					player.getAppearance().transformIntoNPC(266);
					player.setNextWorldTile(new WorldTile(objectX, player.getY() + 3, 0));
				} else if (ticks == 7) {
					player.setNextForceMovement(new ForceMovement(player, 1,
							new WorldTile(objectX, player.getY() + 4, 0), 5, Direction.NORTH));
				} else if (ticks == 10) {
					player.getAppearance().transformIntoNPC(-1);
					player.setNextAnimation(new Animation(10580));
					player.setNextWorldTile(new WorldTile(objectX, player.getY() + 4, 0));
					cancel();
				}
			}
		});
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