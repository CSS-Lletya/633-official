package skills.magic;


import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.player.InterfaceManager.Tabs;
import com.rs.game.player.Player;
import com.rs.game.player.content.MagicStaff;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import skills.ProducingSkillAction;
import skills.Skills;
import skills.smithing.Smelting;
import skills.smithing.Smelting.SmeltingData;

/**
 * Holds functionality for magic on item skills.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 */
public class Enchanting extends ProducingSkillAction {
	
	/**
	 * The definition of the current magic action.
	 */
	private final EnchantingData definition;
	
	/**
	 * The magic spell that was used on this item.
	 */
	private final Item item;
	
	/**
	 * The slot the item is in that the magic spell was casted on.
	 */
	private final int slot;
	
	/**
	 * Constructs a new {@link Enchanting}.
	 * @param player {@link #getPlayer()}.
	 * @param definition {@link #definition}.
	 * @param item {@link #item}.
	 * @param slot {@link #slot}.
	 */
	public Enchanting(Player player, Item item, EnchantingData definition, int slot) {
		super(player, Optional.empty());
		this.definition = definition;
		this.item = item;
		this.slot = slot;
	}
	
	/**
	 * Attempts to execute the skill action for the specified {@code player}.
	 * @param player {@link #getPlayer()}.
	 * @param item {@link #item}.
	 * @param slot {@link #slot}.
	 * @param spellId {@link EnchantingData#spellId}.
	 * @return <true> if the skill action was executed, <false> otherwise.
	 */
	public static boolean cast(Player player, Item item, int spellId, int slot) {
		EnchantingData data = EnchantingData.getDefinition(spellId).orElse(null);
		
		if(data == null) {
			return false;
		}
		if(player.getSkillAction().isPresent()) {
			return false;
		}
		if(!data.canCast(player, item)) {
			return false;
		}
		Enchanting spell = new Enchanting(player, item, data, slot);
		spell.start();
		return true;
	}
	
	@Override
	public void onProduce(Task t, boolean success) {
		if(success) {
			definition.animation.ifPresent(player::setNextAnimation);
			definition.graphic.ifPresent(player::setNextGraphics);
			definition.onCast(player, item, slot);
			t.cancel();
		}
	}
	
	@Override
	public Optional<Item[]> removeItem() {
		return Optional.of(MagicStaff.suppressRunes(player, definition.removed));
	}
	
	@Override
	public Optional<Item[]> produceItem() {
		return Optional.of(definition.produced);
	}
	
	@Override
	public int delay() {
		return 2;
	}
	
	@Override
	public boolean instant() {
		return true;
	}
	
