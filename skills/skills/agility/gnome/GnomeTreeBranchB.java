package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;
import skills.magic.TeleportType;

@AgilitySignature(info = "Gnome tree branch (B) climbing", stage = 5,  object = 2314, duration = 1, levelRequired = 1, completionExperience = 5)
public class GnomeTreeBranchB implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
	}

	@Override
	public void end(Player player, GameObject object) {
		player.getPackets().sendGameMessage("You land on the ground.");
		player.getMovement().move(false, new WorldTile(2486, 3419, 0), TeleportType.LADDER);
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}