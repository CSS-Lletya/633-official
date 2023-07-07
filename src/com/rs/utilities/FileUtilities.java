package com.rs.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Collectors;

import com.rs.cache.loaders.ItemDefinitions;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

public class FileUtilities {

    public static boolean exists(String name) {
        return new File(name).exists();
    }

	public static ByteBuffer fileBuffer(String name) throws IOException {
		File file = new File(name);
		if (!file.exists())
			return null;
		try (FileInputStream in = new FileInputStream(name); FileChannel channel = in.getChannel()) {
			long fileSize = channel.size();
			ByteBuffer buffer = ByteBuffer.allocateDirect((int) fileSize);
			int bytesRead = channel.read(buffer);
			while (bytesRead != -1 && buffer.hasRemaining()) {
				bytesRead = channel.read(buffer);
			}
			buffer.flip();
			return buffer;
		}
	}

    public static void writeBufferToFile(String name, ByteBuffer buffer) throws IOException {
    	File file = new File(name);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream out = new FileOutputStream(name);
             FileChannel channel = out.getChannel()) {
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }
    } 

    public static ItemDefinitions forName(String name) {
        return ItemDefinitions.itemsDefinitions.object2ObjectEntrySet().stream()
                .map(Object2ObjectMap.Entry::getValue)
                .filter(definition -> definition.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static LinkedList<String> readFile(String directory) throws IOException {
    	try (BufferedReader reader = Files.newBufferedReader(Paths.get(directory))) {
            return reader.lines()
                    .collect(Collectors.toCollection(LinkedList::new));
        }
    }
}