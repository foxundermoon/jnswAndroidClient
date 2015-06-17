package com.jnsw.gas.services;

import com.jnsw.core.data.Column;
import com.jnsw.core.data.Table;

/**
 * Created by foxundermoon on 2015/6/9.
 */
public class TableCast {
    public static swTable Table2swTable(Table tb) {
        swTable swtb = new swTable();

        if (tb.rowCount() > 0) {
            for (Column c : tb.getColumns()) {
                swtb.AddColumn(new swColumn(c.getName(),c.getDbType()));
            }
            for (int i = 0; i < tb.rowCount(); i++) {
                swRow r = swtb.CreateNewRow();
                for (int j = 0; j < tb.getColumns().size(); j++) {
                    String v="";
                    Object rv = tb.get(i,j);
                    if(rv instanceof Integer){
                        v= ((Integer) rv).intValue() +"";
                    }else  if(rv instanceof Double){
                        v = ((Double) rv).doubleValue() +"";
                    } else if(rv instanceof String){
                        v = (String) rv;
                    }else if(rv instanceof Boolean){
                        if(((Boolean) rv).booleanValue())
                            v="true";
                        else
                            v="false";
                    }
                    r.setCol(tb.getColumns().get(j).getName(),v);
                }
                swtb.AddRow(r);
            }
            return swtb;
        }else {
            return null;
        }
    }
}
