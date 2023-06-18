package com.rs.net.encoders;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.game.player.Player;
import com.rs.io.OutputStream;
import com.rs.net.Session;
import com.rs.utilities.Utility;

public final class LoginPacketsEncoder extends Encoder {

	public LoginPacketsEncoder(Session connection) {
		super(connection);
	}

	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(0);
		session.write(stream);
	}

	public final void sendClientPacket(int opcode) {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(opcode);
		ChannelFuture future = session.write(stream);
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			session.getChannel().close();
		}
	}

	public final void sendLoginDetails(Player player) {
		OutputStream bldr = new OutputStream(11);
		bldr.writeByte(2);
		bldr.writeByte(13); // length
		bldr.writeByte(player.getDetails().getRights().getValue());
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(1);
		bldr.writeByte(0);
		bldr.writeShort(player.getIndex());
		bldr.writeByte(1);
		bldr.write24BitInteger(0);
		bldr.writeByte(1); // members
		session.write(bldr);
	}
	
	 public void sendLobbyDetails(Player player) {
	    	OutputStream bldr = new OutputStream();
	    	bldr.writePacketVarByte(player, 2);
	        bldr.writeByte((byte) 0);
	        bldr.writeByte((byte) 0);
	        bldr.writeByte((byte) 0);
	        bldr.writeByte((byte) 0);
	        bldr.writeByte((byte) 0);
	        bldr.writeShort(38); // member days left
	        bldr.writeShort(1); // recovery questions
	        bldr.writeShort(2); // unread messages
	        bldr.writeShort(3229 - 4209); // 3229 - lastDays
	        int ipHash = Utility.IPAddressToNumber(session.getIP());
	        bldr.writeInt(ipHash); // last ip
	        bldr.writeByte((byte) 1); // email status (0 - no email, 1 - pending
	        // parental confirmation, 2 - pending
	        // confirmation, 3 - registered)
	        bldr.writeShort(0);
	        bldr.writeShort(0);
	        bldr.writeByte((byte) 0);
	        bldr.writeGJString(player.getUsername());
	        bldr.writeByte((byte) 0);
	        bldr.writeInt(1);
	        bldr.writeShort(1); // current world id
	        bldr.writeGJString(session.getIP());
	        bldr.endPacketVarByte();
	        player.getSession().write(bldr);
	}
}
