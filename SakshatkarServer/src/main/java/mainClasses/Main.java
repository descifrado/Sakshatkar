package mainClasses;

import tools.MysqlConnection;
import tools.SystemProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.sql.Connection;

public class Main {
    public static String user= SystemProperties.getMySQLUserName();
    public static String password=SystemProperties.getMySQLPassword();
    public static String host=SystemProperties.getMySQLHostName();//"jdbc:mysql://localhost:3306/Distribution";
    public static Connection connection = MysqlConnection.connect();
    public static ServerSocketChannel getServerSocketChannel(){
        return serverSocketChannel;
    }
    private static ServerSocketChannel serverSocketChannel = null;
    public static void main(String[] args) {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerSocket serverSocket;
        Socket socket;
        try{
            serverSocket = new ServerSocket(6963);
            System.out.println("Server Started..!!");
        }catch (IOException e){
            e.printStackTrace();
            return;
        }

        while(true){
            try{
                socket = serverSocket.accept();
                Thread t = new Thread(new HandleClientRequest(socket));
                t.start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
