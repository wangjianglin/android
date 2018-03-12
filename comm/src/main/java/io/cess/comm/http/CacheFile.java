package io.cess.comm.http;

/**
 * @author lin
 * @date 2/19/16.
 */
public class CacheFile {

//    private static final String KEY_ID = "_id";
//    private static final int DATABASE_VERSION = 1;
//    private static final String KEY_URL = "_url";
//    private static final String KEY_FILE_NAME = "_file_name";
//    private static final String KEY_FILE_SIZE = "_file_size";
//    private static final String KEY_FILE = "_file";
//    private static final String KEY_LAST_MODIFIED = "_last_modified";
//    private static final String KEY_LAST_ACCESS_TIME = "_last_access_time";
//    private static final String KEY_ACCESS_COUNT = "_access_count";
    private long id;
    private String url;
    private String fileName;
    private String file;
    private long fileSize;
    private long lastModified;
    private long lastAccess;
    private long accessCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }
}
