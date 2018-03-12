package io.cess.comm.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 文件上传参数
 * @author lin
 * @date 1/9/16.
 */
public class FileParamInfo {

    /**
     * 上传文件的 mime-type，如：image/jpeg、image/png
     */
    private String mMimeType;
    /**
     * 文件名称，当下载些文件是，作为下载文件名
     */
    private String mFileName;
    /**
     * 对应文件的输入流
     */
    private InputStream mFile;
    /**
     * 文件大小，以字符为单位，-1表示大小未知
     */
    private long mLength = 0;

    public String getMimeType() {
        return mMimeType;
    }

    void setMimeType(String mimeType) {
        this.mMimeType = mimeType;
    }

    public String getFileName() {
        return mFileName;
    }

    void setFileName(String fileName) {
        this.mFileName = fileName;
    }



    public InputStream getFile() {
        return mFile;
    }

    void setFile(InputStream file) {
        this.mFile = file;
        mLength = -1;
    }

    void setFile(byte[] bs){
        mFile = new ByteArrayInputStream(bs);
        mLength = bs.length;
    }

    void setFile(File f) throws FileNotFoundException {
        mFile = new FileInputStream(f);
        mLength = f.length();
    }

    public long getLength(){
        return mLength;
    }
}
