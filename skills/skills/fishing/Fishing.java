package skills.fishing;

import java.util.Optional;

import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.HarvestingSkillAction;
import skills.Skills;

public final class Fishing extends HarvestingSkillAction {
	
	private final Tool tool;
	
	public Fishing(Player player, Tool tool, WorldTile position) {
		super(player, position);
		this.tool = tool;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(success) {
			int count = 0;
			for(Item item : items) {
				if(item == null)
					continue;
				Catchable catchable = Catchable.getCatchable(item.getId()).orElse(null);
				getPlayer().getSkills().addXp(getSkillId(), catchable.getExperience());
				count += item.getAmount();
			}
		}
	}
	
	@Override
	public void onStop() {
		getPlayer().setNextAnimation(null);
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(new Animation(tool.animation));
	}
	
	@Override
	public int getSkillId() {
		return Skills.FISHING;
	}
	
	@Override
	public double successFactor() {
		return tool.success;
	}
	
	@Override
	public Optional<Item[]> removeItems() {
		if(tool.needed <= 0) {
			return Optional.empty();
		}
		return Optional.of(new Item[]{new Item(tool.needed, 1)});
	}
	
	@Override
	public Item[] harvestItems() {
		return tool.onCatch(getPlayer());
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean initialize() {
		if(!checkFishing()) {
			return false;
		}
		getPackets().sendGameMessage("You begin to fish...");
		getPlayer().setNextAnimation(new Animation(tool.animation));
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return checkFishing();
	}
	
	
	/**
	 * Experience is handled elsewhere.
	 */
	@Override
	public double experience() {
		return 0;
	}
	
	private boolean checkFishing() {
		if(!getPlayer().getInventory().containsItem(new Item(tool.id))) {
			getPackets().sendGameMessage("You need a " + tool + " to fish here!");
			return false;
		}
		if(tool.needed > 0) {
			if(!getPlayer().getInventory().containsItem(new Item(tool.needed))) {
				getPackets().sendGameMessage("You do not have enough bait.");
				return false;
			}
		}
		if(getPlayer().getInventory().getFreeSlots() < 1) {
			getPackets().sendGameMessage("You do not have any space left in your inventory.");
			return false;
		}
		if (!(player.getSkills().getLevel(Skills.FISHING) >= tool.level)) {
			getPackets().sendGameMessage("You must have a Fishing level of " + tool.level + " to use this tool.");
			return false;
		}
		return true;
	}
}