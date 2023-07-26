package skills.crafting;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.TextUtils;

import skills.ProducingSkillAction;
import skills.Skills;

public final class MoltenGlassBlowing extends ProducingSkillAction {
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final GlassData data;
	
	/**
	 * The amount of times this task should run for.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link MoltenGlassBlowing}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 * @param amount {@link #amount}.
	 */
	public MoltenGlassBlowing(Player player, GlassData data, int amount) {
		super(player, Optional.empty());
		this.data = data;
		this.amount = amount;
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			amount--;
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(data.item).getName() + "_Glass_Made").addStatistic("Glass_Items_Made");
			player.getPackets().sendGameMessage("You make an " + ItemDefinitions.getItemDefinitions(data.item).getName() + ".");
			if(amount <= 0)
				t.cancel();
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.GLASSPIPE_BLOWING);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(ItemNames.MOLTEN_GLASS_1775)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{new Item(data.item)});
	}
	
	@Override
	public int delay() {
		return 5;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean initialize() {
		player.getInterfaceManager().closeInterfaces();
		return checkCrafting();
	}
	
	@Override
	public boolean canExecute() {
		return checkCrafting();
	}
	
	@Override
	public double experience() {
		return data.experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
	
	@Override
	public void onStop() {
	}
	
	private boolean checkCrafting() {
		if(player.getSkills().getLevel(Skills.CRAFTING) < data.requirement) {
			player.getPackets().sendGameMessage("You need a crafting level of " + data.requirement + " to create " + TextUtils.appendIndefiniteArticle(ItemDefinitions.getItemDefinitions(data.item).getName()));
			return false;
		}
		return true;
	}
	
	public enum GlassData {
		BEER_GLASS(1919, 1, 17.5),
	    OIL_LAMP(4522, 12, 25),
	    VIAL(229, 33, 35),
	    FISHBOWL(6667, 42, 42.5),
	    UNPOWERED_ORB(567, 46, 52.5),
	    LIGHT_ORB(10980, 87, 104)
		;
		
        /**
         * The ID after.
         */
        public int item;
        /**
         * The required level.
         */
        private int requirement;
        /**
         * The experience gained.
         */
        private double experience;

        /**
         * The single blowing Item.
         *
         * @param beforeId         The Item ID before.
         * @param afterId          The Item ID after.
         * @param skillRequirement The required level of Crafting to make the Item.
         * @param exp              The experience gained for blowing the item.
         */
        GlassData(int item, int requirement, double experience) {
            this.item = item;
            this.requirement = requirement;
            this.experience = experience;
        }
	}
}