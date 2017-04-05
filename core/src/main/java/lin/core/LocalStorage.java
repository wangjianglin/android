package lin.core;

import java.lang.reflect.Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author lin
 * @date Jul 31, 2015 12:52:42 AM
 *
 */
public class LocalStorage {

	public static void clean() {
		helper.getWritableDatabase().delete(DATABASE_TABLE, null, null);
	}
	
	public static void setItem(String name,Object value){
		String json = null;
		try {
//			json = lin.util.json.JSONUtil.serialize(value);
			json = lin.util.JsonUtil.serialize(value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		helper.setItemValue(name,json);
	}
	
	public static void remove(String name){
		helper.remove(name);
	}
	
	public static String getItem(String name){
		return getItem(name,String.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getItem(String name,Class<T> type){
		return (T) getItemImpl(name,type);
	}

	public static <T> T getItem(String name,T def){
		Object r = getItem(name,def.getClass());
		if(r == null){
			return def;
		}
		return (T)r;
	}
	
	public static Object getItem(String name,Type type){
		return getItemImpl(name,type);
	}
	public static Object getItemImpl(String name,Type type){
		String json = helper.getItemValue(name);
		if(json == null){
			return null;
		}
		Object obj = null;
		try {
//			obj = lin.util.json.JSONUtil.deserialize(json, type);
			obj = lin.util.JsonUtil.deserialize(json, type);
		} catch (Throwable e) {
//			e.printStackTrace();
		}
		return obj;
	}
	
	public synchronized static void init(Context context){
		if(helper == null){
			helper = new LocalStorageDatabaseHelper(context, DATABASE_TABLE, null, DATABASE_VERSION);
		}
	}

	private static LocalStorageDatabaseHelper helper;

	private static final String DATABASE_TABLE = "local_storage_data_base_name";
	private static final String KEY_ID = "_id";
	private static final int DATABASE_VERSION = 1;
	private static final String KEY_NAME = "_name";
	private static final String KEY_VALUE = "_value";

	private static class LocalStorageDatabaseHelper extends SQLiteOpenHelper {

		public LocalStorageDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		public void remove(String name) {
			int id = getItemId(name);
			helper.getWritableDatabase().delete(DATABASE_TABLE, KEY_ID + " = "+id, null);
		}

		public int getItemId(String name) {
			Cursor c = this.getWritableDatabase().query(DATABASE_TABLE, new
					String[]{KEY_ID,KEY_NAME,KEY_VALUE}, KEY_NAME + " == '" + name +"'", null, null,
					null, KEY_ID+" desc");
			 if(c == null || !c.moveToNext()){
				 return -1;
			 }
			return c.getInt(c.getColumnIndex(KEY_ID));
		}
		
		public String getItemValue(String name) {
			Cursor c = this.getWritableDatabase().query(DATABASE_TABLE, new
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
				helper.getWritableDatabase().insert(DATABASE_TABLE, null, cvs);
			}else{
				helper.getWritableDatabase().update(DATABASE_TABLE, cvs, KEY_ID + " = " + id,null);
			}
			
		}

		private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID
				+ " integer primary key autoincrement, " + KEY_NAME + " text, " + KEY_VALUE + " text);";

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(_db);
		}

	}
}