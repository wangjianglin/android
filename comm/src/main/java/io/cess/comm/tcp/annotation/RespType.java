package io.cess.comm.tcp.annotation;

import io.cess.comm.tcp.ResponseTcpPackage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lin on 1/28/16.
 */

@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RespType {
//    public Class<T extends ResponseTcpPackage> value();
    public Class<? extends ResponseTcpPackage> value();
}
