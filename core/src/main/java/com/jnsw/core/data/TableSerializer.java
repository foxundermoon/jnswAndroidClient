package com.jnsw.core.data;

import com.google.common.base.Strings;
import com.google.gson.*;
import com.google.gson.internal.Streams;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by foxundermoon on 2015/4/23.
 */
public class TableSerializer implements JsonSerializer<Table>
{

    /**
     * Gson invokes this call-back method during serialization when it encounters a field of the
     * specified type.
     * <p/>
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param src       the object that needs to be converted to Json.
     * @param typeOfSrc the actual type (fully genericized version) of the source object.
     * @param context
     * @return a JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(Table src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray columns = (JsonArray) context.serialize(src.getColumns());
        JsonArray rows = new JsonArray();
        if (src == null && src.getRows().size() < 1) {
            rows.add(JsonNull.INSTANCE);
        } else {
            for (Row row : src.getRows()) {
                rows.add(context.serialize(row, Row.class));
            }
        }
        JsonObject table = new JsonObject();
        table.add("columns", columns);
        table.add("rows", rows);
        if (!Strings.isNullOrEmpty( src.getName()) ){
            table.addProperty(MapKeys.name,src.getName());
        }
        if (!Strings.isNullOrEmpty( src.getDatabase())) {
            table.addProperty(MapKeys.dataBase,src.getDatabase());
        }
        return table;
    }
}
