package io.cess.comm.tcp;

/**
 * Created by lin on 1/26/16.
 */
public class EmptyTcpPackage extends ResponseTcpPackage {
    @Override
    public byte getType() {
        return (byte)255;
    }

    @Override
    public byte[] write() {
        return new byte[0];
    }
}
