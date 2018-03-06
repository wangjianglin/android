package io.cess.comm.http;

import java.io.File;

/**
 * Created by lin on 1/5/16.
 */
public class FileInfo {

    private File file;
    private String fileName;
    private long lastModified;
    private long length;
    private String url;

    public FileInfo(String url,File file,String fileName,long lastModified,long length){
        this.file = file;
        this.fileName = fileName;
        this.lastModified = lastModified;
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getUrl() {
        return url;
    }

    public long getLength() {
        return length;
    }
}
