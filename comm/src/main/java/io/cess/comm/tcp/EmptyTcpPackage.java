package io.cess.comm.tcp;

/**
 * @author lin
 * @date 1/26/16.
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
