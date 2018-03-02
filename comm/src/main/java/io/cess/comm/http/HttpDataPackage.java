package io.cess.comm.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lin on 2/23/16.
 */
public class HttpDataPackage extends HttpPackage{

    private HttpRequestHandle handle = NORMAL;
    private Map<String,Object> params = new HashMap<String,Object>();
    private boolean multipart;

    private Type respType = String.class;

    public Type getRespType() {
        return respType;
    }

    @Override
    public void setRespType(Type respType) {
        this.respType = respType;
    }

    protected HttpDataPackage(){
    }

    public HttpDataPackage(String url){
        super(url);
    }

    public HttpDataPackage(String url,HttpMethod method){
        super(url, method);
    }

    public void add(String name,Object value){

        try {
            FileParamInfo paramValue = null;
            if (value instanceof byte[]) {
                //contentBody = new ByteArrayBody((byte[]) paramValue,mimeType,fileName);
                paramValue = new FileParamInfo();
                paramValue.setFile((byte[]) value);
                paramValue.setMimeType("application/octet-stream");
                paramValue.setFileName(name);
                multipart = true;
            } else if (value instanceof java.io.File) {
                //contentBody = new FileBody((File) paramValue,mimeType,((File) paramValue).getName());
//					contentBody = new FileBody((File) paramValue);
                paramValue = new FileParamInfo();
                paramValue.setFile((File) value);
                paramValue.setMimeType("application/octet-stream");
                paramValue.setFileName(name);
                multipart = true;
            } else if (value instanceof java.io.InputStream) {
//    				contentBody = new InputStreamBody((InputStream) paramValue,mimeType,fileName);
                paramValue = new FileParamInfo();
                paramValue.setFile((InputStream) value);
                paramValue.setMimeType("application/octet-stream");
                paramValue.setFileName(name);
                multipart = true;
            }

            if (paramValue != null) {
                params.put(name, paramValue);
            } else {
                params.put(name, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void remove(String name){
        params.remove(name);
    }


    public void addFile(String name,File file,String fileName)throws FileNotFoundException{
        FileParamInfo  paramValue = new FileParamInfo();
        paramValue.setFile(file);
        paramValue.setMimeType("application/octet-stream");
        paramValue.setFileName(fileName);
        multipart = true;

        params.put(name, paramValue);
    }

    public void addFile(String name,File file)throws FileNotFoundException{
        addFile(name, file, file.getName());
    }

    public void addBytes(String name,byte[] bytes,String fileName){
        FileParamInfo  paramValue = new FileParamInfo();
        paramValue.setFile(bytes);
        paramValue.setMimeType("application/octet-stream");
        paramValue.setFileName(fileName);
        multipart = true;

        params.put(name, paramValue);
    }

    public void addBytes(String name,byte[] bytes){
        addBytes(name, bytes, name);
    }

    public void addInputStream(String name,InputStream inputStream,String fileName){
        FileParamInfo  paramValue = new FileParamInfo();
        paramValue.setFile(inputStream);
        paramValue.setMimeType("application/octet-stream");
        paramValue.setFileName(fileName);
        multipart = true;

        params.put(name, paramValue);
    }

    public void addInputStream(String name,InputStream inputStream){
        addInputStream(name, inputStream, name);
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String,Object> superParams = super.getParams();
        if(superParams != null){
            superParams.putAll(params);
            return superParams;
        }
        return params;
    }

    @Override
    public boolean isMultipart() {
        return multipart;
    }

    public void setHttpRequestHandle(HttpRequestHandle handle){
        this.handle = handle;
    }

    @Override
    public HttpRequestHandle getRequestHandle() {
        return this.handle;
    }
}
