package mainApp;
import java.io.IOException;
import java.net.Socket;
public class Handler implements Runnable
{
    @Override
    public void run() {
        try {

            while (true) {
                try {

                    Socket socketp2p = App.serverSocket.accept();
                    Thread t = new Thread(new HandleClientRequest(socketp2p));
                    t.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
