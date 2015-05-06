package com.jnsw.core.data;

import com.google.common.base.Strings;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by foxundermoon on 2015/4/27.
 */
public class CommandSerilizer implements JsonSerializer<Command>,JsonDeserializer<Command> {
    @Override
    public Command deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
       JsonObject jsonObject = json.getAsJsonObject();
        Command cmd = new Command();
        if (jsonObject.has(MapKeys.name)) {
            cmd.setName(jsonObject.getAsJsonPrimitive(MapKeys.name).getAsString());
        }
        if (jsonObject.has(MapKeys.operation)) {
            cmd.setOperation(jsonObject.getAsJsonPrimitive(MapKeys.operation).getAsString());
        }
        if (jsonObject.has(MapKeys.condition)) {
            cmd.setCondition(jsonObject.getAsJsonPrimitive(MapKeys.condition).getAsString());
        }
        if (jsonObject.has(MapKeys.sql)) {
            cmd.setSql(jsonObject.getAsJsonPrimitive(MapKeys.sql).getAsString());
        }
        if (jsonObject.has(MapKeys.needBroadcast)) {
            cmd.setNeedBroadcast(jsonObject.getAsJsonPrimitive(MapKeys.needBroadcast).getAsBoolean());
        }
        if (jsonObject.has(MapKeys.needResponse)) {
            cmd.setNeedResponse(jsonObject.getAsJsonPrimitive(MapKeys.needResponse).getAsBoolean());
        }
        return cmd;
    }

    @Override
    public JsonElement serialize(Command src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        if (!Strings.isNullOrEmpty(src.getName())) {
            jsonObject.addProperty(MapKeys.name,src.getName());
        }
        if (!Strings.isNullOrEmpty(src.getCondition())) {
            jsonObject.addProperty(MapKeys.condition,src.getCondition());
        }
        if (!Strings.isNullOrEmpty(src.getOperation())) {
            jsonObject.addProperty(MapKeys.operation,src.getOperation());
        }
        if (src.isNeedBroadcast()) {
            jsonObject.addProperty(MapKeys.needBroadcast, true);
        }
        if (src.isNeedResponse()) {
            jsonObject.addProperty(MapKeys.needResponse, true);
        }
        if (!Strings.isNullOrEmpty(src.getSql())) {
            jsonObject.addProperty(MapKeys.sql,src.getSql());
        }
        return jsonObject;
    }
}
