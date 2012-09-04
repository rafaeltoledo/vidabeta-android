package net.rafaeltoledo.vidabeta.db;

import java.sql.SQLException;

import net.rafaeltoledo.vidabeta.model.Podcast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	public static final String DB_NAME = "vidabeta.db";
	public static final int DB_VERSION = 1;
	private Dao<Podcast, Integer> contatoDao = null;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Podcast.class);
		} catch (SQLException ex) {
			Log.e(DatabaseHelper.class.getName(), "Falha ao criar banco de dados", ex);
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
	}
	
	public Dao<Podcast, Integer> getPodcastDao() throws SQLException {
		if (contatoDao == null) {
			contatoDao = DaoManager.createDao(getConnectionSource(), Podcast.class);
		}
		
		return contatoDao;
	}
}
