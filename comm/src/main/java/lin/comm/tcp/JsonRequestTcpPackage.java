package lin.comm.tcp;

import lin.comm.tcp.annotation.RespType;

import java.util.Map;

/**
 * Created by lin on 1/28/16.
 */
@RespType(JsonResponseTcpPackage.class)
public class JsonRequestTcpPackage extends RequestTcpPackage implements JsonTcpPackage {

    JsonPackageImpl impl;

    public JsonRequestTcpPackage()
    {
        impl = new JsonPackageImpl(this.getClass(),this);
    }

    void setValues(Object obj){
        impl.setValues(obj);
    }

    void setHeaders(Map<String,String> headers){
        impl.setHeaders(headers);
    }

    @Override
    public final byte getType()
    { return 6;
    }

    public String getPath() { return impl.getPath(); }

    @Override
    public final byte[] write()
    {
        return impl.write();
    }


    public void setHeader(String header,String value){
        impl.setHeader(header,value);
    }

    public void removeHeader(String header){
        impl.removeHeader(header);
    }

    public Map<String,String> getHeaders(){
        return impl.getHeaders();
    }

    public void setValue(String name,Object value){
        impl.setValue(name,value);
    }

    public Object getValue(String name){

        return impl.getValue(name);
    }

    public void removeValue(String name){
        impl.removeValue(name);
    }

    public Map<String,Object> getValues(){
        return impl.getValues();
    }

}
