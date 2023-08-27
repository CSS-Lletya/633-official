package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;
import skills.magic.TeleportType;

@AgilitySignature(info = "Gnome advanced tree branch climbing", stage = 4, object = 43528, duration = 3, levelRequired = 85, completionExperience = 25)
public class GnomeAdvancedTreeBranch implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		player.getMovement().move(false, new WorldTile(player.getX(), 3419, 3), TeleportType.LADDER);
		player.getPackets().sendGameMessage("You climb the tree..");
	}

	@Override
	public void end(Player player, GameObject object) {
		player.getPackets().sendGameMessage("...to the platform above.");
	}

	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}