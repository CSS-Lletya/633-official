package com.rs.plugin;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.GameConstants;
import com.rs.constants.InterfaceVars;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.map.World;
import com.rs.game.player.CombatDefinitions;
import com.rs.game.player.Equipment;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.io.InputStream;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import skills.Skills;
import skills.summoning.EnchantedHeadwear;

/**
 * @author Dennis
 */
public final class RSInterfacePluginDispatcher {

	/**
	 * The object map which contains all the interface on the world.
	 */
	private static final Object2ObjectArrayMap<RSInterfaceSignature, RSInterfaceListener> INTERFACES = new Object2ObjectArrayMap<>();

	/**
	 * Executes the specified interface if it's registered.
	 * 
	 * @param player the player executing the interface.
	 * @param parts  the string which represents a interface.
	 */
	public static void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		getRSInterface(interfaceId).ifPresent(inter -> inter.execute(player, interfaceId, componentId, packetId, slotId, slotId2));
	}
	
	/**
	 * Executes the specified interface if it's registered.
	 * 
	 * @param player the player executing the interface.
	 * @param parts  the string which represents a interface.
	 */
	public static void executeEquipment(Player player, int interfaceId, int componentId, int packetId, int slotId2) {
		getRSInterface(interfaceId).ifPresent(inter -> inter.executeEquipment(player, new Item(slotId2), componentId, packetId));
	}

	/**
	 * Gets a interface which matches the {@code identifier}.
	 * 
	 * @param identifier the identifier to check for matches.
	 * @return an Optional with the found value, {@link Optional#empty} otherwise.
	 */
	private static Optional<RSInterfaceListener> getRSInterface(int interfaceId) {
	    return INTERFACES.values()
	            .stream()
	            .filter(rsInterface -> isInterface(rsInterface, interfaceId))
	            .findFirst();
	}

	private static boolean isInterface(RSInterfaceListener rsInterface, int interfaceId) {
		Annotation annotation = rsInterface.getClass().getAnnotation(RSInterfaceSignature.class);
		RSInterfaceSignature signature = (RSInterfaceSignature) annotation;
		return Arrays.stream(signature.interfaceId()).anyMatch(right -> interfaceId == right);
	}

	/**
	 * Loads all the interface into the {@link #INTERFACES} list.
	 * <p>
	 * </p>
	 * <b>Method should only be called once on start-up.</b>
	 */
	public static void load() {
		List<RSInterfaceListener> interfaces = Utility.getClassesInDirectory("com.rs.plugin.impl.interfaces").stream()
				.map(clazz -> (RSInterfaceListener) clazz).collect(Collectors.toList());
		interfaces.forEach(inter -> INTERFACES.put(inter.getClass().getAnnotation(RSInterfaceSignature.class), inter));
	}

	/**
	 * Reloads all the interface into the {@link #INTERFACES} list.
	 * <p>
	 * </p>
	 * <b>This method can be invoked on run-time to clear all the commands in the
	 * list and add them back in a dynamic fashion.</b>
	 */
	public static void reload() {
		INTERFACES.clear();
		load();
		LogUtility.log(LogType.INFO, "Reloaded RSInterface Plugins");
	}

	public static void handleButtons(final Player player, InputStream stream, int packetId) {
		int interfaceHash = stream.readInt();
		int interfaceId = interfaceHash >> 16;
		if (Utility.getInterfaceDefinitionsSize() <= interfaceId) {
			return;
		}
		if (!World.containsLobbyPlayer(player.getUsername())) {
			if (player.isDead() || !player.getInterfaceManager().containsInterface(interfaceId)) {
				return;
			}
		}
		final int componentId = interfaceHash - (interfaceId << 16);
		if (componentId != 65535 && Utility.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
			return;
		}
		final int slotId2 = stream.readUnsignedShortLE128();// item slot?
		final int slotId = stream.readUnsignedShortLE128();
		RSInterfacePluginDispatcher.execute(player, interfaceId, componentId, packetId, (byte) slotId, slotId2);

		if (GameConstants.DEBUG)
			LogUtility.log(LogType.INFO,
					"Interface ID: " + interfaceId + " - Component: " + componentId + " - PacketId: " + packetId);
	}

	/**
	 * The external methods used are stored here. For sake of ease of access.
	 */
	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15)
			return;
		player.getMovement().stopAll(false, false);
		Item item = player.getEquipment().getItem(slotId);
		if (item == null || !player.getInventory().addItem(item.getId(), item.getAmount()))
			return;
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAudioManager().sendSound(Sounds.REMOVE_WORN_ITEM);
		player.getAppearance().generateAppearenceData();
		player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
		isWearingTiara(player, () -> player.getVarsManager().sendVar(InterfaceVars.RUNECRAFTING_ALTARS_OPTIONS, 0).submitVarToMap(InterfaceVars.RUNECRAFTING_ALTARS_OPTIONS, 0));
		if (slotId == 3)
			player.getCombatDefinitions().decreaseSpecialAttack(0);
		RSInterfacePluginDispatcher.refreshEquipBonuses(player);
	}

	public static boolean sendWear(Player player, int slotId, int itemId) {
		player.getMovement().stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId)
			return false;
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getAppearance().isMale())) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage("Not enough free space in your inventory.");
			return true;
		}
		Object2ObjectOpenHashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
        boolean hasRequiriments = true;
        if (requiriments != null) {
            for (int skillId : requiriments.keySet()) {
                if (skillId > 24 || skillId < 0)
                    continue;
                int level = requiriments.get(skillId);
                if (level < 0 || level > 120)
                    continue;
                if (player.getSkills().getTrueLevel(skillId) < level) {
                    if (hasRequiriments) {
                        player.getPackets().sendGameMessage("You are not high enough level to use this item.");
                    }
                    hasRequiriments = false;
                    String name = Skills.SKILL_NAME[skillId].toLowerCase();
                    player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " "
                            + name + " level of " + level + ".");
                }

            }
        }
        if (!hasRequiriments)
            return true;
        if (!EnchantedHeadwear.canEquip(itemId, player))
        	return false;
		if (player.getMapZoneManager().execute(controller -> !controller.canEquip(player, targetSlot, itemId))) {
			return false;
		}
		player.getMovement().stopAll(false, false);
		player.getInventory().deleteItem(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(5).getId(),
						player.getEquipment().getItem(5).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(3).getId(),
						player.getEquipment().getItem(3).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId()
						|| !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
				player.getInventory().refresh(slotId);
			} else
				player.getInventory().addItem(new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		player.getAppearance().generateAppearenceData();
		if (targetSlot == 3)
			player.getCombatDefinitions().decreaseSpecialAttack(0);
		EquipSounds.executeSound(player, item);
		player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
		return true;
	}

	static int finalSlot;
	
	public static boolean sendWear2(Player player, int slotId, int itemId) {
		if (player.isFinished() || player.isDead())
			return false;
		player.getMovement().stopAll(false, false);
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId)
			return false;
		if (item.getDefinitions().isNoted()
				|| !item.getDefinitions().isWearItem(player.getAppearance().isMale()) && itemId != 4084) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		finalSlot = targetSlot;
		if (itemId == 4084)
			targetSlot = 3;
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		if (!ItemConstants.canWear(item, player))
			return false;
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage("Not enough free space in your inventory.");
			return false;
		}
		Object2ObjectOpenHashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0)
					continue;
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120)
					continue;
				if (player.getSkills().getTrueLevel(skillId) < level) {
					if (hasRequiriments)
						player.getPackets().sendGameMessage("You are not high enough level to use this item.");
					hasRequiriments = false;
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " "
							+ name + " level of " + level + ".");
				}

			}
		}
		if (!hasRequiriments)
			return false;
		if (!EnchantedHeadwear.canEquip(itemId, player))
        	return false;
		if (player.getMapZoneManager().execute(controller -> !controller.canEquip(player, finalSlot, itemId))) {
			return false;
		}
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null
					&& Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(3, null);
			}

		}
		if (player.getEquipment().getItem(targetSlot) != null
				&& (itemId != player.getEquipment().getItem(targetSlot).getId()
						|| !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
			} else
				player.getInventory().getItems().add(new Item(player.getEquipment().getItem(targetSlot).getId(),
						player.getEquipment().getItem(targetSlot).getAmount()));
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		if (targetSlot == 3)
			player.getCombatDefinitions().decreaseSpecialAttack(0);
		player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
		return true;
	}

	public static void sendWear(Player player, int[] slotIds) {
		if (player.isFinished() || player.isDead())
			return;
		boolean worn = false;
		Item[] copy = player.getInventory().getItems().getItemsCopy();
		for (int slotId : slotIds) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null)
				continue;
			if (sendWear2(player, slotId, item.getId()))
				worn = true;
			isWearingTiara(player, () -> player.getVarsManager().sendVar(InterfaceVars.RUNECRAFTING_ALTARS_OPTIONS, 1).submitVarToMap(InterfaceVars.RUNECRAFTING_ALTARS_OPTIONS, 1));
		}
		player.getInventory().refreshItems(copy);
		if (worn) {
			player.getAppearance().generateAppearenceData();
		}
		
	}

	public static void openItemsKeptOnDeath(Player player) {
		player.getInterfaceManager().sendInterface(17);
		sendItemsKeptOnDeath(player, false);
	}

	public static void sendItemsKeptOnDeath(Player player, boolean wilderness) {
//		boolean skulled = player.hasSkull();
//		Integer[][] slots = GraveStone.getItemSlotsKeptOnDeath(player,
//				wilderness, skulled, player.getPrayer().isProtectingItem());
//		Item[][] items = GraveStone.getItemsKeptOnDeath(player, slots);
//		long riskedWealth = 0;
//		long carriedWealth = 0;
////		for (Item item : items[1])
////			carriedWealth = riskedWealth += GrandExchange
////					.getPrice(item.getId()) * item.getAmount();
////		for (Item item : items[0])
////			carriedWealth += GrandExchange.getPrice(item.getId())
////					* item.getAmount();
//		if (slots[0].length > 0) {
//			for (int i = 0; i < slots[0].length; i++)
//				player.getVarsManager().sendVarBit(9222 + i, slots[0][i]);
//			player.getVarsManager().sendVarBit(9227, slots[0].length);
//		} else {
//			player.getVarsManager().sendVarBit(9222, -1);
//			player.getVarsManager().sendVarBit(9227, 1);
//		}
//		player.getVarsManager().sendVarBit(9226, wilderness ? 1 : 0);
//		player.getVarsManager().sendVarBit(9229, skulled ? 1 : 0);
//		StringBuffer text = new StringBuffer();
//		text.append("The number of items kept on").append("<br>")
//				.append("death is normally 3.").append("<br>").append("<br>")
//				.append("<br>");
//		if (wilderness) {
//			text.append("Your gravestone will not").append("<br>")
//					.append("appear.");
//		} else {
//			int time = GraveStone.getMaximumTicks(player.getGraveStone());
//			int seconds = (int) (time * 0.6);
//			int minutes = seconds / 60;
//			seconds -= minutes * 60;
//
//			text.append("Gravestone:")
//					.append("<br>")
//					.append(ClientScriptMap.getMap(1099).getStringValue(
//							player.getGraveStone()))
//					.append("<br>")
//					.append("<br>")
//					.append("Initial duration:")
//					.append("<br>")
//					.append(minutes + ":" + (seconds < 10 ? "0" : "") + seconds)
//					.append("<br>");
//		}
//		text.append("<br>")
//				.append("<br>")
//				.append("Carried wealth:")
//				.append("<br>")
//				.append(carriedWealth > Integer.MAX_VALUE ? "Too high!" : Utils
//						.getFormattedNumber((int) carriedWealth))
//				.append("<br>")
//				.append("<br>")
//				.append("Risked wealth:")
//				.append("<br>")
//				.append(riskedWealth > Integer.MAX_VALUE ? "Too high!" : Utils
//						.getFormattedNumber((int) riskedWealth)).append("<br>")
//				.append("<br>");
//		if (wilderness) {
//			text.append("Your hub will be set to:").append("<br>")
//					.append("Edgeville.");
//		} else {
//			text.append("Current hub: "
//					+ ClientScriptMap.getMap(3792).getStringValue(
//							DeathEvent.getCurrentHub(player)));
//		}
//		player.getPackets().sendGlobalString(352, text.toString());
	}

	public static void openEquipmentBonuses(final Player player, boolean banking) {
		player.getMovement().stopAll();
		player.getInterfaceManager().sendInventoryInterface(670);
		player.getInterfaceManager().sendInterface(667);
		player.getPackets().sendRunScript(787, 1);
		player.getVarsManager().sendVarBit(8348, banking ? 0 : 1);
		player.getVarsManager().sendVarBit(4894, banking ? 1 : 0);
		player.getPackets().sendItems(93, player.getInventory().getItems());
		player.getPackets().sendInterSetItemsOptionsScript(670, 0, 93, 4, 7, "Equip", "Stats", "Examine");
		player.getPackets().sendUnlockIComponentOptionSlots(670, 0, 0, 27, 0, 1, 2);
		player.getPackets().sendIComponentSettings(667, 7, 0, 14, 1538);
		player.getPackets().sendGlobalConfig(779, player.getEquipment().getWeaponRenderEmote());
		refreshEquipBonuses(player);
		if (banking) {
			player.setCloseInterfacesEvent(() -> {
				player.getAttributes().get(Attribute.IS_BANKING).set(false);
				player.getVarsManager().sendVarBit(4894, 0);
			});
		}
	}

	public static void refreshEquipBonuses(Player player) {
		final int interfaceId = 667;
		for (Object[] element : info) {
			int bonus = player.getCombatDefinitions().getBonuses()[(int) element[1]];
			String sign = bonus > 0 ? "+" : "";
			player.getPackets().sendIComponentText(interfaceId, (int) element[0] - 1, element[2] + ": " + sign + bonus);
		}
	}

	private static final Object[][] info = new Object[][] { { 31, 0, "Stab" }, { 32, 1, "Slash" }, { 33, 2, "Crush" },
			{ 34, 3, "Magic" }, { 35, 4, "Range" }, { 36, 5, "Stab" }, { 37, 6, "Slash" }, { 38, 7, "Crush" },
			{ 39, 8, "Magic" }, { 40, 9, "Range" }, { 41, 10, "Summoning" },
			{ 42, CombatDefinitions.ABSORB_MELEE, "Absorb Melee" },
			{ 43, CombatDefinitions.ABSORB_MAGIC, "Absorb Magic" },
			{ 44, CombatDefinitions.ABSORB_RANGE, "Absorb Range" }, { 45, 14, "Strength" }, { 46, 15, "Ranged Str" },
			{ 47, 16, "Prayer" }, { 48, 17, "Magic Damage" } };
	
	//Note: Not sure how come the rest of altars aren't being unlocked, air unlocks for sure!
	public static boolean isWearingTiara(Player player, Runnable run) {
		if (IntStream.of(1438, 1448, 1444, 1440, 1442, 1446, 1454, 1452, 1462, 1458, 1456, 1450)
				.anyMatch(id -> player.getEquipment().containsOneItem(id)))
			run.run();
		return IntStream.of(1438, 1448, 1444, 1440, 1442, 1446, 1454, 1452, 1462, 1458, 1456, 1450)
				.anyMatch(id -> player.getEquipment().containsOneItem(id));
	}
	
	@AllArgsConstructor
	public enum EquipSounds {
	    AXE(2229, new String[]{"hatchet"}),
	    STAFF(2247, new String[] {"staff"}),//hmm - 2230
	    BANNER(2231, new String[] {"banner"}),
	    BATTLEAXE(2232, new String[] {"battleaxe"}),
	    BLUNT(2233, new String[] {"mace", "hammer", "halberd", "maul", "balmung", "ket-om", "club", "adze", "anchor", "-ak", "-ek", "-tal", "greataxe"}),
	    BODY(2234, new String[]{"body", "bodies"}),
	    BOLT(2235, new String[]{"bolts"}),
	    HANDS(2236, new String[]{"glove", "gauntlets", "vamb"}),
	    FEET(2237, new String[]{"boot"}),
	    FUN(2238, new String[]{"chicken", "yo-yo", "etc stuff"}),
	    METAL_BODY(2239, new String[]{"platebody", "chain", "hauberk", "decorative", "chestplate"}),
	    HELMET(2240, new String[]{"helm", "full", "mask", "sallet", "hat", "helm"}),
	    LEATHER(2241, new String[]{"d'hide", "leather", "coif", "hardleather", "studded", "dragonhide", "cowl", "chaps", "legs", "top", "mitre", "torso", "ranger", "robin"}),
	    LEGS(2242, new String[]{"chaps", "shorts", "skirt", "prob more.."}),
	    METAL_LEGS(2243, new String[]{"plateskirt", "platelegs", "cuisse", "tassets"}),
	    RANGED_WEAPON(2244, new String[]{"bow", "crossbow", "knife", "javelin", "dart", "seercull"}),
	    SHIELD(2245, new String[]{"shield", "sq", "defender", "deflector", "xil", "kiteshield", "throwing", "chinchompa", "-ul"}),
	    CLAWS(1003, new String[]{"claw"}),
	    SPIKED(2246, new String[]{"spike", "climbing"}),
	    SWORD(2247, new String[]{"sword", "scimitar", "dagger", "longsword", "spear", "mjolnir", "excalibur", "blade", "sabre", "rapier", "keris", "godsword"}),
	    WHIP(2248, new String[]{"whip"}),
	    WOODEN(2249, new String[]{"wooden"}),
	    DARK_BOW(3738, new String[]{"darkbow"}),
	    WAND(3738, new String[]{"wand"}),
	    HWEEN(3227, new String[]{"h'ween mask"}),
	    SILVERLIGHT(2990, new String[]{"silverlight"}),
	    BACKPACK(3284, new String[]{"ava's"}),
	    JESTER(3285, new String[] {"jester"}),
	    ;
	    private final int sound;
	    private final String[] itemNames;

	    public static final ImmutableSet<EquipSounds> VALUES = Sets.immutableEnumSet(EnumSet.allOf(EquipSounds.class));

	    public static void executeSound(Player player, Item item) {
	        String itemName = item.getDefinitions().getName().toLowerCase();
	        boolean foundMatch = VALUES.stream()
	                .filter(sound -> Arrays.stream(sound.itemNames).anyMatch(itemName::contains))
	                .peek(sound -> player.getAudioManager().sendSound(sound.sound))
	                .findAny()
	                .isPresent();
	        if (!foundMatch) {
	            player.getAudioManager().sendSound(Sounds.WEARING_ITEM_2);
	        }
	    }
	}
}