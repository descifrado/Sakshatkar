package mainApp;

import constants.RequestCode;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class HandleClientRequest implements Runnable {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    public HandleClientRequest(Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    Request request = null;
    @Override
    public void run() {
        Request request = null;
        while(true)
        {
            try
            {
                try
                {
                    Object object=ois.readObject();
                    System.out.println(object.getClass());
                    request = (Request)object;
                    System.out.println(request);
                }catch (StreamCorruptedException e){
                    e.printStackTrace();
                    return;
                }
                catch (EOFException e)
                {
                    System.out.println("Client Disconnected..!!");
                    return;
                }
                catch (SocketException e)
                {
                    System.out.println("Client Disconnected..!!");
                    return;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();;
            }
        }
    }
}
