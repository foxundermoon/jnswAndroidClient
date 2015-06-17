package com.jnsw.gas.services;

import java.util.ArrayList;

public class swTable {
	String strTableName;
	ArrayList<com.jnsw.gas.services.swColumn> lstColumns = new ArrayList();
	ArrayList<com.jnsw.gas.services.swRow> lstRows = new ArrayList();

	public swTable(String tableName) {
		strTableName = tableName;
	}

	public swTable() {
	}

	public String getTableName() {
		return strTableName;
	}

	public void setTableName(String tableName) {
		strTableName = tableName;
	}

	public int getColumnsCount() {
		return lstColumns.size();
	}

	public int getRowsCount() {
		return lstRows.size();
	}

	public ArrayList<com.jnsw.gas.services.swColumn> getColumns() {
		return lstColumns;
	}

	public ArrayList<com.jnsw.gas.services.swRow> getRows() {
		return lstRows;
	}

	public void AddColumn(com.jnsw.gas.services.swColumn col) {
		com.jnsw.gas.services.swColumn swcol = new com.jnsw.gas.services.swColumn(col.getColumnName(), col.getColumnType());
		lstColumns.add(swcol);
	}

	public com.jnsw.gas.services.swRow CreateNewRow() {
		com.jnsw.gas.services.swRow row = new swRow(this);
		return row;
	}

	public void AddRow(com.jnsw.gas.services.swRow row) {
		lstRows.add(row);
	}

}
