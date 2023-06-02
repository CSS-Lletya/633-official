package com.rs.plugin.impl.interfaces;

import com.rs.game.player.Player;
import com.rs.net.decoders.WorldPacketsDecoder;
import com.rs.plugin.listener.RSInterfaceListener;
import com.rs.plugin.wrapper.RSInterfaceSignature;

@RSInterfaceSignature(interfaceId = { 187 })
public class MusicInterfacePlugin extends RSInterfaceListener {

	// TODO: Fix playing tracks manually, etc..
	@Override
	public void execute(Player player, int interfaceId, int componentId, int packetId, byte slotId, int slotId2) {
		if (componentId == 1) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
				player.getMusicsManager().playAnotherMusicByIndex(slotId / 2);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
				player.getMusicsManager().sendHint(slotId / 2);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
				player.getMusicsManager().addToPlayList(slotId / 2);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
				player.getMusicsManager().removeFromPlayList(slotId / 2);
		} else if (componentId == 4)
			player.getMusicsManager().addPlayingMusicToPlayList();
		else if (componentId == 9) {
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
				player.getMusicsManager().playAnotherMusicFromPlayListByIndex(slotId);
			else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
				player.getMusicsManager().removeFromPlayListByIndex(slotId);

		} else if (componentId == 10)
			player.getMusicsManager().switchPlayListOn();
		else if (componentId == 11)
			player.getMusicsManager().clearPlayList();
		else if (componentId == 13)
			player.getMusicsManager().switchShuffleOn();
	}
}