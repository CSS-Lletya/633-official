package skills;

import com.rs.game.player.Player;
import com.rs.net.encoders.other.Graphics;

public class LevelUp extends Skills {

	public static void advanceLevel(Player player, int skill, int gainedLevels) {
		Musics musicId = Musics.levelup(skill);
		totalMileStone(player);
		player.setNextGraphics(new Graphics(player.getSkills().getLevel(skill) == 99 || player.getSkills().getLevel(skill) == 120 ? 1765 : 199));
		player.getPackets().sendMusicEffect(gainedLevels > 50 ? musicId.getId2() : musicId.getId());
		player.getPackets().sendGameMessage("Congratulations, you've advanced " + gainedLevels
				+ (gainedLevels == 1 ? " level" : " levels") + " in " + SKILL_NAME[skill] + ".");
		
	}

	public static void totalMileStone(Player player) {
		final int TotalLevels[] = { 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500,
				1600, 1700, 1800, 1900, 2000, 2100, 2200, 2300, 2400, 2496 };
		for (int levels : TotalLevels) {
			if (player.getSkills().getTotalLevel(player) == levels) {
				player.getPackets().sendGameMessage("<col=990000>Well done! You've reached the total level "
						+ player.getSkills().getTotalLevel(player) + " milestone!");
			}
			return;
		}
	}
}
