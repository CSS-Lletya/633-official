package skills.hunter;

import java.util.EnumSet;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtility;

import lombok.Getter;

public enum FlyingEntities {

	BABY_IMPLING(1028, 11238, 20, 25, 17), YOUNG_IMPLING(1029, 11240, 48, 65, 22),
	GOURMET_IMPLING(1030, 11242, 82, 113, 28), EARTH_IMPLING(1031, 11244, 126, 177, 36),
	ESSENCE_IMPLING(1032, 11246, 160, 225, 42), ECLECTIC_IMPLING(1033, 11248, 205, 289, 50),
	SPIRIT_IMPLING(7866, 15513, 227, 321, 54) {
		@Override
		public void effect(Player player) {
			if (RandomUtility.random(2) == 0) {
				Item charm = CHARMS[RandomUtility.random(CHARMS.length)];
				int charmAmount = RandomUtility.random(charm.getAmount());
				player.dialogue(d -> d.item(charm.getId(), charmAmount, "The impling was carrying a" + charm.getName().toLowerCase() + "."));
				player.getInventory().addItem(charm.getId(), charmAmount);
			}
		}
	},
	NATURE_IMPLING(1034, 11250, 250, 353, 58), MAGPIE_IMPLING(1035, 11252, 289, 409, 65),
	NINJA_IMPLING(6053, 11254, 339, 481, 74), PIRATE_IMPLING(7845, 13337, 350, 497, 76),
	DRAGON_IMPLING(6054, 11256, 390, 553, 83), ZOMBIE_IMPLING(7902, 15515, 412, 585, 87),
	KINGLY_IMPLING(7903, 15517, 434, 617, 91),
	;

	public static final ImmutableSet<FlyingEntities> VALUES = Sets.immutableEnumSet(EnumSet.allOf(FlyingEntities.class));

	@Getter
	private int npcId, level, reward;
	
	@Getter
	private double puroExperience, experience;
	
	@Getter
	private Graphics graphics;

	private FlyingEntities(int npcId, int reward, double puroExperience, double rsExperience, int level,
			Graphics graphics) {
		this.npcId = npcId;
		this.reward = reward;
		this.puroExperience = puroExperience;
		this.experience = rsExperience;
		this.level = level;
		this.graphics = graphics;
	}

	private FlyingEntities(int npcId, int reward, double puroExperience, double rsExperience, int level) {
		this.npcId = npcId;
		this.reward = reward;
		this.puroExperience = puroExperience;
		this.experience = rsExperience;
		this.level = level;
	}
	
	public void effect(Player player) {

	}
	
	public static Optional<FlyingEntities> getImp(int id) {
		for(FlyingEntities imp : VALUES) {
			if(imp.npcId == id)
				return Optional.of(imp);
		}
		return Optional.empty();
	}
	
	public static final Item[] CHARMS = { new Item(12158, 1), new Item(12159, 1), new Item(12160, 1), new Item(12163, 1) };
}