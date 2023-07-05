package skills.woodcutting.sawmill;

import com.rs.constants.Animations;
import com.rs.content.mapzone.impl.SawmillMapZone;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

import skills.Skills;

public class CutPlank extends Action {

	private static double[] XP = { 10, 15, 20, 18 };
	private SawmillMapZone sawmill;
	private int amount;
	private int type;

	public CutPlank(int type, int amount, SawmillMapZone sawmill) {
		this.type = type;
		this.amount = amount;
		this.sawmill = sawmill;
	}

	@Override
	public boolean start(Player player) {
		return process(player);
	}

	@Override
	public boolean process(Player player) {
		if (!player.getInventory().containsOneItem(8794) && !player.getInventory().containsItem(9625, 1)) {
			player.getPackets().sendGameMessage("You need a saw to cut the planks.");
			return false;
		}
		if (!player.getInventory().containsItem(960, 1)) {
			player.getPackets().sendGameMessage("You have run out of planks.");
			return false;
		}
		return amount > 0;
	}

	@Override
	public int processWithDelay(Player player) {
		boolean crystalSaw = player.getInventory().containsItem(9625, 1);
		player.setNextAnimation(crystalSaw ? Animations.CRYSTAL_SAW_SAWING : Animations.REGULAR_SAWING);
		player.getSkills().addExperience(Skills.WOODCUTTING, crystalSaw ? XP[type] * 2 : XP[type] / 1.3);
		player.getSkills().addExperience(Skills.CONSTRUCTION, crystalSaw ? XP[type] * 2 / 2 : XP[type] / 1.3 / 2);
		player.getInventory().deleteItem(new Item(960, 1));
		if (type == 0) {
			sawmill.addPlank(player, 0, 1);
			sawmill.addPlank(player, 1, 1);
		} else if (type == 1) {
			sawmill.addPlank(player, 2, 2);
		} else if (type == 2) {
			sawmill.addPlank(player, 3, 1);
			sawmill.addPlank(player, 4, 1);
		} else {
			sawmill.addPlank(player, 5, 1);
		}
		return 2;
	}

	@Override
	public void stop(Player player) {
	}

}
