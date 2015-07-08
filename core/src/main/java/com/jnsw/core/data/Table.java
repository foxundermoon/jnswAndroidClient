package com.jnsw.core.data;

import android.app.ActionBar;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by foxundermoon on 2015/4/23.
 */
public class Table implements Serializable, Iterable {
    private List<Row> rows = new LinkedList<Row>();
    private List<Column> columns;
    private String name;
    private String database;


    public Table(List<Column> columns) {
        this.columns = columns;
    }
    public Table(){}

    public Table(Column... columns) {
        this.columns = new LinkedList<Column>();
        for (Column column : columns) {
            this.columns.add(column);
        }
    }

    public Table(String... columns) {
        this.columns = new LinkedList<Column>();
        for (String name : columns) {
            this.columns.add(new Column(name));
        }
    }

    public Row newRow() {
        Row row = new Row(columns);
        return row;
    }
    public Row createRow() {
        Row row= new Row(columns);
        add(row);
        return row;
    }
    public int rowCount(){
        if(rows!=null)
            return rows.size();
        return 0;
    }

    public boolean add(Row row) {
        return rows.add(row);
    }

    public boolean addAll(Collection<Row> rows) {
        return rows.addAll(rows);
    }

    public Row get(int index) {
        return rows.get(index);
    }
    public List<Column> getColumns() {
        return columns;
    }
    public Object get(int row,int column){
        return get(row).get(columns.get(column).getName());
    }
    public  Object get(int row,String column){
        return get(row).get(column);
    }
    public Table setColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }
    public Table appendColumn(Column column) {
        if (columns == null) {
            columns = new LinkedList<Column>();
        }
        columns.add(column);
        return this;
    }

    /**
     * Returns an {@link Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<Row> iterator() {
       return rows.iterator();
    }

    public String getName() {
        return name;
    }

    public Table setName(String name) {
        this.name = name;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public Table setDatabase(String database) {
        this.database = database;
        return this;
    }
    public List<Row> getRows() {
        return rows;
    }


}
