package skills.summoning;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import skills.Skills;

public class Summoning {

	public static void spawnFamiliar(Player player, Pouch pouch) {
		if (player.getFamiliar() != null || player.getPet() != null) {
			player.getPackets().sendGameMessage("You already have a follower.");
			return;
		}
		if (player.getMapZoneManager().execute(controller -> !controller.canSummonFamiliar(player))
				|| player.getSkills().getLevel(Skills.SUMMONING) < pouch.getSummoningCost()) {
			return;
		}
		int levelReq = getRequiredLevel(pouch.getRealPouchId());
		if (player.getSkills().getTrueLevel(Skills.SUMMONING) < levelReq) {
			player.getPackets()
					.sendGameMessage("You need a summoning level of " + levelReq + " in order to use this pouch.");
			return;
		}
		//issue below
		final Familiar npc = createFamiliar(player, pouch);
		if (npc == null) {
			player.getPackets().sendGameMessage("This familiar is not added yet.");
			return;
		}
		player.getInventory().deleteItem(pouch.getRealPouchId(), 1);
		player.getSkills().drainSummoning(pouch.getSummoningCost());
		player.setFamiliar(npc);
	}

	public static Familiar createFamiliar(Player player, Pouch pouch) {
		try {
			return (Familiar) Class.forName("com.rs.game.npc.familiar." + (NPCDefinitions.getNPCDefinitions(getNPCId(pouch.getRealPouchId()))).getName()
					.replace(" ", "")
					.replace("ï", "i")
					.replace("-", "")
					.replace("(", "")
					.replace(")", ""))
					.getConstructor(Player.class, Pouch.class, WorldTile.class, boolean.class).newInstance(player, pouch, player, false);
		} catch (Throwable e) {
				e.printStackTrace();
			return null;
		}
	}
	
	public static boolean hasPouch(Player player) {
		for (Pouch pouch : Pouch.values())
			if (player.getInventory().containsOneItem(pouch.getRealPouchId()))
				return true;
		return false;
	}

	public static enum Pouch {

		SPIRIT_WOLF(67, 12047, 0.1, 4.8, 360000, 1),

		DREADFOWL(69, 12043, 0.1, 9.3, 240000, 1),

		SPIRIT_SPIDER(83, 12059, 0.2, 12.6, 900000, 2),

		THORNY_SNAIL(119, 12019, 0.2, 12.6, 960000, 2),

		GRANITE_CRAB(75, 12009, 0.2, 21.6, 1080000, 2),

		SPIRIT_MOSQUITO(177, 12778, 0.2, 46.5, 720000, 2), // gfx 1440

		DESERT_WYRM(121, 12049, 0.4, 31.2, 1140000, 1),

		SPIRIT_SCORPIAN(101, 12055, 0.9, 83.2, 1020000, 2),

		SPIRIT_TZ_KIH(179, 12808, 1.1, 96.8, 1080000, 3),

		ALBINO_RAT(103, 12067, 2.3, 100.4, 1320000, 3),

		SPIRIT_KALPHITE(99, 12063, 2.5, 110, 1320000, 3),

		COMPOST_MOUNT(137, 12091, 0.6, 49.8, 1440000, 6),

		GIANT_CHINCHOMPA(165, 12800, 2.5, 50, 1860000, 1),

		VAMPYRE_BAT(71, 12053, 1.6, 86, 1980000, 4),

		HONEY_BADGER(105, 12065, 1.6, 90.8, 1500000, 4),

		BEAVER(89, 12021, 0.7, 57.6, 1620000, 4),

		VOID_RAVAGER(157, 12818, 0.7, 59.6, 1620000, 4),

		VOID_SPINNER(157, 12780, 0.7, 59.6, 1620000, 4),

		VOID_TORCHER(157, 12798, 0.7, 59.6, 5640000, 4),

		VOID_SHIFTER(157, 12814, 0.7, 59.6, 5640000, 4),

		BRONZE_MINOTAUR(149, 12073, 2.4, 79.8, 1800000, 9),

		BULL_ANT(91, 12087, 0.6, 52.8, 1800000, 5),

		MACAW(73, 12071, 0.8, 72.4, 1860000, 5),

		EVIL_TURNIP(77, 12051, 2.1, 184.8, 1800000, 5),

		SPIRIT_COCKATRICE(149, 12095, 0.9, 75.2, 2160000, 5),

		SPIRIT_GUTHATRICE(149, 12097, 0.9, 75.2, 2160000, 5),

