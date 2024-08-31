package com.rs.plugin.impl.objects.region;

import com.rs.GameConstants;
import com.rs.constants.Animations;
import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.dialogue.impl.StairsLaddersDialogue;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.loaders.ShopsHandler;

import experimental.Demo;
import skills.magic.TeleportType;

@ObjectSignature(objectId = { 36974, 29355, 12309, 36773, 36774, 36775, 36776, 36777, 36778, 12348, 36984, 36985,
		36986, 36987, 36988, 36989, 36990, 36991, 36976, 36978, 45482, 45483, 45484, 37683, 45481, 36768, 36769,
		36770, 36771, 36772, 37335, 48797, 2145, 52309,52308, 36881, 36880, 36795,36796,36797, 12308}, name = {})
public class LumbridgeRegionObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		if (object.getId() == 36774 || object.getId() == 36777 && optionId == 5) {
			player.getMovement().move(true,
					new WorldTile(player.getX(), player.getY(), player.getPlane() - 1), TeleportType.BLANK);
		}
		//chicken area (hatchet specifically)
		object.doAction(optionId, 36974, "take-hatchet", () -> {
			if (player.getInventory().addItem(new Item(ItemNames.BRONZE_HATCHET_1351))) {
				player.getAudioManager().sendSound(Sounds.REMOVE_AXE);
				GameObject.spawnTempGroundObject(new GameObject(36975, 10, 0, object), 30);
				Demo.demo.demo(player, object);
			}
		});/*.doAction(option, objectId, searchedOption, action);*/
		//basement
		object.doAction(optionId, 12309, "bank", () -> player.getBank().openBank());
		object.doAction(optionId, 12309, "buy-food", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		object.doAction(optionId, 12309, "buy-items", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		
		//castle stairs
		object.doAction(optionId, 36773, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36776, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36774, "climb", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36774, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36775, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36777, "climb", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36777, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36777, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36778, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 12348, "open", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		object.doAction(optionId, 36771, "climb-up", () -> player.getMovement().move(true, new WorldTile(3207, 3222, 3), TeleportType.LADDER));
		object.doAction(optionId, 36772, "climb-down", () -> player.getMovement().move(true, new WorldTile(3207, 3224, 2), TeleportType.LADDER));
		object.doAction(optionId, 37335, "Raise", () -> {
			player.setNextAnimation(Animations.LUMBRIDGE_FLAG_POLE_RAISING);
			GameObject.sendObjectAnimation(object, Animations.LUMBRIDGE_FLAG_POLE);
		});
		//outside stairs
		object.doAction(optionId, 36768, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36769, "climb", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36769, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36769, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36770, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		
		//lumbridge church
		object.doAction(optionId, 36984, "climb-up", () -> player.getMovement().move(true, new WorldTile(3240, 3213, 1), TeleportType.LADDER));
		object.doAction(optionId, 36985, "climb-down", () -> player.getMovement().move(true, new WorldTile(3242, 3213, 0), TeleportType.LADDER));
		object.doAction(optionId, 36986, "climb-up", () -> player.getMovement().move(true, new WorldTile(3247, 3213, 1), TeleportType.LADDER));
		object.doAction(optionId, 36987, "climb-down", () -> player.getMovement().move(true, new WorldTile(3245, 3213, 0), TeleportType.LADDER));
		object.doAction(optionId, 36988, "climb-up", () -> player.getMovement().move(true, new WorldTile(3245, 3206, 2), TeleportType.LADDER));
		object.doAction(optionId, 36989, "climb-up", () -> player.getMovement().move(true, new WorldTile(3242, 3206, 2), TeleportType.LADDER));
		object.doAction(optionId, 36991, "climb-down", () -> player.getMovement().move(true, new WorldTile(3241, 3207, 1), TeleportType.LADDER));
		object.doAction(optionId, 36990, "climb-down", () -> player.getMovement().move(true, new WorldTile(3246, 3207, 1), TeleportType.LADDER));
		object.doAction(optionId, 36976, "ring", () -> player.getPackets().sendGameMessage("The towns people wouldn't appreciate you ringing their bell."));
		object.doAction(optionId, 36978, "play", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		
		//market area (guess that name will do lol)
		object.doAction(optionId, 45481, "climb-up", () -> {
			if (object.getX() == 3215) {
				player.getMovement().move(true, new WorldTile(3214, 3239, 1), TeleportType.BLANK);
			}
			if (object.getX() == 3200) {
				player.getMovement().move(true, new WorldTile(3200, 3242, 1), TeleportType.BLANK);
			}
			if (object.getX() == 3193) {
				player.getMovement().move(true, new WorldTile(3195, 3255, 1), TeleportType.BLANK);
			}
			if (object.getX() == 3212) {
				player.getMovement().move(true, new WorldTile(3214, 3256, 1), TeleportType.BLANK);
			}
			if (object.getX() == 3225) {
				player.getMovement().move(true, new WorldTile(3225, 3264, 1), TeleportType.BLANK);
			}
			if (object.getX() == 3237) {
				player.getMovement().move(true, new WorldTile(3237, 3195, 1), TeleportType.BLANK);
			}
		});
		object.doAction(optionId, 45482, "climb-down", () -> {
			if (object.getX() == 3215) {
				player.getMovement().move(true, new WorldTile(3217, 3239, 0), TeleportType.BLANK);
			}
			if (object.getX() == 3200) {
				player.getMovement().move(true, new WorldTile(3200, 3245, 0), TeleportType.BLANK);
			}
			if (object.getX() == 3193) {
				player.getMovement().move(true, new WorldTile(3192, 3255, 0), TeleportType.BLANK);
			}
			if (object.getX() == 3212) {
				player.getMovement().move(true, new WorldTile(3211, 3256, 0), TeleportType.BLANK);
			}
			if (object.getX() == 3225) {
				player.getMovement().move(true, new WorldTile(3225, 3267, 0), TeleportType.BLANK);
			}
			if (object.getX() == 3237) {
				player.getMovement().move(true, new WorldTile(3237, 3198, 0), TeleportType.BLANK);
			}
		});
		object.doAction(optionId, 45483, "climb-up", () -> {
			if (object.getX() == 3215) {
				player.getMovement().move(true, new WorldTile(3232, 3241, 1), TeleportType.BLANK);
			}
			if (object.getY() == 3205) {
				player.getMovement().move(true, new WorldTile(3229, 3205, 1), TeleportType.BLANK);
			}
			if (object.getY() == 3209) {
				player.getMovement().move(true, new WorldTile(3229, 3209, 1), TeleportType.BLANK);
			}
			if (object.getY() == 3270) {
				player.getMovement().move(true, new WorldTile(3188, 3270, 1), TeleportType.BLANK);
			}
		});
		object.doAction(optionId, 45484, "climb-down", () -> {
			if (object.getX() == 3237) {
				player.getMovement().move(true, new WorldTile(3232, 3238, 0), TeleportType.BLANK);
			}
			if (object.getY() == 3205) {
				player.getMovement().move(true, new WorldTile(3232, 3205, 0), TeleportType.BLANK);
			}
			if (object.getY() == 3209) {
				player.getMovement().move(true, new WorldTile(3232, 3209, 0), TeleportType.BLANK);
			}
			if (object.getY() == 3270) {
				player.getMovement().move(true, new WorldTile(3185, 3270, 0), TeleportType.BLANK);
			}
		});
		object.doAction(optionId, 37683, "climb-down", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		
		//grave area/shops
		object.doAction(optionId, 48797, "climb-down", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		object.doAction(optionId, 2145, "search", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		object.doAction(optionId, 2145, "open", () -> player.getPackets().sendGameMessage(GameConstants.MISSING_CONTENT));
		
		object.doAction(optionId, 52308, "Climb-up", () ->  player.getMovement().move(true, new WorldTile(3222, 3268), TeleportType.LADDER));
		object.doAction(optionId, 52309, "enter", () -> player.getMovement().move(true, new WorldTile(4762, 5891), TeleportType.BLANK));
		
		
		if (object.getId() == 36880) {
			if (player.getInventory().containsItem(1931, 1)) {
				if (player.getAttributes().get(Attribute.WHEAT_GRINDED).getBoolean()) {
					player.getAttributes().get(Attribute.WHEAT_GRINDED).set(false);
					player.getAttributes().get(Attribute.WHEAT_DEPOSITED).set(false);
					player.getPackets().sendGameMessage("You take the ground flour.");
					player.getDetails().getStatistics().addStatistic("Flour_Taken");
					player.setNextAnimation(Animations.TWO_HANDED_GRAB);
					player.getInventory().deleteItem(1931, 1);
					player.getInventory().addItem(1933, 1);
					updateWheat(player);
				}
			} else
				player.getPackets().sendGameMessage("You need an empty pot to gather the flour.");
		}
		object.doAction(optionId, 36795, "climb-up", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36796, "climb", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		object.doAction(optionId, 36797, "climb-down", () -> new StairsLaddersDialogue(object).execute(player, optionId));
		
		if (object.getId() == 12308) {
			switch (optionId) {
			case 1:
				player.getBank().openBank();
				break;
			case 2:
				ShopsHandler.openShop(player, "culinaromancer_food_10");
				break;
			case 5:
				ShopsHandler.openShop(player, "culinaromancer_equipment_10");
				break;
			}
		}
	}
	
	@Override
	public void executeItemOnObject(Player player, GameObject object, Item item) throws Exception {
		if (object.getId() == 36881 && item.getId() == 1947) {
			if (!player.getAttributes().get(Attribute.WHEAT_DEPOSITED).getBoolean()) {
				player.getInventory().deleteItem(1947, 1);
				player.setNextAnimation(Animations.PLACING_WHEAT_INTO_HOPPER);
				player.getPackets().sendGameMessage("You put the wheat in the hopper.");
				player.getAttributes().get(Attribute.WHEAT_DEPOSITED).set(true);
			} else
				player.getPackets().sendGameMessage("You already have wheat in deposited.");
		}
	}
	
	public static void updateWheat(Player player) {
		player.getVarsManager().sendVar(695, (player.getAttributes().get(Attribute.WHEAT_GRINDED).getBoolean() ? 1 : 0)).submitVarToMap(695, (player.getAttributes().get(Attribute.WHEAT_GRINDED).getBoolean() ? 1 : 0));
	}
}