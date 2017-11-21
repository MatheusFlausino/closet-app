package com.matheusflausino.closetapp.repo;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import com.matheusflausino.closetapp.db.DatabaseHelper;
import com.matheusflausino.closetapp.db.DatabaseManager;
import com.matheusflausino.closetapp.model.Clothes;

public class ClothesDAO implements Crud{
	
	private DatabaseHelper helper;
	
	public ClothesDAO(Context context){
		DatabaseManager.init(context);
		helper = DatabaseManager.getInstance().getHelper();
	}

	@Override
	public int create(Object item) {
		
		int index = -1;
		
		Clothes object = (Clothes) item;
		try {
			index = helper.getClothesDao().create(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;

	}
	

	@Override
	public int update(Object item) {
		
		int index = -1;

		Clothes object = (Clothes) item;
		
		try {
			index = helper.getClothesDao().update(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;
	}



	@Override
	public int delete(Object item) {
		
		int index = -1;

		Clothes object = (Clothes) item;
		
		try {
			helper.getClothesDao().delete(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;
	}


	@Override
	public Object findById(int id) {

		Clothes object = null;
		try {
			object = helper.getClothesDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;
	}

	public List<Clothes> findByType(Object t){
		List<Clothes> object = null;
		try {
			object = helper.getClothesDao().queryForEq("type", t);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return object;

	}


	@Override
	public List<Clothes> findAll() {
		
		List<Clothes> items = null;
		
		try {
			items =  helper.getClothesDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
}
