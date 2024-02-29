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
import com.rs.utilities.RandomUtility;

import lombok.val;
import skills.HarvestingSkillAction;
import skills.Skills;

public class Fishing extends HarvestingSkillAction {
	
	private final Tool tool;
	
	public Fishing(Player player, Tool tool, WorldTile position) {
		super(player, Optional.of(position));
		this.tool = tool;
	}
	
	@Override
	public boolean isIgnoreResourceGather() {
		return false;
	}

	@Override
	public void onHarvest(Task t, Item[] items, boolean success) {
		if(success) {
			for(Item item : items) {
				if(item == null)
					continue;
				if (hasBarbtailHarpoon() && RandomUtility.percentageChance(10)) {
					Catchable catchable = Catchable.getCatchable(item.getId()).orElse(null);
					player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(catchable.getId()).getName() + "_Caught").addStatistic("Fish_Caught");
					getPlayer().getSkills().addExperience(getSkillId(), catchable.getExperience());
					player.getPackets().sendGameMessage("You catch an extra fish!");
				}
				Catchable catchable = Catchable.getCatchable(item.getId()).orElse(null);
				player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(catchable.getId()).getName() + "_Caught").addStatistic("Fish_Caught");
				getPlayer().getSkills().addExperience(getSkillId(), catchable.getExperience());
				
				if (tool == Tool.VESSEL) {
					player.getDetails().getKarambwanjiStock().getAndDecrement();
					if (player.getDetails().getKarambwanjiStock().get() == 0) {
						player.getInventory().replaceItems(new Item(3159), new Item(3157));
						player.getPackets().sendGameMessage("You have run out of raw karambwanji to use as bait.");
						t.cancel();
					}
				}
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
    public boolean successful() {
        val fishLevel = tool.level;
        val level = player.getSkills().getTrueLevel(Skills.FISHING);
        val advancedLevels =  level - fishLevel;
        return Math.min(Math.round(advancedLevels * 0.6F) + 30, 70) > RandomUtility.random(100);
    }
	
	@Override
	public Optional<Item[]> removeItems() {
		return tool.needed <= 0 ? Optional.empty() : Optional.of(new Item[]{new Item(tool.needed)});
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
		getPackets().sendGameMessage("You cast out your " +ItemDefinitions.getItemDefinitions(tool.id).getName() +"...");
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
			player.dialogue(d -> d.mes("You can't carry any more fish."));
			return false;
		}
		if (hasBarbtailHarpoon()) {
			return true;
		}
		if(!getPlayer().getInventory().containsItem(new Item(tool.id))) {
			getPackets().sendGameMessage("You need a " + ItemDefinitions.getItemDefinitions(tool.id).getName() + " to fish here!");
			return false;
		}
		if(tool.needed > 0) {
			if(!getPlayer().getInventory().containsItem(new Item(tool.needed))) {
				getPackets().sendGameMessage("You do not have enough bait.");
				return false;
			}
		}
		return true;
	}
}