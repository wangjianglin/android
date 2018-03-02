package io.cess.util;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * 
 * @author lin
 * @date Jul 31, 2015 12:52:42 AM
 *
 */
public class LocalStorage {


	private static final String DEFAULT_DATABASE_TABLE = "local_storage_data_base_name";

	private static Map<String,SoftReference<LocalStorageImpl>> maps = new HashMap<>();

	private static Context mContext;
	private static LocalStorageImpl mGlobal;
	private static LocalStorage mInstance = new LocalStorage();

	private LocalStorage(){
	}

	private static LocalStorageImpl global(){
		if(mGlobal == null){
			mGlobal = getImpl(DEFAULT_DATABASE_TABLE);
		}
		return mGlobal;
	}

	public static void clean() {
		global().clean();
	}
	
	public static void setItem(String name,Object value){
		global().setItem(name,value);
	}
	
	public static void remove(String name){
		global().remove(name);
	}
	
	public static String getItem(String name){
		return global().getItem(name);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getItem(String name,Class<T> type){
		return global().getItem(name,type);
	}

	public static  <T> T getItem(String name,T def){
		return global().getItem(name,def);
	}
	
	public static Object getItem(String name,Type type){
		return global().getItem(name,type);
	}

	public static void init(Context context){
		if(mContext != null){
			return;
		}
		mContext = context;
	}


	public static LocalStorageImpl get(String table){
		return getImpl("local_storage_"+table+"_"+ DeviceUtil.uuid(mContext));
	}
	public synchronized static LocalStorageImpl getImpl(String table){
		if(mContext == null){
			return null;
		}
		SoftReference<LocalStorageImpl> item = maps.get(table);
		LocalStorageImpl impl = null;
		if(item != null){
			impl = item.get();
			if(impl != null){
				return impl;
			}else {
				maps.remove(table);
			}
		}

		impl = new LocalStorageImpl(mInstance,mContext,table);

		item = new SoftReference<LocalStorageImpl>(impl);
		maps.put(table,item);
		return impl;

	}


}