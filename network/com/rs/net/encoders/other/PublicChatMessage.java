package com.rs.net.encoders.other;

import com.rs.game.map.World;
import com.rs.game.player.Player;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class PublicChatMessage extends ChatMessage {

	@Getter
	@Setter
	private int effects;

	public PublicChatMessage(String message, int effects) {
		super(message);
		setEffects(effects);
	}

	@NonNull
	public void sendPublicChatMessage(Player player, PublicChatMessage message) {
		player.getMapRegionsIds().stream().filter(regionalPlayer -> regionalPlayer != null).forEach(regionalPlayer -> {
			ObjectArrayList<Short> playersIndexes = World.getRegion(regionalPlayer).getPlayersIndexes();
			playersIndexes.iterator().forEachRemaining(p -> {
				World.players().filter(
						playerIndex -> playerIndex.getLocalPlayerUpdate().getLocalPlayers()[player.getIndex()] != null)
						.forEach(worldPlayer -> worldPlayer.getPackets().sendPublicMessage(player, message));
			});
		});
	}
}