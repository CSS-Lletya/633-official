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
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import lombok.Cleanup;
import lombok.SneakyThrows;

public final class NPCBonuses {
	
	private final static Double2ObjectOpenHashMap<double[]> npcBonuses = new Double2ObjectOpenHashMap<>();
	private static final String PACKED_PATH = "data/npcs/packedBonuses.nb";

	public static void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedNPCBonuses();
		else
			loadUnpackedNPCBonuses();
	}

	public static double[] getBonuses(int id) {
		return npcBonuses.get(id);
	}

	@SneakyThrows(Throwable.class)
	private static void loadUnpackedNPCBonuses() {
		LogUtility.log(LogType.INFO, "Packing npc bonuses...");
		Try.run(() -> {
			@Cleanup
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			@Cleanup
			BufferedReader in = new BufferedReader(new FileReader("data/npcs/unpackedBonuses.txt"));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2) {
					in.close();
					out.close();
					throw new RuntimeException("Invalid NPC Bonuses line: " + line);
				}
				short npcId = Short.parseShort(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 10);
				if (splitedLine2.length != 10) {
					in.close();
					out.close();
					throw new RuntimeException("Invalid NPC Bonuses line: " + line);
				}
				double[] bonuses = new double[10];
				//	out.writeShort(npcId);
					for (int i = 0; i < bonuses.length; i++) {
						bonuses[i] = Integer.parseInt(splitedLine2[i]);
						//out.writeShort(bonuses[i]);
					}
				npcBonuses.put(npcId, bonuses);
			}
		});
	}

	private static void loadPackedNPCBonuses() {
		Try.run(() -> {
			@Cleanup
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				short npcId = (short) (buffer.getShort() & 0xffff);
				double[] bonuses = new double[10];
				for (int i = 0; i < bonuses.length; i++)
					bonuses[i] = buffer.getShort();
				npcBonuses.put(npcId, bonuses);
			}
		});
	}
}