package skills.summoning;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.player.Player;

import lombok.AllArgsConstructor;
import skills.Skills;

public class EnchantedHeadwear {

    public static boolean canEquip(int itemId, Player player) {
    	Headwear.VALUES.stream().filter(enchantable -> enchantable.baseItem == itemId)
				.forEach(helm -> {
					if ((null != helm) && (helm.baseItem != itemId)) {
						boolean meetsRequirements = true;
						if (player.getSkills().getTrueLevel(Skills.DEFENCE) < helm.defenceRequirement) {
							if (meetsRequirements)
								player.getPackets().sendGameMessage("You are not high enough level to use this item.");
							meetsRequirements = false;
							player.getPackets().sendGameMessage("You need to have a Defence level of "
									+ helm.defenceRequirement + " to equip this.");
						}
						if (player.getSkills().getTrueLevel(Skills.RANGE) < helm.rangeRequirement) {
							if (meetsRequirements)
								player.getPackets().sendGameMessage("You are not high enough level to use this item.");
							meetsRequirements = false;
							player.getPackets().sendGameMessage(
									"You need to have a Range level of " + helm.rangeRequirement + " to equip this.");
						}
						if (player.getSkills().getTrueLevel(Skills.MAGIC) < helm.magicRequirement) {
							if (meetsRequirements)
								player.getPackets().sendGameMessage("You are not high enough level to use this item.");
							meetsRequirements = false;
							player.getPackets().sendGameMessage(
									"You need to have a Magic level of " + helm.magicRequirement + " to equip this.");

						}
						if (!meetsRequirements)
							return;
					}
				});

		return true;
	}

    @AllArgsConstructor
    public enum Headwear {
        ADAMANT_FULL_HELM(1161, 12658, 12659, 30, 1, 1, 1, 50),
        RUNE_FULL_HELM(1163, 12664, 12665, 40, 1, 1, 30, 60),
        DRAGON_HELM(6967, 12666, 12667, 60, 1, 1, 50, 110),
        BERSERKER_HELM(3751, 12674, 12675, 45, 1, 1, 30, 70),
        WARRIOR_HELM(3753, 12676, 12677, 45, 1, 1, 30, 70),
        HELM_OF_NEITIZNOT(10828, 12680, 12681, 55, 1, 1, 45, 90),

        SNAKESKIN_BANDANA(6326, 12660, 12661, 30, 30, 1, 20, 50),
        ARCHER_HELM(3749, 12672, 12673, 45, 45, 1, 30, 70),
        ARMADYL_HELMET(11718, 12670, 12671, 70, 70, 1, 60, 120),

        SPLITBARK_HELM(3385, 12662, 12663, 40, 1, 40, 30, 50),
        FARSEER_HELM(3755, 12678, 12679, 45, 1, 45, 30, 70),
        LUNAR_HELM(10609, 12668, 12669, 40, 1, 60, 55, 110),

        SLAYER_HELMET(13263, 14636, 14637, 10, 20, 20, 20, 50),
        FULL_SLAYER_HELMET(15492, 15496, 15497, 10, 20, 20, 20, 50),
        RED_FULL_SLAYER_HELMET(22528, 22530, 22532, 10, 20, 20, 20, 50),
        BLUE_FULL_SLAYER_HELMET(22534, 22536, 22538, 10, 20, 20, 20, 50),
        GREEN_FULL_SLAYER_HELMET(22540, 22542, 22544, 10, 20, 20, 20, 50),
        YELLOW_FULL_SLAYER_HELMET(22546, 22548, 22550, 10, 20, 20, 20, 50),

        BLUE_FEATHER_HEADDRESS(12210, 12210, 12212, 1, 20, 1, 50, 40),
        YELLOW_FEATHER_HEADDRESS(12213, 12213, 12215, 1, 20, 1, 50, 40),
        RED_FEATHER_HEADDRESS(12216, 12216, 12218, 1, 20, 1, 50, 40),
        STRIPY_FEATHER_HEADDRESS(12219, 12219, 12221, 1, 20, 1, 50, 40),
        ORANGE_FEATHER_HEADDRESS(12222, 12222, 12224, 1, 20, 1, 50, 40),
        ANTLERS(12204, 12204, 12206, 1, 1, 1, 10, 40),
        LIZARD_SKULL(12207, 12207, 12209, 1, 1, 1, 30, 65);

        public int baseItem;
        public int enchantedItem;
        public int chargedHelm;
        public int defenceRequirement;
        public int rangeRequirement;
        public int magicRequirement;
        public int summoningRequirement;
        public int scrollLimit;

        public static final ImmutableSet<Headwear> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Headwear.class));
    }
}