package skills.cooking;


import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Hit;
import com.rs.game.player.Hit.HitLook;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.ForceTalk;
import com.rs.utilities.RandomUtils;

import skills.Skills;

public class Foods {

	public enum Food {

		TURKEY_DRUMSTICK(15428, 1),
		ROAST_POTATOES(15429, 1),
		YULE_LOGS(15430, 1),
		MULLED_WINE(15431, 1),

		CRAFISH(13433, 2),

		KINGWORM(2162, 2),

		ANCHOVIE(319, 1),

		SHRIMP(315, 3),

		FROG_SPAWN(5004, 2),

		KARAMBWANJI(3151, 3),

		SARDINE(325, 3),

		POISON_KARAMBWANJI(3146, 0, Effect.POISION_KARMAMWANNJI_EFFECT),

		KARAMBWANI(3144, 18),

		SLIMY_EEL(3381, 7),

		RAINBOW_FISH(10136, 11),

		CAVE_EEL(5003, 8),

		LAVA_EEL(2149, 7),

		HERRING(347, 5),

		EDIBLE_SEAWEED(403, 4),

		MACKEREL(355, 6),

		TROUT(333, 7),

		COD(339, 7),

		PIKE(351, 8),

		SALMON(329, 9),

		TUNA(361, 10),

		LOBSTER(379, 12),

		BASS(365, 13),

		SWORDFISH(373, 14),

		SWEETCORN(5988, 10),
		STRAWBERRY(5504, 6),

		MONKFISH(7946, 16),

		SHARK(385, 20),

		BARON_SHARK(19948, 28),

		TURTLE(397, 21),

		MANTA(391, 22),

		CAVEFISH(15266, 22),

		ROCKTAIL(15272, 23, 0, null, 10),

		/**
		 * Meats
		 */
		CHICKEN(2140, 3),

		MEAT(2142, 3), // TODO

		RABIT(3228, 5),

		ROAST_RABIT(7223, 7),

		KEBAB(1971, 7),

		ROASTED_BIRD_MEAT(9980, 6),

		CRAB_MEAT(7521, 10), // TODO

		ROASTED_BEAST_MEAT(9988, 8),

		CHOMPY(2878, 10),

		JUBBLY(7568, 15),

		OOMILE(2343, 14),
		
		PEACH(6883, 8),
		
		CHOCOLATE_MILK(1977, 4, 1925),

		/**
		 * Pies
		 */
		REDBERRY_PIE_FULL(2325, 5, 2333),

		REDBERRY_PIE_HALF(2333, 5, 2313),

		MEAT_PIE_FULL(2327, 6, 2331),

		MEAT_PIE_HALF(2331, 6, 2313),

		APPLE_PIE_FULL(2323, 7, 2335),

		APPLE_PIE_HALF(2335, 7, 2313),

		GARDEN_PIE_FULL(7178, 6, 7180, Effect.GARDEN_PIE),

		GARDEN_PIE_HALF(7180, 6, 2313, Effect.GARDEN_PIE),

		FISH_PIE_FULL(7188, 6, 7190, Effect.FISH_PIE),

		FISH_PIE_HALF(7188, 6, 2313, Effect.FISH_PIE),

		ADMIRAL_PIE_FULL(7198, 8, 7200, Effect.ADMIRAL_PIE),

		ADMIRAL_PIE_HALF(7200, 8, 2313, Effect.ADMIRAL_PIE),

		WILD_PIE_FULL(7208, 11, 7210, Effect.WILD_PIE),

		WILD_PIE_HALF(7210, 11, 2313, Effect.WILD_PIE),

		SUMMER_PIE_FULL(7218, 11, 7220, Effect.SUMMER_PIE),

		SUMMER_PIE_HALF(7220, 11, 2313, Effect.SUMMER_PIE),

		/**
		 * Stews
		 */

		STEW(2003, 11, 1923),

		SPICY_STEW(7513, 11, 1923, Effect.SPICY_STEW_EFFECT),

		CURRY(2011, 19, 1923),

		/**
		 * Pizzas
		 */
		PLAIN_PIZZA_FULL(2289, 7, 2291),

		PLAIN_PIZZA_HALF(2291, 7),

