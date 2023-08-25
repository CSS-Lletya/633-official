package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome rope walking", stage = 4, object = 2312, duration = 6, levelRequired = 1, completionExperience = 7.5)
public class GnomeRope implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		player.getAppearance().setRenderEmote(155);
        player.addWalkSteps(2483, 3420, -1, false);
		player.getPackets().sendGameMessage("You carefully cross the tightrope.");
	}

	@Override
	public void end(Player player, GameObject object) {
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}