package skills.smithing;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;

import skills.ProducingSkillAction;
import skills.Skills;

public class Smelting extends ProducingSkillAction {
	
	/**
	 * The definition of the bar we're creating.
	 */
	private final SmeltingData definition;
	
	/**
	 * Determines if we're smelting dependant on the superheat spell.
	 */
	private final boolean spell;
	
	/**
	 * The amount we're producing.
	 */
	private int amount;
	
	/**
	 * Constructs a new {@link Smelting}.
	 * @param player {@link #getPlayer()}.
	 * @param definition {@link #definition}.
	 * @param amount {@link #amount}.
	 * @param spell {@link #spell}.
	 */
	public Smelting(Player player, SmeltingData definition, int amount, boolean spell) {
		super(player, Optional.empty());
		this.definition = definition;
		this.amount = amount;
		this.spell = spell;
	}
	
	/**
	 * Attempts to start smelting for the specified {@code player}.
	 * @param player the player whom is smelting the bar.
	 * @param buttonId the button this player clicked.
	 * @return <true> if the player could smelt, <false> otherwise.
	 */
	public static boolean smelt(Player player, int buttonId) {
		Optional<SmeltingData> data = SmeltingData.getDefinition(buttonId);
		
		if(!data.isPresent()) {
			return false;
		}
		
		smelt(player, data.get(), 28);
		return true;
	}
	
	/**
	 * Smelts the {@code data} for the specified {@code player} and produces the
	 * exact amount the player inputed if he has the requirements.
	 * @param player {@link #getPlayer()}.
	 * @param data {@link #definition}.
	 * @param amount {@link #amount}.
	 */
	public static void smelt(Player player, SmeltingData data, int amount) {
		Smelting smithing = new Smelting(player, data, amount, false);
		smithing.start();
	}
	
	@Override
	public boolean isPrioritized() {
		return spell;
	}
	
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(definition.required);
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			if(!spell) {
				player.setNextAnimation(Animations.SMELTING_INSIDE_FURNACE);
			}
			amount--;
			
			if(amount < 1) {
				t.cancel();
			}
		}
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.SMELTING_INSIDE_FURNACE);
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(definition.produced);
	}
	
	@Override
	public int delay() {
		return spell ? 2 : 3;
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean initialize() {
		return canSmelt();
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
	
	@Override
	public double experience() {
		return definition.experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.SMITHING;
	}
	
	/**
	 * Checks if the player has the requirements to smelt.
	 * @return <true> if the player has the requirements, <false> otherwise.
	 */
	public boolean canSmelt() {
		if(player.getSkills().getTrueLevel(Skills.SMITHING) < definition.requirement) {
			player.getPackets().sendGameMessage("You need a smithing level of " + definition.requirement + " to smelt this bar.");
			return false;
		}
		if(!player.getInventory().containsItems(definition.required)) {
			player.getPackets().sendGameMessage("You don't have the required items to register this smelting bar.");
			return false;
		}
		return true;
	}
	
	public enum SmeltingData {
		BRONZE(0, new Item[]{new Item(438), new Item(436)}, new Item[]{new Item(2349)}, 6.25, 1),
		
		IRON(11, new Item[]{new Item(440)}, new Item[]{new Item(2351)}, 12.5, 15),
		
		SILVER(2, new Item[]{new Item(442)}, new Item[]{new Item(2355)}, 13.67, 20),
		
		STEEL(3, new Item[]{new Item(440), new Item(453, 2)}, new Item[]{new Item(2353)}, 17.5, 30),
		
		GOLD(4, new Item[]{new Item(444)}, new Item[]{new Item(2357)}, 22.5, 40),
		
		MITHRIL(5, new Item[]{new Item(447), new Item(453, 4)}, new Item[]{new Item(2359)}, 30, 50),
		
		ADAMANT(6, new Item[]{new Item(449), new Item(453, 6)}, new Item[]{new Item(2361)}, 37.5, 70),
		
		RUNITE(7, new Item[]{new Item(451), new Item(453, 8)}, new Item[]{new Item(2363)}, 50, 85);
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<SmeltingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(SmeltingData.class));
		
		/**
		 * The button identification.
		 */
		private final int buttonId;
		
		/**
		 * The required items to smelt this bar.
		 */
		private final Item[] required;
		
		/**
		 * The produced items for smelting the required items.
		 */
		private final Item[] produced;
		
		/**
		 * The experience gained upon smelting one bar.
		 */
		private final double experience;
		
		/**
		 * The requirement required to smelt the bar.
		 */
		private final int requirement;
		
		/**
		 * The amount we're producing.
		 */
		@SuppressWarnings("unused")
		private final int amount;
		
		/**
		 * Constructs a new {@link SmeltingData} enumerator.
		 * @param buttonId {@link #buttonId}.
		 * @param required {@link #required}.
		 * @param produced {@link #produced}.
		 * @param experience {@link #experience}.
		 * @param requirement {@link #requirement}.
		 */
		SmeltingData(int buttonId, Item[] required, Item[] produced, double experience, int requirement) {
			this.buttonId = buttonId;
			this.required = required;
			this.produced = produced;
			this.experience = experience;
			this.requirement = requirement;
			this.amount = 28;
		}
		
		/**
		 * Constructs a new {@link SmeltingData} enumerator.
		 * @param buttonId {@link #buttonId}.
		 * @param definition the definition for this bar.
		 * @param amount {@link #amount}.
		 */
		SmeltingData(int buttonId, SmeltingData definition, int amount) {
			this.buttonId = buttonId;
			this.required = definition.required;
			this.produced = definition.produced;
			this.experience = definition.experience;
			this.requirement = definition.requirement;
			this.amount = amount;
		}
		
		/**
		 * Searches for a match for the internal button identification.
		 * @param buttonId the button id to search for matches with.
		 * @return the smeltingdata wrapped in an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<SmeltingData> getDefinition(int buttonId) {
			return VALUES.stream().filter(def -> def.buttonId == buttonId).findAny();
		}
		
		/**
		 * Searches for a match for the internal required items.
		 * @param itemId the required items to search for matches with.
		 * @return the smeltingdata wrapped in an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<SmeltingData> getDefinitionByItem(int itemId) {
			for(SmeltingData data : VALUES) {
				for(Item item : data.required) {
					if(item.getId() == itemId) {
						return Optional.of(data);
					}
				}
			}
			return Optional.empty();
		}
	}
}
