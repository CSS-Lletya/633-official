package skills.crafting;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.TextUtils;

import skills.ProducingSkillAction;
import skills.Skills;

public final class SpinningWheel extends ProducingSkillAction {
	
	/**
	 * The data this skill action is dependent of.
	 */
	private final SpinningData data;
	
	/**
	 * The amount of times this task should run for.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link SpinningWheel}.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #data}.
	 * @param amount {@link #amount}.
	 */
	public SpinningWheel(Player player, SpinningData data, int amount) {
		super(player, Optional.empty());
		this.data = data;
		this.amount = amount;
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			amount--;
			player.getAudioManager().sendSound(Sounds.SPINNING);
			player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(data.produced.getId()).getName() + "_Crafted").addStatistic("Items_Crafted");
			if(amount <= 0)
				t.cancel();
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.CRAFTING_USING_BOTH_HANDS);
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{data.item});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{data.produced});
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
			player.getPackets().sendGameMessage("You need a crafting level of " + data.requirement + " to spin " + TextUtils.appendIndefiniteArticle(data.produced.getDefinitions().getName()));
			return false;
		}
		return true;
	}
	
	public enum SpinningData {
		WOOL(1737, 1759, 1, 2.5),
		FLAX(1779, 1777, 1, 15),
		SINEW(9436, 9438, 10, 15),
		MAGIC_ROOTS(6051, 6038, 19, 30),
		HAIR(10814, 954, 30, 25);
		/**
		 * The item required to spin.
		 */
		public final Item item;
		
		/**
		 * The item produced from spinning.
		 */
		private final Item produced;
		
		/**
		 * The requirement to spin.
		 */
		private final int requirement;
		
		/**
		 * The experience gained from spinning.
		 */
		private final double experience;
		
		/**
		 * Constructs a new {@link SpinningData}.
		 * @param item {@link #item}.
		 * @param produced {@link #produced}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 */
		SpinningData(int item, int produced, int requirement, double experience) {
			this.item = new Item(item);
			this.produced = new Item(produced);
			this.requirement = requirement;
			this.experience = experience;
		}
	}
}