package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.Command;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"sound", "sendsound"}, rights = {Rights.ADMINISTRATOR}, syntax = "Plays a Sound track")
public final class SendSoundCommandPlugin implements Command {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	int soundId = Integer.parseInt(cmd[1]);
		player.getPackets().sendVoice(soundId);
    }
}