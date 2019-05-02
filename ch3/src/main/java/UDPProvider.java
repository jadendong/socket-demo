import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;

/**
 * @author jaden
 */
public class UDPProvider {

    public static void main(String[] args) throws IOException {
        // 生成一份唯一标识
        String sn = UUID.randomUUID().toString();

        // 启动线程
        Provider provider = new Provider(sn);
        provider.start();

        // 读取到键盘任意字符后可以退出
        System.in.read();
        provider.exit();
    }

    private static class Provider extends Thread {
        private final String sn;
        private Boolean done = false;
        private DatagramSocket ds = null;

        Provider(String sn) {
            super();
            this.sn = sn;
        }

        @Override
        public void run() {
            super.run();

            System.out.println("UDPProvider Started.");

            // 作为接收者，指定一个端口用来数据接收
            try {
                ds = new DatagramSocket(20000);
                while (!done) {
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
                    System.out.println("UDPProvider receive from ip" + ip + "\tport:" + port + "\tdata:" + data);

                    int responsePort = MessageCreator.parsePort(data);
                    if (responsePort != -1) {
                        // 构建一份回送数据
                        String responseData = MessageCreator.buildWithSn(sn);
                        byte[] responseDataBytes = responseData.getBytes();
                        // 直接根据发送者构建一份回送信息
                        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes, responseDataBytes.length,
                                receivePack.getAddress(), responsePort);
                        ds.send(responsePacket);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
            // 完成
            System.out.println("UDPProvider Finished.");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        /**
         * 提供结束的方法
         */
        void exit() {
            done = true;
            close();
        }
    }
}
