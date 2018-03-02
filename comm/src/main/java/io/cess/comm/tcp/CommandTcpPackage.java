package io.cess.comm.tcp;


public interface CommandTcpPackage extends TcpPackage//ICommandPackageParser,
{

    int getSize();

    int getCommand();

    byte getMajor();

    byte getMinor();

    byte getRevise();


    void parse(byte[] bs);

    void parse(byte[] bs,int offset);

}

