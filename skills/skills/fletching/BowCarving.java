package skills.fletching;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.utilities.TextUtils;

import skills.ProducingSkillAction;
import skills.Skills;

/**
 * Holds functionality for carving bows.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class BowCarving extends ProducingSkillAction {
	
	/**
	 * The definition of this log.
	 */
	public final Log definition;
	
	/**
	 * Determines if we're cutting dependant of the beaver familiar.
	 */
	private final boolean beaver;
	
	/**
	 * The current array the player selected.
	 */
	private ProduciblePolicy current;
	
	/**
	 * The amount of times this task has to run.
	 */
	private int counter;
	
	/**
	 * Constructs a new {@link BowCarving} skill action.
	 * @param player {@link #getPlayer()}.
	 * @param definition {@link #definition}.
	 * @param beaver {@link #beaver}.
	 */
	public BowCarving(Player player, Log definition, boolean beaver, int buttonId) {
		super(player, Optional.empty());
		
		this.definition = definition;
		this.beaver = beaver;
//		fletch(player, buttonId);
	}
	
	/**
	 * Fletches all the possible items a player can fletch.
	 * @param player the player we're fletching items for.
	 * @param buttonId the button id pressed.
	 * @return <true> if this action started, <false> otherwise.
	 */
	public static boolean fletch(Player player, int buttonId) {
		Optional<ProduciblePolicy> pol = Log.getProducibles((BowCarving) player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).get(), buttonId);
		
		if(!pol.isPresent()) {
			System.out.println("not rp");
			return false;
		}
		
		BowCarving fletch = (BowCarving) player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).get();
		fletch.current = pol.get();
		fletch(player, pol.get(), 28);
		
//		player.getInterfaceManager().closeInterfaces();
//		if(Log.getAmount(buttonId) == 28) {
//			BowCarving fletch = (BowCarving) player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).get();
//			fletch.current = pol.get();
//			player.out(new SendEnterAmount("How many you would like to make?", s -> () -> BowCarving.fletch(player, fletch.getCurrent(), Integer.parseInt(s))));
//			return true;
//		}
		return true;
	}
	
	/**
	 * Attempts to start fletching for the {@code player}.
	 * @param player {@link #getPlayer()}.
	 * @param producable {@link #current}.
	 * @param amount the amount to register.
	 */
	public static void fletch(Player player, ProduciblePolicy producable, int amount) {
		BowCarving fletch = (BowCarving) player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).get();
		fletch.counter = amount;
		fletch.current = producable;
		player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletch);
		fletch.start();
	}
	
//	/**
//	 * Opens the interface for the player.
//	 * @param player the player we're opening this interface for.
//	 * @param firstItem the item this player used on the {@code secondItem}.
//	 * @param secondItem the secondItem that was used by the {@code firstItem}.
//	 * @return <true> if this interface opened, <false> otherwise.
//	 */
//	public static boolean openInterface(Player player, Item firstItem, Item secondItem, boolean beaver) {
//		Optional<Log> log = Log.getLog(firstItem.getId(), secondItem.getId());
//		
//		if(!log.isPresent()) {
//			return false;
//		}
//		
//		if(firstItem.getId() == log.get().log.getId() && secondItem.getId() == 946 || firstItem.getId() == 946 && secondItem.getId() == log.get().log.getId() || beaver) {
//			BowCarving fletching = new BowCarving(player, log.get(), beaver);
//			player.getAttributes().get(Attribute.BOW_FLETCHING_CARVING).set(fletching);
//			player.getAttributes().get(Attribute.BOW_FLETCHING).set(true);
//			if(log.get().producibles.length == 2) {
//				player.text(8879, "What would you like to make?");
//				player.out(new SendItemModelInterface(8870, 200, fletching.definition.producibles[0].producible.getId()));
//				player.out(new SendItemModelInterface(8869, 200, fletching.definition.producibles[1].producible.getId()));
//				player.text(8874, "\\n\\n\\n\\n\\nShortbow");
//				player.text(8878, "\\n\\n\\n\\n\\nLongbow");
//				player.chatWidget(8866);
//			} else if(log.get().producibles.length == 3) {
//				player.text(8898, "What would you like to make?");
//				player.out(new SendItemModelInterface(8884, 200, fletching.definition.producibles[0].producible.getId()));
//				player.out(new SendItemModelInterface(8883, 200, fletching.definition.producibles[1].producible.getId()));
//				player.out(new SendItemModelInterface(8885, 200, fletching.definition.producibles[2].producible.getId()));
//				player.text(8889, "\\n\\n\\n\\n\\nShortbow");
//				player.text(8893, "\\n\\n\\n\\n\\nLongbow");
//				player.text(8897, "\\n\\n\\n\\n\\nCrossbow Stock");
//				player.chatWidget(8880);
//			} else if(log.get().producibles.length == 4) {
//				player.text(8922, "What would you like to make?");
//				player.out(new SendItemModelInterface(8902, 200, fletching.definition.producibles[0].producible.getId()));
//				player.out(new SendItemModelInterface(8903, 200, fletching.definition.producibles[1].producible.getId()));
//				player.out(new SendItemModelInterface(8904, 200, fletching.definition.producibles[2].producible.getId()));
//				player.out(new SendItemModelInterface(8905, 200, fletching.definition.producibles[3].producible.getId()));
//				player.text(8906, "\\n\\n\\n\\n\\nShortbow");
//				player.text(8910, "\\n\\n\\n\\n\\nLongbow");
//				player.text(8914, "\\n\\n\\n\\n\\nCrossbow Stock");
//				player.text(8918, "\\n\\n\\n\\n\\nArrow Shafts");
//				player.chatWidget(8899);
//			}
//			return true;
//		}
//		return false;
//	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
//			if(beaver) {
//				Familiar familiar = player.getFamiliar().orElse(null);
//				
//				if(familiar != null) {
//					player.faceEntity(familiar);
//					player.animation(new Animation(827));
//					familiar.animation(new Animation(7722));
//				}
//				player.message("The beaver carefully cut the logs into " + TextUtils.appendIndefiniteArticle(current.producible.getDefinition().getName()) + ".");
//			} else {
				player.setNextAnimation(new Animation(1248));
				player.getPackets().sendGameMessage("You carefully cut the logs into " + TextUtils.appendIndefiniteArticle(current.producible.getDefinitions().getName()) + ".");
