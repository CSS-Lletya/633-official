package skills.prayer;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public final class PrayerBoneBury extends DestructionSkillAction {
	
	private final Bone bone;
	private final int itemId;
	
	public PrayerBoneBury(Player player, int itemId, Bone bone) {
		super(player, Optional.empty());
		this.bone = bone;
		this.itemId = itemId;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public Item destructItem() {
		return new Item(itemId);
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			getPlayer().setNextAnimation(Animations.TOUCH_GROUND);
			getPlayer().getPackets().sendGameMessage("You bury the " + bone + ".");
			getPlayer().getDetails().getBoneBury().reset();
		}
		t.cancel();
	}
	
	@Override
	public int delay() {
		return 0;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public double experience() {
		return bone.getExperience();
	}
	
	public int getSkillId() {
		return Skills.PRAYER;
	}

	@Override
	public boolean initialize() {
		return getPlayer().getDetails().getBoneBury().elapsed(1200);
	}
	
}