package skills.magic.spells.modern.charge;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;
import com.rs.game.player.content.Magic;

import skills.Skills;

public class ChargeEarthOrb extends Action {

    public boolean checkAll(Player player) {
		if (!player.getInventory().containsItem(567, 1)) {
			player.getPackets().sendGameMessage("You've ran out of unpowered orbs.", true);
		    return false;
		}
		if (!Magic.checkSpellRequirements(player, 60, false, 557, 30, 564, 3))
		    return false;
		return true;
    }

    @Override
    public boolean process(Player player) {
    	return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
    	if (checkAll(player)) {
    		player.getInventory().replaceItems(new Item(567), new Item(575));
    		player.getAudioManager().sendSound(Sounds.CHARGE_EARTH_ORB);
		    player.getSkills().addExperience(Skills.MAGIC, 70);
		    player.setNextAnimationNoPriority(Animations.CHARGE_ORB);
			player.getPackets().sendGameMessage("You fill the orb with the power of Earth.", true);
		} else
			stop(player);
		return 0;
    }

    @Override
    public boolean start(Player player) {
    	return checkAll(player);
    }

    @Override
    public void stop(final Player player) {
    }
}