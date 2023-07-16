package skills.prayer;

import com.rs.game.player.Player;

public class DungeoneeringNecklaces {

    public static void handleNecklaces(Player player, int boneId) {
        if (player.getEquipment().getAmuletId() == 19886) {
            player.getPrayer().restorePrayer(50);
        }
        if (player.getEquipment().getAmuletId() == 19887) {
            if (boneId == Bone.BONES.getId() || boneId == Bone.BAT_BONES.getId())
                player.getPrayer().restorePrayer(50);
            else
                player.getPrayer().restorePrayer(100);
        }
        if (player.getEquipment().getAmuletId() == 19888) {
            if (boneId == Bone.BONES.getId() || boneId == Bone.BAT_BONES.getId())
                player.getPrayer().restorePrayer(20);
            else if (boneId == Bone.BIG_BONES.getId() || boneId == Bone.BABYDRAGON_BONES.getId() || boneId == Bone.DRAGON_BONES.getId() || boneId == Bone.OURG_BONES.getId() || boneId == Bone.DAGANNOTH_BONES.getId())
                player.getPrayer().restorePrayer(60);
            else
                player.getPrayer().restorePrayer(25);
        }
    }
   
}