import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author jaden
 */
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        // 设置超时时间
        socket.setSoTimeout(3000);

        // 连接本地,端口2000，超时3000毫秒
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 2000), 3000);

        System.out.println("已发起服务器连接....");
        System.out.println("客户端信息：" + socket.getLocalAddress() + ":" + socket.getLocalPort());
        System.out.println("服务器信息：" + socket.getInetAddress() + ":" + socket.getPort());

        try {
            // 发送接收数据
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        // 关闭连接，释放资源
        socket.close();
        System.out.println("客户端已退出...");
    }

    private static void todo(Socket client) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        // 得到socket输出流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        // 得到socket输入流
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        boolean flag = true;
        do {
            // 键盘读取一行
            String str = input.readLine();
            // 发送到服务器
            socketPrintStream.println(str);

            // 从服务器读取一行
            String echo = socketBufferedReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }

        } while (flag);

        // 资源释放
        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
