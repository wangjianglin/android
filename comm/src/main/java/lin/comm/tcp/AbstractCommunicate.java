package lin.comm.tcp;

/**
 * Created by lin on 1/21/16.
 */
public abstract class AbstractCommunicate implements Communicate{


    // public static readonly Lin.Util.MapIndexProperty<byte, Type>
    // ProtocolParsers = new Util.MapIndexProperty<byte, Type>();
    // public static Lin.Util.MapIndexProperty<byte, Type> ProtocolParsers {
    // get; private set; }
//    public Class<?> getProtocolParser(byte type) {
//        return null;
//    }


    protected abstract void init();


    public void reconnection() {
        this.close();
        this.init();
    }
}