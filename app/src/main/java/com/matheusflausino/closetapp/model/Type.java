package com.matheusflausino.closetapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by matheusflausino on 13/11/17.
 */
@DatabaseTable(tableName = "types")
public class Type {
    @DatabaseField(defaultValue = "", columnName = "type")
    private String t;
    @DatabaseField(generatedId = true)
    private int id;

    public Type (){}

    public Type(String t) {
        this.t = t;
    }

    public String getType() {
        return t;
    }

    public void setType(String t) {
        this.t = t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