		MEAT_PIZZA_FULL(2293, 8, 2295),

		MEAT_PIZZA_HALF(2295, 8),

		ANCHOVIE_PIZZA_FULL(2297, 9, 2299),

		ANCHOVIE_PIZZA_HALF(2299, 9),

		PINEAPPLE_PIZZA_FULL(2301, 11, 2303),

		PINEAPPLE_PIZZA_HALF(2303, 11),

		/**
		 * Potato Toppings
		 */
		SPICEY_SAUCE(7072, 2, 1923),

		CHILLI_CON_CARNIE(7062, 14, 1923),

		SCRAMBLED_EGG(7078, 5, 1923),

		EGG_AND_TOMATO(7064, 8, 1923),

		FRIED_ONIONS(7084, 9, 1923),

		MUSHROOM_AND_ONIONS(7066, 11, 1923),

		FRIED_MUSHROOMS(7082, 5, 1923),

		TUNA_AND_CORN(7068, 13, 1923),

		/**
		 * Baked Potato
		 */
		BAKED_POTATO(6701, 4),

		POTATO_WITH_BUTTER(6703, 14),

		CHILLI_POTATO(7054, 14),

		POTATO_WITH_CHEESE(6705, 16),

		EGG_POTATO(7056, 16),

		MUSHROOM_AND_ONION_POTATO(7058, 20),

		TUNA_POTATO(7060, 24),

		/**
		 * Gnome Food
		 */
		TOAD_CRUNCHIES(2217, 8),
		PM_TOAD_CRUNCHIES(2243, 8),

		SPICY_CRUNCHIES(2213, 7),
		PM_SPICY_CRUNCHIES(2241, 7),

		WORM_CRUNCHIES(2205, 8),
		PM_WORM_CRUNCHIES(2237, 8),

		CHOCOCHIP_CRUNCHIES(2209, 7),
		PM_CHOCOCHIP_CRUNCHIES(2239, 7),

		FRUIT_BATTA(2277, 11),
		PM_FRUIT_BATTA(2225, 11),

		TOAD_BATTA(2255, 11),
		PM_TOAD_BATTA(2221, 11),

		WORM_BATTA(2253, 11),
		PM_WORM_BATTA(2219, 11),

		VEGETABLE_BATTA(2281, 11),
		PM_VEGETABLE_BATTA(2227, 11),

		CHEESE_AND_TOMATO_BATTA(9535, 11),
		PM_CHEESE_AND_TOMATO_BATTA(2223, 11),

		WORM_HOLE(2191, 12),
		PM_WORM_HOLE(2233, 12),

		VEG_BALL(2195, 12),
		PRE_MADE_VEG_BALL(2235, 12),

		TANGLED_TOAD_LEGS(2187, 15),
		PM_TANGLED_TOAD_LEGS(2231, 15),

		CHOCOLATE_BOMB(2185, 15),
		PM_CHOCOLATE_BOMB(2229, 15),

		/**
		 * Misc
		 */
		CHOCOLATE_BAR(1973, 2),
		EASTER_EGG(1961, 12),
		EASTER_EGG1(7928, 12),
		EASTER_EGG2(7929, 12),
		EASTER_EGG3(7930, 12),
		EASTER_EGG4(7931, 12),
		EASTER_EGG5(7932, 12),
		EASTER_EGG6(7933, 12),

		GGS(10960, 2),
		FSG(10961, 2),
		FB(10962, 2),
		CFL(10963, 2),
		BSH(10964, 2),
		FINGERS(10965, 2),
		GLM(10966, 2),
		RF(10967, 2),
		MUSHROOMS(10968, 2),
		FILLETS(10969, 2),
		LOACH(10970, 3),
		EELSUSHI(10971, 10),

		CAKE(1891, 4, 1893),

		TWO_THIRDS_CAKE(1893, 4, 1895),

		SLICE_OF_CAKE(1895, 4),

		CHOCOLATE_CAKE(1897, 4, 1899),

		TWO_THIRDS_CHOCOLATE_CAKE(1899, 4, 1901),

		CHOCOLATE_SLICE(1901, 4),

		FISHCAKE(7530, 11),

		BREAD(2309, 5),

