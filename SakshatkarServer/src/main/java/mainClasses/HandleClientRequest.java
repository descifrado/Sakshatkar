package mainClasses;

import authenticationHandler.Login;
import authenticationHandler.SignUp;
import constants.RequestCode;

import constants.ResponseCode;
import data.User;
import filehandler.FileReciever;
import filehandler.FileSender;
import request.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class
HandleClientRequest implements Runnable{
    private Socket socket ;
    ObjectOutputStream oos;
    ObjectInputStream ois ;
    String userIP;
    public HandleClientRequest(Socket socket){
        this.socket=socket;
        System.out.println(socket.getInetAddress().getCanonicalHostName());
        try{
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            userIP = socket.getInetAddress().getCanonicalHostName();
        }catch (IOException e){
            e.printStackTrace();
        }
    }




    public void run() {
        Request request = null;
        while(true){
            try{

                try{
                    Object object=ois.readObject();
                    System.out.println(object.getClass());
                    request = (Request)object;
                }catch (EOFException e){
                    System.out.println("Client Disconnected..!!");
                    return;
                }catch (SocketException e){
                    System.out.println("Client Disconnected..!!");
                    return;
                }

                if(request.getRequestCode().equals(RequestCode.SIGNUP_REQUEST)) {
                    SignUp signUp = new SignUp((SignUpRequest) request);
                    Response response = signUp.insert();
                    String cwd = System.getProperty("user.dir");
                    String loc = cwd+"/profilePics/";
                    FileReciever fileReciever = new FileReciever();
                    fileReciever.readFile(fileReciever.createSocketChannel(Main.getServerSocketChannel()),((SignUpRequest) request).getUser().getUserUID(),loc);
                    System.out.println("Recieving profile pic..!!");
                    oos.writeObject(response);
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.LOGIN_REQUEST)){
                    Login login = new Login((LoginRequest)request);
                    oos.writeObject(login.getResponse());
                    oos.flush();
                    String cwd = System.getProperty("user.dir");
                    String loc = cwd+"/profilePics/";
                    if(login.getResponse().getResponseCode().equals(ResponseCode.SUCCESS)){
                        FileSender fileSender = new FileSender();
                        String userId = ((User)login.getResponse().getResponseObject()).getUserUID();
                        fileSender.sendFile(fileSender.createSocketChannel(socket.getInetAddress().getCanonicalHostName()),loc+userId);
                        System.out.println("Sending Profile Pic..!!");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
