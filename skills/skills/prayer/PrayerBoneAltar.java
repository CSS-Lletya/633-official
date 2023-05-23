package skills.prayer;

import java.util.Optional;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.DestructionSkillAction;
import skills.Skills;

public final class PrayerBoneAltar extends DestructionSkillAction {
	
	private final Bone bone;
	@SuppressWarnings("unused")
	private final GameObject altar;
	
	public PrayerBoneAltar(Player player, GameObject object, Bone bone) {
		super(player, Optional.of(object));
		this.bone = bone;
		altar = object;
	}
	
	@Override
	public boolean initialize() {
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public Item destructItem() {
		return new Item(bone.getId());
	}
	
	@Override
	public void onDestruct(Task t, boolean success) {
		if(success) {
			getPlayer().setNextAnimation(Animations.BONE_ON_ALTAR);
			getPlayer().getPackets().sendGameMessage("You offer the " + bone + " to the gods... they seem pleased.");
		}
	}
	
	@Override
	public int delay() {
		return 6;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public double experience() {
		return (bone.getExperience() * 1.4);
	}
	
	
	@Override
	public int getSkillId() {
		return Skills.PRAYER;
	}
}