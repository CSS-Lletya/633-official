package skills.firemaking;

import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.task.Task;

/**
 * Represents the task for creating and deregistering fires.
 * 
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
final class FiremakingTask extends Task {

	/**
	 * The skill this task is active for.
	 */
	private final Firemaking firemaking;

	/**
	 * The object we're registering and deregistering at a later state.
	 */
	private GameObject object;

	/**
	 * Constructs a new {@link FiremakingTask}.
	 * 
	 * @param firemaking the firemaking skill action we're starting this task for.
	 */
	FiremakingTask(Firemaking firemaking) {
		super(firemaking.getLogType().getTimer(), false);
		this.firemaking = firemaking;
	}

	@Override
	public void onSubmit() {
		object = new GameObject(new GameObject(firemaking.getFireLighter().getObjectId(), 10, 0, firemaking.getPlayer()));
		GameObject.spawnTempGroundObject(object, 60, () -> FloorItem.addGroundItem(new Item(592), object, firemaking.getPlayer(), true, 60));
	}

	@Override
	public void execute() {
		this.cancel();
	}

}
