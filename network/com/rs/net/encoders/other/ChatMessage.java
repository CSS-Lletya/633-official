package com.rs.net.encoders.other;

import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.utilities.TextUtils;
import com.rs.utilities.loaders.Censor;

public class ChatMessage {

	private String message;
	private String filteredMessage;

	public ChatMessage(String message) {
		if (!(this instanceof QuickChatMessage)) {
			filteredMessage = Censor.getFilteredMessage(message);
			this.message = TextUtils.fixChatMessage(message);
		} else
			this.message = message;
	}

	public String getMessage(boolean filtered) {
		return filtered ? filteredMessage : message;
	}
	
	/**
	 * Gets the Message Icon (Crown/Icon) for chat messages, interface displaying
	 * @return
	 */
	public static int getMessageIcon(Player player) {
		return player.getDetails().getRights() == Rights.ADMINISTRATOR ? 2 : player.getDetails().getRights() == Rights.MODERATOR ? 1 : 0;
	}
}
