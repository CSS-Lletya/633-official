package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;

@AgilitySignature(info = "Gnome log walking", stage = 1, object = 2295, duration = 7, levelRequired = 1, completionExperience = 7.5)
public class GnomeLog implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		player.getAppearance().setRenderEmote(155);
        player.addWalkSteps(2474, 3429, -1, false);
		player.getPackets().sendGameMessage("You walk carefully across the slippery log...");
	}

	@Override
	public void end(Player player, GameObject object) {
		player.getPackets().sendGameMessage("... and make it safely to the other side.");
	}

	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}