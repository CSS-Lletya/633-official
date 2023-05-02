package com.rs.game.player.content;

import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.GeneralRequirementMap;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;

/**
 * TODO: Fix dialogue support for npc 
 * @author Dennis
 *
 */
public final class PlayerLook {

	public static void openCharacterCustomizing(Player player) {
		player.getPackets().sendRootInterface(1028, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(1028, 50, 0, 11, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(1028, 107, 0, 50, 0);

		player.getPackets().sendUnlockIComponentOptionSlots(1028, 111, 0, 250, 0); // unlock more colors

		player.getVarsManager().sendVarBit(8093, player.getAppearance().isMale() ? 0 : 1);
	}

	public static void handleCharacterCustomizingButtons(Player player, int buttonId, int slotId) {
		if (buttonId == 117) { // confirm
			player.getPackets().sendRootInterface(player.getInterfaceManager().isResizableScreen() ? 746 : 548, 0);
			// no need to remove since the interface is still saved!
			// player.getTemporaryAttributtes().remove("SelectWearDesignD");
			// player.getTemporaryAttributtes().remove("ViewWearDesign");
			// player.getTemporaryAttributtes().remove("ViewWearDesignD");
			player.getAppearance().generateAppearenceData();
		} else if (buttonId >= 48 && buttonId <= 67) {
			player.getAttributes().getAttributes().put("ViewWearDesign", (buttonId - 48));
			player.getAttributes().getAttributes().put("ViewWearDesignD", 0);
			setDesign(player, buttonId - 48, 0);
		} else if (buttonId >= 83 && buttonId <= 88) {
			Integer index = (Integer) player.getAttributes().getAttributes().get("ViewWearDesign");
			if (index == null)
				return;
			player.getAttributes().getAttributes().put("ViewWearDesignD", (buttonId - 83));
			setDesign(player, index, buttonId - 83);
		} else if (buttonId == 38 || buttonId == 39) {
			setGender(player, buttonId == 38);
		} else if (buttonId == 45) {
			setSkin(player, slotId);

		} else if (buttonId >= 95 && buttonId <= 100) {// select the index
			player.getAttributes().getAttributes().put("SelectWearDesignD", (buttonId - 95));
		} else if (buttonId == 107) {
			Integer index = (Integer) player.getAttributes().getAttributes().get("SelectWearDesignD");
			if (index == null || index == 1) {
				boolean male = player.getAppearance().isMale();
				int map1 = ClientScriptMap.getMap(male ? 3304 : 3302).getIntValue(slotId);
				if (map1 == 0)
					return;
				GeneralRequirementMap map = GeneralRequirementMap.getMap(map1);
				player.getAppearance().setHairStyle((short) map.getIntValue(788));
				if (!male)
					player.getAppearance().setBeardStyle((short) player.getAppearance().getHairStyle());
			} else if (index == 2) {
				player.getAppearance().setTopStyle((short) ClientScriptMap
						.getMap(player.getAppearance().isMale() ? 3287 : 1591).getIntValue(slotId));
				player.getAppearance().setArmsStyle((short) (player.getAppearance().isMale() ? 26 : 65)); // default
				player.getAppearance().setWristsStyle((short) (player.getAppearance().isMale() ? 34 : 68)); // default
				player.getAppearance().generateAppearenceData();
			} else if (index == 3)
				player.getAppearance().setLegsStyle((short) ClientScriptMap
						.getMap(player.getAppearance().isMale() ? 3289 : 1607).getIntValue(slotId));
			else if (index == 4)
				player.getAppearance().setBootsStyle((short) ClientScriptMap
						.getMap(player.getAppearance().isMale() ? 1136 : 1137).getIntValue(slotId));
			else if (player.getAppearance().isMale())
				player.getAppearance().setBeardStyle((short) ClientScriptMap.getMap(3307).getIntValue(slotId));
		} else if (buttonId == 111) {
			Integer index = (Integer) player.getAttributes().getAttributes().get("SelectWearDesignD");
			if (index == null || index == 0)
				setSkin(player, slotId);
			else {
				if (index == 1 || index == 5)
					player.getAppearance().setHairColor(ClientScriptMap.getMap(2345).getIntValue(slotId));
				else if (index == 2)
					player.getAppearance().setTopColor(ClientScriptMap.getMap(3283).getIntValue(slotId));
				else if (index == 3)
					player.getAppearance().setLegsColor(ClientScriptMap.getMap(3283).getIntValue(slotId));
				else
					player.getAppearance().setBootsColor(ClientScriptMap.getMap(3297).getIntValue(slotId));
			}
		}
	}

	public static void setGender(Player player, boolean male) {
		if (male == player.getAppearance().isMale())
			return;
		if (!male)
			player.getAppearance().female();
		else
			player.getAppearance().male();
		Integer index1 = (Integer) player.getAttributes().getAttributes().get("ViewWearDesign");
		Integer index2 = (Integer) player.getAttributes().getAttributes().get("ViewWearDesignD");
		setDesign(player, index1 != null ? index1 : 0, index2 != null ? index2 : 0);
		player.getAppearance().generateAppearenceData();
		player.getVarsManager().sendVarBit(8093, male ? 0 : 1);
	}

	public static void setSkin(Player player, int index) {
		player.getAppearance().setSkinColor(ClientScriptMap.getMap(748).getIntValue(index));
	}

	public static void setDesign(Player player, int index1, int index2) {
		int map1 = ClientScriptMap.getMap(3278).getIntValue(index1);
		if (map1 == 0)
			return;
		boolean male = player.getAppearance().isMale();
		int map2Id = GeneralRequirementMap.getMap(map1).getIntValue((male ? 1169 : 1175) + index2);
		if (map2Id == 0)
			return;
		GeneralRequirementMap map = GeneralRequirementMap.getMap(map2Id);
		for (int i = 1182; i <= 1186; i++) {
			int value = map.getIntValue(i);
			if (value == -1)
				continue;
			player.getAppearance().setLook(i - 1180, (short) value);
		}
		for (int i = 1187; i <= 1190; i++) {
			int value = (short) map.getIntValue(i);
			if (value == -1 || (i - 1186) == 4) // skip skin color
				continue;
			player.getAppearance().setColor(i - 1186, (short) value);
		}
		if (!player.getAppearance().isMale())
			player.getAppearance().setBeardStyle((short) player.getAppearance().getHairStyle());

	}

	public static void handleMageMakeOverButtons(Player player, int buttonId) {
		if (buttonId == 14 || buttonId == 16 || buttonId == 15 || buttonId == 17)
			player.getAttributes().getAttributes().put("MageMakeOverGender", buttonId == 14 || buttonId == 16);
		else if (buttonId >= 20 && buttonId <= 31) {

			int skin;
			if (buttonId == 31)
				skin = 11;
			else if (buttonId == 30)
				skin = 10;
			else if (buttonId == 20)
				skin = 9;
			else if (buttonId == 21)
				skin = 8;
			else if (buttonId == 22)
				skin = 7;
			else if (buttonId == 29)
				skin = 6;
			else if (buttonId == 28)
				skin = 5;
			else if (buttonId == 27)
				skin = 4;
			else if (buttonId == 26)
				skin = 3;
			else if (buttonId == 25)
				skin = 2;
			else if (buttonId == 24)
				skin = 1;
			else
				skin = 0;
			player.getAttributes().getAttributes().put("MageMakeOverSkin", skin);
		} 
		else if (buttonId == 33) {
			Boolean male = (Boolean) player.getAttributes().getAttributes().remove("MageMakeOverGender");
			Integer skin = (Integer) player.getAttributes().getAttributes().remove("MageMakeOverSkin");
			player.getInterfaceManager().closeInterfaces();
			if (male == null || skin == null)
				return;
//			if (male == player.getAppearance().isMale() && skin == player.getAppearance().getSkinColor())
//				player.getDialogueManager().startDialogue("MakeOverMage", 2676, 1);
			else {
//				player.getDialogueManager().startDialogue("MakeOverMage", 2676, 2);
				if (player.getAppearance().isMale() != male) {
					if (player.getEquipment().wearingArmour()) {
						player.dialog(new DialogueEventListener(player) {
							@Override
							public void start() {
								player(sad, "You cannot have armor on while changing your gender.");
							}
						});
						return;
					}
					if (male)
						player.getAppearance().resetAppearence();
					else
						player.getAppearance().female();
				}
				player.getAppearance().setSkinColor(skin);
				player.getAppearance().generateAppearenceData();
			}
		}
	}

	public static void handleHairdresserSalonButtons(Player player, int buttonId, int slotId) {// Hair
		if (buttonId == 6)
			player.getAttributes().getAttributes().put("hairSaloon", true);
		else if (buttonId == 7)
			player.getAttributes().getAttributes().put("hairSaloon", false);
		else if (buttonId == 18) {
			player.getInterfaceManager().closeInterfaces();
		} else if (buttonId == 10) {
			Boolean hairSalon = (Boolean) player.getAttributes().getAttributes().get("hairSaloon");
			if (hairSalon != null && hairSalon) {
				int value = (int) ClientScriptMap.getMap(player.getAppearance().isMale() ? 2339 : 2342)
						.getKeyForValue(slotId / 2);
				if (value == -1)
					return;
				player.getAppearance().setHairStyle((short) value);
			} else if (player.getAppearance().isMale()) {
				int value = ClientScriptMap.getMap(703).getIntValue(slotId / 2);
				if (value == -1)
					return;
				player.getAppearance().setBeardStyle((short) value);
			}
		} else if (buttonId == 16) {
			int value = ClientScriptMap.getMap(2345).getIntValue(slotId / 2);
			if (value == -1)
				return;
			player.getAppearance().setHairColor(value);
		}
	}

	public static void openMageMakeOver(Player player) {
		player.getInterfaceManager().sendInterface(900);
		player.getPackets().sendIComponentText(900, 33, "Confirm");
		player.getVarsManager().sendVarBit(6098, player.getAppearance().isMale() ? 0 : 1);
		player.getVarsManager().sendVarBit(6099, player.getAppearance().getSkinColor());
		player.getAttributes().getAttributes().put("MageMakeOverGender", player.getAppearance().isMale());
		player.getAttributes().getAttributes().put("MageMakeOverSkin", player.getAppearance().getSkinColor());
	}

	public static void handleThessaliasMakeOverButtons(Player player, int buttonId, int slotId) {
		if (buttonId == 6)
			player.getAttributes().getAttributes().put("ThessaliasMakeOver", 0);
		else if (buttonId == 7) {
			if (ClientScriptMap.getMap(player.getAppearance().isMale() ? 690 : 1591)
					.getKeyForValue(player.getAppearance().getTopStyle()) >= 32) {
				player.getAttributes().getAttributes().put("ThessaliasMakeOver", 1);
			} else
				player.getPackets().sendGameMessage("You can't select different arms to go with that top.");
		} else if (buttonId == 8) {
			if (ClientScriptMap.getMap(player.getAppearance().isMale() ? 690 : 1591)
					.getKeyForValue(player.getAppearance().getTopStyle()) >= 32) {
				player.getAttributes().getAttributes().put("ThessaliasMakeOver", 2);
			} else
				player.getPackets().sendGameMessage("You can't select different wrists to go with that top.");
		} else if (buttonId == 9)
			player.getAttributes().getAttributes().put("ThessaliasMakeOver", 3);
		else if (buttonId == 19) { // confirm
			player.getInterfaceManager().closeInterfaces();
		} else if (buttonId == 12) { // set part
			Integer stage = (Integer) player.getAttributes().getAttributes().get("ThessaliasMakeOver");
			if (stage == null || stage == 0) {
				player.getAppearance().setTopStyle((short) ClientScriptMap
						.getMap(player.getAppearance().isMale() ? 690 : 1591).getIntValue(slotId / 2));
				player.getAppearance().setArmsStyle((short) (player.getAppearance().isMale() ? 26 : 65)); // default
				player.getAppearance().setWristsStyle((short) (player.getAppearance().isMale() ? 34 : 68)); // default
			} else if (stage == 1) // arms
				player.getAppearance().setArmsStyle((short) ClientScriptMap
						.getMap(player.getAppearance().isMale() ? 711 : 693).getIntValue(slotId / 2));
			else if (stage == 2) // wrists
				player.getAppearance().setWristsStyle((short) ClientScriptMap.getMap(751).getIntValue(slotId / 2));
			else
				player.getAppearance().setLegsStyle((short) ClientScriptMap
						.getMap(player.getAppearance().isMale() ? 1586 : 1607).getIntValue(slotId / 2));

		} else if (buttonId == 17) {// color
			Integer stage = (Integer) player.getAttributes().getAttributes().get("ThessaliasMakeOver");
			if (stage == null || stage == 0 || stage == 1)
				player.getAppearance().setTopColor(ClientScriptMap.getMap(3282).getIntValue(slotId / 2));
			else if (stage == 3)
				player.getAppearance().setLegsColor(ClientScriptMap.getMap(3284).getIntValue(slotId / 2));
		}
	}

	public static void openThessaliasMakeOver(final Player player) {
		if (player.getEquipment().wearingArmour()) {
			player.getPackets().sendGameMessage("You're not able to try on my clothes with all that armour. Take it off and then speak to me again.");
			return;
		}
		player.setNextAnimation(new Animation(11623));
		player.getInterfaceManager().sendInterface(729);
		player.getPackets().sendIComponentText(729, 21, "Free!");
		player.getAttributes().getAttributes().put("ThessaliasMakeOver", 0);
		player.getPackets().sendUnlockIComponentOptionSlots(729, 12, 0, 100, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(729, 17, 0, ClientScriptMap.getMap(3282).getSize() * 2, 0);
		player.setCloseInterfacesEvent(() -> {
			player.setNextAnimation(new Animation(-1));
			player.getAppearance().getAppeareanceData();
			player.getAttributes().getAttributes().remove("ThessaliasMakeOver");

		});
	}

	public static void openHairdresserSalon(final Player player) {
		if (player.getEquipment().getHatId() != -1) {
			player.getPackets().sendGameMessage("I'm afraid I can't see your head at the moment. Please remove your headgear first.");
			return;
		}
		if (player.getEquipment().getWeaponId() != -1 || player.getEquipment().getShieldId() != -1) {
			player.getPackets().sendGameMessage("I don't feel comfortable cutting hair when you are wielding something. Please remove what you are holding first.");
			return;
		}
		player.setNextAnimation(new Animation(11623));
		player.getInterfaceManager().sendInterface(309);
		player.getPackets().sendUnlockIComponentOptionSlots(309, 10, 0,
				ClientScriptMap.getMap(player.getAppearance().isMale() ? 2339 : 2342).getSize() * 2, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(309, 16, 0, ClientScriptMap.getMap(2345).getSize() * 2, 0);
		player.getPackets().sendIComponentText(309, 20, "Free!");
		player.getAttributes().getAttributes().put("hairSaloon", true);
		player.setCloseInterfacesEvent(() -> {
			player.getPackets().sendGameMessage("An excellent choise, " + (player.getAppearance().isMale() ? "sir" : "lady") + ".");
			player.setNextAnimation(new Animation(-1));
			player.getAppearance().getAppeareanceData();
			player.getAttributes().getAttributes().remove("hairSaloon");
		});
	}

	public static void openYrsaShop(final Player player) {
		if (player.getEquipment().getBootsId() != -1) {
			player.getPackets().sendGameMessage("I don't feel comfortable helping you try on new boots when you are wearing some already Please remove your boots first.");
			return;
		}
		player.setNextAnimation(new Animation(11623));
		player.getInterfaceManager().sendInterface(728);
		player.getPackets().sendIComponentText(728, 16, "Free");
		player.getAttributes().getAttributes().put("YrsaBoot", 0);
		player.getPackets().sendUnlockIComponentOptionSlots(728, 12, 0, 500, 0);
		player.getPackets().sendUnlockIComponentOptionSlots(728, 7, 0, ClientScriptMap.getMap(3297).getSize() * 2, 0);
		player.setCloseInterfacesEvent(() -> {
			player.getPackets().sendGameMessage("Hey, They look great!");
			player.setNextAnimation(new Animation(-1));
			player.getAppearance().getAppeareanceData();
			player.getAttributes().getAttributes().remove("YrsaBoot");
		});
	}

	public static void handleYrsaShoes(Player player, int componentId, int slotId) {
		if (componentId == 14)
			player.getInterfaceManager().closeInterfaces();
		else if (componentId == 12) {// setting the colors.
			player.getAppearance().setBootsColor(ClientScriptMap.getMap(3297).getIntValue(slotId / 2));
			player.getAppearance().generateAppearenceData();
		} else if (componentId == 7) {// /boot style
			player.getAppearance().setBootsStyle((short) ClientScriptMap
					.getMap(player.getAppearance().isMale() ? 3290 : 3293).getIntValue(slotId / 2));
			player.getAppearance().generateAppearenceData();
		}
	}

	private PlayerLook() {

	}

}
