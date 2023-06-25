package com.rs.net.encoders.other;

import com.rs.game.player.Player;

public class QuickChatMessage extends PublicChatMessage {

	private int fileId;

	public QuickChatMessage(Player player, int fileId, byte[] data) {
		super(player, data == null ? null : new String(data), 0x8000);
		this.fileId = fileId;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

}
