package com.matheusflausino.closetapp.db;

import android.content.Context;

public class DatabaseManager {

	private static DatabaseManager instance;
	private DatabaseHelper helper;


	public static void init(Context ctx) {
		if (null==instance) {
			instance = new DatabaseManager(ctx);
		}
	}

	public static DatabaseManager getInstance() {
		return instance;
	}

	private DatabaseManager(Context ctx) {
		helper = new DatabaseHelper(ctx);
	}

	public DatabaseHelper getHelper() {
		return helper;
	}

}