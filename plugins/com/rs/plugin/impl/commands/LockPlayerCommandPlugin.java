package com.rs.plugin.impl.commands;

import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

/**
 * You will need to add lock checks to specific things you want to block off
 * such as button clicking etc.. right now its just movement.
 * 
 * @author Dennis
 *
 */
@CommandSignature(alias = {"lockplayer", "lp"}, rights = {Rights.ADMINISTRATOR}, syntax = "toggles a target players movement/interactions")
public final class LockPlayerCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	String name;
    	Player target;
    	
    	name = "";
		for (int i = 1; i < cmd.length; i++)
			name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
		target = World.getPlayerByDisplayName(name);
		if (target != null) {
			if (target.getMovement().isLocked()) {
				target.getMovement().unlock();
			} else
				target.getMovement().lock();
			player.getPackets().sendGameMessage(target.getDisplayName() + "'s movement & interactions state is: " + target.getMovement().isLocked());
		}
    }
}