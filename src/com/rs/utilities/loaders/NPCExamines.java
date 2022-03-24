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

import com.rs.game.npc.NPC;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Cleanup;
import lombok.SneakyThrows;

public class NPCExamines {

	private final static Object2ObjectOpenHashMap<Integer, String> npcExamines = new Object2ObjectOpenHashMap<Integer, String>();
	private final static String PACKED_PATH = "data/npcs/packedExamines.e";
	private final static String UNPACKED_PATH = "data/npcs/unpackedExamines.txt";

	public static final void init() {
		if (new File(PACKED_PATH).exists())
			loadPackeddNPCExamines();
		else
			loadUnpackedNPCExamines();
	}

	public static final String getExamine(NPC npc) {
		String examine = npcExamines.get(npc.getId());
		if (examine != null)
			return examine;
		return "It's a " + npc.getDefinitions().getName() + ".";
	}

	@SneakyThrows(Throwable.class)
	private static void loadPackeddNPCExamines() {
		@Cleanup
		RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
		@Cleanup
		FileChannel channel = in.getChannel();
		ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
		while (buffer.hasRemaining())
			npcExamines.put(buffer.getShort() & 0xffff, readAlexString(buffer));
	}

	private static void loadUnpackedNPCExamines() {
		LogUtility.log(LogType.INFO, "Packing npc examines...");
		Try.run(() -> {
			@Cleanup
			BufferedReader in = new BufferedReader(new FileReader(UNPACKED_PATH));
			@Cleanup
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				line = line.replace("﻿", "");
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length < 2) {
					in.close();
					throw new RuntimeException("Invalid list for npc examine line: " + line);
				}
				int npcId = Integer.valueOf(splitedLine[0]);
				if (splitedLine[1].length() > 255)
					continue;
				out.writeShort(npcId);
				writeAlexString(out, splitedLine[1]);
				npcExamines.put(npcId, splitedLine[1]);
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
