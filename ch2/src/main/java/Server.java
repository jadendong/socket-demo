import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author jaden
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(2000);

        System.out.println("服务器准备就绪");
        System.out.println("服务器端信息：" + server.getInetAddress() + ":" + server.getLocalPort());

        // 创建一个死循环
        for (; ; ) {
            //得到一个客端端
            Socket socket = server.accept();
            // 创建线程
            ClientHandler clientHandler = new ClientHandler(socket);
            clientHandler.start();
        }

    }

    /**
     * 客户端消息处理
     */
    private static class ClientHandler extends Thread {
        private Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客端端连接了：" + socket.getInetAddress() + ":" + socket.getPort());

            try {
                // 得到打印流，用于数据输出
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                // 得到流，接收的数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                boolean flag = true;
                do {
                    // 从客户端拿到一条数据
                    String str = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(str)) {
                        // 回送:
                        socketOutput.println("bye");
                    } else {
                        flag = false;
                        // 打印，并回送一条数据
                        System.out.println(str);
                        socketOutput.println("回送: " + str.length());
                    }

                } while (flag);

                socketInput.close();
                socketOutput.close();

            } catch (Exception e) {
                System.out.println("连接异常断开");
            } finally {
                // 连接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Socket 关闭失败");
                    e.printStackTrace();
                }
            }

            System.out.println("客户端已退出: " + socket.getInetAddress() + ":" + socket.getPort());
        }
    }
}
