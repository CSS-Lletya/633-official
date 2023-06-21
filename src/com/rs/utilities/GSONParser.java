package com.rs.utilities;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rs.content.quests.Quest;
import com.rs.game.player.Player;

import lombok.SneakyThrows;

/**
* @author Melvin 27 jan. 2020
* @project Game
* 
*/

public class GSONParser {

	private static Gson GSON;

	static {
		GSON = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(Quest.class, new QuestJsonAdapter())
                .setPrettyPrinting().disableInnerClassSerialization().enableComplexMapKeySerialization()
                .setDateFormat(DateFormat.LONG).setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
	}

	@SneakyThrows(IOException.class)
	public static Player load(String dir, Type type) {
		try (Reader reader = Files.newBufferedReader(Paths.get(dir))) {
			return GSON.fromJson(reader, type);
		}
	}

	public static void save(Object src, String dir, Type type) {
		try (Writer writer = Files.newBufferedWriter(Paths.get(dir))) {
			writer.write(GSON.toJson(src, type));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    public static Object loadFile(String dir, Type type) {
        try (Reader reader = Files.newBufferedReader(Paths.get(dir))) {
            return GSON.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}