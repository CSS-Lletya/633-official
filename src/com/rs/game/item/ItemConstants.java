package com.rs.game.item;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableSet;
import com.rs.GameConstants;
import com.rs.constants.ItemNames;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

public class ItemConstants {
	
	/**
	 * Items that are protected upon a Players death by default in the Wilderness
	 */
	public static final String[] PROTECT_ON_DEATH = { "chaotic", "stream", "defender",
			"fire cape", "farseer kiteshield", "eagle-eye kiteshield", "gravite" };
	
	/**
	 * A Players starter kit when joining the game for the first time.
	 */
	public final static ImmutableSet<Item> STATER_KIT = ImmutableSet.of(
			new Item(ItemNames.BRONZE_HATCHET_1351, 1), new Item(ItemNames.TINDERBOX_590), new Item(ItemNames.SMALL_FISHING_NET_303),
			new Item(ItemNames.SHRIMPS_315), new Item(ItemNames.EMPTY_BUCKET_3727), new Item(ItemNames.EMPTY_POT_1931)
			, new Item(ItemNames.BREAD_2309), new Item(ItemNames.BRONZE_PICKAXE_1265), new Item(ItemNames.BRONZE_DAGGER_1205)
			, new Item(ItemNames.BRONZE_SWORD_1277), new Item(ItemNames.WOODEN_SHIELD_1171), new Item(ItemNames.SHORTBOW_841)
			, new Item(ItemNames.BRONZE_ARROW_882, 25), new Item(ItemNames.AIR_RUNE_556, 25), new Item(ItemNames.MIND_RUNE_558,15)
			, new Item(ItemNames.WATER_RUNE_555,6), new Item(ItemNames.EARTH_RUNE_557, 4), new Item(ItemNames.MIND_RUNE_558, 2)
	);

	public static boolean canWear(Item item, Player player) {
		if (player.getDetails().getRights() == Rights.ADMINISTRATOR)
			return true;
		if (!item.getDefinitions().isWearItem())
			return false;
		if (item.getId() == ItemNames.FIRE_CAPE_6570) {
			if (player.getDetails().getCompletedFightCaves().isFalse()) {
				player.getPackets()
						.sendGameMessage("You need to complete at least once fight cave minigame to use this cape.");
				return false;
			}
		}
		if (item.getId() == ItemNames.QUEST_POINT_CAPE_9813 || item.getId() == ItemNames.QUEST_POINT_HOOD_9814) {
			if (player.getDetails().getQuestPoints().get() != GameConstants.TOTAL_QUEST_POINTS) {
				player.getPackets().sendGameMessage("You need to complete all quests before wearing this.");
				return false;
			}
		}
		return true;
	}

	public static boolean isDestroyable(Item item) {
		return item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended();
	}

	public static boolean isTradeable(Item item) {
		if ((!item.getDefinitions().isStackable() && item.getDefinitions().getCertId() == -1)
				|| item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended())
			return false;
		if (!item.getDefinitions().isExchangableItem())
			return false;
		if (item.getDefinitions().isDestroyItem() || item.getDefinitions().isLended())
			return false;
		String[] UNTRADABLE_NAMES = { "brawling gloves", "charm", "clue scroll", "lumberjack", "constructor's", "agile",
				"fishing", "penance", "fighter", "charming imp", "black ibis", "irit gloves", "avantoe gloves",
				"kwuarm gloves", "cadantine gloves", "ring of fire", "book of char", "flame gloves", "chaotic",
				"shark gloves", "swordfish gloves", "grace", "novite", "bathus", "marmaros", "kratonite", "fractite",
				"zephyrium", "argonite", "katagon", "gorgonite", "promethium", "primal" };
		if (Arrays.stream(UNTRADABLE_NAMES)
				.anyMatch(name -> item.getDefinitions().getName().toLowerCase().contains(name)))
			return false;
		if (IntStream.range(18337, 19888).anyMatch(id -> id == item.getId()))
			return false;
		if (IntStream.range(5509, 5515).anyMatch(id -> id == item.getId()))
			return false;
		if (IntStream
				.of(10844, 10845, 10846, 10847, 10848, 10849, 10850, 10581, 35, 11283, 10588, 772, 6570, 6529, 7462,
						23659, 19784, 15433, 15435, 15432, 15434, 12158, 12159, 12160, 12163, 5070, 5071, 5072, 5073,
						5074, 7413, 11966, 14387, 14389, 20072, 8844, 8845, 8846, 8847, 8848, 8849, 8850)
				.anyMatch(id -> item.getId() == id))
			return false;
		return true;
	}

	public static boolean canBankItem(Player player, Item item) {
		String[] ITEMS = { "lamp", "skilling" };
		return Arrays.stream(ITEMS).anyMatch(items -> item.getDefinitions().getName().toLowerCase().contains(items)
				&& !item.getDefinitions().getName().toLowerCase().contains("oil"));
	}
	
	public static int removeAttachedId(Item item) {
	    int[][] cases = {
	        {12675, 12674, 3751},
	        {12678, 12679, 3755},
	        {12672, 12673, 3749},
	        {12676, 12677, 3753},
	        {15018, 6731},
	        {15019, 6733},
	        {15020, 6735},
	        {15220, 6737},
	        {15017, 6575},
	        {19335, 19333},
	        {19336, 19346},
	        {19337, 19350},
	        {19338, 19339, 19348},
	        {19340, 19352},
	        {19341, 19354},
	        {19342, 19358},
	        {19343, 19344, 19356},
	        {19345, 19360}
	    };
	    return Arrays.stream(cases)
	            .filter(caseItem -> Arrays.stream(caseItem, 0, caseItem.length - 1).anyMatch(id -> id == item.getId()))
	            .findFirst()
	            .map(caseItem -> caseItem[caseItem.length - 1])
	            .orElse(-1);
	}
	
	public static int removeAttachedId2(Item item) {
	    if (item.getDefinitions().getName().toLowerCase().contains("vine whip"))
	        return 4151;

	    int[][] cases = {
	        {19335, 6585},
	        {19336, 19341, 11335},
	        {19337, 19342, 14479},
	        {19338, 19343, 4087},
	        {19339, 19344, 4585},
	        {19340, 19345, 1187}
	    };

	    return Arrays.stream(cases)
	            .filter(caseItem -> Arrays.stream(caseItem, 0, caseItem.length - 1).anyMatch(id -> id == item.getId()))
	            .findFirst()
	            .map(caseItem -> caseItem[caseItem.length - 1])
	            .orElse(-1);
	}

}