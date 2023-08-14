package com.rs.cache.loaders;

import com.rs.cache.Cache;
import com.rs.io.InputStream;

import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public final class VarBitDefinitions {

	static Short2ObjectOpenHashMap<VarBitDefinitions> varpbitDefs = new Short2ObjectOpenHashMap<>();

	public int id;
	public int baseVar;
	public int startBit;
	public int endBit;

	public static final VarBitDefinitions getClientVarpBitDefinitions(int id) {
		VarBitDefinitions script = varpbitDefs.get((short) id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.STORE.getIndexes()[22].getFile(id >>> 1416501898, id & 0x3ff);
		script = new VarBitDefinitions();
		script.id = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		varpbitDefs.put((short) id, script);
		return script;

	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			baseVar = stream.readUnsignedShort();
			startBit = stream.readUnsignedByte();
			endBit = stream.readUnsignedByte();
		}
	}

	private VarBitDefinitions() {

	}
}
