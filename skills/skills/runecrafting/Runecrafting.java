package skills.runecrafting;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.constants.ItemNames;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.player.Inventory;
import com.rs.game.player.Player;
import com.rs.game.task.Task;

import skills.ProducingSkillAction;
import skills.Skills;

public final class Runecrafting extends ProducingSkillAction {

	/**
	 * The altar this player is producing his runes from.
	 */
	private final Altar altar;

	/**
	 * The amount of essences this player sacrificed.
	 */
	private int count;

	/**
	 * Represents the pure essence item identification.
	 */
	private static final Item PURE_ESSENCE = new Item(ItemNames.PURE_ESSENCE_7936);

	/**
	 * Represents the rune essence item identification.
	 */
	private static final Item RUNE_ESSENCE = new Item(ItemNames.RUNE_ESSENCE_1436);

	/**
	 * Represents the a mapping for the Pouches
	 */
	private static Map<PouchType, Pouch> pouches = new HashMap<>(3);

	/**
	 * Constructs a new {@link Runecrafting}
	 * @param player {@link #player}.
	 * @param object the object the {@code player} is interacting with.
	 */
	public Runecrafting(Player player, GameObject object, Altar altar) {
		super(player, Optional.of(object));
		this.altar = altar;
	}

	@Override
	public Optional<String> message() {
		return Optional.of(altar.isDiverse() ? "You do not have any essence left." : "You do not have any pure essence left.");
	}

	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			getPlayer().setNextAnimation(Animations.RUNECRAFTING);
			getPlayer().setNextGraphics(Graphic.RUNECRAFTING);
			player.getDetails().getStatistics()
			.addStatistic(ItemDefinitions.getItemDefinitions(altar.getRune().getItem().getId()).getName() + "_Runecrafted")
			.addStatistic("Runes_Crafted");
			t.cancel();
		}
	}

	@Override
	public Optional<Item[]> removeItem() {
		List<Item> remove = new ArrayList<>();
		Inventory inventory = player.getInventory();

		if(altar.isDiverse() && !inventory.containsAny(PURE_ESSENCE.getId(), RUNE_ESSENCE.getId())) {
			return Optional.of(new Item[]{PURE_ESSENCE, RUNE_ESSENCE});
		} else if(!altar.isDiverse() && !inventory.containsItem(PURE_ESSENCE)) {
			return Optional.of(new Item[]{PURE_ESSENCE});
		}

		if(altar.isDiverse()) {
			int pure = inventory.getAmountOf(PURE_ESSENCE.getId());
			int rune = inventory.getAmountOf(RUNE_ESSENCE.getId());
			if(pure > 0) {
				remove.add(new Item(PURE_ESSENCE.getId(), pure));
			}
			if(rune > 0) {
				remove.add(new Item(RUNE_ESSENCE.getId(), rune));
			}
			count = pure + rune;
		} else {
			int rune = inventory.getAmountOf(PURE_ESSENCE.getId());
			remove.add(new Item(PURE_ESSENCE.getId(), rune));
			count = rune;
		}
		return Optional.of(remove.toArray(new Item[remove.size()]));
	}

	@Override
	public Optional<Item[]> produceItem() {
		Optional<RunecraftingMultiplier> m = altar.getRune().getBestMultiplier(player);
		int amount = m.map(RunecraftingMultiplier::getMultiply).orElse(1);
		return Optional.of(new Item[]{new Item(altar.getRune().getItem().getId(), count * amount)});
	}

	@Override
	public int delay() {
		return 0;
	}

	@Override
	public boolean instant() {
		return true;
	}

	@Override
	public boolean initialize() {
		return checkRunecrafting();
	}

	@Override
	public boolean canExecute() {
		return checkRunecrafting();
	}

	@Override
	public double experience() {
		return altar.getExperience() * count;
	}

	@Override
	public int getSkillId() {
		return Skills.RUNECRAFTING;
	}

	private boolean checkRunecrafting() {
		if(altar == null) {
			getPlayer().getPackets().sendGameMessage("This is not a valid Altar.");
			return false;
		}
		if(getPlayer().getSkills().getLevel(Skills.RUNECRAFTING) < altar.getRequirement()) {
			getPlayer().getPackets().sendGameMessage("You need a level of " + altar.getRequirement() + " to use this altar!");
			return false;
		}
		return true;
	}

	/**
	 * gets the essence id the player is using, we check for pure essence first
	 * @param player The player checking for essence
	 * @return The id of the essence
	 */
	private static int getEssenceId(Player player) {
		if(player.getInventory().containsItem(PURE_ESSENCE)) {
			return PURE_ESSENCE.getId();
		} else if(player.getInventory().containsItem(RUNE_ESSENCE)) {
			return RUNE_ESSENCE.getId();
		} else {
			return -1;
		}
	}

	public static void fill(Player player, PouchType type) {
		int amount = player.getInventory().getAmountOf(getEssenceId(player));
		if(amount <= 0) {
			player.getPackets().sendGameMessage("You have no essence to store in the pouch.");
			return;
		}
		amount = amount < type.getMaxAmount() ? amount : type.getMaxAmount();
		Optional<Pouch> originalPouch = Optional.ofNullable(pouches.get(type));
		if(originalPouch.isPresent()) {
			if(originalPouch.get().getAmount() >= type.getMaxAmount()) {
				player.getPackets().sendGameMessage("Your pouch is already full.");
				return;
			}
			amount -= originalPouch.get().getAmount();
		}
		Pouch pouch = new Pouch(getEssenceId(player), originalPouch.isPresent() ? originalPouch.get().getAmount() + amount : amount);
		pouches.put(type, pouch);
		IntStream.range(0, amount).forEach(i -> player.getInventory().removeItems(new Item(pouch.getId())));
		player.getPackets().sendGameMessage("You fill the " + amount + " essence.");

	}

	public static void examine(Player player, PouchType type) {
		Optional<Pouch> pouch = Optional.ofNullable(pouches.get(type));
		if(!pouch.isPresent()) {
			player.getPackets().sendGameMessage("This pouch does not contain any essence.");
			return;
		}
		player.getPackets().sendGameMessage("This pouch contains " + pouch.get().getAmount() + (pouch.get().getId() == PURE_ESSENCE.getId() ? " pure" : " rune") + " essence.");
	}

	public static void empty(Player player, PouchType type) {
		Optional<Pouch> pouch = Optional.ofNullable(pouches.get(type));
		if(!pouch.isPresent()) {
			player.getPackets().sendGameMessage("This pouch does not contain any essence.");
			return;
		}
		pouches.remove(type);
		IntStream.range(0, pouch.get().getAmount()).forEach(i -> player.getInventory().addItem(new Item(pouch.get().getId())));
		player.getPackets().sendGameMessage("You empty the " + pouch.get().getAmount() + " essence left in the pouch.");
	}
	
	@Override
	public boolean canIgnoreIventoryCheck() {
		return player.getInventory().containsAny(PURE_ESSENCE.getId(), RUNE_ESSENCE.getId());
	}
}