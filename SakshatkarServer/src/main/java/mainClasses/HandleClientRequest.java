package mainClasses;

import authenticationHandler.Login;
import authenticationHandler.SignUp;
import constants.RequestCode;

import constants.ResponseCode;
import data.User;
import feedbackHandler.FeedBackHandler;
import filehandler.FileReciever;
import filehandler.FileSender;
import friendsHandler.FriendAddHandler;
import friendsHandler.FriendSuggestionHandler;
import onlineUserHandler.OnlineUserListHandler;
import onlineUserHandler.UserIPHandler;
import request.*;
import friendsHandler.FriendListHandler;
import searchHandler.SearchHandler;
import statusHandler.OnlineStatusHandler;
import statusHandler.OnlineUserHandler;
import tools.UIDGenerator;

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
                    onlineUserHandler=new OnlineUserHandler(((User)response.getResponseObject()).getUserUID(),socket.getInetAddress().getCanonicalHostName());
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
                }else if(request.getRequestCode().equals(RequestCode.PROFILEPHOTO_REQUEST)){
                    ProfilePhotoRequest pp = (ProfilePhotoRequest)request;
                    oos.writeObject(new Response(UIDGenerator.generateuid(),null,ResponseCode.SUCCESS));
                    String userId = pp.getUserUID();
                    String cwd = System.getProperty("user.dir");
                    String loc = cwd+"/profilePics/";
                    FileSender fileSender = new FileSender();
                    fileSender.sendFile(fileSender.createSocketChannel(socket.getInetAddress().getCanonicalHostName()),loc+userId);
                    System.out.println("Sending Profile Pic..!!");

                }else if(request.getRequestCode().equals(RequestCode.FRIENDLIST_REQUEST)){
                    FriendListHandler friendListHandler = new FriendListHandler((FriendListRequest)request);
                    oos.writeObject(friendListHandler.getResponse());
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.ADDFRIEND_REQUEST)){
                    FriendAddHandler friendAddHandler = new FriendAddHandler((AddFriendRequest)request);
                    oos.writeObject(friendAddHandler.getResponse());
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.LOGOUT_REQUEST)){
                    String userUID = ((LogoutRequest)request).getUserUID();
                    onlineUserHandler = new OnlineUserHandler(userUID,socket.getInetAddress().getCanonicalHostName());
                    onlineUserHandler.makeUserOffline();
                    Response response = new Response(UIDGenerator.generateuid(),null,ResponseCode.SUCCESS);
                    oos.writeObject(response);
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.USERIP_REQUEST)){
                    UserIPHandler userIPHandler = new UserIPHandler((UserIPRequest)request);
                    oos.writeObject(userIPHandler.getResponse());
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.FRIENDSUGGESTION_REQUEST)){
                    FriendSuggestionHandler friendSuggestionHandler = new FriendSuggestionHandler((FriendSuggestionRequest)request);
                    oos.writeObject(friendSuggestionHandler.getResponse());
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.FEEDBACK_REQUEST)){
                    FeedBackHandler feedBackHandler = new FeedBackHandler((FeedbackRequest)request);
                    oos.writeObject(feedBackHandler.getResponse());
                    oos.flush();
                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
