package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;
import skills.magic.TeleportType;

@AgilitySignature(info = "Gnome tree branch (A) climbing", stage = 3, object = 35970, duration = 1, levelRequired = 1, completionExperience = 5)
public class GnomeTreeBranchA implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		player.getPackets().sendGameMessage("You climb the tree...");
		player.getMovement().move(false, new WorldTile(2473, 3420, 2), TeleportType.LADDER);
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