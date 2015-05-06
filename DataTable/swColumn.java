package com.example.swmobileapp.services;

public class swColumn
{
    String strColumnName;
    String strColumnType;
    public swColumn(String colName, String colType)
    {
        strColumnName = colName;
        strColumnType = colType;
    }
    public swColumn( )
    {
         
    }
    public String getColumnName()
    {
    	return strColumnName;
    }
    public void setColumnName(String colName)
    {
    	strColumnName = colName;
    }
    public String getColumnType()
    {
    	return strColumnType;
    }
    public void setColumnType(String colType)
    {
    	strColumnType = colType;
    }
}