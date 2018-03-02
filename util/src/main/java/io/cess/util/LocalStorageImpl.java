package io.cess.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Type;

/**
 * 
 * @author lin
 * @date Jul 31, 2015 12:52:42 AM
 *
 */
public class LocalStorageImpl {

	private static final String KEY_ID = "_id";
	private static final int DATABASE_VERSION = 1;
	private static final String KEY_NAME = "_name";
	private static final String KEY_VALUE = "_value";


	private LocalStorageDatabaseHelper mHelper;

	public LocalStorageImpl(LocalStorage l, Context context, String table){
		mHelper = new LocalStorageDatabaseHelper(context, table, null, DATABASE_VERSION);
	}

	public void clean() {
//		mHelper.getWritableDatabase().delete(DATABASE_TABLE, null, null);
		mHelper.clean();
	}
	
	public void setItem(String name,Object value){
		String json = null;
		try {
//			json = io.cess.util.json.JSONUtil.serialize(value);
			json = io.cess.util.JsonUtil.serialize(value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		mHelper.setItemValue(name,json);
	}
	
	public void remove(String name){
		mHelper.remove(name);
	}
	
	public String getItem(String name){
		return getItem(name,String.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getItem(String name,Class<T> type){
		return (T) getItemImpl(name,type);
	}

	public <T> T getItem(String name,T def){
		Object r = getItem(name,def.getClass());
		if(r == null){
			return def;
		}
		return (T)r;
	}
	
	public Object getItem(String name,Type type){
		return getItemImpl(name,type);
	}
	private Object getItemImpl(String name,Type type){
		String json = mHelper.getItemValue(name);
		if(json == null){
			return null;
		}
		Object obj = null;
		try {
//			obj = io.cess.util.json.JSONUtil.deserialize(json, type);
			obj = io.cess.util.JsonUtil.deserialize(json, type);
		} catch (Throwable e) {
//			e.printStackTrace();
		}
		return obj;
	}

	private static Context mContext;
	public synchronized static void init(Context context){
		mContext = context;
//		if(mHelper == null){
//			mHelper = new LocalStorageDatabaseHelper(context, DEFAULT_DATABASE_TABLE, null, DATABASE_VERSION);
//		}
	}


	private static class LocalStorageDatabaseHelper extends SQLiteOpenHelper {


		private String mDatabaseTable = null;

		private String mDataCreate = null;

		public LocalStorageDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
			mDatabaseTable = name;
			mDataCreate = "create table " + mDatabaseTable + " (" + KEY_ID
					+ " integer primary key autoincrement, " + KEY_NAME + " text, " + KEY_VALUE + " text);";
		}

		public void remove(String name) {
			int id = getItemId(name);
			this.getWritableDatabase().delete(mDatabaseTable, KEY_ID + " = "+id, null);
		}

		public void clean(){
			this.getWritableDatabase().delete(mDatabaseTable, null, null);
		}

		public int getItemId(String name) {
			Cursor c = this.getWritableDatabase().query(mDatabaseTable, new
					String[]{KEY_ID,KEY_NAME,KEY_VALUE}, KEY_NAME + " == '" + name +"'", null, null,
					null, KEY_ID+" desc");
			 if(c == null || !c.moveToNext()){
				 return -1;
			 }
			return c.getInt(c.getColumnIndex(KEY_ID));
		}
		
		public String getItemValue(String name) {
			Cursor c = this.getWritableDatabase().query(mDatabaseTable, new
					String[]{KEY_ID,KEY_NAME,KEY_VALUE}, KEY_NAME + " == '" + name +"'", null, null,
					null, KEY_ID+" desc");
			 if(c == null || !c.moveToNext()){
				 return null;
			 }
			return c.getString(c.getColumnIndex(KEY_VALUE));
		}

		public void setItemValue(String name, String json) {
			int id = getItemId(name);
			
			ContentValues cvs = new ContentValues();
			
			cvs.put(KEY_NAME, name);
			cvs.put(KEY_VALUE, json);
			
			if(id == -1){
				this.getWritableDatabase().insert(mDatabaseTable, null, cvs);
			}else{
				this.getWritableDatabase().update(mDatabaseTable, cvs, KEY_ID + " = " + id,null);
			}
			
		}


		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(mDataCreate);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			_db.execSQL("DROP TABLE IF EXISTS " + mDatabaseTable);
			onCreate(_db);
		}

	}
}