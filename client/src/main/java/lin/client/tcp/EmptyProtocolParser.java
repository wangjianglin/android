package lin.client.tcp;

import lin.client.tcp.annotation.ProtocolParserType;

/**
 * Created by lin on 1/26/16.
 */
@ProtocolParserType((byte)255)
public class EmptyProtocolParser extends AbstractProtocolParser {
    @Override
    public TcpPackage getPackage() {
        return new EmptyTcpPackage();
    }
}
