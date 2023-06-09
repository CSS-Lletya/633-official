package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"s","sound", "sendsound"}, rights = {Rights.ADMINISTRATOR}, syntax = "Plays a Sound track")
public final class SendSoundCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	int soundId = Integer.parseInt(cmd[1]);
    	player.getAudioManager().sendSound(soundId);
    }
}