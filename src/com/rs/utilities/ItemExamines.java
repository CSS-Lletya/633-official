package com.rs.utilities;

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
import java.util.HashMap;

import com.rs.game.item.Item;

public class ItemExamines {

	private final static HashMap<Integer, String> itemExamines = new HashMap<Integer, String>();
	private final static String PACKED_PATH = "data/items/packedExamines.e";
	private final static String UNPACKED_PATH = "data/items/unpackedExamines.txt";

	public static final String getExamine(Item item) {
		if (item.getDefinitions().isNoted())
			return "Swap this note at any bank for the equivalent item.";
		return itemExamines.get(item.getId());
	}

	public static final void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedItemExamines();
		else
			loadUnpackedItemExamines();
	}

	private static void loadPackedItemExamines() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining())
				itemExamines.put(buffer.getShort() & 0xffff, readAlexString(buffer));
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private static void loadUnpackedItemExamines() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(UNPACKED_PATH));
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				line = line.replace("﻿", "");
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length < 2)
					throw new RuntimeException("Invalid list for item examine line: " + line);
				int itemId = Integer.valueOf(splitedLine[0]);
				if (splitedLine[1].length() > 255)
					continue;
				out.writeShort(itemId);
				writeAlexString(out, splitedLine[1]);
				itemExamines.put(itemId, splitedLine[1]);
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

	public static final String getGEExamine(Item item) {
		if (item.getDefinitions().isNoted())
			item.setId(item.getDefinitions().getCertId());
		String examine = itemExamines.get(item.getId());
		if (examine != null)
			return examine;
		return "It's an " + item.getDefinitions().getName() + ".";
	}
}