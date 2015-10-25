package lin.client.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import lin.util.ByteUtils;
import lin.util.thread.AutoResetEvent;

    public class Session
    {
        public Map<String, Object> attributes = new HashMap<String,Object>();// { get; private set; }


        
        private Object SendPackageLock = new Object();
        private long sequeue = 1;
        private  Socket socket;// { get; private set; }
//        private Communicate communicate;
        CommunicateRecv recv;
        Session(Communicate communicate, Socket socket)
        {
            if (communicate.isServer())
            {
                sequeue = 2;
            }
            this.socket = socket;
//            this.communicate = communicate;
        }

        public String getSessionId(){
        	return null;
        }


        private Object sequeueLock = new Object();
        public PackageResponse send(Package pack)
        {
            synchronized (sequeueLock)
            {
            	if (this.sequeue == 0)
                {
                    this.sequeue += 2;
                }
                pack.setSequeue(this.sequeue);
                this.sequeue += 2;

          }

   AutoResetEvent set = new AutoResetEvent(false);
   final PackageResponse r = new PackageResponse(set);
   recv.addRequest(pack.getSequeue(),new Response() {
	
	@Override
	public void response(Package pack) {
		r.response(pack);
	}
});
            this.sendImpl(pack,true);
            return r;
        }

        Response response(final Package pack)
        {
            return new Response() {
				
				@Override
				public void response(Package p) {
					p.setSequeue(pack.getSequeue());
	                Session.this.sendImpl(p,false);
				}
			};
        }

        private void sendImpl(Package pack,boolean isRequest)
        {
            //int size = pack.size();
            //byte[] bs = new byte[size];
            byte[] bs = pack.write();
            byte[] tmpBs = new byte[2 * bs.length + 3 + 18];	//14
            int pos = 0;
            //将包中的数据写入数组
            tmpBs[0] = (byte)0xC0;
            tmpBs[1] = pack.getType();
            if(isRequest){
            	tmpBs[2] = 0;
            }else{
            	tmpBs[2] = 1;
            }
            ByteUtils.writeLong(tmpBs, pack.getSequeue(), 3);
            pos = 11;

            for (int n = 0; n < bs.length; n++)
            {				//解析FF、FA
                if ((byte)bs[n] == 0xC0)
                {
                    tmpBs[pos] = (byte)0xDB;
                    pos++;
                    tmpBs[pos] = (byte)0xDC;
                    pos++;
                }
                else if ((byte)bs[n] == 0xDB)
                {
                    tmpBs[pos] = (byte)0xDB;
                    pos++;
                    tmpBs[pos] = (byte)0xDD;
                    pos++;
                }
                else
                {
                    tmpBs[pos] = bs[n];
                    pos++;
                }
            }
            tmpBs[pos] = (byte)0xC0;

            synchronized (this.SendPackageLock)
            {
                //this.socket.send(tmpBs, pos + 1, SocketFlags.None);
            	try {
					this.socket.getOutputStream().write(tmpBs, 0, pos+1);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }

        public void setAttribute(String name,Object value){
        	this.attributes.put(name, value);
        }
        
        public Object getAttriubte(String name){
        	return attributes.get(name);
        }
        
        public void removeAttribute(String name){
        	this.attributes.remove(name);
        }
        
        public Socket getSocket(){
        	return this.socket;
        }
        
    }
