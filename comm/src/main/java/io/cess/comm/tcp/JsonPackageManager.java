package io.cess.comm.tcp;

import io.cess.comm.tcp.annotation.Path;

/**
 * @author lin
 * @date 1/28/16.
 */
public class JsonPackageManager {
//    private static PackageManagerImpl<String,JsonRequestTcpPackage,JsonResponseTcpPackage> impl = new
//            PackageManagerImpl<>(cls ->{
//        return getPath(cls);
//    });

    private static PackageManagerImpl<String,JsonRequestTcpPackage,JsonResponseTcpPackage> impl = new
            PackageManagerImpl<String,JsonRequestTcpPackage,JsonResponseTcpPackage>(new io.cess.util.Function<String,Class<?>>(){

        @Override
        public String function(Class<?> cls) {
            return getPath(cls);
        }

    });

    private static String getPath(Class<?> cls){
        if(cls == null
                || cls == CommandRequestTcpPackage.class
                || cls == Object.class ){
            return "";
        }

        Path path = cls.getAnnotation(Path.class);

        if(path != null){
            return path.value();
        }

        return getPath(cls.getSuperclass());
    }

    public static void regist(Class<? extends JsonRequestTcpPackage> cls){
        impl.regist((Class<JsonRequestTcpPackage>) cls);
    }

    public static String request(Class<? extends JsonResponseTcpPackage> cls){
        String path = impl.request(cls);
        if(path == null || "".equals(path)){
            return "";
        }
        return path;
    }

    public static JsonRequestTcpPackage newRequestInstance(String key){

        return impl.newRequestInstance(key);
    }

    public static JsonResponseTcpPackage newResponseInstance(String key){

        return impl.newResponseInstance(key);
    }
}
