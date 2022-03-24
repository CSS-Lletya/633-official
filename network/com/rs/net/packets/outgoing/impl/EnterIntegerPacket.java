package com.rs.net.packets.outgoing.impl;

import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacket;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.utilities.IntegerInputAction;

@OutgoingPacketSignature(packetId = 81, description = "Represents an Input of Integer values only.")
public class EnterIntegerPacket implements OutgoingPacket {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isRunning() || player.isDead())
			return;
		int value = stream.readInt();
		if (value < 0)
			return;
		
		if (player.getAttributes().getAttributes().get("integer_input_action") != null) {
			IntegerInputAction action = (IntegerInputAction) player.getAttributes().getAttributes().remove("integer_input_action");
			action.handle(value);
			return;
		}
		
		if ((player.getInterfaceManager().containsInterface(762) && player
				.getInterfaceManager().containsInterface(763))
				|| player.getInterfaceManager().containsInterface(11)) {
			Integer bank_item_X_Slot = (Integer) player
					.getAttributes().getAttributes().remove("bank_item_X_Slot");
			if (bank_item_X_Slot == null)
				return;
			player.getBank().setLastX(value);
			player.getBank().refreshLastX();
			if (player.getAttributes().getAttributes().remove("bank_isWithdraw") != null)
				player.getBank().withdrawItem(bank_item_X_Slot, value);
			else
				player.getBank()
						.depositItem(
								bank_item_X_Slot,
								value,
								player.getInterfaceManager()
										.containsInterface(11) ? false
										: true);
		} else if (player.getInterfaceManager().containsInterface(206)
				&& player.getInterfaceManager().containsInterface(207)) {
			Integer pc_item_X_Slot = (Integer) player
					.getAttributes().getAttributes().remove("pc_item_X_Slot");
			if (pc_item_X_Slot == null)
				return;
			if (player.getAttributes().getAttributes().remove("pc_isRemove") != null)
				player.getPriceCheckManager().removeItem(pc_item_X_Slot,
						value);
			else
				player.getPriceCheckManager()
						.addItem(pc_item_X_Slot, value);
		} else if (player.getInterfaceManager().containsInterface(671)
				&& player.getInterfaceManager().containsInterface(665)) {
			if (player.getFamiliar() == null
					|| player.getFamiliar().getBob() == null)
				return;
			Integer bob_item_X_Slot = (Integer) player
					.getAttributes().getAttributes().remove("bob_item_X_Slot");
			if (bob_item_X_Slot == null)
				return;
			if (player.getAttributes().getAttributes().remove("bob_isRemove") != null)
				player.getFamiliar().getBob()
						.removeItem(bob_item_X_Slot, value);
			else
				player.getFamiliar().getBob()
						.addItem(bob_item_X_Slot, value);
		} else if (player.getInterfaceManager().containsInterface(335)
				&& player.getInterfaceManager().containsInterface(336)) {
			Integer trade_item_X_Slot = (Integer) player
					.getAttributes().getAttributes().remove("trade_item_X_Slot");
			if (trade_item_X_Slot == null)
				return;
			if (player.getAttributes().getAttributes().remove("trade_isRemove") != null)
				player.getTrade().removeItem(trade_item_X_Slot, value);
			else
				player.getTrade().addItem(trade_item_X_Slot, value);
		} else if (player.getAttributes().getAttributes().remove("xformring") == Boolean.TRUE)
			player.getAppearance().transformIntoNPC(value);
	}
}