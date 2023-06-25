package com.rs.game.item;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public class ItemWeights {

	private final static HashMap<Integer, Double> itemWeights = new HashMap<Integer, Double>();
	private final static String PACKED_PATH = "data/items/packedWeights.dat";

	public static final void init() {
		loadPackedItemWeights();
	}

	public static final double getWeight(Item item, boolean equiped) {
		Double weight = itemWeights.get(item.getId());
		if (weight == null || item.getDefinitions().isNoted())
			return 0;
		return weight < 0.0 ? equiped ? weight : 0 : weight;
	}

	private static void loadPackedItemWeights() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining())
				itemWeights.put(buffer.getShort() & 0xffff, buffer.getDouble());
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}