		SPIRIT_SARATRICE(149, 12099, 0.9, 75.2, 2160000, 5),

		SPIRIT_ZAMATRICE(149, 12101, 0.9, 75.2, 2160000, 5),

		SPIRIT_PENGATRICE(149, 12103, 0.9, 75.2, 2160000, 5),

		SPIRIT_CORAXATRICE(149, 12105, 0.9, 75.2, 2160000, 5),

		SPIRIT_VULATRICE(149, 12107, 0.9, 75.2, 2160000, 5),

		IRON_MINOTAUR(149, 12075, 4.6, 404.8, 2220000, 9),

		PYRELORD(185, 12816, 2.3, 202.4, 1920000, 5), // TODO SPECIAL ONCE
		// JEWLARY MAKING IS ADDED

		MAGPIE(81, 12041, 0.9, 83.2, 2040000, 5),

		BLOATED_LEECH(131, 12061, 2.4, 115.2, 2040000, 5),

		SPIRIT_TERRORBIRD(129, 12007, 0.7, 68.4, 2160000, 6),

		ABYSSAL_PARASITE(125, 12035, 1.1, 94.8, 1800000, 6),

		SPIRIT_JELLY(123, 12027, 5.5, 100, 2580000, 6),

		STEEL_MINOTAUR(149, 12077, 5.6, 492.8, 2760000, 9),

		IBIS(85, 12531, 1.1, 98.8, 2280000, 6),

		SPIRIT_KYATT(169, 12812, 5.7, 201.6, 2940000, 6),

		SPIRIT_LARUPIA(181, 12784, 5.7, 201.6, 2940000, 6),

		SPIRIT_GRAAHK(167, 12810, 5.6, 201.6, 2940000, 6),

		KARAMTHULU_OVERLOAD(135, 12023, 5.8, 210.4, 2640000, 6), // TODO 1457
		// IMPORTANT

		SMOKE_DEVIL(133, 12085, 3.1, 268, 2880000, 7),

		ABYSSAL_LURKER(87, 12037, 1.9, 109.6, 2460000, 7),

		SPIRIT_COBRA(115, 12015, 3.1, 276.8, 3360000, 7),

		STRANGER_PLANT(141, 12045, 3.2, 281.6, 2940000, 7),

		MITHRIL_MINOTAUR(149, 12079, 6.6, 580.8, 3300000, 9),

		BARKER_TOAD(107, 12123, 1, 87, 480000, 7),

		WAR_TORTOISE(117, 12031, 0.7, 58.6, 2580000, 7),

		BUNYIP(153, 12029, 1.4, 119.2, 2640000, 7),

		FRUIT_BAT(79, 12033, 1.4, 121.2, 2700000, 7),

		RAVENOUS_LOCUST(97, 12820, 1.5, 132.0, 1440000, 4),

		ARCTIC_BEAR(109, 12057, 1.1, 93.2, 1680000, 8),

		PHEONIX(-1, 14623, 3, 101, 1800000, 8),

		OBSIDIAN_GOLEM(173, 12792, 7.3, 342.4, 3300000, 8),

		GRANITE_LOBSTER(93, 12069, 3.7, 325.6, 2920000, 8),

		PRAYING_MANTIS(95, 12011, 3.6, 329.6, 4140000, 8),

		FORGE_REGENT(187, 12782, 1.5, 134, 2700000, 9),

		ADAMANT_MINOTAUR(149, 12081, 8.6, 668.8, 3960000, 9),

		TALON_BEAST(143, 12794, 3.8, 105.2, 2940000, 9),

		GIANT_ENT(139, 12013, 1.6, 136.8, 2940000, 8),

		FIRE_TITAN(159, 12802, 7.9, 395.2, 3720000, 9),

		MOSS_TITAN(159, 12804, 7.9, 395.2, 3720000, 9),

		ICE_TITAN(159, 12806, 7.9, 395.2, 3720000, 9),

		HYDRA(145, 12025, 1.6, 140.8, 2940000, 8), // TODO Only combat part
		// (easy)

		SPIRIT_DAGANNOTH(147, 12017, 4.1, 364.8, 3420000, 9),

		LAVA_TITAN(171, 12788, 8.3, 330.4, 3660000, 9),

		SWAMP_TITAN(155, 12776, 4.2, 373.6, 3360000, 9),

		RUNE_MINOTAUR(149, 12083, 8.6, 756.8, 9060000, 9),

		UNICORN_STALLION(113, 12039, 1.8, 154.4, 3240000, 9),

