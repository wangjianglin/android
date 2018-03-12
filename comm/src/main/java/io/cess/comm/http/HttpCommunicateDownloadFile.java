package io.cess.comm.http;

import java.net.URL;
import java.util.Map;

/**
 * @author lin
 * @date 1/9/16.
 */
public interface HttpCommunicateDownloadFile  extends Aboutable{
//    void setPackage(HttpPackage pack);

    void setImpl(HttpCommunicateImpl impl);

    void setListener(ProgressResultListener listener);

    void setPackage(HttpPackage pack);

//    void download(URL url);

    void download(String url);

    HttpFileInfo getFileInfo(String url);

    void setParams(HttpCommunicate.Params params);

    class HttpFileInfo{
        private long fileSize;
        private long lastModified;
        private String fileName;

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

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
