import org.apache.mina.common.IoConnector;
import org.apache.mina.transport.socket.nio.DatagramConnector;

public class TestUdpClient {

    private IoConnector ioConnector;

    public static void main(String[] args) {
        TestUdpClient test = new TestUdpClient();
        test.run();
    }

    private void run() {
        ioConnector = new DatagramConnector();

    }
}
