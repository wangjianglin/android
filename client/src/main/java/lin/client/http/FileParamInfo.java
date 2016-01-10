package lin.client.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by lin on 1/9/16.
 */
public class FileParamInfo {

    private String mimeType;
    private String fileName;
    private InputStream file;
    private long length = 0;

    public String getMimeType() {
        return mimeType;
    }

    void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public InputStream getFile() {
        return file;
    }

    void setFile(InputStream file) {
        this.file = file;
        length = -1;
    }

    void setFile(byte[] bs){
        file = new ByteArrayInputStream(bs);
        length = bs.length;
    }

    void setFile(File f) throws FileNotFoundException {
        file = new FileInputStream(f);
        length = f.length();
    }

    public long getLength(){
        return length;
    }
}
