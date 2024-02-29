package com.rs.game.player.content.traveling;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.FadingScreen;
import com.rs.net.encoders.other.Animation;

import lombok.AllArgsConstructor;
import skills.Skills;
import skills.woodcutting.Hatchet;

public class Canoes {

	private static int CANOE_SELECTION = 52, AREA_SELECTION = 53;
	private static final int[] VISIBLE_COMPONENTS = { 3, 2, 5 };
	private static final int[] INVISIBLE_COMPONENTS = { 9, 10, 8 };

	public static void chopCanoeTree(Player player, final int configIndex) {
		player.getAction().setAction(new Action() {

			private Hatchet hatchet;
			private boolean isComplete;

			@Override
			public boolean start(Player player) {
				if (!checkAll(player))
					return false;
				return true;
			}

			private boolean checkAll(Player player) {
				for (Hatchet def : Hatchet.values()) {
					if (player.getInventory().containsAny(def.getHatchet().getId())
							|| player.getEquipment().getWeaponId() == def.getHatchet().getId()) {
						hatchet = def;
						if (player.getSkills().getLevel(Skills.WOODCUTTING) < hatchet.getRequirement()) {
							hatchet = null;
							break;
						}
					}
				}
				if (player.getSkills().getLevel(Skills.WOODCUTTING) < 12) {
					player.getPackets().sendGameMessage("You need a woodcutting level of at least 12 to make a canoe.");
					return false;
				}
				if (hatchet == null) {
					hatchet = Hatchet.BRONZE;
					player.getPackets()
							.sendGameMessage("A nearby overseer hands you a bronze hatchet to temporarily use.");
				}
				return true;
			}

			@Override
			public boolean process(Player player) {
				player.setNextAnimation(hatchet.getAnimation());
				return !isComplete;
			}

			@Override
			public int processWithDelay(Player player) {
				player.getAttributes().get(Attribute.CANOE_CHOPPED).set(true);
				player.getAttributes().get(Attribute.CANOE_CONFIG).set(1839 + configIndex);
				player.getVarsManager().sendVarBit(1839 + configIndex, 10);
				player.getAudioManager().sendSound(Sounds.FALLING_TREE);
				isComplete = true;
				stop(player);
				return 4;
			}

			@Override
			public void stop(Player player) {
				player.setNextAnimation(Animations.RESET_ANIMATION);
			}
		});
	}

	public static void openSelectionInterface(Player player) {
		player.getInterfaceManager().sendInterface(CANOE_SELECTION);
		int level = 12;
		for (int index = 0; index < VISIBLE_COMPONENTS.length; index++) {
			level += 15;
			if (player.getSkills().getLevel(Skills.WOODCUTTING) >= level) {
				player.getPackets().sendHideIComponent(CANOE_SELECTION, VISIBLE_COMPONENTS[index], false);
				player.getPackets().sendHideIComponent(CANOE_SELECTION, INVISIBLE_COMPONENTS[index], true);
			}
		}
	}

	public static void createShapedCanoe(final Player player) {
		final int selectedCanoe = player.getAttributes().get(Attribute.CANOE_SELECTED).getInt();
		player.getInterfaceManager().closeInterfaces();
		Hatchet hatchet = null;
		for (Hatchet def : Hatchet.values()) {
			if (player.getInventory().containsAny(def.getHatchet().getId())
					|| player.getEquipment().getWeaponId() == def.getHatchet().getId()) {
				hatchet = def;
				if (player.getSkills().getLevel(Skills.WOODCUTTING) < hatchet.getRequirement()) {
					hatchet = null;
					break;
				}
			}
		}
		if (hatchet == null)
			hatchet = Hatchet.BRONZE;
		player.setNextAnimation(new Animation(hatchet.ordinal() + 11594));
		player.getMovement().lock();
		player.getAudioManager().sendSound(Sounds.BUILD_CANOE);
		player.task(6, woodcutter -> {
			player.getAudioManager().sendSound(Sounds.ROLL_CANOE);
			woodcutter.toPlayer().getAttributes().get(Attribute.CANOE_SHAPED).set(true);
			woodcutter.toPlayer().getMovement().unlock();
			woodcutter.toPlayer().getVarsManager().sendVarBit(woodcutter.toPlayer().getAttributes().get(Attribute.CANOE_CONFIG).getInt(),
					11 + selectedCanoe);
		});
	}

	public static void openTravelInterface(Player player, int canoeArea) {
		player.getAttributes().get(Attribute.CANOE_LOCATION_SET).set(canoeArea);
		player.getInterfaceManager().sendInterface(AREA_SELECTION);
		player.getPackets().sendHideIComponent(AREA_SELECTION, 21, false);
		player.getPackets().sendHideIComponent(AREA_SELECTION, canoeArea == 3 ? 19 : 22 + (3 - canoeArea), false);
	}

	public static void deportCanoeStation(Player player, int selectedArea) {
		final int selectedCanoe = player.getAttributes().get(Attribute.CANOE_SELECTED).getInt();
		int canoeArea = player.getAttributes().get(Attribute.CANOE_LOCATION_SET).getInt();
		if (selectedArea != canoeArea) {
			if (selectedArea > (canoeArea + selectedCanoe + 1) || selectedArea < (canoeArea - selectedCanoe - 1)) {
				player.getPackets().sendGameMessage(
						"This is too far to reach, please pick another plot point or make a better canoe.");
				return;
			} else {
				player.getInterfaceManager().closeInterfaces();
				player.getAttributes().get(Attribute.CANOE_LOCATION_SET).set(0);
				player.getAttributes().get(Attribute.CANOE_CONFIG).set(-1);
				player.getAttributes().get(Attribute.CANOE_CHOPPED).set(false);
				player.getAttributes().get(Attribute.CANOE_SHAPED).set(false);
				player.getVarsManager().sendVarBit(player.getAttributes().get(Attribute.CANOE_CONFIG).getInt(), 0);
				CanoeDestinations.VALUES.stream().filter(area -> area.areaId == selectedArea)
						.forEach(travel -> FadingScreen.fade(player,
								() -> {
									player.getAudioManager().sendSound(Sounds.SINK_CANOE);
									player.setNextWorldTile(new WorldTile(travel.destination));
								}));
			}
		} else
			player.getPackets().sendGameMessage("You are already at this location!");
	}

	@AllArgsConstructor
	public enum CanoeDestinations {
		LUMBRIDGE(0, new WorldTile(3232, 3252, 0)),
		CHAMPIONS_GUILD(1, new WorldTile(3199, 3343, 0)),
		BARBARIAN_VILLAGE(2, new WorldTile(3113, 3406, 0)),
		EDGEVILLE(3, new WorldTile(3133, 3506, 0));
		
		private int areaId;
		private WorldTile destination;
		
		public static final ImmutableSet<CanoeDestinations> VALUES = Sets.immutableEnumSet(EnumSet.allOf(CanoeDestinations.class));
	}
}