	@Override
	public boolean initialize() {
		if(player.getSkills().getLevel(Skills.MAGIC) < definition.requirement) {
			player.getPackets().sendGameMessage("You need a magic level of " + definition.requirement + " to cast this spell.");
			return false;
		}
		if(!player.getInventory().containsItem(item)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return initialize();
	}
	
	@Override
	public double experience() {
		return definition.experience;
	}
	
	@Override
	public int getSkillId() {
		return Skills.MAGIC;
	}
	
	/**
	 * The enumerated type whose elements represent the data required
	 * for handling magic on items.
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	private enum EnchantingData {
		LEVEL_1_ENCHANT(29, 7, 17.5, new Item[]{}, new Item[]{new Item(555), new Item(564)}, new Animation(719), new Graphics(114, 0, 95)) {
			
			private final Int2IntArrayMap SAPPHIRE = new Int2IntArrayMap(ImmutableMap.of(1637, 2550, 1694, 1727, 1656, 3853));
			
			@Override
			public boolean canCast(Player player, Item item) {
				if(!SAPPHIRE.containsKey(item.getId())) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				player.getAudioManager().sendSound(Sounds.SAPPHIRE_ENCHANT);
				player.getInventory().replaceItems(item, new Item(SAPPHIRE.get(item.getId())));
			}
		},
		LOW_ALCH(38, 21, 21, new Item[]{}, new Item[]{new Item(561), new Item(554, 3)}, new Animation(713), new Graphics(112)) {
			@Override
			public boolean canCast(Player player, Item item) {
				if(item.getId() == 995) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				player.getAudioManager().sendSound(Sounds.HIGH_ALCHEMY_SPELL);
				player.getInventory().deleteItem(new Item(item.getId(), 1));
				player.getInventory().addItem(new Item(995, item.getDefinitions().getSellPrice()));
				player.getInterfaceManager().sendTab(Tabs.MAGIC);
			}
		},
		LEVEL_2_ENCHANT(1165, 41, 37, new Item[]{}, new Item[]{new Item(556, 3), new Item(564, 1)}, new Animation(719), new Graphics(114, 0, 95)) {
			
			private final Int2IntArrayMap EMERALD = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder().put(1639, 2552).put(1658, 5521).put(11076, 11079).put(1677, 1729).put(6041, 6040).put(13155, 13156).build());
			
			@Override
			public boolean canCast(Player player, Item item) {
				if(!EMERALD.containsKey(item.getId())) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				player.getAudioManager().sendSound(Sounds.EMERALD_ENCHANT);
				player.getInventory().replaceItems(item, new Item(EMERALD.get(item.getId())));
			}
		},
		SUPERHEAT_ITEM(50, 43, 53, new Item[]{}, new Item[]{new Item(561, 1), new Item(554, 4)}, new Animation(723), new Graphics(148, 0, 100)) {
			@Override
			public boolean canCast(Player player, Item item) {
				Optional<SmeltingData> data = SmeltingData.getDefinitionByItem(item.getId());
				if(!data.isPresent()) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				Optional<SmeltingData> data = SmeltingData.getDefinitionByItem(item.getId());
				new Smelting(player, data.get(), true).start();
				player.getInterfaceManager().sendTab(Tabs.MAGIC);
			}
		},
		LEVEL_3_ENCHANT(53, 49, 59, new Item[]{}, new Item[]{new Item(554, 5), new Item(564)}, new Animation(720), new Graphics(115, 0, 95)) {
			
			private final Int2IntArrayMap RUBY = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder().put(1641, 2568).put(11085, 11088).put(1679, 11194).build());
			
			@Override
			public boolean canCast(Player player, Item item) {
				if(!RUBY.containsKey(item.getId())) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				player.getAudioManager().sendSound(Sounds.RUBY_ENCHANT);
				player.getInventory().replaceItems(item, new Item(RUBY.get(item.getId())));
			}
		},
		HIGH_ALCH(59, 55, 40, new Item[]{}, new Item[]{new Item(561), new Item(554, 5)}, new Animation(713), new Graphics(113)) {
			@Override
			public boolean canCast(Player player, Item item) {
				if (item.getDefinitions().getName().toLowerCase().contains("brawling"))
					return true;
				if (item.getId() == 995 || item.getName().equals("Coins")) {
					player.getPackets().sendGameMessage("You can't cast this spell on an object made of gold.");
					return false;
				}
				if (!ItemConstants.isTradeable(item)) {
					player.getPackets().sendGameMessage("You can't cast this spell on an object like that.");
					return false;
				}
				Item coins = new Item(995, item.getDefinitions().getHighAlchPrice());
				if (coins.getAmount() > 1 && !player.getInventory().hasFreeSlots()) {
					player.getPackets().sendGameMessage("Not enough space in your inventory!");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				int value = Arrays.stream(Skills.BRAWLING_GLOVES).anyMatch(glove -> item.getId() == glove[1]) ? 30_000 : item.getDefinitions().getHighAlchPrice();
				player.getAudioManager().sendSound(Sounds.HIGH_ALCHEMY_SPELL);
				player.getInventory().deleteItem(new Item(item.getId(), 1));
				player.getInventory().addItem(new Item(995, value));
				player.getInterfaceManager().sendTab(Tabs.MAGIC);
			}
		},
		LEVEL_4_ENCHANT(61, 57, 67, new Item[]{}, new Item[]{new Item(557, 10), new Item(564)}, new Animation(720), new Graphics(115, 0, 95)) {
			
			private final Int2IntArrayMap DIAMOND = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder().put(1643, 2570).put(1662, 11090).put(11092, 11095).put(1681, 1731).build());
			
			@Override
			public boolean canCast(Player player, Item item) {
				if(!DIAMOND.containsKey(item.getId())) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				player.getAudioManager().sendSound(Sounds.DIAMOND_ENCHANT);
				player.getInventory().replaceItems(item, new Item(DIAMOND.get(item.getId())));
			}
		},
		LEVEL_5_ENCHANT(76, 68, 78, new Item[]{}, new Item[]{new Item(557, 15), new Item(555, 15), new Item(564)}, new Animation(721), new Graphics(116, 0, 95)) {
			
			private final Int2IntArrayMap DRAGONSTONE = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder().put(1645, 2572).put(1664, 11105).put(11115, 11118).put(1683, 1704).build());
			
			@Override
			public boolean canCast(Player player, Item item) {
				if(!DRAGONSTONE.containsKey(item.getId())) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				player.getAudioManager().sendSound(Sounds.DRAGONSTONE_ENCHANT);
				player.getInventory().replaceItems(item, new Item(DRAGONSTONE.get(item.getId())));
			}
		},
		LEVEL_6_ENCHANT(88, 87, 97, new Item[]{}, new Item[]{new Item(557, 15), new Item(554, 15), new Item(564)}, new Animation(721), new Graphics(452, 0, 95)) {
			
			private final Int2IntArrayMap ONYX = new Int2IntArrayMap(ImmutableMap.<Integer, Integer>builder().put(6575, 6583).put(6577, 11128).put(11130, 11133).put(6581, 6585).build());
			
			@Override
			public boolean canCast(Player player, Item item) {
				if(!ONYX.containsKey(item.getId())) {
					player.getPackets().sendGameMessage("You can't cast this spell on this item.");
					return false;
				}
				return true;
			}
			
			@Override
			public void onCast(Player player, Item item, int slot) {
				player.getAudioManager().sendSound(Sounds.ONYX_ENCHANT);
				player.getInventory().replaceItems(item, new Item(ONYX.get(item.getId())));
			}
		};
		
		/**
		 * Caches our enum values.
		 */
		private static final ImmutableSet<EnchantingData> VALUES = Sets.immutableEnumSet(EnumSet.allOf(EnchantingData.class));
		
		/**
		 * The spell id for this magic action.
		 */
		private final int spellId;
		
		/**
		 * The requirement for this magic action.
		 */
		private final int requirement;
		
		/**
		 * The experience gained for this magic action.
		 */
		private final double experience;
		
		/**
		 * The items that should be added upon this magic action.
		 */
		private final Item[] produced;
		
		/**
		 * The items that should be removed upon this magic action.
		 */
		private final Item[] removed;
		
		/**
		 * The animation played for this magic action.
		 */
		private final Optional<Animation> animation;
		
		/**
		 * The graphic played for this magic action.
		 */
		private final Optional<Graphics> graphic;
		
		/**
		 * Constructs a new {@link EnchantingData}.
		 * @param spellId {@link #spellId}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param produced {@link #produced}.
		 * @param removed {@link #removed}.
		 * @param animation {@link #animation}.
		 * @param graphic {@link #graphic}.
		 */
		EnchantingData(int spellId, int requirement, double experience, Item[] produced, Item[] removed, Optional<Animation> animation, Optional<Graphics> graphic) {
			this.spellId = spellId;
			this.requirement = requirement;
			this.experience = experience;
			this.produced = produced;
			this.removed = removed;
			this.animation = animation;
			this.graphic = graphic;
		}
		
		/**
		 * Constructs a new {@link EnchantingData}.
		 * @param spellId {@link #spellId}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param produced {@link #produced}.
		 * @param removed {@link #removed}.
		 * @param animation {@link #animation}.
		 * @param graphic {@link #graphic}.
		 */
		EnchantingData(int spellId, int requirement, double experience, Item[] produced, Item[] removed, Animation animation, Graphics graphic) {
			this.spellId = spellId;
			this.requirement = requirement;
			this.experience = experience;
			this.produced = produced;
			this.removed = removed;
			this.animation = Optional.of(animation);
			this.graphic = Optional.of(graphic);
		}
		
		/**
		 * Constructs a new {@link EnchantingData}.
		 * @param spellId {@link #spellId}.
		 * @param requirement {@link #requirement}.
		 * @param experience {@link #experience}.
		 * @param produced {@link #produced}.
		 * @param removed {@link #removed}.
		 * @param animation {@link #animation}.
		 * @param graphic {@link #graphic}.
		 */
		EnchantingData(int spellId, int requirement, double experience, Item[] produced, Item[] removed, int animation, int graphic) {
			this.spellId = spellId;
			this.requirement = requirement;
			this.experience = experience;
			this.produced = produced;
			this.removed = removed;
			this.animation = Optional.of(new Animation(animation));
			this.graphic = Optional.of(new Graphics(graphic));
		}
		
		/**
		 * Checks if this spell can be casted.
		 * @param player the player to check for.
		 * @param item the item that this spell was used on.
		 * @return <true> if the player can, <false> otherwise.
		 */
		public boolean canCast(Player player, Item item) {
			return true;
		}
		
		/**
		 * Any extra functionality that should be handled when this spell is casted.
		 * @param player the player to execute the functionality for.
		 * @param item the item that this spell was used on.
		 * @param slot the slot that this item is in.
		 */
		public void onCast(Player player, Item item, int slot) {

		}
		
		/**
		 * Finds an enumerator whoms spell id matches the specified {@code id}.
		 * @param id the id to check for.
		 * @return an enumerator wrapped in an Optional, {@link Optional#empty()} otherwise.
		 */
		protected static Optional<EnchantingData> getDefinition(int id) {
			for(EnchantingData data : VALUES) {
				if(data.spellId == id)
					return Optional.of(data);
			}
			return Optional.empty();
		}
	}
}
