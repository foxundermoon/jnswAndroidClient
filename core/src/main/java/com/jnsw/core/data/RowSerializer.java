package com.jnsw.core.data;

import com.google.gson.*;
import com.google.gson.internal.Streams;

import java.lang.reflect.Type;

/**
 * Created by foxundermoon on 2015/4/23.
 */
public class RowSerializer implements JsonSerializer<Row> {


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
    public JsonElement serialize(Row src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        for (Column column : src.getColumns()) {
            if (src.hasColumn(column)) {
                Object value = src.get(column.getName());
                jsonArray.add(context.serialize(value));
            } else {
                jsonArray.add(JsonNull.INSTANCE);
            }
        }
        return jsonArray;
    }
}
