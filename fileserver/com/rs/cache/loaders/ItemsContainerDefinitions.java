package com.rs.cache.loaders;

import java.util.concurrent.ConcurrentHashMap;

import com.rs.cache.Cache;
import com.rs.io.InputStream;

public final class ItemsContainerDefinitions {

	@SuppressWarnings("unused")
	private int length;
	private int[] ids, amounts;

	private static final ConcurrentHashMap<Integer, ItemsContainerDefinitions> maps = new ConcurrentHashMap<Integer, ItemsContainerDefinitions>();

	public static final ItemsContainerDefinitions getContainer(int id) {
		ItemsContainerDefinitions def = maps.get(id);
		if (def != null)
			return def;
		byte[] data = Cache.STORE.getIndexes()[2].getFile(5, id);
		def = new ItemsContainerDefinitions();
		if (data != null)
			def.decode(new InputStream(data));
		maps.put(id, def);
		return def;
	}

	private void decode(InputStream stream) {
		l: while (true) {
			switch (stream.readUnsignedByte()) {
			case 2:
				length = stream.readUnsignedShort();
				break;
			case 4:
				int size = stream.readUnsignedByte();
				ids = new int[size];
				amounts = new int[size];
				for (int i = 0; i < size; i++) {
					ids[i] = stream.readUnsignedShort();
					amounts[i] = stream.readUnsignedShort();
				}
				break;
			default:
				break l;
			}
		}
	}

}
