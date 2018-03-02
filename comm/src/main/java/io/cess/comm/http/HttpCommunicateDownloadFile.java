package io.cess.comm.http;

import java.net.URL;

/**
 * Created by lin on 1/9/16.
 */
public interface HttpCommunicateDownloadFile  extends Aboutable{
//    void setPackage(HttpPackage pack);

    void setImpl(HttpCommunicateImpl impl);

    void setListener(ProgressResultListener listener);

    void download(URL url);

    HttpFileInfo getFileInfo(URL url);

    void setParams(HttpCommunicate.Params params);

    static class HttpFileInfo{
        private long fileSize;
        private long lastModified;

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
    }
}
