package com.rs.plugin.impl.commands;

import com.rs.constants.Animations;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"emote"}, rights = {Rights.ADMINISTRATOR}, syntax = "Perform an Emote")
public final class EmoteCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	player.setNextAnimation(Animations.RESET_ANIMATION);
		if (cmd.length < 2) {
			player.getPackets().sendPanelBoxMessage("Use: ;;emote id");
			return;
		}
		try {
			player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
		} catch (NumberFormatException e) {
			player.getPackets().sendPanelBoxMessage("Use: ;;emote id");
		}
    }
}