package skills;

import com.rs.constants.Graphic;
import com.rs.constants.InterfaceVars;
import com.rs.constants.Sounds;
import com.rs.game.player.Player;

public class LevelUp extends Skills {

	public static void advanceLevel(Player player, int skill, int gainedLevels) {
		int flashingConfig = 0;
		int spriteConfig = 0;
		int currentLevel = player.getSkills().getTrueLevel(skill);
		Musics musicId = Musics.levelup(skill);
		totalMileStone(player);
        player.getSkills().getLeveledUp()[skill] = true;
        for (int i = 0; i < player.getSkills().getLeveledUp().length; i++) {
            if (player.getSkills().getLeveledUp()[i]) {
                flashingConfig |= FLASH_VALUES[i];
                spriteConfig = CONFIG_VALUES[i];
                flashingConfig |= FLASH_VALUES[skill];
                spriteConfig = CONFIG_VALUES[skill];
            }
        }
        player.getVarsManager().sendVar(InterfaceVars.LEVEL_UP_AND_FLASH, spriteConfig << 26 | flashingConfig);
		player.getPackets().sendIComponentText(740, 1, "You have now reached level " + player.getSkills().getLevel(skill) + "!");
		player.getPackets().sendIComponentText(740, 0, "Congratulations, you've advanced " + 
		(gainedLevels == 1? "a" : gainedLevels) + " " +Skills.SKILL_NAME[skill] + (gainedLevels == 1 ? " level!" : " levels!"));
		player.getAudioManager().sendSound(currentLevel == 99 ? Sounds.MASTERED_SKILL : Sounds.LEVEL_UP);
		player.setNextGraphics(Graphic.LEVEL_UP);
		player.task(3, p -> player.getPackets().sendMusicEffect(currentLevel > 50 ? musicId.getId2() : musicId.getId()));
		if (currentLevel == 99) {
			player.getSkills().trimCapes();
		}
		player.getPackets().sendGameMessage("Congratulations, you've advanced " + 
				(gainedLevels == 1? "a" : gainedLevels) + " "+Skills.SKILL_NAME[skill] + (gainedLevels == 1 ? " level!" : " levels!"));
		player.getInterfaceManager().sendChatBoxInterface(740);
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
	
    public static final int[] FLASH_VALUES = {
            1, 4, 2, 64, 8, 16, 32, 32768, 131072, 2048, 16384, 65536, 1024, 8192, 4096, 256, 128,
            512, 524288, 1048576, 262144, 4194304, 2097152, 8388608, 16777216,
    };
    
    public static final int[] CONFIG_VALUES =
        {
                1, 5, 2, 6, 3, 7, 4, 16, 18, 19, 15, 17, 11, 14, 13, 9, 8, 10, 20, 21, 12, 23, 22, 24, 25
        };
    
    public static void sendFlashIcons(Player player) {
        int flashingConfig = 0;
        int spriteConfig = 0;
        for (int i = 0; i < player.getSkills().getLeveledUp().length; i++) {
            if (player.getSkills().getLeveledUp()[i]) {
                flashingConfig |= FLASH_VALUES[i];
                spriteConfig = CONFIG_VALUES[i];
            }
        }
        player.getVarsManager().sendVar(InterfaceVars.LEVEL_UP_AND_FLASH, spriteConfig << 26 | flashingConfig);
    }
}
