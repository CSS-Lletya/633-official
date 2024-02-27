package skills.agility.gnome;

import java.util.Optional;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.utilities.MutableNumber;

import skills.agility.AgilitySignature;
import skills.agility.Obstacle;
import skills.magic.TeleportType;

@AgilitySignature(info = "Gnome net climbing (A)", stage = 2,  object = 2285, duration = 2, levelRequired = 1, completionExperience = 7.5)
public class GnomeNetClimbA implements Obstacle {

	@Override
	public void start(Player player, GameObject object) {
		player.getPackets().sendGameMessage("You climb the netting.");
		player.getMovement().move(false, new WorldTile(player.getX(), 3424, 1), TeleportType.LADDER);
	}

	@Override
	public void end(Player player, GameObject object) {
		
	}
	
	@Override
	public Optional<MutableNumber> stageKey(Player player) {
		return Optional.of(player.getDetails().getGnomeAgilityStage());
	}
}