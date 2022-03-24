package com.alex.util.crc32;

import java.util.zip.CRC32;

import lombok.Synchronized;

public final class CRC32HGenerator {

	public static final CRC32 CRC32Instance = new CRC32();

	public static int getHash(byte[] data) {
		return getHash(data, 0, data.length);
	}

	@Synchronized("CRC32Instance")
	public static int getHash(byte[] data, int offset, int length) {
		CRC32Instance.update(data, offset, length);
		int hash = (int) CRC32Instance.getValue();
		CRC32Instance.reset();
		return hash;
	}
}