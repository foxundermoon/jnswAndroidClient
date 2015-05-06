package com.jnsw.core.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by foxundermoon on 2015/4/23.
 */
public class Row implements Iterable<Map.Entry<String, Object>> {
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    private Map<String, Object> data = new HashMap<String, Object>();

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    private List<Column> columns;

    public Row(List<Column> columns) {
        this.columns = columns;
    }

    private Row() {
    }

    public static Row creatEmptyRow() {
        return new Row();
    }

    public Row put(String key, Object value) throws Exception {
        if (columns == null || columns.size() < 1) {
            throw new Exception("table no columns");
        } else {
            if (hasColumn(key)) {
                data.put(key, value);
                return this;
            } else {
                throw new Exception("table no " + key + " column");
            }

        }

    }

    public Row putAll(Map<String, Object> data) {
        data.putAll(data);
        return this;
    }

    public boolean hasColumn(Column column) {
        return hasColumn(column.getName());
    }
    public boolean hasColumn(String name) {
        for (Column column : columns) {
            if (column.getName().equalsIgnoreCase(name)) {
                return true;
            }

        }
        return false;
    }
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * Returns an {@link Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return data.entrySet().iterator();
    }
}
