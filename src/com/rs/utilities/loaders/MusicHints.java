package com.rs.utilities.loaders;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Cleanup;
import lombok.SneakyThrows;

public class MusicHints {

	private final static Object2ObjectOpenHashMap<Integer, String> musicHints = new Object2ObjectOpenHashMap<Integer, String>();
	private final static String PACKED_PATH = "data/musics/packedMusicHints.mh";
	private final static String UNPACKED_PATH = "data/musics/unpackedMusicHints.txt";

	public static void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedItemExamines();
		else
			loadUnpackedItemExamines();
	}

	public static String getHint(int musicId) {
		String hint = musicHints.get(musicId);
		if (hint == null)
			return "somewhere.";
		return hint;
	}

	@SneakyThrows(Throwable.class)
	private static void loadPackedItemExamines() {
		@Cleanup
		RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
		FileChannel channel = in.getChannel();
		ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		while (buffer.hasRemaining())
			musicHints.put(buffer.getShort() & 0xffff, readAlexString(buffer));
	}

	private static void loadUnpackedItemExamines() {
		LogUtility.log(LogType.INFO, "Packing music hints...");
		Try.run(() -> {
			@Cleanup
			BufferedReader in = new BufferedReader(new FileReader(UNPACKED_PATH));
			@Cleanup
			DataOutputStream out = new DataOutputStream(Files.newOutputStream(Paths.get(PACKED_PATH)));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length < 2) {
					in.close();
					throw new RuntimeException("Invalid list for music hints line: " + line);
				}
				int musicId = Integer.parseInt(splitedLine[0]);
				if (splitedLine[1].length() > 255)
					continue;
				out.writeShort(musicId);
				writeAlexString(out, splitedLine[1]);
				musicHints.put(musicId, splitedLine[1]);
			}
		});
	}

	public static String readAlexString(ByteBuffer buffer) {
		int count = buffer.get() & 0xff;
		byte[] bytes = new byte[count];
		buffer.get(bytes, 0, count);
		return new String(bytes);
	}

	public static void writeAlexString(DataOutputStream out, String string) throws IOException {
		byte[] bytes = string.getBytes();
		out.writeByte(bytes.length);
		out.write(bytes);
	}
}
