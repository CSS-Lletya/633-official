package com.rs.plugin.impl.objects.region;

import com.rs.constants.Sounds;
import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.magic.TeleportType;

@ObjectSignature(objectId = { 16149, 16080, 16148, 16118, 16154, 16078, 16081, 16114, 16115, 16049, 16112, 16048, 16150,
		16082, 16116, 16050, 16043, 16044, 16065, 16066, 16089, 16090, 16124, 16123, 16135, 16077, 16047,
		16146 }, name = {})
public class StrongholdOfSecurityDungeonRegionObjectPugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		int id = object.getId();
		if (id == 16149) {
			player.getMovement().move(false, new WorldTile(2042, 5245, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16080) {
			player.getMovement().move(false, new WorldTile(1902, 5223, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16148) {
			player.getMovement().move(false, new WorldTile(3081, 3421, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16154) {
			player.getMovement().move(false, new WorldTile(1860, 5244, 0), TeleportType.BLANK);
			return;
		}
		if (id == 16078) {
			player.getMovement().move(false, new WorldTile(1902, 5223, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16081) {
			player.getMovement().move(false, new WorldTile(2122, 5251, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16114) {
			player.getMovement().move(false, new WorldTile(1902, 5223, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16115) {
			player.getMovement().move(false, new WorldTile(2358, 5215, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16049) {
			player.getMovement().move(false, new WorldTile(2147, 5284, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16112) {
			player.getMovement().move(false, new WorldTile(2026, 5217, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16048) {
			player.getMovement().move(false, new WorldTile(2147, 5284, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16146) {
			player.getMovement().move(false, new WorldTile(1859, 5243, 0), TeleportType.LADDER);
			return;
		}
		if (id == 16150) {
			if (player.getDetails().getStrongholdLevels().get() < 1) {
				player.getPackets().sendGameMessage("You must complete this floor before using this portal.");
				return;
			}
			player.setNextWorldTile(new WorldTile(2042, 5245, 0));
			return;
		}
		if (id == 16082) {
			if (player.getDetails().getStrongholdLevels().get() < 2) {
				player.getPackets().sendGameMessage("You must complete this floor before using this portal.");
				return;
			}
			player.setNextWorldTile(new WorldTile(2122, 5251, 0));
			return;
		}
		if (id == 16116) {
			if (player.getDetails().getStrongholdLevels().get() < 3) {
				player.getPackets().sendGameMessage("You must complete this floor before using this portal.");
				return;
			}
			player.setNextWorldTile(new WorldTile(2358, 5215, 0));
			return;
		}
		if (id == 16050) {
			if (player.getDetails().getStrongholdLevels().get() < 4) {
				player.getPackets().sendGameMessage("You must complete this floor before using this portal.");
				return;
			}
			player.setNextWorldTile(new WorldTile(2350, 5214, 0));
			return;
		}
		if (id == 16043 || id == 16044 || id == 16065 || id == 16066 || id == 16089 || id == 16090 || id == 16124
				|| id == 16123) {
			player.setNextAnimation(new Animation(547));
			switch (object.getRotation()) {
			case 0:
				if (player.getX() < object.getX())
					player.setNextWorldTile(new WorldTile(player.getX() + 2, player.getY(), player.getPlane()));
				if (player.getX() == object.getX())
					player.setNextWorldTile(new WorldTile(player.getX() - 1, player.getY(), player.getPlane()));
				break;
			case 1:
				if (player.getY() > object.getY())
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY() - 2, player.getPlane()));
				if (player.getY() == object.getY())
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 1, player.getPlane()));
				break;
			case 2:
				if (player.getX() > object.getX())
					player.setNextWorldTile(new WorldTile(player.getX() - 2, player.getY(), player.getPlane()));
				if (player.getX() == object.getX())
					player.setNextWorldTile(new WorldTile(player.getX() + 1, player.getY(), player.getPlane()));
				break;
			case 3:
				if (player.getY() < object.getY())
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY() + 2, player.getPlane()));
				if (player.getY() == object.getY())
					player.setNextWorldTile(new WorldTile(player.getX(), player.getY() - 1, player.getPlane()));
				break;
			}
			return;
		}
		if (id == 16135) {
			if (player.getDetails().getStrongholdLevels().get() == 1) {
				player.getPackets().sendGameMessage("You have already taken your reward from here.");
				return;
			}
			player.getInventory().addItemDrop(995, 2000);
			player.dialogue(new DialogueEventListener(player) {
				@Override
				public void start() {
					mes("The box hinges creak and appear to be forming audible words....");
					mes("...Congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Flap emote!");
					player.task(1, p -> {
						player.getDetails().getStrongholdLevels().getAndIncrement();
						player.getVarsManager().sendVar(802, 1).submitVarToMap(802, 1);
					});
				}
			});
			return;
		}
		if (id == 16077) {
			if (player.getDetails().getStrongholdLevels().get() == 2) {
				player.getPackets().sendGameMessage("You have already taken your reward from here.");
				return;
			}
			player.getInventory().addItemDrop(995, 3000);
			player.dialogue(new DialogueEventListener(player) {
				@Override
				public void start() {
					mes("The box hinges creak and appear to be forming audible words....");
					mes("...Congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Slap head emote!");
					player.task(1, p -> {
						player.getDetails().getStrongholdLevels().getAndIncrement();
						player.getVarsManager().sendVar(802, 19).submitVarToMap(802, 19);
					});
				}
			});
			player.getDetails().getStrongholdLevels().getAndIncrement();
			player.getVarsManager().sendVar(802, 19).submitVarToMap(802, 19);
			return;
		}
		if (id == 16118) {
			if (player.getDetails().getStrongholdLevels().get() == 3) {
				player.getPackets().sendGameMessage("You have already taken your reward from here.");
				return;
			}
			player.getInventory().addItemDrop(995, 5000);
			player.dialogue(new DialogueEventListener(player) {
				@Override
				public void start() {
					mes("The box hinges creak and appear to be forming audible words....");
					mes("...Congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Idea emote!");
					player.task(1, p -> {
						player.getDetails().getStrongholdLevels().getAndIncrement();
						player.getVarsManager().sendVar(802, 23).submitVarToMap(802, 23);
					});
				}
			});
			return;
		}
		if (id == 16047) {
			if (player.ownsItems(new Item(9005), new Item(9006))) {
				player.getPackets().sendGameMessage("You have already taken your reward from here.s");
				return;
			}
			player.dialogue(new DialogueEventListener(player) {
				@Override
				public void start() {
					option("Colorful ones", () -> {
						player.getInventory().addItemDrop(9005, 1);
						player.dialogue(new DialogueEventListener(player) {
							@Override
							public void start() {
								mes("The box hinges creak and appear to be forming audible words....");
								mes("...Congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Stomp emote!");
								player.task(1, p -> {
									player.getDetails().getStrongholdLevels().getAndIncrement();
									player.getVarsManager().sendVar(802, 15).submitVarToMap(802, 15);
								});
							}
						});
						player.getAudioManager().sendSound(Sounds.FINDING_TREASURE);
					}, "Combat ones", () -> {
						player.getInventory().addItemDrop(9006, 1);
						player.dialogue(new DialogueEventListener(player) {
							@Override
							public void start() {
								mes("The box hinges creak and appear to be forming audible words....");
								mes("...Congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Stomp emote!");
								player.task(1, p -> {
									player.getDetails().getStrongholdLevels().getAndIncrement();
									player.getVarsManager().sendVar(802, 15).submitVarToMap(802, 15);
								});
							}
						});
						player.getAudioManager().sendSound(Sounds.FINDING_TREASURE);
					});
				}
			});
		}
	}
}