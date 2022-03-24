package com.rs.cache.loaders;

import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.utilities.TextUtils;
import com.rs.utilities.Utility;
import com.rs.utilities.loaders.MusicHints;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public final class ClientScriptMap {

	@SuppressWarnings("unused")
	private char aChar6337;
	@SuppressWarnings("unused")
	private char aChar6345;
	private String defaultStringValue;
	private int defaultIntValue;
	private Object2ObjectArrayMap<Long, Object> values;
	
	static Object2ObjectArrayMap<Integer, ClientScriptMap> interfaceScripts = new Object2ObjectArrayMap<>();

	public static void main(String[] args) throws IOException {
		// Cache.STORE = new Store("C:/.jagex_cache_32/runescape/");
		Cache.init();
		MusicHints.init();
		ClientScriptMap names = ClientScriptMap.getMap(1345);
		ClientScriptMap hint1 = ClientScriptMap.getMap(952);
		// ClientScriptMap hint2 = ClientScriptMap.getMap(1349);
		System.out.println(hint1);
		for (Object v : names.values.values()) {
			int key = (int) ClientScriptMap.getMap(1345).getKeyForValue(v);
			int id = ClientScriptMap.getMap(1351).getIntValue(key);

			/*
			 * String text = hint.getStringValue(key); if(text.equals("automatically."))
			 * System.out.println(id);
			 */
			String hint = MusicHints.getHint(id);/*
													 * hint1.getValues().containsKey ((long) key) ? hint1
													 * .getStringValue(key) : hint2.getStringValue(key);
													 */

			System.out.println(id + ", " + v + "; " + hint + ", ");
		}
	}

	public static final ClientScriptMap getMap(int scriptId) {
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

	public Object2ObjectArrayMap<Long, Object> getValues() {
		return values;
	}

	public Object getValue(long key) {
		if (values == null)
			return null;
		return values.get(key);
	}

	public long getKeyForValue(Object value) {
		for (Long key : values.keySet()) {
			if (values.get(key).equals(value))
				return key;
		}
		return -1;
	}

	public int getSize() {
		if (values == null)
			return 0;
		return values.size();
	}

	public int getIntValue(long key) {
		if (values == null)
			return defaultIntValue;
		Object value = values.get(key);
		if (value == null || !(value instanceof Integer))
			return defaultIntValue;
		return (Integer) value;
	}

	public int getKeyIndex(long key) {
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

	public String getStringValue(long key) {
		if (values == null)
			return defaultStringValue;
		Object value = values.get(key);
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
				values = new Object2ObjectArrayMap<Long, Object>(Utility.getHashMapSize(count));
			for (int i = 0; i < loop; i++) {
				int key = opcode == 7 || opcode == 8 ? stream.readUnsignedShort() : stream.readInt();
				Object value = opcode == 5 || opcode == 7 ? stream.readString() : stream.readInt();
				values.put((long) key, value);
			}
		}
	}

	private ClientScriptMap() {
		defaultStringValue = "null";
	}
}
