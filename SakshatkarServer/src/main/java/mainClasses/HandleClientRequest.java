package mainClasses;

import authenticationHandler.Login;
import authenticationHandler.SignUp;
import constants.RequestCode;

import constants.ResponseCode;
import data.User;
import filehandler.FileReciever;
import filehandler.FileSender;
import onlineUserHandler.OnlineUserListHandler;
import request.*;
import searchHandler.SearchHandler;
import statusHandler.OnlineStatusHandler;
import statusHandler.OnlineUserHandler;

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
    OnlineUserHandler onlineUserHandler;
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
//                    onlineUserHandler.makeUserOffline();
                    return;
                }catch (SocketException e){
                    System.out.println("Client Disconnected..!!");
//                    onlineUserHandler.makeUserOffline();
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
                    Response response=login.getResponse();
                    onlineUserHandler=new OnlineUserHandler(((User)response.getResponseObject()).getUserUID());
                    onlineUserHandler.makeUserOnline();
                    oos.writeObject(response);
                    oos.flush();
                    String cwd = System.getProperty("user.dir");
                    String loc = cwd+"/profilePics/";
                    if(login.getResponse().getResponseCode().equals(ResponseCode.SUCCESS)){
                        FileSender fileSender = new FileSender();
                        String userId = ((User)login.getResponse().getResponseObject()).getUserUID();
                        fileSender.sendFile(fileSender.createSocketChannel(socket.getInetAddress().getCanonicalHostName()),loc+userId);
                        System.out.println("Sending Profile Pic..!!");
                    }
                }else if(request.getRequestCode().equals(RequestCode.STATUS_CHANGE_REQUEST)){
                    StatusChangeRequest statusChangeRequest= (StatusChangeRequest) request;
                    OnlineStatusHandler onlineStatusHandler=new OnlineStatusHandler(statusChangeRequest.getUserUID());
                    oos.writeObject(onlineStatusHandler.changeOnlineStatus(statusChangeRequest.getStatus()));
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.USERSEARCH_REQUEST)){
                    SearchHandler searchHandler = new SearchHandler((UserSearchRequest)request);
                    Response response = searchHandler.getResponse();
                    oos.writeObject(response);
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.ONLINEUSER_REQUEST)){
                    OnlineUserListHandler onlineUserListHandler = new OnlineUserListHandler((OnlineUserRequest)request);
                    oos.writeObject(onlineUserListHandler.getResponse());
                    oos.flush();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