		GEYSER_TITAN(161, 12786, 8.9, 383.2, 4140000, 10),

		WOLPERTINGER(151, 12089, 4.6, 404.8, 3720000, 10),

		ABYSSAL_TITAN(175, 12796, 1.9, 163.2, 1920000, 10),

		IRON_TITAN(183, 12822, 8.6, 417.6, 3600000, 10),

		PACK_YAK(111, 12093, 4.8, 422.2, 3480000, 10),

		STEEL_TITAN(163, 12790, 4.9, 435.2, 3840000, 10),

		/**
		 * Dungeoneering
		 */

		CUB_BLOODRAGER(-1, 17935, 0.5, 5.0, 2700000, 1),
		CUB_DEATHSLINGER(-1, 17985, 1, 5.7, 2700000, 1),
		CUB_STORMBRINGER(-1, 17945, .6, 6.4, 2700000, 1),
		CUB_HOARDSTALKER(-1, 17955, 0.7, 7.1, 2700000, 1),
		CUB_WORLDBEARER(-1, 17975, 0.9, 7.8, 2700000, 1),
		CUB_SKINWEAVER(-1, 17965, 0.8, 8.5, 2700000, 1),

		LITTLE_BLOODRAGER(-1, 17936, 1, 19.5, 2700000, 2),
		LITTLE_DEATHSLINGER(-1, 17986, 1.5, 20.5, 2700000, 2),
		LITTLE_STORMBRINGER(-1, 17946, 1.1, 21.5, 2700000, 2),
		LITTLE_HOARDSTALKER(-1, 17956, 1.2, 22.5, 2700000, 2),
		LITTLE_WORLDBEARER(-1, 17976, 1.4, 23.5, 2700000, 2),
		LITTLE_SKINWEAVER(-1, 17966, 1.3, 24.5, 2700000, 2),

		NAIVE_BLOODRAGER(-1, 17937, 1.5, 43, 2700000, 3),
		NAIVE_DEATHSLINGER(-1, 17987, 2, 44.4, 2700000, 3),
		NAIVE_STORMBRINGER(-1, 17947, 1.6, 45.8, 2700000, 3),
		NAIVE_HOARDSTALKER(-1, 17957, 1.7, 47.2, 2700000, 3),
		NAIVE_WORLDBEARER(-1, 17977, 1.9, 48.6, 2700000, 3),
		NAIVE_SKINWEAVER(-1, 17967, 1.8, 50, 2700000, 3),

		KEEN_BLOODRAGER(-1, 17938, 2, 68.5, 2700000, 4),
		KEEN_DEATHSLINGER(-1, 17988, 2.5, 70.4, 2700000, 4),
		KEEN_STORMBRINGER(-1, 17948, 2.1, 72.3, 2700000, 4),
		KEEN_HOARDSTALKER(-1, 17958, 2.2, 74.2, 2700000, 4),
		KEEN_WORLDBEARER(-1, 17978, 2.4, 76.1, 2700000, 4),
		KEEN_SKINWEAVER(-1, 17968, 2.3, 78, 2700000, 4),

		BRAVE_BLOODRAGER(-1, 17939, 2.5, 99.5, 2700000, 5),
		BRAVE_DEATHSLINGER(-1, 17989, 3, 102, 2700000, 5),
		BRAVE_STORMBRINGER(-1, 17949, 2.6, 104.5, 2700000, 5),
		BRAVE_HOARDSTALKER(-1, 17959, 2.7, 107, 2700000, 5),
		BRAVE_WORLDBEARER(-1, 17979, 2.9, 109.5, 2700000, 5),
		BRAVE_SKINWEAVER(-1, 17969, 2.8, 112, 2700000, 5),

		BRAH_BLOODRAGER(-1, 17940, 3, 157, 2700000, 6),
		BRAH_DEATHSLINGER(-1, 17990, 3.5, 160.5, 2700000, 6),
		BRAH_STORMBRINGER(-1, 17950, 3.1, 164, 2700000, 6),
		BRAH_HOARDSTALKER(-1, 17960, 3.2, 167.5, 2700000, 6),
		BRAH_WORLDBEARER(-1, 17980, 3.4, 171, 2700000, 6),
		BRAH_SKINWEAVER(-1, 17970, 3.3, 174.5, 2700000, 6),

