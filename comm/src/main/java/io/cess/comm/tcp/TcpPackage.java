package io.cess.comm.tcp;

/**
 * @author lin
 * @date 1/28/16.
 */
public interface TcpPackage {

    byte getType();

    PackageState getState();


    long getSequeue();


    byte[] write();// byte[] bs, int offset = 0);
}
