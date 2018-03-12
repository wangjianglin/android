package io.cess.test;

import junit.framework.Assert;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.cess.comm.http.Error;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpCommunicateResult;
import io.cess.comm.http.HttpPackage;
import io.cess.util.thread.AutoResetEvent;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by lin on 07/07/2017.
 */

public class HttpMock {


    private static HttpMockImpl mGlobal = new HttpMockImpl("global");

    private HttpMock(){}

    public static void revert(){
        mGlobal.revert();
    }

    public static void clear(){
        mGlobal.clear();
    }

    public static void reset(){
        mGlobal.reset();
    }

    public static void httpMock(HttpPackage pack,Object result){
        mGlobal.httpMock(pack,result);
    }

    public static void httpMock(Class<? extends HttpPackage> cls,Object result){
        mGlobal.httpMock(cls,result);
    }

    public static void httpMock(HttpPackage pack, Error error){
        mGlobal.httpMock(pack,error);
    }

    public static void httpMock(Class<? extends HttpPackage> cls,Error error){
        mGlobal.httpMock(cls,error);
    }

    public static void removeHttpMock(HttpPackage pack){
        mGlobal.removeHttpMock(pack);
    }

    public static void removeHttpMock(Class<? extends HttpPackage> cls){
        mGlobal.removeHttpMock(cls);
    }

    public static void setResultListener(ResultListener resultListener){
        mGlobal.setResultListener(resultListener);
    }

    public static void setErrorListener(ErrorListener errorListener){
        mGlobal.setErrorListener(errorListener);
    }

    public interface ResultListener {
        Object result(HttpPackage pack);
    }

    public interface ErrorListener{
        Error error(HttpPackage pack);
    }

}
