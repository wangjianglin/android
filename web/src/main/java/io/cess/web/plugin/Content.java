package io.cess.web.plugin;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


/**
 * 
 * @author lin
 * @date May 14, 2015 5:13:12 PM
 *
 */
public class Content extends ContentProvider{

	private static final UriMatcher sUriMatcher = new UriMatcher(0);
	
	static{
		 sUriMatcher.addURI("lin.web", "image", 1);  
		  
		    sUriMatcher.addURI("lin.web", "image/#", 2);  
	}
	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
}