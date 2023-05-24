package com.rs.plugin.impl.commands;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.plugin.listener.CommandListener;
import com.rs.plugin.wrapper.CommandSignature;

@CommandSignature(alias = {"music", "playmusic"}, rights = {Rights.ADMINISTRATOR}, syntax = "Plays a Music track")
public final class SendMusicCommandPlugin implements CommandListener {
    @Override
    public void execute(Player player, String[] cmd, String command) {
    	int musicId = Integer.parseInt(cmd[1]);
		player.getPackets().sendMusic(musicId);
    }
}