		TEA(1978, 3, 1980, Effect.TEA_MESSAGE),
		WINE(1993, 11, 1935),

		CABBAGE(1965, 1, Effect.CABAGE_MESSAGE),

		ONION(1957, 1, Effect.ONION_MESSAGE),

		EVIL_TURNIP(12134, 1),

		POT_OF_CREAM(2130, 1),

		CHEESE_WHEEL(18789, 2),
		SPINACH_ROLL(1969, 2),

		PAPAYA(5972, 8, Effect.PAPAYA),

		BANANA(1963, 2),

		THIN_SNAIL_MEAT(3369, 5 + RandomUtils.random(2)),

		LEAN_SNAIL_MEAT(3371, 8),

		FAT_SNAIL_MEAT(3373, 8 + RandomUtils.random(2)),

		HEIM_CRAB(18159, 2),

		BLUE_CRAB(18175, 22),

		BOULDABASS(18171, 17),

		CAVE_MORAY(18177, 25),

		DUSK_EEL(18163, 7),

		GIANT_FLATFISH(18165, 10),

		RED_EYE(18161, 5),

		SALVE_EEL(18173, 20),

		SHORT_FINNED_EEL(18167, 12),

		WEB_SNIPER(18169, 15);

		/**
		 * The food id
		 */
		private int id;

		/**
		 * The healing health
		 */
		private int heal;

		/**
		 * The new food id if needed
		 */
		private int newId;

		private int extraHP;

		/**
		 * Our effect
		 */
		private Effect effect;

		/**
		 * A map of object ids to foods.
		 */
		private static Map<Integer, Food> foods = new HashMap<>();

		/**
		 * Gets a food by an object id.
		 *
		 * @param itemId
		 *            The object id.
		 * @return The food, or <code>null</code> if the object is not a food.
		 */
		public static Food forId(int itemId) {
			return foods.get(itemId);
		}

		/**
		 * Populates the tree map.
		 */
		static {
			for (final Food food : Food.values())
				foods.put(food.id, food);
		}

		/**
		 * Represents a food being eaten
		 *
		 * @param id
		 *            The food id
		 * @param heal
		 *            The healing health received
		 */
		private Food(int id, int heal) {
			this.id = id;
			this.heal = heal;
		}

		/**
		 * Represents a part of a food item being eaten (example: cake)
		 *
		 * @param id
		 *            The food id
		 * @param heal
		 *            The heal amount
		 * @param newId
		 *            The new food id
		 */
		private Food(int id, int heal, int newId) {
			this(id, heal, newId, null);
		}

		private Food(int id, int heal, int newId, Effect effect) {
			this(id, heal, newId, effect, 0);
		}

		private Food(int id, int heal, int newId, Effect effect, int extraHP) {
			this.id = id;
			this.heal = heal;
			this.newId = newId;
			this.effect = effect;
			this.extraHP = extraHP;
		}

		private Food(int id, int heal, Effect effect) {
			this(id, heal, 0, effect);
		}

		/**
		 * Gets the id.
		 *
		 * @return The id.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Gets the exp amount.
		 *
		 * @return The exp amount.
		 */
		public int getHeal() {
			return heal;
		}

		/**
		 * Gets the new food id
		 *
		 * @return The new food id.
		 */
		public int getNewId() {
			return newId;
		}

		public int getExtraHP() {
			return extraHP;
		}
	}

	public enum Effect {
		SUMMER_PIE {

			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				int runEnergy = (int) (player.getDetails().getRunEnergy() * 1.1);
				if (runEnergy > 100)
					runEnergy = 100;
				player.getDetails().setRunEnergy(runEnergy);
				int level = player.getSkills().getLevel(Skills.AGILITY);
				int realLevel = player.getSkills().getTrueLevel(Skills.AGILITY);
				player.getSkills().set(Skills.AGILITY, level >= realLevel ? realLevel + 5 : level + 5);
			}

		},

