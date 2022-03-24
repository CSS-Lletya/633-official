package com.rs.cache.loaders;

import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.io.InputStream;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public final class VarBitDefinitions {

	static Object2ObjectArrayMap<Integer, VarBitDefinitions> varpbitDefs = new Object2ObjectArrayMap<>();

	public int id;
	public int baseVar;
	public int startBit;
	public int endBit;

	public static final void main(String[] args) throws IOException {
		Cache.init();
		System.out.println(
				"There are currently: " + Cache.STORE.getIndexes()[22].getLastArchiveId() * 0x3ff + " bitConfigs.");
		// List<BitConfigDefinitions> configs = new
		// ArrayList<BitConfigDefinitions>();
		for (int i = 0; i < Cache.STORE.getIndexes()[22].getLastArchiveId() * 0x3ff; i++) {
			VarBitDefinitions cd = getClientVarpBitDefinitions(i);
			if (cd.baseVar == 563) {
				System.out.println("BitConfig: " + i + ", from bitshift:" + cd.startBit + ", till bitshift: "
						+ cd.endBit + ", " + cd.baseVar);
			}
		}
	}

	public static final VarBitDefinitions getClientVarpBitDefinitions(int id) {
		VarBitDefinitions script = varpbitDefs.get(id);
		if (script != null)// open new txt document
			return script;
		byte[] data = Cache.STORE.getIndexes()[22].getFile(id >>> 1416501898, id & 0x3ff);
		script = new VarBitDefinitions();
		script.id = id;
		if (data != null)
			script.readValueLoop(new InputStream(data));
		varpbitDefs.put(id, script);
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
