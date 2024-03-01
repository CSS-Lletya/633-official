package skills.magic.spells.modern.charge;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Action;

import skills.Skills;
import skills.magic.Magic;

public class ChargeFireOrb extends Action {

    public boolean checkAll(Player player) {
		if (!player.getInventory().containsItem(567, 1)) {
			player.getPackets().sendGameMessage("You've ran out of unpowered orbs.", true);
		    return false;
		}
		if (!Magic.checkSpellRequirements(player, 63, false, 554, 30, 564, 3))
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
    		player.getInventory().replaceItems(new Item(567), new Item(569));
    		player.getAudioManager().sendSound(Sounds.CHARGE_FIRE_ORB);
		    player.getSkills().addExperience(Skills.MAGIC, 73);
		    player.setNextAnimationNoPriority(Animations.CHARGE_ORB);
			player.getPackets().sendGameMessage("You fill the orb with the power of Fire.", true);
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