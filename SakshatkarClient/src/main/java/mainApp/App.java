package mainApp;

import data.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import soundHandlers.AudioListen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

/**
 * JavaFX App
 */
public class App extends Application {

    public static String serverIP = "192.168.0.110";
    public static int portNo = 6963;
    public static Socket sockerTracker ;
    public static ObjectOutputStream oosTracker ;
    public static ObjectInputStream oisTracker;
    public static User user;
    public static ServerSocketChannel serverClientSocketChannel;
    public static Socket socketp2p;
    public static ServerSocket serverSocket;
    public static ServerSocket serverSocketFrame;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setTitle("Distribution");
        primaryStage.setScene(new Scene(root, 1081, 826));
        primaryStage.show();

        System.out.println("Hello I am Suraj");
        try {
            serverClientSocketChannel = ServerSocketChannel.open();
            serverClientSocketChannel.socket().bind(new InetSocketAddress(9000));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try{
            App.serverSocket = new ServerSocket(6963);
            App.serverSocketFrame = new ServerSocket(7000);
            System.out.println("Client Started..!!");
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        new Thread(new Handler()).start();
        new Thread(new AudioListen()).start();


    }
    public static ServerSocketChannel getServerSocketChannel(){
        return serverClientSocketChannel;
    }


    public static void main(String[] args) {
        System.loadLibrary("opencv_java411");
        launch(args);
    }

}