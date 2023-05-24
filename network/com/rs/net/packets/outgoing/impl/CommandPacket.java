package com.rs.net.packets.outgoing.impl;

import com.rs.GameConstants;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.packets.outgoing.OutgoingPacketListener;
import com.rs.net.packets.outgoing.OutgoingPacketSignature;
import com.rs.plugin.CommandPluginDispatcher;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

@OutgoingPacketSignature(packetId = 28, description = "A command that the Player is sending to the client")
public class CommandPacket implements OutgoingPacketListener {

	@Override
	public void execute(Player player, InputStream stream) {
		if (!player.isRunning())
			return;
		boolean clientCommand = stream.readUnsignedByte() == 1;
		boolean console = stream.readUnsignedByte() == 1;
		String command = stream.readString();
		
		if (!processCommand(player, command, console, clientCommand) && GameConstants.DEBUG)
			LogUtility.log(LogType.INFO, "Command: " + command);
	}
	
	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
		if (command.length() == 0)
			return false;
		String[] cmd = command.toLowerCase().split(" ");
		if (cmd.length == 0)
			return false;
		if (clientCommand) {
			switch (cmd[0]) {
			case "tele":
				cmd = cmd[1].split(",");
				int plane = Integer.valueOf(cmd[0]);
				int x = Integer.valueOf(cmd[1]) << 6 | Integer.valueOf(cmd[3]);
				int y = Integer.valueOf(cmd[2]) << 6 | Integer.valueOf(cmd[4]);
				player.setNextWorldTile(new WorldTile(x, y, plane));
				return true;
			}
		}
		CommandPluginDispatcher.execute(player, cmd, command);
		return false;
	}
}