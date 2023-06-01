package skills.cooking;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.ProducingSkillAction;
import skills.Skills;

public final class Cooking extends ProducingSkillAction {

	static final Item BURNT_PIE = new Item(2329);
	private static final double COOKING_BURN_RATE = 50.0;
	private final CookingData data;
	private final GameObject object;
	private final boolean cookStove;
	private int counter;
	private boolean burned;
	private final boolean spell;
	private final ThreadLocalRandom random = ThreadLocalRandom.current();

	public Cooking(Player player, GameObject object, CookingData data, boolean cookStove, int counter, boolean spell) {
		super(player, spell ? Optional.empty() : Optional.of(object));
		this.data = data;
		this.object = object;
		this.cookStove = cookStove;
		this.counter = counter;
		this.spell = spell;
	}

	public Cooking(Player player, GameObject object, CookingData data, boolean cookStove, int counter) {
		this(player, object, data, cookStove, counter, false);
	}

	@Override
	public void onStop() {
		
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			player.setNextAnimation(spell ? Animations.BAKE_PIE_SPELL : !cookStove ? Animations.COOKING_ON_FIRE : Animations.COOKING_ON_STOVE);
			if(!spell) {
				player.getPackets().sendGameMessage((burned ? "Oops! You accidently burn the " : "You cook the ").concat(ItemDefinitions.getItemDefinitions(data.getRawId()).getName()));
			}
			counter--;
			if(counter == 0)
				t.cancel();
		}
	}

	@Override
	public boolean canExecute() {
		if(!checkCooking()) {
			player.getInterfaceManager().closeChatBoxInterface();
			return false;
		}
		burned = !determineBurn();
		return true;
	}

	@Override
	public boolean initialize() {
		player.getInterfaceManager().closeChatBoxInterface();
		return checkCooking();
	}

	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(data.getRawId())});
	}

	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{burned ? new Item(data.getBurntId()) : new Item(data.getCookedId())});
	}

	@Override
	public double experience() {
		return burned ? 0 : data.getExperience();
	}

	@Override
	public int delay() {
		return 4;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public int getSkillId() {
		return Skills.COOKING;
	}

	private boolean determineBurn() {
		if(player.getSkills().getLevel(Skills.COOKING) >= data.getMasterLevel() || spell) {
			return true;
		}
		double burn_chance = (COOKING_BURN_RATE - (cookStove ? 5.0 : 1.0));
		double cook_level = player.getSkills().getLevel(Skills.COOKING);
		double lev_needed = data.getLevel();
		double burn_stop = data.getMasterLevel();
		double multi_a = (burn_stop - lev_needed);
		double burn_dec = (burn_chance / multi_a);
		double multi_b = (cook_level - lev_needed);
		burn_chance -= (multi_b * burn_dec);
		double randNum = random.nextDouble() * 100.0;
		return burn_chance <= randNum;
	}

	private boolean checkCooking() {
		if(counter == 0)
			return false;
		if(!spell && object.getDefinitions().getName().toLowerCase().contains("fire") && GameObject.getObjectWithId(object, object.getId()) == null) 
			return false;
		if(!player.getInventory().containsItem(new Item(data.getRawId()))) {
			player.getPackets().sendGameMessage("You don't have any " + data.toString() + " to cook.");
			return false;
		}
		if(player.getSkills().getLevel(Skills.COOKING) < data.getLevel()) {
			player.getPackets().sendGameMessage("You need a cooking level of " + data.getLevel() + " to cook " + data.toString() + ".");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canIgnoreIventoryCheck() {
		return player.getInventory().containsAny(data.getRawId());
	}
}