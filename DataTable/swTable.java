package com.example.swmobileapp.services;

import java.util.ArrayList;

public class swTable {
	String strTableName;
	ArrayList<swColumn> lstColumns = new ArrayList();
	ArrayList<swRow> lstRows = new ArrayList();

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

	public ArrayList<swColumn> getColumns() {
		return lstColumns;
	}

	public ArrayList<swRow> getRows() {
		return lstRows;
	}

	public void AddColumn(swColumn col) {
		swColumn swcol = new swColumn(col.getColumnName(), col.getColumnType());
		lstColumns.add(swcol);
	}

	public swRow CreateNewRow() {
		swRow row = new swRow(this);
		return row;
	}

	public void AddRow(swRow row) {
		lstRows.add(row);
	}

}