		NAABE_BLOODRAGER(-1, 17941, 3.5, 220, 2700000, 7),
		NAABE_DEATHSLINGER(-1, 17991, 4, 224.6, 2700000, 7),
		NAABE_STORMBRINGER(-1, 17951, 3.6, 229.2, 2700000, 7),
		NAABE_HOARDSTALKER(-1, 17961, 3.7, 233.8, 2700000, 7),
		NAABE_WORLDBEARER(-1, 17981, 3.9, 238.4, 2700000, 7),
		NAABE_SKINWEAVER(-1, 17971, 3.8, 243, 2700000, 7),

		WISE_BLOODRAGER(-1, 17942, 4, 325, 2700000, 8),
		WISE_DEATHSLINGER(-1, 17992, 4.5, 330.8, 2700000, 8),
		WISE_STORMBRINGER(-1, 17952, 4.1, 336.6, 2700000, 8),
		WISE_HOARDSTALKER(-1, 17962, 4.2, 342.4, 2700000, 8),
		WISE_WORLDBEARER(-1, 17982, 4.4, 348.2, 2700000, 8),
		WISE_SKINWEAVER(-1, 17972, 4.3, 354, 2700000, 8),

		ADEPT_BLOODRAGER(-1, 17943, 4.5, 517.5, 2700000, 10),
		ADEPT_DEATHSLINGER(-1, 17993, 5, 524.6, 2700000, 10),
		ADEPT_STORMBRINGER(-1, 17953, 4.6, 531.7, 2700000, 10),
		ADEPT_HOARDSTALKER(-1, 17963, 4.7, 538.8, 2700000, 10),
		ADEPT_WORLDBEARER(-1, 17983, 4.9, 545.9, 2700000, 10),
		ADEPT_SKINWEAVER(-1, 17973, 4.8, 553, 2700000, 10),

		SACHEM_BLOODRAGER(-1, 17944, 5, 810, 2700000, 10),
		SACHEM_DEATHSLINGER(-1, 17994, 5.5, 818.5, 2700000, 10),
		SACHEM_STORMBRINGER(-1, 17954, 5.1, 827, 2700000, 10),
		SACHEM_HOARDSTALKER(-1, 17964, 5.2, 835.5, 2700000, 10),
		SACHEM_WORLDBEARER(-1, 17984, 5.4, 844, 2700000, 10),
		SACHEM_SKINWEAVER(-1, 17974, 5.3, 852.5, 2700000, 10),

		CLAY_BEAST1(-1, 14422, 0, 0, 1800000, 1),
		CLAY_BEAST2(-1, 14424, 0, 0, 1800000, 3),
		CLAY_BEAST3(-1, 14426, 0, 0, 1800000, 5),
		CLAY_BEAST4(-1, 14428, 0, 0, 1800000, 7),
		CLAY_BEAST5(-1, 14430, 0, 0, 1800000, 10);

		private static final Map<Integer, Pouch> pouches = new HashMap<Integer, Pouch>();

		static {
			for (Pouch pouch : Pouch.values()) {
				pouches.put(pouch.realPouchId, pouch);
			}
		}

		public static Pouch forId(int id) {
			return pouches.get(id);
		}

		private int realPouchId;
		private int summoningCost;
		private double minorExperience;
		private double experience;
		private int pouchSetting;
		private long pouchTime;

		private Pouch(int pouchSetting, int realPouchId, double minorExperience, double experience, long pouchTime, int summoningCost) {
			this.pouchSetting = pouchSetting;
			this.realPouchId = realPouchId;
			this.minorExperience = minorExperience;
			this.experience = experience;
			this.summoningCost = summoningCost;
			this.pouchTime = pouchTime;
		}

		public int getRealPouchId() {
			return realPouchId;
		}

		public int getSummoningCost() {
			return summoningCost;
		}

		public double getMinorExperience() {
			return minorExperience;
		}

		public double getExperience() {
			return experience;
		}

		public int getPouchSetting() {
			return pouchSetting;
		}

		public long getPouchTime() {
			return pouchTime;
		}
	}

	public static final int POUCHES_INTERFACE = 672, SCROLLS_INTERFACE = 666;
	private static final Animation POUCH_INFUSION_ANIMATION = new Animation(725);
	private static final Graphics POUCH_INFUSION_GRAPHICS = new Graphics(1207);

	public static int getScrollId(int id) {
		return ClientScriptMap.getMap((short) 1283).getIntValue(id);
	}

	public static int getRequiredLevel(int id) {
		return ClientScriptMap.getMap((short) 1185).getIntValue(id);
	}

