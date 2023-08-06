package skills.crafting;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.TextUtils;

import skills.ProducingSkillAction;
import skills.Skills;

public class DragonhideArmorCrafting extends ProducingSkillAction {
	
	/**
	 * The definition of this log.
	 */
	public final DragonHideData definition;
	
	/**
	 * The current array the player selected.
	 */
	private ProduciblePolicy current;
	
	/**
	 * The amount of times this task has to run.
	 */
	private int counter;
	
	/**
	 * Constructs a new {@link DragonhideArmorCrafting} skill action.
	 * @param player {@link #getPlayer()}.
	 * @param definition {@link #definition}.
	 * @param beaver {@link #beaver}.
	 */
	public DragonhideArmorCrafting(Player player, DragonHideData definition) {
		super(player, Optional.empty());
		this.definition = definition;
	}
	
	/**
	 * Fletches all the possible items a player can fletch.
	 * @param player the player we're fletching items for.
	 * @param buttonId the button id pressed.
	 * @return <true> if this action started, <false> otherwise.
	 */
	public static boolean fletch(Player player, int buttonId) {
		Optional<ProduciblePolicy> pol = DragonHideData.getProducibles((DragonhideArmorCrafting) player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).get(), buttonId);
		if(!pol.isPresent())
			return false;
		DragonhideArmorCrafting craft = (DragonhideArmorCrafting) player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).get();
		craft.current = pol.get();
		fletch(player, pol.get(), 28);
		return true;
	}
	
	/**
	 * Attempts to start fletching for the {@code player}.
	 * @param player {@link #getPlayer()}.
	 * @param producable {@link #current}.
	 * @param amount the amount to register.
	 */
	private static void fletch(Player player, ProduciblePolicy producable, int amount) {
		DragonhideArmorCrafting craft = (DragonhideArmorCrafting) player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).get();
		craft.counter = amount;
		craft.current = producable;
		player.getAttributes().get(Attribute.DRAGONHIDE_TYPE).set(craft);
		craft.start();
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			LeatherItemCrafting.handleThreadRemoval(player);
			player.getPackets().sendGameMessage("You make a " + TextUtils.appendIndefiniteArticle(current.producible.getDefinitions().getName()) + ".");

			if(--counter < 1) {
				t.cancel();
			}
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{new Item(definition.hide.getId(), current.hideRequired)});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{current.producible});
	}
	
	@Override
	public Optional<Animation> animation() {
		return Optional.of(Animations.CRAFTING_LEATHER);
	}
	
	@Override
	public int delay() {
		return 3;
	}
	
	@Override
	public boolean instant() {
		return false;
	}
	
	@Override
	public boolean initialize() {
		return checkFletching();
	}
	
	@Override
	public boolean canExecute() {
		return checkFletching();
	}
	
	@Override
	public double experience() {
		return current.experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.CRAFTING;
	}
	
	@Override
	public void onStop() {
		player.getAttributes().get(Attribute.BOW_FLETCHING).set(false);
	}
	
	public boolean checkFletching() {
		if(player.getSkills().getLevel(Skills.CRAFTING) < current.requirement) {
			player.getPackets().sendGameMessage("You need a crafting level of " + current.requirement + " to craft this.");
			return false;
		}
		return true;
	}
	
	/**
	 * @return the current
	 */
	public ProduciblePolicy getCurrent() {
		return current;
	}
	
	/**
	 * @param current the current to set
	 */
	public void setCurrent(ProduciblePolicy current) {
		this.current = current;
	}

	public enum DragonHideData {
		GREEN(new Item(1745,1), new ProduciblePolicy(1065, 1, 57, 62, new ButtonConfiguration(0, 28)), new ProduciblePolicy(1099, 60, 2,124, new ButtonConfiguration(1, 28)), new ProduciblePolicy(1135, 63, 3,186, new ButtonConfiguration(2, 28))),
		BLUE(new Item(2505), new ProduciblePolicy(2487, 66,1, 70, new ButtonConfiguration(0, 28)), new ProduciblePolicy(2493, 68, 2,140, new ButtonConfiguration(1, 28)), new ProduciblePolicy(2499, 71, 3,210, new ButtonConfiguration(2, 28))),
		RED(new Item(2507), new ProduciblePolicy(2489, 73, 1,78, new ButtonConfiguration(0, 28)), new ProduciblePolicy(2495, 75, 2,156, new ButtonConfiguration(1, 28)), new ProduciblePolicy(2501, 77, 3,234, new ButtonConfiguration(2, 28))),
		BLACK(new Item(2509), new ProduciblePolicy(2491, 79, 1,86, new ButtonConfiguration(0, 28)), new ProduciblePolicy(2497, 82, 2,172, new ButtonConfiguration(1, 28)), new ProduciblePolicy(2503, 84, 3,258, new ButtonConfiguration(2, 28))),
		;
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<DragonHideData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(DragonHideData.class));
		
		public final Item hide;
		
		public final ProduciblePolicy[] producibles;
		
		DragonHideData(Item hide, ProduciblePolicy... producibles) {
			this.hide = hide;
			this.producibles = producibles;
		}
		
		public static Optional<DragonHideData> getLog(int firstId, int secondId) {
			return VALUES.stream().filter(def -> def.hide.getId() == firstId || def.hide.getId() == secondId).findAny();
		}
		
		public static Optional<ProduciblePolicy> getProducibles(DragonhideArmorCrafting bowCarving, int button) {
			for(DragonHideData log : VALUES) {
				for(ProduciblePolicy pol : log.producibles) {
					for(ButtonConfiguration buttons : pol.button) {
						if(buttons.buttonId == button && bowCarving.definition.equals(log)) {
							return Optional.of(pol);
						}
					}
				}
			}
			return Optional.empty();
		}
		
		public static int getAmount(int button) {
			for(DragonHideData log : VALUES) {
				for(ProduciblePolicy pol : log.producibles) {
					for(ButtonConfiguration buttons : pol.button) {
						if(buttons.buttonId == button) {
							return buttons.amount;
						}
					}
				}
			}
			return 1;
		}
	}
	
	/**
	 * Represents the producible items this log can make if the player
	 * has the respective requirement.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public static class ProduciblePolicy {
		
		/**
		 * The identification for this bow.
		 */
		public final Item producible;
		
		/**
		 * The requirement required for fletching this bow.
		 */
		public final int requirement;
		
		public final int hideRequired;
		
		/**
		 * The experience gained for this bow.
		 */
		private final double experience;
		
		/**
		 * The button configuration for this bow.
		 */
		private final ButtonConfiguration[] button;
		
		/**
		 * Constructs a new {@link ProduciblePolicy}.
		 * @param producible {@link #producible}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param button {@link #button}.
		 */
		public ProduciblePolicy(Item producible, int requirement, int hideRequired, double experience, ButtonConfiguration... button) {
			this.producible = producible;
			this.requirement = requirement;
			this.hideRequired = hideRequired;
			this.experience = experience;
			this.button = button;
		}
		
		/**
		 * Constructs a new {@link ProduciblePolicy}.
		 * @param producibleId {@link #producible}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param button {@link #button}.
		 */
		public ProduciblePolicy(int producibleId, int requirement, int hideRequired, double experience, ButtonConfiguration... button) {
			this(new Item(producibleId), requirement, hideRequired, experience, button);
		}
	}
	
	/**
	 * Represents a button configuration, basically chains the amount to produce to the button.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private static class ButtonConfiguration {
		
		/**
		 * The button identification.
		 */
		private final int buttonId;
		
		/**
		 * The amount chained to this button.
		 */
		private final int amount;
		
		/**
		 * Constructs a new {@link ButtonConfiguration}.
		 * @param buttonId {@link #buttonId}.
		 * @param amount {@link #amount}.
		 */
		public ButtonConfiguration(int buttonId, int amount) {
			this.buttonId = buttonId;
			this.amount = amount;
		}
	}
}