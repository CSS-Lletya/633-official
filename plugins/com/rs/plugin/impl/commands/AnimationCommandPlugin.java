package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"anim", "an", "animation"}, rights = {Rights.ADMINISTRATOR}, syntax = "Perform an animation")
public final class AnimationCommandPlugin implements Command {
	
	@Override
	public void execute(Player player, String[] cmd, String command) throws Exception {
		player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
	}
}