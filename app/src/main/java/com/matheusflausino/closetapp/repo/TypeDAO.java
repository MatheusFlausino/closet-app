package com.matheusflausino.closetapp.repo;

import android.content.Context;

import com.matheusflausino.closetapp.db.DatabaseHelper;
import com.matheusflausino.closetapp.db.DatabaseManager;
import com.matheusflausino.closetapp.model.Clothes;
import com.matheusflausino.closetapp.model.Type;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by matheusflausino on 13/11/17.
 */

public class TypeDAO implements Crud {

    private DatabaseHelper helper;

    public TypeDAO(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;

        Type object = (Type) item;

        try {
            helper.getTypeDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;

        Type object = (Type) item;

        try {
            index = helper.getTypeDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;

        Type object = (Type) item;

        try {
            helper.getTypeDao().delete(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public Object findById(int id) {
        Type object = null;
        try {
            object = helper.getTypeDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    public List<Type> findByName(String name){
        List<Type> object = null;
        try {
            object = helper.getTypeDao().queryForEq("type", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;

    }

    @Override
    public List<Type> findAll() {
        List<Type> items = null;

        try {
            items =  helper.getTypeDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}
