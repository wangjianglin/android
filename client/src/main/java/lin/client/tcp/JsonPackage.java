package lin.client.tcp;

import java.io.UnsupportedEncodingException;

import lin.util.json.JSONException;
import lin.util.json.JSONUtil;

    public abstract class JsonPackage extends Package
    {

        private String path = "/json/test/";
        public JsonPackage()
        {
            //this._path = this.GetType().GetCustomAttribute<JsonPath>().Path;
        }
        
        public void parser(String json)
        {

//            MethodInfo[] ms = typeof(Lin.Util.Json.JsonUtil).GetMethods();
//            //Console.WriteLine("ms:" + ms);
//            MethodInfo deserializeMethod = null;
//            foreach(MethodInfo m in ms){
//                if (m.Name == "Deserialize" && m.IsGenericMethod && m.ReturnParameter.ParameterType.IsGenericParameter && m.GetParameters().Length == 1 && m.GetParameters()[0].ParameterType == typeof(string))
//                {
//                    deserializeMethod = m;
//                    break;
//                }
//            }
//
//            PropertyInfo pi = this.GetType().GetProperty("Params", BindingFlags.NonPublic | BindingFlags.Instance);
//            JsonParamsType jsonType = pi.GetCustomAttribute<JsonParamsType>();
//            deserializeMethod = deserializeMethod.MakeGenericMethod(jsonType.Type);
//            object[] args = new object[] { json };
//            object obj = deserializeMethod.Invoke(null, args);
//
//            this.Params = obj;
        }
        
        @Override
        public final byte getType()
        { return 6; 
        }

        public String getPath() { return path; } 

        @Override
        public final byte[] write()
        {
            String json = null;
			try {
				json = JSONUtil.serialize(this.getParams());
			} catch (JSONException e) {
				e.printStackTrace();
			}
            //return json.GetType()
            StringBuilder builder = new StringBuilder();

            //path
            builder.append("path:");
            builder.append(this.getPath());
            builder.append("\r\n");
            //coding
            builder.append("coding:");
            //builder.append(Encoding.Default.BodyName);
            builder.append("\r\n");
            //sequeue id
            //builder.Append("sequeue id:");
            //builder.Append("0");
            //builder.Append("\r\n");

            //version
            builder.append("version:0.1.0build0");
            builder.append("\r\n");

            //end
            builder.append("\r\n");
            builder.append(json);

            //return Encoding.Default.GetBytes(builder.ToString());
            try {
				return builder.toString().getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
            return new byte[0];
        }

        protected abstract Object getParams();
        //public abstract void test();
    }
