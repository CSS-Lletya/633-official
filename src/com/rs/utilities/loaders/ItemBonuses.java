package com.rs.utilities.loaders;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Cleanup;
import lombok.SneakyThrows;

public final class ItemBonuses {

	static Object2ObjectOpenHashMap<Integer, int[]> itemBonuses = new Object2ObjectOpenHashMap<>();
	private final static String PACKED_PATH = "data/items/bonuses.ib";

	public static final void init() {
		if (new File(PACKED_PATH).exists())
			loadItemBonuses();
		else
			throw new RuntimeException("Missing item bonuses.");
	}

	public static final int[] getItemBonuses(int itemId) {
		return itemBonuses.get(itemId);
	}

	@SneakyThrows(Throwable.class)
	private static final void loadItemBonuses() {
		@Cleanup
		RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
		FileChannel channel = in.getChannel();
		ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		itemBonuses = new Object2ObjectOpenHashMap<Integer, int[]>(buffer.remaining() / 38);
		while (buffer.hasRemaining()) {
			int itemId = buffer.getShort() & 0xffff;
			int[] bonuses = new int[18];
			for (int index = 0; index < bonuses.length; index++) {
				bonuses[index] = buffer.getShort();

			}
			itemBonuses.put(itemId, bonuses);
		}
	}

	private ItemBonuses() {

	}

}