//			}
			if(--counter < 1) {
				t.cancel();
			}
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(new Item[]{definition.log});
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(new Item[]{current.producible});
	}
	
	@Override
	public int delay() {
		return beaver ? 6 : 3;
	}
	
	@Override
	public boolean instant() {
		return true;
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
		return Skills.FLETCHING;
	}
	
	@Override
	public void onStop() {
		player.getAttributes().get(Attribute.BOW_FLETCHING).set(false);
	}
	
	public boolean checkFletching() {
		if(player.getSkills().getLevel(Skills.FLETCHING) < current.requirement) {
			player.getPackets().sendGameMessage("You need a fletching level of " + current.requirement + " to fletch this.");
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

	/**
	 * The enumerated type whose elements represent the data required for fletching.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum Log {
		NORMAL(1511, new ProduciblePolicy(50, 1, 5.0, new ButtonConfiguration(14, 28)), new ProduciblePolicy(48, 10, 10.0, new ButtonConfiguration(15, 28)), new ProduciblePolicy(9440, 9, 6.0, new ButtonConfiguration(2, 1)), new ProduciblePolicy(new Item(52, 15), 1, 15.0, new ButtonConfiguration(3, 1))),
//		OAK(1521, new ProduciblePolicy(54, 20, 16.5, FIRST_THREE_MENU), new ProduciblePolicy(56, 25, 25, SECOND_THREE_MENU), new ProduciblePolicy(9442, 24, 16.0, THIRD_THREE_MENU)),
//		WILLOW(1519, new ProduciblePolicy(60, 35, 33.3, FIRST_THREE_MENU), new ProduciblePolicy(58, 40, 41.5, SECOND_THREE_MENU), new ProduciblePolicy(9444, 39, 22, THIRD_THREE_MENU)),
//		MAPLE(1517, new ProduciblePolicy(64, 50, 50, FIRST_THREE_MENU), new ProduciblePolicy(62, 55, 58.3, SECOND_THREE_MENU), new ProduciblePolicy(9448, 54, 32, THIRD_THREE_MENU)),
//		YEW(1515, new ProduciblePolicy(68, 65, 67.5, FIRST_THREE_MENU), new ProduciblePolicy(66, 70, 75, SECOND_THREE_MENU), new ProduciblePolicy(9452, 69, 50, THIRD_THREE_MENU)),
//		MAGIC(1513, new ProduciblePolicy(72, 80, 83.3, new ButtonConfiguration(34170, 1), new ButtonConfiguration(34169, 5), new ButtonConfiguration(34168, 10), new ButtonConfiguration(34167, 28)), new ProduciblePolicy(70, 85, 91.5, new ButtonConfiguration(34174, 1), new ButtonConfiguration(34173, 5), new ButtonConfiguration(34172, 10), new ButtonConfiguration(34171, 28)));
		;
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<Log> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Log.class));
		
		/**
		 * The identification for this log.
		 */
		public final Item log;
		
		/**
		 * The producible items for this log.
		 */ 
		public final ProduciblePolicy[] producibles;
		
		/**
		 * Constructs a new {@link Log} enumerator.
		 * @param logId {@link #log}.
		 * @param producibles {@link #producibles}.
		 */
		Log(int logId, ProduciblePolicy... producibles) {
			this.log = new Item(logId);
			this.producibles = producibles;
		}
		
		/**
		 * Gets the definition for this log by checking if the log
		 * item identifier matched with the {@code id}.
		 * @return a {@link Log} wrapped in an Optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<Log> getLog(int firstId, int secondId) {
			return VALUES.stream().filter(def -> def.log.getId() == firstId || def.log.getId() == secondId).findAny();
		}
		
		/**
		 * Gets the definition for what item we should produce.
		 * @param button the button the player clicked.
		 * @return a producible policy wrapped in an optional, {@link Optional#empty()} otherwise.
		 */
		public static Optional<ProduciblePolicy> getProducibles(BowCarving bowCarving, int button) {
			for(Log log : VALUES) {
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
		
		/**
		 * Gets the amount to produce chained to this button.
		 * @param button the button to check the amount for.
		 * @return the amount to produce.
		 */
		public static int getAmount(int button) {
			for(Log log : VALUES) {
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
		public ProduciblePolicy(Item producible, int requirement, double experience, ButtonConfiguration... button) {
			this.producible = producible;
			this.requirement = requirement;
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
		public ProduciblePolicy(int producibleId, int requirement, double experience, ButtonConfiguration... button) {
			this(new Item(producibleId), requirement, experience, button);
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
