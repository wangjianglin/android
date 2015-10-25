package lin.client.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import lin.util.ByteUtils;


class CommunicateRecv
    {

	private static Map<Byte, Class<?>> protocolParsers = new HashMap<Byte, Class<?>>();
        static
        {
//            IList<AttributeToken<ProtocolParserType>> tokens = Lin.Util.Assemblys.AssemblyStore.FindAllAttributesForCurrentDomain<ProtocolParserType>(true);
//            //Console.WriteLine("count:" + tokens.Count);
//            foreach (AttributeToken<ProtocolParserType> token in tokens)
//            {
//                Communicate.ProtocolParsers[token.Attribute.Type] = token.OwnerType;
//            }
        	protocolParsers.put((byte)1, CommandProtocolParser_010.class);
        	protocolParsers.put((byte)6, JsonProtocolParser.class);
        	protocolParsers.put((byte)0, ErrorPackageParser.class);
        }
        private CommunicateListener listener;
//        private Communicate communicate;
        //private ISessionListener sessionListener;
        private Session session;
        public CommunicateRecv(Communicate communicate, Session session, CommunicateListener listener)
        {
//            this.communicate = communicate;
            this.listener = listener;
            this.session = session;
            //this.sessionListener = sessionListener;
        }
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
			} catch (Throwable e) {
				e.printStackTrace();
			}
            //parser = (IProtocolParser)Activator.CreateInstance(Communicate.ProtocolParsers[b]);
//            protocolParserInts[b] = parser;
            return parser;
        }

        void addRequest(long sequeue,Response response){
            sequeues.put(sequeue,response);
        }
        //private void CommunicateListener(Session session,Package package,Response pesonse){
        /// <summary>
        /// 无需考虑线程安全问题
        /// </summary>
        private Map<Long, Response> sequeues = new HashMap<Long, Response>();
//        private boolean isResponse = false;//改为线程变量
        private ThreadLocal<Boolean> isResponse = new  ThreadLocal<Boolean>();
        private void CommunicateListener(final Package pack)
        {
        	//采用线程池技术
        	new Thread(new Runnable() {
				
				@Override
				public void run() {
					CommunicateListenerImpl(pack);
				}
			}).start();
        }
        private void CommunicateListenerImpl(Package pack){
            
//            if (r != null)
//            {
//            	pack.setState(PackageState.RESPONSE);
//            }
//            else
//            {
//            	pack.setState(PackageState.REQUEST);
//            }
        	isResponse.set(false);
            final Response sr = this.session.response(pack);
            if (this.listener != null)
            {
                //try {
            	if(pack.getState() == PackageState.REQUEST){
                    listener.listener(this.session, pack, new Response() {
						
						@Override
						public void response(Package pack) {
							isResponse.set(true); 
							sr.response(pack);
						}
					});
            	}else{
            		listener.listener(this.session, pack, null);
            	}
                //}
                //finally
                //{
                   
                //}
            }
            if (pack.getState() == PackageState.RESPONSE)
            {
//            	Response r = sequeues.get(pack.getSequeue());
            	Response r = sequeues.remove(pack.getSequeue());
            	if(r != null){
            		r.response(pack);
            	}
            }
            else
            {
                if (!isResponse.get())
                {
                    sr.response(new ErrorPackage());
                }
            }
        }

        
        private static int bufferSize = 2048;
        byte dataType = 0;//数据类型

        //bool state = false;//0表示还没有开始读数，true表示正在读取数据,
        //bool isC0 = false;//数据结束标记
        boolean isDB = false;//表示前一个数据是否为0xDB
        //bool isFA = false;//0表示前一个数据不是0xFA,true表示前一个数据是0xFA
        boolean isFirst = true;//表示当前为此数据表的第一个数据
        //bool isFirstNew = false;
        //int length = 0;//用以记录数据的长度



        ProtocolParser parser = null;


        int sequeueCount = 0;
        long sequeue = 0;
        
        void recvData()
        {
            
            //byte versionSize;
            //char[] ch = new char[bufferSize];
            byte[] ch = new byte[bufferSize];
           

            byte[] sequeueBytes = new byte[9];//请求标识和存储序列号
            int n;
            
            //数据产生异常
//            java.lang.Runnable initStatue = () ->
//            {
//                //isC0 = false;
//                isDB = false;
//                isFirst = true;
//                sequeueCount = 0;
//                //packageSize = 0;
//                if (parser != null)
//                {
//                    parser.clear();
//                }
//                parser = null;
//            };
            java.lang.Runnable initStatue = new Runnable() {
				
				@Override
				public void run() {
					isDB = false;
	                isFirst = true;
	                sequeueCount = 0;
	                //packageSize = 0;
	                if (parser != null)
	                {
	                    parser.clear();
	                }
	                parser = null;
				}
			};
            //PACKAGELISTENER listener;
            //MLog LogFile;
            //LogFile.InitAndWriteLog("communicate listener调用成功");
            InputStream _in = null;
			try {
				_in = this.session.getSocket().getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
            while (true)
            {
                //int num = Socket::recv(client,ch,bufferSize,0);
                int num = 0;
				try {
					num = _in.read(ch);
				} catch (IOException e) {
					//e.printStackTrace();
				}
                //		cout << "received data numbers=" << num << endl;
                if (num <= 0)
                {//连接已经断开
                    //LogFile.InitAndWriteLog("前台客户端退出 socket close");
                    //cout<<"socket close..."<<endl;
                    break;
                }

                for (n = 0; n < num; n++)
                {
                    if (ch[n] == (byte)0xC0)
                    {//如果isCo0为true，表示数据结束
                        //isC0 = false;
                        //if (packageSize != 0)
                        //{
                        //if (listener != null && parser != null)
                        if(parser != null)
                        {
                            //Package pack = PackageManager.Parse(packageBody);
                            Package pack = parser.getPackage();
                            //Utils.Read(sequeueBytes, out sequeue);
                            if(sequeueBytes[0] == 0){
                            	pack.setState(PackageState.REQUEST);
                            }else{
                            	pack.setState(PackageState.RESPONSE);
                            }
                            sequeue = ByteUtils.readLong(sequeueBytes,1);
                            pack.setSequeue(sequeue);
                            initStatue.run();
                            //listener(null,pack,this.session.Response(pack));
                            CommunicateListener(pack);
                        }
                        continue;
                        //}
                    }

                    if (ch[n] == (byte)0xDB)
                    {
                        if (isDB)
                        {
                            //异常
                            initStatue.run();
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
                            initStatue.run();
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
