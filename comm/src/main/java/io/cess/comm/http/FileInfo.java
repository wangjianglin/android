package io.cess.comm.http;

import java.io.File;

/**
 * 用于保存文件下载的相关信息
 * @author lin
 * @date 1/5/16.
 */
public class FileInfo {

    /**
     * 下载后存储到本地的缓存文件
     */
    private File mFile;
    /**
     * 文件名
     */
    private String mFileName;
    /**
     * 文件的最后修改时间
     */
    private long mLastModified;
    /**
     * 文件长度
     */
    private long mLength;
    /**
     * 下载url
     */
    private String mUrl;

    public FileInfo(String url,File file,String fileName,long lastModified,long length){
        this.mFile = file;
        this.mFileName = fileName;
        this.mLastModified = lastModified;
        this.mLength = length;
        this.mUrl = url;
    }

    public File getFile() {
        return mFile;
    }

    public String getFileName() {
        return mFileName;
    }

    public long getLastModified() {
        return mLastModified;
    }

    public String getUrl() {
        return mUrl;
    }

    public long getLength() {
        return mLength;
    }
}
