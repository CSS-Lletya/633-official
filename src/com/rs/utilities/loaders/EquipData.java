package com.rs.utilities.loaders;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

public class EquipData {

	public static final byte SLOT = 0, TYPE = 1;

	private final static Short2ObjectOpenHashMap<short[]> equipData = new Short2ObjectOpenHashMap<>();
	private final static String PACKED_PATH = "data/items/packedEquipData.e";
	private final static String UNPACKED_PATH = "data/items/unpackedEquipData.txt";

	public static final void init() {
		if (new File(PACKED_PATH).exists())
			loadUnpackedEquips();
		else
			loadPackedEquips();
	}

	private static void loadUnpackedEquips() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				short id = (short) (buffer.getShort() & 0xffff);
				short slot = buffer.get();
				short type = buffer.get();
				equipData.put(id, new short[] { slot, type });
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void loadPackedEquips() {
		try {
			BufferedReader in = new BufferedReader(
					new FileReader(UNPACKED_PATH));
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					PACKED_PATH));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length < 2) {
					in.close();
					out.close();
					throw new RuntimeException(
							"Invalid list for equip data line: " + line);
				}

				String[] splitedLine2 = splitedLine[1].split(" ", 3);

				short id = Short.valueOf(splitedLine[0]);
				short slot = Short.valueOf(splitedLine2[0]);
				short type = Short.valueOf(splitedLine2[1]);
				out.writeShort(id);
				out.writeByte(slot);
				out.writeByte(type);
				equipData.put(id, new short[] { slot, type });

			}

			in.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean canEquip(int id) {
		if (equipData.get((short) id) != null)
			return true;
		return false;
	}

	public static int getEquipSlot(int id) {
		if (equipData.get((short) id) == null)
			return -1;
		return equipData.get((short) id)[SLOT];
	}

	public static int getEquipType(int id) {
		if (equipData.get((short) id) == null)
			return -1;
		return equipData.get((short) id)[TYPE];
	}

}