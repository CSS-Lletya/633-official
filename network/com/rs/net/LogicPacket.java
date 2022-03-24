package com.rs.net;

import com.rs.io.InputStream;

import lombok.Data;

@Data
public class LogicPacket {

	private final int id;
	private final byte[] data;

	public LogicPacket(int id, int size, InputStream stream) {
		this.id = id;
		data = new byte[size];
		stream.getBytes(data, 0, size);
	}
}