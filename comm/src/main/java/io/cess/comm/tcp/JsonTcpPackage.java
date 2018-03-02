package io.cess.comm.tcp;

import java.util.Map;

public interface JsonTcpPackage extends TcpPackage
{

    String getPath();


    void setHeader(String header,String value);

    void removeHeader(String header);

    Map<String,String> getHeaders();

    void setValue(String name,Object value);

    Object getValue(String name);

    void removeValue(String name);

    Map<String,Object> getValues();
}

//public class JsonTcpPackage extends TcpPackage
//{
//
//    private String path = "/";
//    private Map<String,String> headers = new HashMap<>();
//    private Map<String,Object> values = new HashMap<>();
//
//    public JsonTcpPackage()
//    {
//        init();
//    }
//
//    private void init(){
//        Class<?> cls = this.getClass();
//        Path pathAnnon = cls.getAnnotation(Path.class);
//        if(pathAnnon != null){
//            path = pathAnnon.path();
//        }
//
//        Field[] fs = cls.getDeclaredFields();
//        Param param = null;
//        String paramName = null;
//        for(Field f : fs){
//            param = f.getAnnotation(Param.class);
//            if(param != null){
//                paramName = param.value();
//                if(paramName == null || "".equals(paramName.trim())){
//                    paramName = f.getName();
//                }
//                f.setAccessible(true);
//                values.put(paramName,f);
//            }
//        }
//    }
//
//    void setValues(Object obj){
//        if(obj instanceof Map){
//            values.putAll((Map<String,Object>)obj);
//        }
//    }
//
//    void setHeaders(Map<String,String> headers){
//        this.headers.putAll(headers);
//    }
//
//    @Override
//    public final byte getType()
//    { return 6;
//    }
//
//    public String getPath() { return path; }
//
//    @Override
//    public final byte[] write()
//    {
//
//        StringBuilder builder = new StringBuilder();
//
//        //path
//        builder.append("path:");
//        builder.append(this.getPath());
//        builder.append("\r\n");
//        //coding
//        builder.append("encoding:");
//        //builder.append(Encoding.Default.BodyName);
//        builder.append("\r\n");
//
////        builder.append("version:0.1.0build0");
////        builder.append("\r\n");
//
//        for(Map.Entry<String,String> item:headers.entrySet()){
//            builder.append(item.getKey());
//            builder.append(':');
//            if(item.getValue() != null){
//                builder.append(item.getValue());
//            }
//            builder.append("\r\n");
//        }
//
//        builder.append("\r\n");
//
//
//        String json = JsonUtil.serialize(getValues());
//        builder.append(json);
//
//
//        try {
//            return builder.toString().getBytes("utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    public void setHeader(String header,String value){
//        headers.put(header,value);
//    }
//
//    public void removeHeader(String header){
//        headers.remove(header);
//    }
//
//    public Map<String,String> getHeaders(){
//        return Collections.unmodifiableMap(headers);
//    }
//
//    public void setValue(String name,Object value){
//        Object v = values.get(name);
//        if(v instanceof Field){
//            try {
//                ((Field)v).set(this,value);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }else {
//            values.put(name, value);
//        }
//    }
//
//    public Object getValue(String name){
//        Object v = values.get(name);
//        if(v instanceof Field){
//            try {
//                return ((Field)v).get(this);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return v;
//
//    }
//
//    public void removeValue(String name){
//        values.remove(name);
//    }
//
//    public Map<String,Object> getValues(){
//        //return Collections.unmodifiableMap(values);
//        Map<String,Object> maps = new HashMap<>();
//
//        for(Map.Entry<String,Object> item : values.entrySet()){
//            if(item.getValue() instanceof Field){
//                try {
//                    maps.put(item.getKey(),((Field)item.getValue()).get(this));
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                maps.put(item.getKey(),item.getValue());
//            }
//        }
//        return maps;
//    }
//}
