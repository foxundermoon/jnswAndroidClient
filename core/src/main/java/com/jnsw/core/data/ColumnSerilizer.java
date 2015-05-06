package com.jnsw.core.data;

import com.google.common.base.Strings;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by foxundermoon on 2015/4/25.
 */
public class ColumnSerilizer implements JsonSerializer<Column> ,JsonDeserializer<Column> {

    @Override
    public Column deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Column c = new Column();
        if (json.isJsonObject()) {
         JsonObject   jsonColumn = json.getAsJsonObject();
            c.setName(jsonColumn.getAsJsonPrimitive(MapKeys.name).getAsString());
            try{
                c.setIsPrimaryKey(jsonColumn.getAsJsonPrimitive(MapKeys.isPrimaryKey).getAsBoolean());
                c.setDbType(jsonColumn.getAsJsonPrimitive(MapKeys.dbType).getAsString());
            }catch(NullPointerException ignore){
            }
        }
        return c;
    }

    @Override
    public JsonElement serialize(Column src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonColumn = new JsonObject();
        jsonColumn.addProperty(MapKeys.name,src.getName());
        if (src.hasType()) {
            jsonColumn.addProperty(MapKeys.dbType,src.getDbType());
        }
        if (src.isPrimaryKey()) {
            jsonColumn.addProperty(MapKeys.isPrimaryKey,src.isPrimaryKey());
        }
        if (!Strings.isNullOrEmpty(src.getComment())) {
            jsonColumn.addProperty(MapKeys.comment,src.getComment());
        }
        if (src.getLength() > 0) {
            jsonColumn.addProperty(MapKeys.length,src.getLength());
        }
        return jsonColumn;
    }
}
