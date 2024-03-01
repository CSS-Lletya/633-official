package com.rs.utilities;

import com.google.gson.*;
import com.rs.game.npc.familiar.Familiar;

import java.lang.reflect.Type;

public class FamiliarAdapter implements JsonSerializer<Familiar>, JsonDeserializer<Familiar> {

    @Override
    public JsonElement serialize(Familiar familiar, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(familiar.getClass().getSimpleName()));
        result.add("properties", context.serialize(familiar, familiar.getClass()));
        return result;
    }

    @Override
    public Familiar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            String thePackage = "com.rs.game.npc.familiar.";
            return context.deserialize(element, Class.forName(thePackage + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }

}