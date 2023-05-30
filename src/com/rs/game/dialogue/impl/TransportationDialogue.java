package com.rs.game.dialogue.impl;

import com.rs.game.dialogue.DialogueEventListener;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.content.ItemTeleports;

public class TransportationDialogue extends DialogueEventListener {

	
	public TransportationDialogue(Player player, Item item, boolean isWearing, String... tile) {
		super(player);
		this.item = item;
		this.isWearing = isWearing;
		this.tile = tile;
	}

	private String[] tile;
	private Item item;
	private boolean isWearing;

	@Override
	public void start() {
		player.getDialogueInterpreter().sendOptions("Where would you like to teleport to", tile);
	}

	@Override
	public void listenToDialogueEvent(int button) {
		ItemTeleports.sendTeleport(player, (isWearing ? player.getEquipment().getItems().lookup(item.getId()) : player.getInventory().getItems().lookup(item.getId())), button == 11 ? 0 : button -2, false,
				tile.length);
	}
}