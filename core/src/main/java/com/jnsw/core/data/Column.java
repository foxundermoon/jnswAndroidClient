package com.jnsw.core.data;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by foxundermoon on 2015/4/22.
 */
public class Column {
    private String name;
    private String dbType;
    private String comment;
    private int length;
    private Type type;
    private boolean isPrimaryKey;
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public Column() {
    }

    public Column(String name) {
        this.name = name;
    }

    public void setDbType(String dbType) {
        
        this.dbType = dbType;
    }

    public boolean hasType() {
        if (!Strings.isNullOrEmpty(dbType)) {
            return true;
        }else if (type != null && length > 0) {
            return true;
        } else {
            return false;
        }
    }
    public String getName() {
        return name;
    }

    public Column setName(String name) {
        this.name = name;
        return this;
    }

    public String getDbType() {
        if (Strings.isNullOrEmpty(dbType)) {
            return dbType;
        } else if (type != null && length > 0) {
            return typeToDbType(type) + " ( " + length + ")";
        } else {
            return null;
        }
    }

    private String typeToDbType(Type type) {
        if (type instanceof Class) {
            if (type.equals(String.class)) {
                if (length > 500 && length < 2000) {
                    return "text";
                } else if (length >= 2000) {
                    return "longtext";
                } else if (length <= 500) {
                    return "varchar";
                }
            } else if (type.equals(int.class)) {
                return "int";
            } else if (type.equals(byte.class)) {
                if (length < 16 * 1024 * 1024 && length < 65 * 1024) {
                    return "mediumblob";
                } else if (length >= 16 * 1024 * 1024) {
                    return "LongBlob";
                } else if (length < 255) {
                    return "TinyBlob";
                } else {
                    return "Blob";
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return null;
    }
    public String getComment() {
        return comment;
    }
    public Column setComment(String comment) {
        this.comment = comment;
        return this;
    }
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
    public Column setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
        return this;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
