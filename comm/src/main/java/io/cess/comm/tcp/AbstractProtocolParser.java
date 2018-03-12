package io.cess.comm.tcp;

/**
 * @author lin
 * @date 1/26/16.
 */
public abstract class AbstractProtocolParser implements ProtocolParser {

    protected byte[] buffer = new byte[100];
    private int bufferInterval = 100;
    protected int count = 0;

    protected PackageState state = PackageState.REQUEST;

    /// <summary>
    /// 单线程访问，不用考虑多线程问题
    /// </summary>
    /// <param name="bs"></param>
    final public void put(byte...bs)
    {
        this.expansion();
        for (int n = 0; n < bs.length; n++)
        {
            buffer[count++] = bs[n];
        }
    }

    @Override
    public void setState(PackageState state) {
        this.state = state;
    }

    @Override
    public PackageState getState() {
        return this.state;
    }

    private void expansion()
    {
        if (count >= buffer.length)
        {
            byte[] tmp = new byte[buffer.length + bufferInterval];
            for (int n = 0; n < buffer.length; n++)
            {
                tmp[n] = buffer[n];
            }
            buffer = tmp;
        }

    }

    final public void clear()
    {
        count = 0;
    }
}
