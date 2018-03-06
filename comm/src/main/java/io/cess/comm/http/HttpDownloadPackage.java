package io.cess.comm.http;

/**
 * Created by lin on 05/03/2018.
 */

public class HttpDownloadPackage extends HttpPackage<FileInfo> {

    public HttpDownloadPackage(String url){
        super(url,HttpMethod.GET);
    }

    @Override
    public final HttpMethod getMethod() {
        return HttpMethod.GET;
    }
}
