package skills.herblore;

import com.rs.game.item.Item;
import com.rs.game.player.Player;

import skills.Skills;

public class HerbicideHandler {

	public static boolean handleDrop(Player player, Item item) {
		int i = 0;
		if (player.getInventory().containsItem(19675, 1)) {
			for (HerbicideSettings settings : HerbicideSettings.values()) {
				if (settings.isHerb(item.getId()) && player.getDetails().herbicideSettings[i]) {
					player.getSkills().addExperience(Skills.HERBLORE, settings.getExperience());
					player.getPackets().sendGameMessage("The herbicide instantly incinerates the"
							+ item.getName().replaceAll("Grimy", "").replaceAll("Clean", "") + ".", true);
					return true;
				}
				i++;
			}
		}
		return false;
	}


    public enum HerbicideSettings {

        GUAM(5, 3, 199, 249),
        MARRENTILL(7.6, 5, 201, 251),
        TARROMIN(10, 11, 203, 253),
        HARRALANDER(12.6, 20, 205, 255),
        RANARR(15, 25, 207, 257),
        TOADFLAX(16, 30, 3049, 2998),
        SPIRIT_WEED(15.6, 35, 12174, 12172),
        IRIT(17.6, 40, 209, 259),
        WERGALI(19, 41, 14836, 14854),
        AVANTOE(20, 48, 211, 261),
        KWUARM(22.6, 54, 213, 263),
        SNAPDRAGON(23.6, 59, 3051, 3000),
        CADANTINE(25, 65, 215, 265),
        LANTADYME(26.2, 67, 2485, 2481),
        DWARF_WEED(27.6, 70, 217, 267),
        FELLSTALK(33.6, 91, 21626, 21624),
        TORSTOL(30, 75, 219, 269);

        private int[] herbs;
        private int level;
        private double experience;

        HerbicideSettings(double experience, int level, int... herbs) {
            this.level = level;
            this.experience = experience;
            this.herbs = herbs;
        }

        public boolean isHerb(int id) {
            for (int i = 0; i < herbs.length; i++) {
                if (herbs[i] == id)
                    return true;
            }
            return false;
        }

        public double getExperience() {
            return experience;
        }

        public int getLevel() {
            return level;
        }
    }
}