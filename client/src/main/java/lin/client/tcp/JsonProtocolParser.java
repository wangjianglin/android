package lin.client.tcp;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@ProtocolParserType(type=6)
public class JsonProtocolParser implements ProtocolParser
    {
        //private static IDictionary<string, Type> paths = new Dictionary<string, Type>();
        private static Map<String, Class<?>> paths = new HashMap<String, Class<?>>();
        static
        {
        	byte[] end_flag = null;
        	try {
        		end_flag = "\r\n\r\n".getBytes("ascii");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        	END_FLAG = end_flag;
//            LINE_FLAG = "\r\n\r\n".getBytes("assic");
            byte[] line_flag = null;
        	try {
        		line_flag = "\r\n".getBytes("ascii");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        	LINE_FLAG = line_flag;
//            IList<Type> types = Lin.Util.Assemblys.AssemblyStore.FindTypesForCurrentDomain<JsonPackage>();
//            foreach (Type type in types)
//            {
//                if (!type.IsAbstract)
//                {
//                    try
//                    {
//                        //parser = System.Activator.CreateInstance(type) as CommandPackage;
//                        //CommandPackageManager.RegisterPackage(type.GetCustomAttribute<Command>().Commaand, type);
//                        paths[type.GetCustomAttribute<JsonPath>().Path]= type;
//                    }
//                    catch (Exception e)
//                    {
//                        Console.WriteLine();
//                        Console.WriteLine("type:" + type.Name);
//                        Console.WriteLine(e.StackTrace);
//                    }
//                }
//            }
        	paths.put("/json/test", JsonTestPackage.class);
        }

        /// <summary>
        /// 头结束标识
        /// </summary>
        @SuppressWarnings("unused")
		private static final byte[] END_FLAG;// = null;//"\r\n\r\n".getBytes("assic");
        /// <summary>
        /// 换行标识
        /// </summary>
        private static final byte[] LINE_FLAG;// = null;//Encoding.Default.GetBytes("\r\n");


        public Package getPackage()
        {
            //throw new NotImplementedException();

            //Type type = this.GetType();
            //Console.WriteLine("json type:" + jsonType.Type.Name);
            //byte[] bs = builder.
            //IDictionary<string, string> headers = new Dictionary<string, string>();
            Map<String, String> headers = new HashMap<String, String>();
            int end = 0;
            int start = 0;
            for (int n = 0; n < count - 2; n++)
            {
                if (buffer[n] == LINE_FLAG[0]
                    && buffer[n + 1] == LINE_FLAG[1])
                {
                    end = n;
                    if (start == end)
                    {
                        start = end = end + 2;
                        break;
                    }
                    String tmp = null;
					try {
						tmp = new String(buffer, start, end-start,"ascii");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
//                    String tmp = Encoding.Default.GetString(buffer, start, end-start);
                    String[] tmp2 = tmp.split(":");
//                    headers[tmp2[0]] = tmp2[1];
                    if(tmp2.length == 1){
                    	headers.put(tmp2[0], "");
                    }else{
                    	headers.put(tmp2[0], tmp2[1]);
                    }
                    start = end = end + 2;
                }
            }
            try{
            @SuppressWarnings("unused")
			String json = new String(buffer, start, count - start,"utf-8");
            JsonPackage pack = (JsonPackage) paths.get(headers.get("path")).newInstance();// Activator.CreateInstance(paths[headers["path"]]) as JsonPackage;
//            package.Parser(json);
            return pack;
            }catch(Throwable e){
            	
            }
            return null;
        }
        //private StringBuilder builder = new StringBuilder();
        private byte[] buffer = new byte[100];
        private int bufferInterval = 100;
        private int count = 0;

        /// <summary>
        /// 单线程访问，不用考虑多线程问题
        /// </summary>
        /// <param name="bs"></param>
        public void put(byte...bs)
        {
            //builder.Append(bs);
            this.Expansion();
            for (int n = 0; n < bs.length; n++)
            {
                buffer[count++] = bs[n];
            }
        }


        private void Expansion()
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

        public void clear()
        {
            //builder.Clear();
            count = 0;
        }
    }
