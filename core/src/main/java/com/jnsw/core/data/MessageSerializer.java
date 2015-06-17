package com.jnsw.core.data;

import android.widget.Toast;
import com.google.gson.*;
import com.jnsw.core.CustomApplication;

import java.lang.reflect.Type;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by foxundermoon on 2015/4/25.
 */
public class MessageSerializer implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            Message m = new Message();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (MapKeys.dataTable.equals(entry.getKey())) {
                    JsonObject jsonTable = entry.getValue().getAsJsonObject();
                    ArrayList<Column> columns = null;
                    if (jsonTable.has(MapKeys.columns)) {
                        JsonArray jsonColumns = jsonTable.getAsJsonArray(MapKeys.columns);
                        columns = new ArrayList<>(jsonColumns.size());
                        for (JsonElement jsonColumn : jsonColumns) {
                            JsonObject column = jsonColumn.getAsJsonObject();
                            Column c = context.deserialize(column, Column.class);
                            columns.add(c);
                        }
                    }
                    if ((jsonTable.has(MapKeys.rows)) && (columns != null)) {
                        m.setDataTable(new Table(columns));
                        JsonArray jsonRows = jsonTable.getAsJsonArray(MapKeys.rows);
                        if (jsonRows.size() > 0) {
                            for (int i = 0; i < jsonRows.size(); i++) {
                                Row row = m.getDataTable().createRow();
                                JsonArray jsonRow = jsonRows.get(i).getAsJsonArray();
                                for (int j = 0; j < jsonRow.size(); j++) {
                                    JsonElement jv = jsonRow.get(j);
                                    try {
                                        if (jv.isJsonPrimitive()) {
                                            JsonPrimitive jp = jv.getAsJsonPrimitive();
                                            if (jp.isBoolean())
                                                row.put(columns.get(j).getName(), jp.getAsBoolean());
                                            if (jp.isNumber())
                                                row.put(columns.get(j).getName(), jp.getAsNumber());
                                            if (jp.isString())
                                                row.put(columns.get(j).getName(), jp.getAsString());
                                            if (jp.isJsonNull())
                                                row.put(columns.get(j).getName(), null);
                                        } else if (jv.isJsonNull()) {
                                            row.put(columns.get(j).getName(), null);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } else if (MapKeys.error.equalsIgnoreCase(entry.getKey())) {
                    m.hasError = true;
                    m.errorType = entry.getValue().getAsString();
                } else if (MapKeys.errorMessage.equalsIgnoreCase(entry.getKey())) {
                    m.errorMessage = entry.getValue().getAsString();
                } else {
//                    String t = entry.getValue().getAsString();
//                    String f = entry.getValue().toString();
                    m.addProperty(entry.getKey(), entry.getValue().getAsString());
                }
            }
            return m;
        }
        return null;
    }

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
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        if (src == null) {
            return jsonObject;
        } else {
            if (src.hasFile) {


            } else {
                if (src.getId() != null) {
                    jsonObject.addProperty(MapKeys.id, src.getId());
                }
                if (src.hasError) {
                    jsonObject.addProperty(MapKeys.error, src.errorType);
                    jsonObject.addProperty(MapKeys.errorMessage, src.errorMessage);
                }
                if (src.propertiesCount() > 0) {
                    for (Map.Entry<String, Object> property : src.getProperties().entrySet()) {
                        jsonObject.add(property.getKey(), context.serialize(property.getValue()));
                    }
                }
                if (src.getDataTable() != null) {
                    jsonObject.add(MapKeys.dataTable, context.serialize(src.getDataTable()));
                }
            }
        }
        return jsonObject;
    }
}
