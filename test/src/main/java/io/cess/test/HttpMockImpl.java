package io.cess.test;

import junit.framework.Assert;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.ref.WeakReference;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author lin
 * @date 10/03/2018 23:15
 */
public class HttpMockImpl {

    private Map<Object,Object> httpResult = new HashMap<>();
    private Map<Object,Error> httpError = new HashMap<>();
    private HttpMock.ResultListener mResultListener;
    private HttpMock.ErrorListener mErrorListener;

    private WeakReference<HttpCommunicateImpl> mOriginImpl;
    private HttpCommunicateImpl mMockImpl = null;
    private String mName;

    public HttpMockImpl(String name){
        this.mName = name;
        httpMock();
    }

    private synchronized HttpCommunicateImpl http(){

        HttpCommunicateImpl impl = mock(HttpCommunicateImpl.class);

        mMockImpl = impl;

        setImpl(impl);

        return impl;

    }

    private void setImpl(HttpCommunicateImpl impl){

        try {
            Field mImplsField = HttpCommunicate.class.getDeclaredField("mImpls");
            mImplsField.setAccessible(true);

            Map<String,WeakReference<HttpCommunicateImpl>> mImples = (Map<String, WeakReference<HttpCommunicateImpl>>) mImplsField.get(null);

            mOriginImpl = mImples.get(mName);

            WeakReference<HttpCommunicateImpl> mockItem = new WeakReference<HttpCommunicateImpl>(impl);

            mImples.put(mName,mockItem);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void revert(){
        if(mOriginImpl != null){
            setImpl(mOriginImpl.get());
        }
        clear();
    }

    public void clear(){
        httpResult.clear();
        httpError.clear();
        mResultListener = null;
        mErrorListener = null;
    }

    public void reset(){
        clear();
        httpMock();
    }

    private void httpMock(){
        HttpCommunicateImpl impl = http();
        when(impl.request(any(HttpPackage.class), any(io.cess.comm.http.ResultListener.class))).then(new HttpAnswer());
    }

    public void httpMock(HttpPackage pack,Object result){
        httpResult.put(pack,result);
    }

    public void httpMock(Class<? extends HttpPackage> cls,Object result){
        httpResult.put(cls,result);
    }

    public void httpMock(HttpPackage pack, Error error){
        httpError.put(pack,error);
    }

    public void httpMock(Class<? extends HttpPackage> cls,Error error){
        httpError.put(cls,error);
    }

    public void removeHttpMock(HttpPackage pack){
        httpResult.remove(pack);
        httpError.remove(pack);
    }

    public void removeHttpMock(Class<? extends HttpPackage> cls){
        httpResult.remove(cls);
        httpError.remove(cls);
    }

    public void setResultListener(HttpMock.ResultListener resultListener){
        httpMock();
        mResultListener = resultListener;
    }

    public void setErrorListener(HttpMock.ErrorListener errorListener){
        httpMock();
        mErrorListener = errorListener;
    }

    class HttpAnswer implements Answer<HttpCommunicateResult> {

        HttpAnswer(){
        }
        @Override
        public HttpCommunicateResult answer(InvocationOnMock invocationOnMock) throws Throwable {
            Object[] args = invocationOnMock.getArguments();
            HttpPackage pack = null;
            io.cess.comm.http.ResultListener listener = null;
            if(args != null){
                if(args.length > 0 && args[0] instanceof HttpPackage){
                    pack = (HttpPackage) args[0];
                }
                if(args.length > 1 && args[1] instanceof io.cess.comm.http.ResultListener) {
                    listener = (io.cess.comm.http.ResultListener) args[1];
                }
            }

            Assert.assertNotNull("http 请求 pack 对象不能为 null.",pack);

            Error error = httpError.get(pack);
            if(error == null){
                error = httpError.get(pack.getClass());
            }

            if(error == null && mErrorListener != null){
                error = mErrorListener.error(pack);
            }

            boolean isSuccess = false;
            Object result = null;
            if(error == null){
                isSuccess = true;

                result = httpResult.get(pack);
                if(result == null){
                    result = httpResult.get(pack.getClass());
                }

                if(result == null && mResultListener != null){
                    result = mResultListener.result(pack);
                }
                if(listener != null){
                    listener.result(result,null);
                }
            }else{
                listener.fault(error);
            }

            HttpCommunicateResult httpResult = new HttpCommunicateResult();
            httpResult.setResult(isSuccess,result,null,error);
            set(httpResult);
            return httpResult;
        }
    }

    private static Method getAutoResetEventMethod;
    private static void set(HttpCommunicateResult result) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(getAutoResetEventMethod == null){
            getAutoResetEventMethod = HttpCommunicateResult.class.getDeclaredMethod("getAutoResetEvent");
            getAutoResetEventMethod.setAccessible(true);
        }

        AutoResetEvent set = (AutoResetEvent) getAutoResetEventMethod.invoke(result);
        set.set();
    }
}