	public static int getNPCId(int id) {
		return ClientScriptMap.getMap( (short) 1320).getIntValue(id);
	}

	public static String getRequirementsMessage(int id) {
		return ClientScriptMap.getMap((short) 1186).getStringValue(id);
	}

	public static void openInfusionInterface(Player player) {
		player.getInterfaceManager().sendInterface(POUCHES_INTERFACE);
		player.getPackets().sendPouchInfusionOptionsScript(POUCHES_INTERFACE, 16, 78, 8, 10, "Infuse<col=FF9040>",
				"Infuse-5<col=FF9040>", "Infuse-10<col=FF9040>", "Infuse-X<col=FF9040>", "Infuse-All<col=FF9040>",
				"List<col=FF9040>");
		player.getPackets().sendIComponentSettings(POUCHES_INTERFACE, 16, 0, 462, 190);
		player.getAttributes().get(Attribute.INFUSING_SCROLL).set(false);
	}

	public static void openScrollInfusionInterface(Player player) {
		player.getInterfaceManager().sendInterface(SCROLLS_INTERFACE);
		player.getPackets().sendScrollInfusionOptionsScript(SCROLLS_INTERFACE, 16, 78, 8, 10, "Transform<col=FF9040>",
				"Transform-5<col=FF9040>", "Transform-10<col=FF9040>", "Transform-X<col=FF9040>",
				"Transform-All<col=FF9040>");
		player.getPackets().sendIComponentSettings(SCROLLS_INTERFACE, 16, 0, 462, 126);
		player.getAttributes().get(Attribute.INFUSING_SCROLL).set(true);
	}

	public static void handlePouchInfusion(Player player, int slotId, int creationCount) {
		int slotValue = (slotId - 2) / 5;
		Pouch pouch = Pouch.values()[slotValue];
		if (pouch == null)
			return;
		boolean infusingScroll = (boolean) player.getAttributes().get(Attribute.INFUSING_SCROLL).getBoolean(),
				hasRequirements = false;
		ItemDefinitions def = ItemDefinitions.getItemDefinitions(pouch.getRealPouchId());
		ObjectArrayList<Item> itemReq = def.getCreateItemRequirements(infusingScroll);
		int level = getRequiredLevel(pouch.getRealPouchId());
		if (itemReq != null) {
			itemCount: for (int i = 0; i < creationCount; i++) {
				if (!player.getInventory().containsListItems(itemReq)) {
					sendItemList(player, infusingScroll, creationCount, slotId);
					break itemCount;
				} else if (!player.getInventory().hasFreeSlots()) {
					player.getPackets().sendGameMessage("You currently have no space in your inventory.");
					break itemCount;
				} else if (player.getSkills().getTrueLevel(Skills.SUMMONING) < level) {
					player.getPackets()
							.sendGameMessage("You need a summoning level of " + level + " to create this pouch.");
					break itemCount;
				}
				hasRequirements = true;
				player.getInventory().removeItems(itemReq);
				player.getInventory()
						.addItem(new Item(infusingScroll ? getScrollId(pouch.getRealPouchId()) : pouch.getRealPouchId(),
								infusingScroll ? 10 : 1));
				player.getSkills().addExperience(Skills.SUMMONING,
						infusingScroll ? pouch.getMinorExperience() : pouch.getExperience());
			}
		}
		if (!hasRequirements) {
			player.getAttributes().get(Attribute.INFUSING_SCROLL).set(infusingScroll);
			return;
		}
		player.getInterfaceManager().closeInterfaces();
		player.setNextAnimation(POUCH_INFUSION_ANIMATION);
		player.setNextGraphics(POUCH_INFUSION_GRAPHICS);
	}

	public static void switchInfusionOption(Player player) {
		boolean infusingScroll = (boolean) player.getAttributes().get(Attribute.INFUSING_SCROLL).getBoolean();
		if (infusingScroll)
			openInfusionInterface(player);
		else
			openScrollInfusionInterface(player);
	}

	public static void sendItemList(Player player, boolean infusingScroll, int count, int slotId) {
		int slotValue = (slotId - 2) / 5;
		Pouch pouch = Pouch.values()[slotValue];
		if (pouch == null)
			return;
		if (infusingScroll)
			player.getPackets().sendGameMessage("This scroll requires 1 "
					+ ItemDefinitions.getItemDefinitions(pouch.getRealPouchId()).name.toLowerCase() + ".");
		else
			player.getPackets().sendGameMessage(getRequirementsMessage(pouch.getRealPouchId()));
	}
}
