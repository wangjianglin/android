package lin.comm.tcp;

import lin.comm.tcp.annotation.ProtocolParserType;
import lin.util.Bytes;


@ProtocolParserType(1)
public class CommandProtocolParser_010 extends AbstractProtocolParser
{
//    private static Map<Integer, Class<?>> commands = new HashMap<Integer, Class<?>>();

    static
    {
//        commands.put(1, DetectTcpPackage.class);
        CommandPackageManager.regist(DetectTcpPackage.class);
    }

    public TcpPackage getPackage()
    {

        CommandTcpPackage pack = null;
        try {
            int command = Bytes.readInt(buffer,3);
//            pack = (CommandTcpPackage) commands.get(command).newInstance();
            if (state == PackageState.REQUEST){
                pack = CommandPackageManager.newRequestInstance(command);
            }else{
                pack = CommandPackageManager.newResponseInstance(command);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        pack.parse(buffer,11);
//        pack.setMajor((byte)0);
//        pack.setMinor((byte)1);
//        pack.setRevise((byte)0);
        return pack;
    }

}
