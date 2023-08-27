package skills.agility.gnome;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Appearance;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.ForceMovement;
import com.rs.utilities.Direction;
import com.rs.utilities.MutableNumber;

import skills.Skills;
import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome advanced barrier jumping", stage = 7, object = 43539, duration = 6, levelRequired = 85, completionExperience = 25)
public class GnomeAdvancedBarrierJumping implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		final WorldTile toTile = new WorldTile(2485, 3436, 0);
		World.get().submit(new Task(1) {
			int ticks;
			@Override
			protected void execute() {
				switch(ticks++) {
				case 0:
					player.setNextAnimation(Animations.GNOME_BARRIER_JUMP);
					break;
				case 1:
					player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, Direction.NORTH));
					break;
				case 3:
					player.getAppearance().transformIntoNPC(Appearance.SHADOW_NPC);
					break;
				case 4:
					player.setNextWorldTile(toTile);
					break;
				case 6:
					player.getAppearance().transformIntoNPC(Appearance.RESET_AS_NPC);
					player.setNextAnimation(Animations.GNOME_BARRIER_END);
					break;
				}
			}
		});
	}

	@Override
	public void end(Player player, GameObject object) {
		player.getSkills().addExperience(Skills.AGILITY, 605);
		player.getDetails().getStatistics().addStatistic("Agility_Advanced_Courses_Completed");
		player.getDetails().getStatistics().addStatistic("Agility_Advanced_Gnome_Completed");
	}

	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}