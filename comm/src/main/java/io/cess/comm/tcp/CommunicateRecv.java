package io.cess.comm.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.cess.util.Bytes;


class CommunicateRecv
{

    private static Map<Byte, Class<?>> protocolParsers = new HashMap<Byte, Class<?>>();
    static
    {
        protocolParsers.put((byte)1, CommandProtocolParser_010.class);
        protocolParsers.put((byte)6, JsonProtocolParser.class);
        protocolParsers.put((byte)0, ErrorPackageParser.class);
        protocolParsers.put((byte)255, EmptyProtocolParser.class);
    }
    private CommunicateListener listener;

    private Session session;
    public CommunicateRecv(Communicate communicate, Session session, CommunicateListener listener)
    {

        this.listener = listener;
        this.session = session;
    }

    //每个revc需要一个解析器实例,因为解析器是有状态的
    private Map<Byte, ProtocolParser> protocolParserInts = new HashMap<Byte, ProtocolParser>();
    private ProtocolParser getProtocolParser(byte b)
    {
        ProtocolParser parser = protocolParserInts.get(b);
        if (parser != null)
        {
            return parser;
        }
        try {
            parser = (ProtocolParser) protocolParsers.get(b).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        protocolParserInts.put(b,parser);
        return parser;
    }

    void addRequest(long sequeue,Response response){
        sequeues.put(sequeue,response);
    }
    //private void CommunicateListener(Session session,TcpPackage package,Response pesonse){
    /// <summary>
    /// 无需考虑线程安全问题
    /// </summary>
    private Map<Long, Response> sequeues = new HashMap<Long, Response>();
//        private boolean isResponse = false;//改为线程变量
    private ThreadLocal<Boolean> isResponse = new  ThreadLocal<Boolean>();
    private void CommunicateListener(final TcpPackage pack)
    {
        //采用线程池技术
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommunicateListenerImpl(pack);
            }
        }).start();
    }
    private void CommunicateListenerImpl(final TcpPackage pack){

        isResponse.set(false);//响应不能在其他线程上面进行响应，除非手动调用异步
        //Response sr = this.session.response(pack);
        if (this.listener != null)
        {

            if(pack.getState() == PackageState.REQUEST){
                listener.listener(this.session, pack, new Response() {
                    @Override
                    public void response(ResponseTcpPackage p) {
                        isResponse.set(true);
//                    sr.response(p);
                        p.setSequeue(pack.getSequeue());
                        session.response(p);
                    }
                });
            }else{
                listener.listener(this.session, pack, new Response() {
                    @Override
                    public void response(ResponseTcpPackage pack) {

                    }
                });
            }
        }
        if (pack.getState() == PackageState.RESPONSE)
        {

            Response r = sequeues.remove(pack.getSequeue());
            if(r != null){
                r.response((ResponseTcpPackage) pack);
            }
        }
        else
        {
            if (!isResponse.get())
            {
                //sr.response(new EmptyTcpPackage());
                EmptyTcpPackage empty = new EmptyTcpPackage();
                empty.setSequeue(pack.getSequeue());

                session.response(empty);
            }
        }
    }


    private static int bufferSize = 2048;
    byte dataType = 0;//数据类型


    boolean isDB = false;//表示前一个数据是否为0xDB

    boolean isFirst = true;//表示当前为此数据表的第一个数据


    ProtocolParser parser = null;

    private int sequeueCount = 0;
    private long sequeue = 0;

    void recvData()
    {

        byte[] ch = new byte[bufferSize];

        byte[] sequeueBytes = new byte[9];//请求标识和存储序列号
        int n;

        //数据产生异常
        io.cess.util.Action initStatue = new io.cess.util.Action(){

            public void action() {
                isDB = false;
                isFirst = true;
                sequeueCount = 0;

                if (parser != null) {
                    parser.clear();
                }
                parser = null;
            }
        };

        InputStream _in = null;
        try {
            _in = this.session.getSocket().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true)
        {

            int num = 0;
            try {
                num = _in.read(ch);
            } catch (IOException e) {
                //e.printStackTrace();
            }

            if (num <= 0)
            {//连接已经断开

                break;
            }

            for (n = 0; n < num; n++)
            {
                if (ch[n] == (byte)0xC0)
                {
                    if(parser != null)
                    {
                        //TcpPackage pack = PackageManager.Parse(packageBody);
                        if (sequeueBytes[0] == 0){
                            parser.setState(PackageState.REQUEST);
                        }else{
                            parser.setState(PackageState.RESPONSE);
                        }
                        AbstractTcpPackage pack = (AbstractTcpPackage) parser.getPackage();
                        //Utils.Read(sequeueBytes, out sequeue);
//                        if(sequeueBytes[0] == 0){
//                            pack.setState(PackageState.REQUEST);
//                        }else{
//                            pack.setState(PackageState.RESPONSE);
//                        }
                        sequeue = Bytes.readLong(sequeueBytes,1);
                        pack.setSequeue(sequeue);
                        initStatue.action();

                        CommunicateListener(pack);
                    }
                    continue;

                }

                if (ch[n] == (byte)0xDB)
                {
                    if (isDB)
                    {
                        //异常
                        initStatue.action();
                        continue;
                    }
                    isDB = true;
                    continue;
                }
                if (isDB == true)//如果前一个数据为0xDB，则需要进行数据转义
                {
                    isDB = false;
                    if (ch[n] == (byte)0xDC)
                    {
                        ch[n] = (byte)0xC0;
                    }
                    else if (ch[n] == (byte)0xDD)
                    {
                        ch[n] = (byte)0xDB;
                    }
                    else
                    {
                        //异常，回到初始状态
                        initStatue.action();
                        continue;
                    }
                }
                if (isFirst)
                {
                    dataType = (byte)ch[n];
                    parser = this.getProtocolParser(dataType);
                    isFirst = false;
                    continue;
                }
                if (sequeueCount < sequeueBytes.length)
                {
                    sequeueBytes[sequeueCount++] = ch[n];
                    continue;
                }
                if (parser != null)
                {
                    parser.put(ch[n]);
                }
            }
        }
    }

}
