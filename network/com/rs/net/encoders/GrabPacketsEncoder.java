package com.rs.net.encoders;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.GameConstants;
import com.rs.cache.Cache;
import com.rs.io.OutputStream;
import com.rs.net.Session;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class GrabPacketsEncoder extends Encoder {

	private byte[] UKEYS_FILE;

	private int encryptionValue;

	public final ChannelBuffer getUkeysFile() {
		if (UKEYS_FILE == null)
			UKEYS_FILE = Cache.generateUkeysFile();
		return getContainerPacketData(255, 255, UKEYS_FILE);
	}

	public GrabPacketsEncoder(Session connection) {
		super(connection);
	}

	public final void sendOutdatedClientPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(6);
		ChannelFuture future = session.write(stream);
		if (future != null)
			future.addListener(ChannelFutureListener.CLOSE);
		else
			session.getChannel().close();
	}

	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(
				1 + GameConstants.GRAB_SERVER_KEYS.length * 4);
		stream.writeByte(0);
		for (int key : GameConstants.GRAB_SERVER_KEYS)
			stream.writeInt(key);
		session.write(stream);
	}

	public final void sendCacheArchive(int indexId, int containerId,
			boolean priority) {
		if (indexId == 255 && containerId == 255)
			session.write(getUkeysFile());
		else {
			session.write(getArchivePacketData(indexId, containerId, priority));
		}
	}

	public final ChannelBuffer getArchivePacketData(int indexId, int archiveId,
			boolean priority) {
		byte[] archive = (indexId == 255 ? Cache.STORE.getIndex255()
				: Cache.STORE.getIndexes()[indexId].getMainFile())
				.getArchiveData(archiveId);
		if (archive == null)
			return null;

		int compression = archive[0] & 0xff;
		int length = ((archive[1] & 0xff) << 24) + ((archive[2] & 0xff) << 16)
				+ ((archive[3] & 0xff) << 8) + (archive[4] & 0xff);
		int settings = compression;
		if (!priority)
			settings |= 0x80;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(indexId);
		buffer.writeShort(archiveId);
		buffer.writeByte(settings);
		buffer.writeInt(length);
		int realLength = compression != 0 ? length + 4 : length;
		for (int index = 5; index < realLength + 5; index++) {
			if (buffer.writerIndex() % 512 == 0) {
				buffer.writeByte(255);
			}
			buffer.writeByte(archive[index]);
		}
		int v = encryptionValue;
		if (v != 0) {
			for (int i = 0; i < buffer.arrayOffset(); i++)
				buffer.setByte(i, buffer.getByte(i) ^ v);
		}
		return buffer;
	}

	/*
	 * only using for ukeys atm, doesnt allow keys encode
	 */
	public static final ChannelBuffer getContainerPacketData(int indexFileId,
			int containerId, byte[] archive) {
		ChannelBuffer stream = ChannelBuffers.dynamicBuffer(archive.length + 4);
		stream.writeByte(indexFileId);
		stream.writeShort(containerId);
		stream.writeByte(0);
		stream.writeInt(archive.length);
		for (int index = 0; index < archive.length; index++) {
			if (stream.writerIndex() % 512 == 0) {
				stream.writeByte(255);
			}
			stream.writeByte(archive[index]);
		}
		return stream;
	}
}