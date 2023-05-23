package skills.prayer;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Bone {
	BONES(526, 7.5),
	BAT_BONES(530, 2.9),
	MONKEY_BONES(3179, 10),
	WOLF_BONES(2859, 14.5),
	BIG_BONES(532, 19),
	BABYDRAGON_BONES(534, 30),
	DRAGON_BONES(536, 52),
	OURG_BONES(4834, 90),
	DAGANNOTH_BONES(6729, 120);

	public static final ImmutableSet<Bone> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Bone.class));

	private final int id;
	private final double experience;

	public int getId() {
		return id;
	}

	public double getExperience() {
		return experience;
	}

	public static Optional<Bone> getBone(int id) {
		for(Bone bone : VALUES) {
			if(bone.id == id)
				return Optional.of(bone);
		}
		return Optional.empty();
	}

	@Override
	public String toString() {
		return name().toLowerCase().replaceAll("_", " ");
	}

}