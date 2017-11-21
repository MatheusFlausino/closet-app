package com.matheusflausino.closetapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by matheus on 02/11/17.
 */

@DatabaseTable(tableName = "clothes")
public class Clothes {
    @DatabaseField(defaultValue = "0", columnName = "favorite")
    private int f;
    @DatabaseField(canBeNull = false, columnName = "model")
    private String m;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false, columnName = "type")
    private Type t;
    @DatabaseField(defaultValue = "", columnName = "color")
    private String c;
    @DatabaseField(defaultValue = "", columnName = "image")
    private String i;
    @DatabaseField(generatedId = true)
    private int id;

    public Clothes(){}

    public Clothes(String m, Type t, String c, String i, int f) {
        this.f = f;
        this.m = m;
        this.t = t;
        this.c = c;
        this.i = i;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFavorite() {
        return f;
    }

    public void setFavorite(int f) {
        this.f = f;
    }

    public String getModel() {
        return m;
    }

    public void setModel(String m) {
        this.m = m;
    }

    public Type getType() {
        return t;
    }

    public void setType(Type t) {
        this.t = t;
    }

    public String getColor() {
        return c;
    }

    public void setColor(String c) {
        this.c = c;
    }

    public String getImage() {
        return i;
    }

    public void setImage(String i) {
        this.i = i;
    }

}
