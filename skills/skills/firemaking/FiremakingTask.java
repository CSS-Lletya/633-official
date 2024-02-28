package skills.firemaking;

import com.rs.constants.Sounds;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtility;

/**
 * Represents the task for creating and deregistering fires.
 * 
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class FiremakingTask extends Task {

	/**
	 * The skill this task is active for.
	 */
	private final Firemaking firemaking;
	
	private final WorldTile tile;

	/**
	 * Constructs a new {@link FiremakingTask}.
	 * 
	 * @param firemaking the firemaking skill action we're starting this task for.
	 */
	FiremakingTask(Firemaking firemaking, WorldTile tile) {
		super(firemaking.getLogType().getTimer(), false);
		this.firemaking = firemaking;
		this.tile = tile;
	}

	@Override
	public void onSubmit() {
		GameObject object = new GameObject(new GameObject(firemaking.getFireLighter().getObjectId(), 10, 0, tile));
		GameObject.spawnTempGroundObject(object, RandomUtility.inclusive(60, 120), () -> FloorItem.addGroundItem(new Item(592), object, firemaking.getPlayer(), true, 60));
		
		final FloorItem item = World.getRegion(firemaking.getPlayer().getRegionId()).getGroundItem(firemaking.getLogType().getLog().getId(), tile, firemaking.getPlayer());
		if (item == null)
			return;
		if (firemaking.isGroundLog)
			FloorItem.removeGroundItem(firemaking.getPlayer(), item, false);
		firemaking.getPlayer().getAudioManager().sendSound(Sounds.CREATED_FIRE);
	}

	@Override
	public void execute() {
		this.cancel();
	}

}
