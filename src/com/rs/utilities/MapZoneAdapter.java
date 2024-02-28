package com.rs.utilities;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.rs.game.map.zone.MapZone;

public class MapZoneAdapter implements JsonSerializer<MapZone>, JsonDeserializer<MapZone> {

    @Override
    public JsonElement serialize(MapZone src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("package", new JsonPrimitive(src.getClass().getName().toString()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public MapZone deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("package").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            return context.deserialize(element,
                    Class.forName(type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}