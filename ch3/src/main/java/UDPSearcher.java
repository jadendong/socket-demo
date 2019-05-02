import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author jaden
 */
public class UDPSearcher {

    public static void main(String[] args) throws IOException {
        System.out.println("UDPSearcher Started.");

        // 作为接收者，随机分配端口
        DatagramSocket ds = new DatagramSocket();

        // 发送一份数据
        String requestData = "HelloWorld";
        byte[] requestDataBytes = requestData.getBytes();
        // 发送一份信息
        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes, requestDataBytes.length);
        requestPacket.setAddress(InetAddress.getLocalHost());
        // 指定往本机20000上发
        requestPacket.setPort(20000);
        ds.send(requestPacket);


        // 构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

        // 接收
        ds.receive(receivePack);

        // 打印接收到信息，和发送者的信息
        // 发送者的IP
        String ip = receivePack.getAddress().getHostAddress();
        int port = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("UDPSearcher receive from ip" + ip + "\tport:" + port + "\tdata:" + data);

        // 完成
        System.out.println("UDPSearcher Finished.");
        ds.close();
    }
}