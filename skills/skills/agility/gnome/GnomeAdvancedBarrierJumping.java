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

@AgilitySignature(info = "Gnome advanced barrier jumping", stage = 7, object = 43539, duration = 6, levelRequired = 85, completionExperience = 25)
public class GnomeAdvancedBarrierJumping implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		final WorldTile toTile = new WorldTile(2485, 3436, 0);
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(1, () -> player.setNextAnimation(Animations.GNOME_BARRIER_JUMP));
		seq.connect(1, () -> player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, Direction.NORTH)));
		seq.connect(1, () -> player.getAppearance().transformIntoNPC(Appearance.SHADOW_NPC));
		seq.connect(1, () -> player.setNextWorldTile(toTile));
		seq.connect(1, () -> {
			player.getAppearance().transformIntoNPC(Appearance.RESET_AS_NPC);
			player.setNextAnimation(Animations.GNOME_BARRIER_END);
		}).start();
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