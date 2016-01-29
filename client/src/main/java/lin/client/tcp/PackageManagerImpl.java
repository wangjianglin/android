package lin.client.tcp;

import lin.client.tcp.annotation.RespType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lin on 1/28/16.
 */
class PackageManagerImpl<K,Q extends RequestTcpPackage,R extends ResponseTcpPackage> {

    private Map<K,Class<Q>> requests = new HashMap<K,Class<Q>>();
    private Map<K,Class<R>> responses = new HashMap<K,Class<R>>();
    private Map<String,K> responseKeys = new HashMap<String,K>();

    private lin.util.Function<K,Class<?>> toKey;
    PackageManagerImpl(lin.util.Function<K,Class<?>> toKey){
        this.toKey = toKey;
    }

    void regist(Class<Q> cls){

        if(cls == null){
            return;
        }

        K key = toKey.function(cls);

        if (key == null){
            return;
        }

        requests.put(key,cls);

        Class<? extends ResponseTcpPackage> respType = getRespType(cls);
        if(respType != null){
            responses.put(key, (Class<R>) respType);
            responseKeys.put(respType.getName(),key);
        }

    }

    private Class<? extends ResponseTcpPackage> getRespType(Class<?> cls){
        if(cls == null
                || cls == RequestTcpPackage.class
                || cls == Object.class ){
            return null;
        }

        RespType respType = cls.getAnnotation(RespType.class);

        if(respType != null){
            return respType.value();
        }

        return getRespType(cls.getSuperclass());
    }

//    private String clsToString(Class<?> cls){
//        return cls.getPackage()+":"+cls.getName();
//    }

    K request(Class<?> cls){
        return responseKeys.get(cls.getName());
    }

    Q newRequestInstance(K key){

        Class<Q> cls = requests.get(key);
        if (cls == null){
            return null;
        }
        try {
            return  cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    R newResponseInstance(K key){

        Class<R> cls = responses.get(key);
        if (cls == null){
            return null;
        }
        try {
            return  cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
