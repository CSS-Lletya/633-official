package com.rs.game.item;

import com.rs.GameConstants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;

import skills.Skills;

public class ItemConstants {

	public static boolean canCheckCharges(int id) {
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149
				|| id == 20153 || id == 20157 || id == 20161 || id == 20165
				|| id == 20169 || id == 20171 || id == 20173) // all nex weapons and armours
			return true;

		return false;
	}

	// return amt of charges
	public static int getItemDefaultCharges(int id) {
		// Pvp Armors
		if (id == 13910 || id == 13913 || id == 13916 || id == 13919
				|| id == 13922 || id == 13925 || id == 13928 || id == 13931
				|| id == 13934 || id == 13937 || id == 13940 || id == 13943
				|| id == 13946 || id == 13949 || id == 13952)
			return 1500 * GameConstants.DEGRADE_GEAR_RATE; // 15minutes
		if (id == 13960 || id == 13963 || id == 13966 || id == 13969
				|| id == 13972 || id == 13975)
			return 2000 * GameConstants.DEGRADE_GEAR_RATE;// 20 min.
		if (id == 13860 || id == 13863 || id == 13866 || id == 13869
				|| id == 13872 || id == 13875 || id == 13878 || id == 13886
				|| id == 13889 || id == 13892 || id == 13895 || id == 13898
				|| id == 13901 || id == 13904 || id == 13907 || id == 13960)
			return 6000 * GameConstants.DEGRADE_GEAR_RATE; // 1hour
		// Nex Armor
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149
				|| id == 20153 || id == 20157 || id == 20161 || id == 20165
				|| id == 20169)
			return 60000 * GameConstants.DEGRADE_GEAR_RATE; // 10 hour
		if (id >= 24450 && id <= 24454) // rouge gloves
			return 6000;
		if (id >= 22358 && id <= 22369) // dominion gloves
			return 24000 * GameConstants.DEGRADE_GEAR_RATE;
		if (id == 22444) // neem oil
			return 2000;
		// polipore armors
		if (id == 22460 || id == 22464 || id == 22468 || id == 22472
				|| id == 22476 || id == 22480 || id == 22484 || id == 22488
				|| id == 22492)
			return 60000 * GameConstants.DEGRADE_GEAR_RATE; // 10 hour
		if (id == 22496)
			return 3000;
		if (id == 20171 || id == 20173)
			return 60000;
		if (id == 11283)
			return 50;
		return -1;
	}

	// return what id it degrades to when charges end, -1 for disapear which is
	// default so we
	public static int getItemDegrade(int id) {
		if (id == 11283) // DFS
			return 11284;
		if (id == 22444) // neem oil
			return 1935;
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149
				|| id == 20153 || id == 20157 || id == 20161 || id == 20165
				|| id == 20169)
			return id + 1;
		// visor
		if (id == 22460 || id == 22472 || id == 22484)
			return 22452;
		if (id == 22464 || id == 22476 || id == 22488)
			return 22454;
		if (id == 22468 || id == 22480 || id == 22492)
			return 22456;
		if (id == 22496) // polypore staff
			return 22498; // stick
		if (id == 20171 || id == 20173)
			return 20174;
		return -1;
	}

	// returns what it degrades into when wear(usualy first time)
	public static int getDegradeItemWhenWear(int id) {
		// Pvp armors
		if (id == 13958 || id == 13961 || id == 13964 || id == 13967
				|| id == 13970 || id == 13973 || id == 13908 || id == 13911
				|| id == 13914 || id == 13917 || id == 13920 || id == 13923
				|| id == 13941 || id == 13944 || id == 13947 || id == 13950
				|| id == 13958 || id == 13938 || id == 13926 || id == 13929
				|| id == 13932 || id == 13935)
			return id + 2; // When equiping it becomes Corrupted
		return -1;
	}

	// returns what it degrades into when start to combating(usualy first time)
	public static int getDegradeItemWhenCombating(int id) {
		// Nex and Pvp
		if (id == 20135 || id == 20139 || id == 20143 || id == 20147
				|| id == 20151 || id == 20155 || id == 20159 || id == 20163
				|| id == 20167 || id == 20171 || id == 13858 || id == 13861
				|| id == 13864 || id == 13867 || id == 13870 || id == 13873
				|| id == 13876 || id == 13884 || id == 13887 || id == 13890
				|| id == 13893 || id == 13896 || id == 13905 || id == 13902
				|| id == 13899)
			return id + 2;
		// polipore armors
		if (id == 22458 || id == 22462 || id == 22466 || id == 22470
				|| id == 22474 || id == 22478 || id == 22482 || id == 22486
				|| id == 22490)
			return id + 2;
		return -1;
	}

	public static boolean itemDegradesWhileHit(int id) {
		if (id == 2550)
			return true;
		return false;
	}

	// removes a charge per ticket when wearing this
	public static boolean itemDegradesWhileWearing(int id) {
		String name = ItemDefinitions.getItemDefinitions(id).getName()
				.toLowerCase();
		if (name.contains("c. dragon") || name.contains("corrupt dragon")
				|| name.contains("vesta's") || name.contains("statius'")
				|| name.contains("morrigan's") || name.contains("zuriel's"))
			return true;
		return false;
	}

	// removes a charge per ticket when wearing this and attacking
	public static boolean itemDegradesWhileCombating(int id) {
		// nex armors
		if (id == 20137 || id == 20141 || id == 20145 || id == 20149
				|| id == 20153 || id == 20157 || id == 20161 || id == 20165
				|| id == 20169)
			return true; // 10 hour
		// polypore gear
		if (id == 22460 || id == 22464 || id == 22468 || id == 22472
				|| id == 22476 || id == 22480 || id == 22484 || id == 22488
				|| id == 22492)
			return true;
		if (id == 20171 || id == 20173) // zaryte bow
			return true;
		if (id >= 24450 && id <= 24454 // rouge gloves
				|| id >= 22358 && id <= 22369) // dominion gloves
			return true;
		return false;
	}

	public static boolean canWear(Item item, Player player) {
		if (player.getDetails().getRights() == Rights.ADMINISTRATOR)
			return true;
		if (!item.getDefinitions().isWearItem())
			return false;
		else if (item.getId() == 20767) {
			for (int skill = 0; skill < 24; skill++) {
				if (skill == Skills.DUNGEONEERING
						|| skill == Skills.CONSTRUCTION) {
					continue;
				}
				if (player.getSkills().getLevel(skill) < (skill == Skills.DUNGEONEERING ? 120
						: 99)) {
					player.getPackets()
							.sendGameMessage(
									"You must have the maximum level of each skill in order to use this cape.");
					return false;
				}
			}
		} else if ((item.getId() == 20769 || item.getId() == 20771)) {

			for (int skill = 0; skill < 24; skill++) {
				if (skill == Skills.DUNGEONEERING
						|| skill == Skills.CONSTRUCTION) {
					continue;
				}
				if (player.getSkills().getLevel(skill) < (skill == Skills.DUNGEONEERING ? 120
						: 99)) {
					player.getPackets()
							.sendGameMessage(
									"You must have the maximum level of each skill in order to use this cape.");
					return false;
				}
			}
		} else if (item.getId() == 6570 || item.getId() == 10566
				|| item.getId() == 10637) { // temporary
//			if (!player.isCompletedFightCaves()) {
//				player.getPackets()
//						.sendGameMessage(
//								"You need to complete at least once fight cave minigame to use this cape.");
//				return false;
//			}
		}
		return true;
	}

	public static boolean isDestroy(Item item) {
		return item.getDefinitions().isDestroyItem()
				|| item.getDefinitions().isLended();
	}

	public static boolean isTradeable(Item item) {
		if (item.getDefinitions().isDestroyItem()
				|| item.getDefinitions().isLended()
				|| ItemConstants.getItemDefaultCharges(item.getId()) != -1)
			return false;
		if (item.getDefinitions().getName().toLowerCase()
				.contains("flaming skull"))
			return false;
		switch (item.getId()) {
		/*
		 * case 18349: //chaotic case 18350: case 18351: case 18352: case 18353:
		 * case 18354: case 18355: case 18356: case 18357: case 18358: case
		 * 18359: case 18360: case 18361: case 18362: case 18363: case 18364:
		 */
		case 20763: // veteran cape
		case 20767: // max cape and hood
		case 20768:
		case 10844: // sq'irk
		case 10845:
		case 10846:
		case 10847:
		case 10848:
		case 10849:
		case 10850:
		case 10581:
		case 23044: // mindspike
		case 23045:
		case 23046:
		case 23047:
		case 35: // excalibur
		case 22496: // polypore degraded gear
		case 22492:
		case 22488:
		case 22484:
		case 22480:
		case 22476:
		case 22472:
		case 22468:
		case 22464:
		case 22460:
		case 11283: // dragonfire shield
		case 24444: // neem drupe stuff
		case 24445:
		case 10588: // Salve amulet (e)
		case 772: // dramen staff
		case 6570: // firecape
		case 6529: // tokkul
		case 7462: // barrow gloves
		case 23659: // tookhaar-kal
		case 19784: // korasi
		case 24455:// crucible weapons
		case 24456:
		case 24457:
		case 15433:// red and blue cape from nomad
		case 15435:
		case 15432:
		case 15434:
		case 12158:
		case 12159:
		case 12160:
		case 12163:
			// bird nests with search
		case 5070:
		case 5071:
		case 5072:
		case 5073:
		case 5074:
		case 7413:
		case 11966:
			// stealing creation capes
		case 14387:
		case 14389:
		case 20072: // defenders
		case 8844:
		case 8845:
		case 8846:
		case 8847:
		case 8848:
		case 8849:
		case 8850:
			return false;
		default:
			return true;
		}
	}

	private static final int ITEMS_RANGE = 500;

	public static int[] CHARM_IDS = { 12158, 12159, 12160, 12163 };

	private static double price = 0;
	
	public static boolean repairItem(Player player, int slot) {
		Item item = player.getInventory().getItem(slot);
		String itemName = item.getName().toLowerCase().replace(" (broken)", "")
				.replace(" (damaged)", "").replace(" 0", "").replace(" 25", "")
				.replace(" 50", "").replace(" 75", "").replace(" 100", "");
		if (!isRepairable(item.getName()))
			return false;
		for (int itemIndex = item.getId() - ITEMS_RANGE; itemIndex < item
				.getId() + ITEMS_RANGE; itemIndex++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(itemIndex);
			if (def == null || !def.getName().toLowerCase().contains(itemName))
				continue;
			String indexName = item.getName().toLowerCase()
					.replace(itemName, "").replace(" (", "").replace(")", "")
					.replace(" ", "");
			if (indexName.equals("broken") || itemName.equals("damaged"))
				price = (int) (def.getValue() * .25);
			else
				price = (int) (def.getValue() * (1 - (Integer
						.parseInt(indexName) * .01)));
			if (player.getInventory().getCoinsAmount() >= price)
				player.getInventory().deleteItem(
						new Item(995, (int) price));
			else {
				player.dialog(new DialogueEventListener(player) {
					
					@Override
					public void start() {
						mes("You don't have enough coins, you need " + (int) price
								+ " coins to repair this item.");
					}
				});
				return true;
			}
			player.getInventory().getItem(slot).setId(itemIndex);
			player.getInventory().refresh(slot);
			return true;
		}
		return false;
	}

	private static boolean isRepairable(String itemName) {
		return itemName.endsWith(" (broken)")
				|| itemName.endsWith(" (damaged)") || itemName.endsWith(" 0")
				|| itemName.endsWith(" 25") || itemName.endsWith(" 50")
				|| itemName.endsWith(" 75") || itemName.endsWith(" 100");
	}
}
