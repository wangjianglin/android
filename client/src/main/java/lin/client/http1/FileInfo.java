package lin.client.http1;

import java.io.File;

/**
 * Created by lin on 1/5/16.
 */
@Deprecated
public class FileInfo {

    private File file;
    private String fileName;
    private long lastModified;

    FileInfo(File file,String fileName,long lastModified){
        this.file = file;
        this.fileName = fileName;
        this.lastModified = lastModified;
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
}
