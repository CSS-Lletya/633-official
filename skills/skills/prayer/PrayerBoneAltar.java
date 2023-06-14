package skills.prayer;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Graphics;

import skills.DestructionSkillAction;
import skills.Skills;

public final class PrayerBoneAltar extends DestructionSkillAction {

	private static final Graphics GFX = new Graphics(624);
	private final Bone bone;
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
			World.sendGraphics(GFX, altar);
			getPlayer().setNextAnimation(Animations.BONE_ON_ALTAR);
			getPlayer().getPackets().sendGameMessage("You offer the " + bone + " to the gods... they seem pleased.");
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(bone.getId()).getName() +"_Bones_Offered").addStatistic("Bones_Offered");
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