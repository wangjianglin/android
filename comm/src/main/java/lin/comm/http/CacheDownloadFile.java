package lin.comm.http;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lin on 2/15/16.
 */
public class CacheDownloadFile {

    public static void clean() {
        helper.getWritableDatabase().delete(DATABASE_TABLE, null, null);
    }

    public static void save(FileInfo fileInfo){
        CacheFile cacheFile = new CacheFile();
        cacheFile.setAccessCount(1);
        cacheFile.setLastModified(fileInfo.getLastModified());
        cacheFile.setLastAccess(new Date().getTime());
        cacheFile.setUrl(fileInfo.getUrl());
        cacheFile.setFile(fileInfo.getFile().getAbsolutePath());
        cacheFile.setFileSize(fileInfo.getFile().length());
        cacheFile.setFileName(fileInfo.getFileName());

        helper.saveValue(cacheFile);
    }

    public static FileInfo getFileInfo(String url){
        CacheFile cacheFile = helper.getItemIdByUrl(url);
        if(cacheFile == null){
            return null;
        }
        helper.addAccess(url);

        return new FileInfo(url,new File(cacheFile.getFile()),cacheFile.getFileName(),cacheFile.getLastModified());
    }
    public synchronized static void init(Context context){
        if(helper == null){
            helper = new CacheDatabaseHelper(context, DATABASE_TABLE, null, DATABASE_VERSION);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try{
                            cleanCache();
                        }catch (Throwable e){

                        }

                        try {
                            Thread.sleep(10 * 60 * 1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }).start();
        }
    }

    private static void cleanCache(){
        while (helper.getLastAccess() != null){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static CacheDatabaseHelper helper;

    private static final String DATABASE_TABLE = "cache_download_fFile_data_base_name";
    private static final String KEY_ID = "_id";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_URL = "_url";
    private static final String KEY_FILE_NAME = "_file_name";
    private static final String KEY_FILE_SIZE = "_file_size";
    private static final String KEY_FILE = "_file";
    private static final String KEY_LAST_MODIFIED = "_last_modified";
    private static final String KEY_LAST_ACCESS = "_last_access_time";
    private static final String KEY_ACCESS_COUNT = "_access_count";


    ////url,filename,第一次时间，最后访问时间，访问次数，文件名，文件大小

    private static class CacheDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID
                + " integer primary key autoincrement, "
                + KEY_URL + " text, "
                + KEY_FILE + " text,"
                + KEY_FILE_NAME + " text,"
                + KEY_FILE_SIZE + " integer,"
                + KEY_LAST_MODIFIED + " integer,"
                + KEY_LAST_ACCESS + " integer,"
                + KEY_ACCESS_COUNT + " integer"
                + ");";
        private static final String[] COLUMNS = new String[]{KEY_ID,KEY_URL,KEY_FILE_NAME,KEY_FILE_SIZE,KEY_FILE,KEY_LAST_MODIFIED,KEY_LAST_ACCESS,KEY_ACCESS_COUNT};

        private ReentrantLock lock = new ReentrantLock();

        public CacheDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

//        public void removeByUrl(String url) {
//            helper.getWritableDatabase().delete(DATABASE_TABLE, KEY_URL + " = '" + url + "'", null);
//        }

        private void removeById(long id) {
            helper.getWritableDatabase().delete(DATABASE_TABLE, KEY_ID + " = " + id, null);
        }

        public CacheFile getItemIdByUrl(String url) {

            lock.lock();
            try {

                Cursor c = this.getWritableDatabase().query(DATABASE_TABLE,
                        COLUMNS, KEY_URL + " == '" + url + "'", null, null,
                        null, KEY_ID + " desc");
                if (c == null || !c.moveToNext()) {
                    return null;
                }

                CacheFile obj = constructorCacehFileObj(c);
                c.close();
                return obj;
            }finally {
                lock.unlock();
            }
        }

        private long size(){
            Cursor c = this.getWritableDatabase().rawQuery("SELECT sum(" + KEY_FILE_SIZE + ") FROM " + DATABASE_TABLE, null);
            if(c == null || !c.moveToNext()){
                return 0;
            }
            return c.getLong(0);
        }

        public CacheFile getLastAccess(){

            lock.lock();
            try {
                if (size() < HttpCommunicate.getCacheSize()) {
                    return null;
                }
                Cursor c = this.getWritableDatabase().query(DATABASE_TABLE,
                        COLUMNS, null, null, null,
                        null, KEY_LAST_ACCESS + " asc");
                if (c == null || !c.moveToNext()) {
                    return null;
                }

                //return c.getInt(c.getColumnIndex(KEY_ID));

                CacheFile obj = constructorCacehFileObj(c);
                c.close();
                this.removeById(obj.getId());

                new File(obj.getFile()).delete();

                return obj;
            }finally {
                lock.unlock();
            }
        }

        private CacheFile constructorCacehFileObj(Cursor c){
            CacheFile obj = new CacheFile();

            obj.setAccessCount(c.getLong(c.getColumnIndex(KEY_ACCESS_COUNT)));
            obj.setFile(c.getString(c.getColumnIndex(KEY_FILE)));
            obj.setFileName(c.getString(c.getColumnIndex(KEY_FILE_NAME)));
            obj.setFileSize(c.getLong(c.getColumnIndex(KEY_FILE_SIZE)));
            obj.setId(c.getLong(c.getColumnIndex(KEY_ID)));
            obj.setLastModified(c.getLong(c.getColumnIndex(KEY_LAST_MODIFIED)));
            obj.setLastAccess(c.getLong(c.getColumnIndex(KEY_LAST_ACCESS)));
            obj.setUrl(c.getString(c.getColumnIndex(KEY_URL)));

            return obj;
        }

        public void addAccess(String url){
            lock.lock();
            try {
                CacheFile obj = this.getItemIdByUrl(url);

                if (obj != null) {
                    obj.setAccessCount(obj.getAccessCount() + 1);
                    obj.setLastAccess(new Date().getTime());
                }
                saveValue(obj);
            }finally {
                lock.unlock();
            }
        }

        public void saveValue(CacheFile obj) {
//            int id = getItemId(name);
            if(obj == null){
                return;
            }
            lock.lock();
            try {
                ContentValues cvs = new ContentValues();

                cvs.put(KEY_FILE, obj.getFile());
                cvs.put(KEY_FILE_NAME, obj.getFileName());
                cvs.put(KEY_FILE_SIZE, obj.getFileSize());
                cvs.put(KEY_LAST_ACCESS, obj.getLastAccess());
                cvs.put(KEY_LAST_MODIFIED, obj.getLastModified());
                cvs.put(KEY_URL, obj.getUrl());

                CacheFile oldObj = this.getItemIdByUrl(obj.getUrl());
                if (oldObj == null) {
                    cvs.put(KEY_ACCESS_COUNT, obj.getAccessCount());
                    helper.getWritableDatabase().insert(DATABASE_TABLE, null, cvs);
                } else {
                    helper.getWritableDatabase().update(DATABASE_TABLE, cvs, KEY_ID + " = " + oldObj.getId(), null);
                }
            }finally {
                lock.unlock();
            }
        }



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
