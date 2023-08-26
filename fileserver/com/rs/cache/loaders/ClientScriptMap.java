package com.rs.cache.loaders;

import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.utilities.TextUtils;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public final class ClientScriptMap {

	@SuppressWarnings("unused")
	private char aChar6337;
	@SuppressWarnings("unused")
	private char aChar6345;
	private String defaultStringValue;
	private int defaultIntValue;
	private Short2ObjectOpenHashMap<Object> values;
	
	static Short2ObjectOpenHashMap<ClientScriptMap> interfaceScripts = new Short2ObjectOpenHashMap<>();

	public static final ClientScriptMap getMap(short scriptId) {
		ClientScriptMap script = interfaceScripts.get(scriptId);
		if (script != null)
			return script;
		byte[] data = Cache.STORE.getIndexes()[17].getFile(scriptId >>> 0xba9ed5a8, scriptId & 0xff);
		script = new ClientScriptMap();
		if (data != null)
			script.readValueLoop(new InputStream(data));
		interfaceScripts.put(scriptId, script);
		return script;

	}

	public int getDefaultIntValue() {
		return defaultIntValue;
	}

	public String getDefaultStringValue() {
		return defaultStringValue;
	}

	public Short2ObjectOpenHashMap<Object> getValues() {
		return values;
	}

	public Object getValue(short key) {
		if (values == null)
			return null;
		return values.get(key);
	}

	public long getKeyForValue(Object value) {
		for (Short key : values.keySet()) {
			if (values.get((short) key).equals(value))
				return key;
		}
		return -1;
	}

	public int getSize() {
		if (values == null)
			return 0;
		return values.size();
	}

	public int getIntValue(int key) {
		if (values == null)
			return defaultIntValue;
		Object value = values.get((short) key);
		if (value == null || !(value instanceof Integer))
			return defaultIntValue;
		return (Integer) value;
	}

	public int getKeyIndex(short key) {
		if (values == null)
			return -1;
		int i = 0;
		for (long k : values.keySet()) {
			if (k == key)
				return i;
			i++;
		}
		return -1;
	}

	public int getIntValueAtIndex(int i) {
		if (values == null)
			return -1;
		return (int) values.values().toArray()[i];
	}

	public String getStringValue(int key) {
		if (values == null)
			return defaultStringValue;
		Object value = values.get((short) key);
		if (value == null || !(value instanceof String))
			return defaultStringValue;
		return (String) value;
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
		if (opcode == 1)
			aChar6337 = TextUtils.method2782((byte) stream.readByte());
		else if (opcode == 2)
			aChar6345 = TextUtils.method2782((byte) stream.readByte());
		else if (opcode == 3)
			defaultStringValue = stream.readString();
		else if (opcode == 4)
			defaultIntValue = stream.readInt();
		else if (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 8) {
			int count = stream.readUnsignedShort();
			int loop = opcode == 7 || opcode == 8 ? stream.readUnsignedShort() : count;
			if (values == null)
				values = new Short2ObjectOpenHashMap<Object>(Utility.getHashMapSize(count));
			for (int i = 0; i < loop; i++) {
				int key = opcode == 7 || opcode == 8 ? stream.readUnsignedShort() : stream.readInt();
				Object value = opcode == 5 || opcode == 7 ? stream.readString() : stream.readInt();
				values.put((short) key, value);
			}
		}
	}

	private ClientScriptMap() {
		defaultStringValue = "null";
	}
}
