package com.alex.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

import lombok.Cleanup;

public class ItemBonusesPacker {

	public static final void main(String[] args) throws IOException {
		Cache.init();
		@Cleanup
		DataOutputStream out = new DataOutputStream(new FileOutputStream("data/items/bonuses.ib"));
		for (int itemId = 0; itemId < CacheUtils.getItemDefinitionsSize(); itemId++) {
			File file = new File("data/items/bonuses/" + itemId + ".txt");
			LogUtility.log(LogType.TRACE, "Packing bonuses for item: " + itemId);
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				out.writeShort(itemId);
				reader.readLine();
				// att bonuses
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.readLine();
				// def bonuses
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.readLine();
				// Damage absorption
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.readLine();
				// Other bonuses
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				out.writeShort(Integer.valueOf(reader.readLine()));
				reader.close();

			}
		}
		LogUtility.log(LogType.INFO, "Packed item bonuses.");
	}
}