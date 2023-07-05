package skills.fishing;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.RandomUtils;

import skills.HarvestingSkillAction;
import skills.Skills;

public final class Fishing extends HarvestingSkillAction {
	
	private final Tool tool;
	
	public Fishing(Player player, Tool tool, WorldTile position) {
		super(player, Optional.of(position));
		this.tool = tool;
	}

	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(success) {
			for(Item item : items) {
				if(item == null)
					continue;
				if (hasBarbtailHarpoon() && RandomUtils.percentageChance(10)) {
					Catchable catchable = Catchable.getCatchable(item.getId()).orElse(null);
					player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(catchable.getId()).getName() + "_Caught").addStatistic("Fish_Caught");
					getPlayer().getSkills().addExperience(getSkillId(), catchable.getExperience());
					player.getPackets().sendGameMessage("You catch an extra fish!");
				}
				Catchable catchable = Catchable.getCatchable(item.getId()).orElse(null);
				player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(catchable.getId()).getName() + "_Caught").addStatistic("Fish_Caught");
				getPlayer().getSkills().addExperience(getSkillId(), catchable.getExperience());
			}
		}
	}
	
	private boolean hasBarbtailHarpoon() {
		return player.getInventory().containsAny(ItemNames.BARB_TAIL_HARPOON_10129) || player.getEquipment().containsAny(ItemNames.BARB_TAIL_HARPOON_10129) && tool == Tool.HARPOON;
	}
	
	@Override
	public void onStop() {
		getPlayer().setNextAnimation(Animations.RESET_ANIMATION);
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(tool.animation);
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
		return tool.needed <= 0 ? Optional.empty() : Optional.of(new Item[]{new Item(tool.needed, 1)});
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
		if(!checkFishing())
			return false;
		getPackets().sendGameMessage("You begin to fish...");
		getPlayer().setNextAnimation(tool.animation);
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
		if (player.getSkills().getLevel(Skills.FISHING) <  tool.level) {
			getPackets().sendGameMessage("You must have a Fishing level of " + tool.level + " to fish here.");
			return false;
		}
		if(getPlayer().getInventory().getFreeSlots() < 1) {
			getPackets().sendGameMessage("You do not have any space left in your inventory.");
			return false;
		}
		if(tool.needed > 0) {
			if(!getPlayer().getInventory().containsItem(new Item(tool.needed))) {
				getPackets().sendGameMessage("You do not have enough bait.");
				return false;
			}
		}
		if (hasBarbtailHarpoon()) {
			return true;
		}
		if(!getPlayer().getInventory().containsItem(new Item(tool.id))) {
			getPackets().sendGameMessage("You need a " + tool + " to fish here!");
			return false;
		}
		return true;
	}
}