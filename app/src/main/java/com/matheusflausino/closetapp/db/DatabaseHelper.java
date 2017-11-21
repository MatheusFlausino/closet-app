package com.matheusflausino.closetapp.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.matheusflausino.closetapp.R;
import com.matheusflausino.closetapp.model.Clothes;
import com.matheusflausino.closetapp.model.Type;
import com.matheusflausino.closetapp.util.UtilString;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "ClosetDB.sqlite";
	private static final int DATABASE_VERSION = 1;

	// the DAO object we use to access the SimpleData table
    private Dao<Clothes, Integer> clothesDao = null;
	private Dao<Type, Integer> typeDao = null;

	private Context context;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
		try {

			TableUtils.createTable(connectionSource, Clothes.class);
			TableUtils.createTable(connectionSource, Type.class);

			String[] default_types = context.getResources().getStringArray(R.array.default_types);
			String[] default_models = context.getResources().getStringArray(R.array.default_models);
			String[] default_colors = context.getResources().getStringArray(R.array.default_colors);

			for(int cont = 0; cont < default_types.length; cont++){
				Type t = new Type(default_types[cont]);
				getTypeDao().create(t);
			}

            UtilString.setPreference(context, UtilString.PREFERENCES_MODELS, new Gson().toJson(default_models));
            UtilString.setPreference(context, UtilString.PREFERENCES_COLORS, new Gson().toJson(default_colors));

		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

	@Override
	public void onUpgrade(SQLiteDatabase database,ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Clothes.class, true);
			TableUtils.dropTable(connectionSource, Type.class, true);

			onCreate(database, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
			throw new RuntimeException(e);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}

	}

	public Dao<Clothes, Integer> getClothesDao() {
		if (null == clothesDao) {
			try {
                clothesDao = getDao(Clothes.class);
			}catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return clothesDao;
	}

	public Dao<Type, Integer> getTypeDao() {
		if (null == typeDao) {
			try {
				typeDao = getDao(Type.class);
			}catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
		}
		return typeDao;
	}
}
