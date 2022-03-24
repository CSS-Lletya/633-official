package com.rs.utilities.json;

import java.io.FileReader;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The utility class that provides functions for parsing {@code .json} files.
 * @author lare96 <http://github.com/lare96>
 */
public abstract class JsonLoader implements Runnable {
	
	/**
	 * The path to the {@code .json} file being parsed.
	 */
	private final String path;
	
	/**
	 * Creates a new {@link JsonLoader}.
	 * @param path the path to the {@code .json} file being parsed.
	 */
	public JsonLoader(String path) {
		this.path = path;
	}
	
	@Override
	public void run() {
		load();
	}
	
	/**
	 * Initializes the loader with the size of the objects array.
	 * @param size the object array size
	 */
	protected void initialize(int size) {
	}
	
	/**
	 * A dynamic method that allows the user to read and modify the parsed data.
	 * @param reader the reader for retrieving the parsed data.
	 * @param builder the builder for retrieving the parsed data.
	 */
	public abstract void load(JsonObject reader, Gson builder);
	
	/**
	 * Loads the parsed data. How the data is loaded is defined by
	 * {@link JsonLoader#load(JsonObject, Gson)}.
	 * @return the loader instance, for chaining.
	 */
	public final JsonLoader load() {
		start();
		try(FileReader in = new FileReader(Paths.get(path).toFile())) {
			JsonParser parser = new JsonParser();
			JsonArray array = (JsonArray) parser.parse(in);
			Gson builder = new GsonBuilder().create();
			
			initialize(array.size());
			for(int i = 0; i < array.size(); i++) {
				JsonObject reader = (JsonObject) array.get(i);
				load(reader, builder);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		end();
		return this;
	}
	
	/**
	 * A method called on the start of this loading.
	 */
	public void start() {

	}
	
	/**
	 * A method called on the end of this loading.
	 */
	public void end() {

	}
}