		GARDEN_PIE {

			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				int level = player.getSkills().getLevel(Skills.FARMING);
				int realLevel = player.getSkills().getTrueLevel(Skills.FARMING);
				player.getSkills().set(Skills.FARMING, level >= realLevel ? realLevel + 3 : level + 3);
			}

		},

		FISH_PIE {

			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				int level = player.getSkills().getLevel(Skills.FISHING);
				int realLevel = player.getSkills().getTrueLevel(Skills.FISHING);
				player.getSkills().set(Skills.FISHING, level >= realLevel ? realLevel + 3 : level + 3);
			}
		},

		ADMIRAL_PIE {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				int level = player.getSkills().getLevel(Skills.FISHING);
				int realLevel = player.getSkills().getTrueLevel(Skills.FISHING);
				player.getSkills().set(Skills.FISHING, level >= realLevel ? realLevel + 5 : level + 5);
			}
		},

		WILD_PIE {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				int level = player.getSkills().getLevel(Skills.SLAYER);
				int realLevel = player.getSkills().getTrueLevel(Skills.SLAYER);
				player.getSkills().set(Skills.SLAYER, level >= realLevel ? realLevel + 4 : level + 4);
				int level2 = player.getSkills().getLevel(Skills.RANGE);
				int realLevel2 = player.getSkills().getTrueLevel(Skills.RANGE);
				player.getSkills().set(Skills.RANGE, level2 >= realLevel2 ? realLevel2 + 4 : level2 + 4);
			}
		},

		SPICY_STEW_EFFECT {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				if (RandomUtils.random(100) > 5) {
					int level = player.getSkills().getLevel(Skills.COOKING);
					int realLevel = player.getSkills().getTrueLevel(Skills.COOKING);
					player.getSkills().set(Skills.COOKING, level >= realLevel ? realLevel + 6 : level + 6);
				} else {
					int level = player.getSkills().getLevel(Skills.COOKING);
					player.getSkills().set(Skills.COOKING, level <= 6 ? 0 : level - 6);
				}
			}

		},

		CABAGE_MESSAGE {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				player.getPackets().sendGameMessage("You don't really like it much.", true);
			}
		},

		PAPAYA {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				int restoredEnergy = (int) (player.getDetails().getRunEnergy() + 5);
				player.getDetails().setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
			}
		},

		TEA_MESSAGE {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				player.setNextForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
			}
		},

		ONION_MESSAGE {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				player.getPackets().sendGameMessage("It hurts to see a grown " + (player.getAppearance().isMale() ? "man" : "woman") + " cry.");
			}
		},

		POISION_KARMAMWANNJI_EFFECT {
			@Override
			public void effect(Object object) {
				Player player = (Player) object;
				player.applyHit(new Hit(player, 50, HitLook.POISON_DAMAGE));
			}
		};

		public void effect(Object object) {
		}
	}

	public static boolean eat(final Player player, Item item, int slot) {
		return eat(player, item, slot, null);
	}

	public static boolean eat(final Player player, Item item, int slot, Player givenFrom) {
		Food food = Food.forId(item.getId());
		if (food == null)
			return false;
		if (player.getMapZoneManager().execute(zone -> !zone.canEat(player, food)))
			return false;
		String name = ItemDefinitions.getItemDefinitions(food.getId()).getName().toLowerCase();
		int foodDelay = name.contains("half") ? 2 : 3;
		if (!player.getDetails().getFood().elapsed(foodDelay * 600))
			return false;
		player.getAudioManager().setDelay(1).sendSound(Sounds.EATING_FOOD);
		player.getPackets().sendGameMessage("You eat the " + name + ".");
		player.setNextAnimation(Animations.CONSUMING_ITEM);
		player.getDetails().getFood().reset();
		player.getInventory().getItems().set(slot, food.getNewId() == 0 ? null : new Item(food.getNewId(), 1));
		player.getInventory().refresh(slot);
		int hp = player.getHitpoints();
		player.heal(food.getHeal() * 10, food.getExtraHP() * 10);
		if (player.getHitpoints() > hp)
			player.getPackets().sendGameMessage("It heals some health.");
		player.getInterfaceManager().refreshHitPoints();
		player.getInventory().refresh();
		if (food.effect != null)
			food.effect.effect(player);
		player.getDetails().getStatistics().addStatistic(ItemDefinitions.getItemDefinitions(food.id).getName() + "_Eaten").addStatistic("Food_Eaten");
		return true;
	}

	public static boolean isConsumable(Item item) {
		return Food.forId(item.getId()) == null;
	}
}