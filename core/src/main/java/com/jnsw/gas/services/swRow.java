package com.jnsw.gas.services;

import java.util.HashMap;
import java.util.Map;

public class swRow {
	Map<String, String> lstFields = new HashMap();

	public swRow(swTable table) {

		for (int i = 0; i < table.getColumns().size(); i++) {
			swColumn col = (swColumn) table.getColumns().get(i);
			lstFields.put(col.getColumnName(), "");
		}

	}

	public boolean setCol(String strColName, String strVal) {
		if (!lstFields.containsKey(strColName))
			return false;
		lstFields.put(strColName, strVal);
		return true;
	}

	public String GetCol(String strColumnName) {
		if (!lstFields.containsKey(strColumnName))
			return "";

		return lstFields.get(strColumnName);
	}

}