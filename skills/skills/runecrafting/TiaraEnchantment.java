package skills.runecrafting;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Graphic;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import lombok.AllArgsConstructor;
import skills.Skills;

public class TiaraEnchantment {

	private static Item EMPTY_TIARA = new Item(5525);

	@AllArgsConstructor
	public enum RunecraftingTiara {
		AIR_TIARA(1438, 2478, 5527, 1, 25.0),
		MIND_TIARA(1448, 2479, 5529, 2, 27.5),
		WATER_TIARA(1444, 2480, 5531, 5, 30.0),
		EARTH_TIARA(1440, 2481, 5535, 9, 32.5),
		FIRE_TIARA(1442, 2482, 5537, 14, 35.0),
		BODY_TIARA(1446, 2483, 5533, 20, 37.5),
		COSMIC_TIARA(1454, 2484, 5539, 27, 40.0),
		CHAOS_TIARA(1452, 2487, 5543, 35, 42.5),
		NATURE_TIARA(1462, 2486, 5541, 44, 45.0),
		LAW_TIARA(1458, 2485, 5545, 54, 47.5),
		DEATH_TIARA(1456, 2488, 5547, 65, 50.0),
		BLOOD_TIARA(1450, 30624, 5549, 77, 52.5);

		public int talisman;
		public int altarId;
		public int tiara;
		public int level;
		public double exp;
		
		public static final ImmutableSet<RunecraftingTiara> VALUES = Sets.immutableEnumSet(EnumSet.allOf(RunecraftingTiara.class));
	}

	public static void enchant(Player player, int talimanUsed) {
		RunecraftingTiara.VALUES.stream().filter(tali -> tali.talisman == talimanUsed)
		.forEach(tiara -> {
			if (player.getSkills().getTrueLevel(Skills.RUNECRAFTING) < tiara.level) {
				player.getPackets().sendGameMessage("You need a level of " + tiara.level + " Runecrafting to create "
						+ ItemDefinitions.getItemDefinitions(tiara.tiara).getName() + ".");
				return;
			}
			if (!player.getInventory().containsAny(EMPTY_TIARA.getId())) {
				player.getPackets().sendGameMessage(
						"You need a empty tiara to create " + ItemDefinitions.getItemDefinitions(tiara.tiara).getName() + ".");
				return;
			}
			player.getMovement().lock(1);
			player.setNextGraphics(Graphic.RUNECRAFTING);
			player.setNextAnimation(Animations.RUNECRAFTING);
			player.getInventory().deleteItem(new Item(talimanUsed));
			player.getInventory().replaceItems(EMPTY_TIARA, new Item(tiara.tiara));
			player.getSkills().addExperience(Skills.RUNECRAFTING, tiara.exp);
			player.getPackets().sendGameMessage("You bind the power of the talisman into your tiara.");
		});
	}
}