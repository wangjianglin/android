package io.cess.comm.http.okhttp3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import io.cess.comm.http.FileParamInfo;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpPackage;
import io.cess.comm.http.HttpUtils;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @author lin
 * @date 4/25/16.
 */
class Utils {

    public static Request.Builder get(HttpCommunicateImpl impl, HttpPackage pack,Map<String,Object> requestParams) throws UnsupportedEncodingException {
        String url = HttpUtils.uri(impl,pack);
        url = HttpUtils.urlAddQueryString(url,HttpUtils.generQueryString(requestParams));
        return new Request.Builder()
                .url(url);
    }

    public static Request.Builder post(HttpCommunicateImpl impl, HttpPackage pack,Map<String,Object> requestParams) throws UnsupportedEncodingException {

        RequestBody requestBody = null;
        if(pack.isMultipart()){
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
            for(Map.Entry<String,Object> item : requestParams.entrySet()){
                Object value = item.getValue();
                if(value instanceof FileParamInfo){
                    FileParamInfo fileParamInfo = (FileParamInfo) value;
                    multipartBodyBuilder.addFormDataPart(item.getKey(),
                            fileParamInfo.getFileName(),
                            fileRequestBody(fileParamInfo)
                    );
                }else{
                    multipartBodyBuilder.addFormDataPart(item.getKey(),HttpUtils.encode(item.getValue().toString()));
                }
            }
            requestBody = multipartBodyBuilder.build();
        }else{
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for(Map.Entry<String,String> item : HttpUtils.queryMap(requestParams,false).entrySet()){
                bodyBuilder.add(item.getKey(),item.getValue());
            }
            requestBody = bodyBuilder.build();
        }

        return new Request.Builder()
                .url(HttpUtils.uri(impl, pack))
                .post(requestBody);
    }

    private static RequestBody fileRequestBody(final FileParamInfo fileInfo){

        return new RequestBody() {
            @Override public MediaType contentType() {
                return MediaType.parse(fileInfo.getMimeType());
            }

            @Override public long contentLength() {
                return fileInfo.getLength();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(fileInfo.getFile());
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
}
