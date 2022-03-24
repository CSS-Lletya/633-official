package com.rs.utilities.loaders;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Cleanup;
import lombok.SneakyThrows;

public final class MapArchiveKeys {

	private final static Object2ObjectOpenHashMap<Integer, int[]> keys = new Object2ObjectOpenHashMap<Integer, int[]>();
	private final static String PACKED_PATH = "data/map/archiveKeys/packed.mcx";

	public static final int[] getMapKeys(int regionId) {
		return keys.get(regionId);
	}

	public static void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedKeys();
		else
			loadUnpackedKeys();
	}

	@SneakyThrows(Throwable.class)
	private static final void loadPackedKeys() {
		@Cleanup
		RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
		FileChannel channel = in.getChannel();
		ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		while (buffer.hasRemaining()) {
			int regionId = buffer.getShort() & 0xffff;
			int[] xteas = new int[4];
			for (int index = 0; index < 4; index++)
				xteas[index] = buffer.getInt();
			keys.put(regionId, xteas);
		}
	}

	public static final void loadUnpackedKeys() {
		LogUtility.log(LogType.INFO, "Packing map containers xteas...");
		Try.run(() -> {
			@Cleanup
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			File unpacked = new File("data/map/archiveKeys/unpacked/");
			File[] xteasFiles = unpacked.listFiles();
			for (File region : xteasFiles) {
				String name = region.getName();
				if (!name.contains(".txt")) {
					region.delete();
					continue;
				}
				int regionId = Short.parseShort(name.replace(".txt", ""));
				if (regionId <= 0) {
					region.delete();
					continue;
				}
				@Cleanup
				BufferedReader in = new BufferedReader(new FileReader(region));
				out.writeShort(regionId);
				final int[] xteas = new int[4];
				for (int index = 0; index < 4; index++) {
					xteas[index] = Integer.parseInt(in.readLine());
					out.writeInt(xteas[index]);
				}
				keys.put(regionId, xteas);
			}
		});
	}
}