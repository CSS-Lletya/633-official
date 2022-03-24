package com.rs.utilities.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.SneakyThrows;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 1, 2014
 */
public abstract class GsonLoader<T> {

	/**
	 * Loads up all of the data
	 */
	public abstract void initialize();

	/**
	 * Gets the location of the file to load from
	 *
	 * @return
	 */
	public abstract String getFileLocation();

	/**
	 * Populates a list with all of the data in the {@link #getFileLocation()}
	 *
	 * @return The list with data
	 */
	protected abstract ObjectArrayList<T> load();

	/**
	 * Generates a list, if there are no elements in {@link #load()}, it will be
	 * null, so this will make sure there is no null return type from
	 * {@link #load()}
	 */
	public synchronized ObjectArrayList<T> generateList() {
		ObjectArrayList<T> list = load();
		if (list == null) {
			list = new ObjectArrayList<>();
		}
		return (ObjectArrayList<T>) Collections.synchronizedList(list);
	}

	/**
	 * Saves the data to the file
	 *
	 * @param data
	 *            The list to save
	 */
	@SneakyThrows(Exception.class)
	public void save(ObjectArrayList<T> data) {
		File file = new File(getFileLocation());
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileWriter fw = new FileWriter(getFileLocation());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(gson.toJson(data));
		bw.close();
	}

	/**
	 * The gson instance
	 */
	protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
}
