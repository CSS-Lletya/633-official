package skills.prayer;

import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import skills.Skills;

public class BoneCrusher {

	public static boolean handleDrop(Player player, Item item) {
		if (player.getSkills().getTrueLevel(Skills.PRAYER) < 21)
			return false;
		if (player.getInventory().containsAny(ItemNames.BONECRUSHER_18337)) {
			Bone.VALUES.stream().filter(bone -> bone.getId() == item.getId())
					.forEach(bone -> player.getSkills().addExperience(Skills.PRAYER, bone.getExperience() / 2));
		}
		return Bone.VALUES.stream().anyMatch(id -> id.getId() == item.getId());
	}
}