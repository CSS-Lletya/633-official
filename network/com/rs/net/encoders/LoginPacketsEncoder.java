package com.rs.net.encoders;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.game.player.Player;
import com.rs.io.OutputStream;
import com.rs.net.Session;

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
}
