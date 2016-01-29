package lin.client.tcp;

import lin.client.tcp.annotation.Param;
import lin.client.tcp.annotation.Path;
import lin.util.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lin on 1/28/16.
 */
class JsonPackageImpl {
    private String path = "/";
    private Map<String,String> headers = new HashMap<>();
    private Map<String,Object> values = new HashMap<>();

    private Class<?> cls;
    private Object obj;
    public JsonPackageImpl(Class<?> cls,Object obj)
    {
        this.cls = cls;
        this.obj = obj;
        init();
    }

    private void init(){

        Path pathAnnon = cls.getAnnotation(Path.class);
        if(pathAnnon != null){
            path = pathAnnon.value();
        }

        Field[] fs = cls.getDeclaredFields();
        Param param = null;
        String paramName = null;
        for(Field f : fs){
            param = f.getAnnotation(Param.class);
            if(param != null){
                paramName = param.value();
                if(paramName == null || "".equals(paramName.trim())){
                    paramName = f.getName();
                }
                f.setAccessible(true);
                values.put(paramName,f);
            }
        }
    }

    void setValues(Object obj){
        if(obj instanceof Map){
            values.putAll((Map<String,Object>)obj);
        }
    }

    void setHeaders(Map<String,String> headers){
        this.headers.putAll(headers);
    }


    public String getPath() { return path; }

    public final byte[] write()
    {

        StringBuilder builder = new StringBuilder();

        //path
        builder.append("path:");
        builder.append(this.getPath());
        builder.append("\r\n");
        //coding
        builder.append("encoding:");
        //builder.append(Encoding.Default.BodyName);
        builder.append("\r\n");

//        builder.append("version:0.1.0build0");
//        builder.append("\r\n");

        for(Map.Entry<String,String> item:headers.entrySet()){
            builder.append(item.getKey());
            builder.append(':');
            if(item.getValue() != null){
                builder.append(item.getValue());
            }
            builder.append("\r\n");
        }

        builder.append("\r\n");


        String json = JsonUtil.serialize(getValues());
        builder.append(json);


        try {
            return builder.toString().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setHeader(String header,String value){
        headers.put(header,value);
    }

    public void removeHeader(String header){
        headers.remove(header);
    }

    public Map<String,String> getHeaders(){
        return Collections.unmodifiableMap(headers);
    }

    public void setValue(String name,Object value){
        Object v = values.get(name);
        if(v instanceof Field){
            try {
                ((Field)v).set(this.obj,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else {
            values.put(name, value);
        }
    }

    public Object getValue(String name){
        Object v = values.get(name);
        if(v instanceof Field){
            try {
                return ((Field)v).get(this.obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return v;

    }

    public void removeValue(String name){
        values.remove(name);
    }

    public Map<String,Object> getValues(){
        //return Collections.unmodifiableMap(values);
        Map<String,Object> maps = new HashMap<>();

        for(Map.Entry<String,Object> item : values.entrySet()){
            if(item.getValue() instanceof Field){
                try {
                    maps.put(item.getKey(),((Field)item.getValue()).get(this.obj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                maps.put(item.getKey(),item.getValue());
            }
        }
        return maps;
    }

}
