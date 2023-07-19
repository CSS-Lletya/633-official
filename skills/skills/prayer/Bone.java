package skills.prayer;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Bone {
	NORMAL(526, 4.5),
    BURNT(528, 4.5),
    WOLF(2859, 4.5),
    MONKEY(3183, 5),
    BAT(530, 5),
    BIG(532, 15),
    JOGRE(3125, 15),
    ZOGRE(4812, 22.5),
    SHAIKAHAN(3123, 25),
    BABY(534, 30),
    WYVERN(6812, 50),
    DRAGON(536, 72),
    FAYRG(4830, 84),
    RAURG(4832, 96),
    DAGANNOTH(6729, 125),
    ICE_GIANT_RIBS(7875, 27),
    OURG(4834, 140),
    FROST_DRAGON(18830, 180), //dung ones
    FROST_DRAGON_2(18832, 180), //real ones
	;

	public static final ImmutableSet<Bone> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Bone.class));

	@Getter
	private final int id;
	@Getter
	private final double experience;
}