package com.rs.game.item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;

import lombok.SneakyThrows;

/**
 * Item weights according to the standards of 2010 runescape. All available
 * weight reductions has been added.
 * 
 * @author Dennis
 *
 */
public class ItemWeights {

	/**
	 * The Item Weights map
	 */
	private final static HashMap<Integer, Double> itemWeights = new HashMap<Integer, Double>();

	/**
	 * The path to the item weights file
	 */
	private final static String UNPACKED_PATH = "data/items/weights.txt";

	/**
	 * Loads the Item weights data
	 */
	public static final void init() {
		load();
	}

	/**
	 * Gets the Item weight itself and checks if it's being equiped (for possible
	 * reduction effect)
	 * 
	 * @param item
	 * @param equiped
	 * @return
	 */
	public static double getWeight(Item item, boolean equiped) {
		Double weight = itemWeights.get(item.getId());
		if (weight == null || item.getDefinitions().isNoted())
			return 0;
		return weight < 0.0 ? equiped ? weight : 0 : weight;
	}

	/**
	 * Populates the Item weights map and loads the data.
	 */
	@SneakyThrows(IOException.class)
	private static void load() {
		LogUtility.log(LogType.INFO, "Loading Item weights.");
		BufferedReader in = new BufferedReader(new FileReader(UNPACKED_PATH));
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
				throw new RuntimeException("Invalid list for item weight line: " + line);
			}
			int itemId = Integer.valueOf(splitedLine[0]);
			double weight = Double.valueOf(splitedLine[1]);
			itemWeights.put(itemId, weight);
		}
		in.close();
	}
}