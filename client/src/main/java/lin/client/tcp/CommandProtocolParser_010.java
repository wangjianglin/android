package lin.client.tcp;

import java.util.HashMap;
import java.util.Map;



@ProtocolParserType(type=1)
    public class CommandProtocolParser_010 implements ProtocolParser
    {
        private static Map<Integer, Class<?>> commands = new HashMap<Integer, Class<?>>();

        static
        {
//            IList<Type> types = Lin.Util.Assemblys.AssemblyStore.FindTypesForCurrentDomain<CommandPackage>();
//            //CommandPackage parser = null;
//            foreach (Type type in types)
//            {
//                if (!type.IsAbstract)
//                {
//                    try
//                    {
//                        //parser = System.Activator.CreateInstance(type) as CommandPackage;
//                        commands[type.GetCustomAttribute<Command>().Commaand] = type;
//                    }
//                    catch (Exception e)
//                    {
//                        Console.WriteLine();
//                        Console.WriteLine("type:" + type.Name);
//                        Console.WriteLine(e.StackTrace);
//                    }
//                }
//            }
        	commands.put(1, DetectPackage.class);
        }

        int packageSize = 0;//已经读取的数据个数
        int maxPackageSize = 100;//packageBody的大小、
        byte[] packageBody = new byte[100];//存储消息，包括消息头体
        

        CommandPackageMessageHeader messageHeader = new CommandPackageMessageHeader();

        byte[] header = new byte[11];//BYTE header[16];//存储消息头	//2013-08-07 liyk
        public Package getPackage()
        {
            //throw new NotImplementedException();
            //return CommandPackageManager.Parse(this.packageBody);
            //int num;
            //byte num2;
            //byte num3;
            //byte num4;
            CommandPackage pack = null;
            try {
				pack = (CommandPackage) commands.get(messageHeader.getCommand()).newInstance();
			} catch (Throwable e) {
				e.printStackTrace();
			}
//            package = Activator.CreateInstance(commands[messageHeader.command]) as CommandPackage;
            pack.parser(this.packageBody);
            pack.setMajor((byte)0);
            pack.setMinor((byte)1);
            pack.setRevise((byte)0);
            return pack;
        }

        /// <summary>
        /// 单线程访问，不用考虑多线程问题
        /// </summary>
        /// <param name="bs"></param>
        public void put(byte ... bs)
        {
            for (int n = 0; n < bs.length; n++)
            {
                putImpl(bs[n]);
            }
        }

        private void putImpl(byte b){
            if (packageSize < header.length)
            {//packageSize=19时，跳到packageSize++,读出length，下一次进入下一个if
                header[packageSize] = b;
            }
            if (packageSize >= header.length)
            {
                if (packageSize >= messageHeader.getLength())
                {//数据异常
                    //LogFile.InitAndWriteLog("读取文件头19字节时异常, length is %d",length);
                    //state = false;
                    //isFF = false;
                    //isFA = false;
                    //packageSize = 0;
                    //initStatue();
                    //continue;
                    return;
                }
                //					delete [] packageBody;
                packageBody[packageSize - header.length] = b;
            }
            packageSize++;

            if (packageSize == header.length)
            {
                //解析消息头
                messageHeader.read(header);
                if (messageHeader.getLength() < header.length)
                {//数据异常，抛弃些数据段
                    //initStatue();
                }
                else
                {
                    //避免频繁释放内存
                    if (messageHeader.getLength() - header.length >= maxPackageSize)
                    {
                        packageBody = new byte[messageHeader.getLength() - header.length];
                        maxPackageSize = messageHeader.getLength() - header.length;
                    }
                    //CommunicateSocketListener_body(&packageBody,length);
                    //for (int n1 = 0; n1 < header.Length; n1++)
                    //{
                    //    packageBody[n1] = header[n1];
                    //}
                }
            }
        }

        public void clear()
        {
            packageSize = 0;
        }
    }
