package mainClasses;

import authenticationHandler.Login;
import authenticationHandler.SignUp;
import constants.RequestCode;

import constants.ResponseCode;
import data.Message;
import data.Notification;
import data.User;
import feedbackHandler.FeedBackHandler;
import filehandler.FileReciever;
import filehandler.FileSender;
import friendsHandler.FriendAddHandler;
import friendsHandler.FriendSuggestionHandler;
import notificationHandler.NotificationHandler;
import offlineUserChatHandler.OfflineUserChatHandler;
import offlineUserChatHandler.OfflineUserNotificationDelete;
import onlineUserHandler.OnlineUserListHandler;
import onlineUserHandler.UserIPHandler;
import request.*;
import friendsHandler.FriendListHandler;
import searchHandler.SearchHandler;
import statusHandler.GetStatusHandler;
import statusHandler.GetUserOnlineStatus;
import statusHandler.OnlineStatusHandler;
import statusHandler.OnlineUserHandler;
import tools.UIDGenerator;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

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
                else if(request.getRequestCode().equals(RequestCode.GETSTATUS_REQUEST))
                {
                    GetStatusHandler getStatusHandler=new GetStatusHandler((GetStatusRequest) request);
                    oos.writeObject(getStatusHandler.getResponse());
                    oos.flush();
                }
                else if(request.getRequestCode().equals(RequestCode.STATUS_CHANGE_REQUEST))
                {
                    OnlineStatusHandler onlineStatusHandler=new OnlineStatusHandler((StatusChangeRequest)request);
                    oos.writeObject(onlineStatusHandler.getResponse());
                    oos.flush();
                }else if(request.getRequestCode().equals(RequestCode.MESSAGESEND_REQUEST)){
                    String ip = ((MessageSendRequest)request).getRecieverIP();
                    User sender,reciever;
                    sender = ((MessageSendRequest)request).getMessage().getSender();
                    reciever =((MessageSendRequest)request).getMessage().getReciever();
                    ResponseCode response = GetUserOnlineStatus.getStatus(reciever.getUserUID());
                    String message = ((MessageSendRequest)request).getMessage().getMsg();
                    if(response.equals(ResponseCode.SUCCESS)){
                        Socket socket = new Socket(ip,7575);
                        System.out.println(socket);
                        ObjectOutputStream loos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream lois = new ObjectInputStream(socket.getInputStream());
                        Response r = new Response(UIDGenerator.generateuid(),((MessageSendRequest)request).getMessage(),ResponseCode.SUCCESS);
                        loos.writeObject(r);
                        loos.flush();
                    }else{
                        OfflineUserChatHandler offlineUserChatHandler=new OfflineUserChatHandler(((MessageSendRequest) request).getMessage().getSender().getUserUID(),((MessageSendRequest) request).getMessage().getReciever().getUserUID());
                        offlineUserChatHandler.insert();
                        String cwd = System.getProperty("user.dir");
                        String loc = cwd+"/chat";
                        File file = new File(loc);
                        if(!file.exists()){file.mkdir();}
                        loc = loc+"/"+sender.getUserUID()+reciever.getUserUID();
                        file = new File(loc);
                        file.createNewFile();
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
                        bufferedWriter.write(message+"\n");
                        bufferedWriter.flush();
                    }
                    oos.writeObject(new Response(UIDGenerator.generateuid(),null,ResponseCode.SUCCESS));
                }else if(request.getRequestCode().equals(RequestCode.GET_NOTIFICATION_REQUEST)){
                    NotificationHandler notificationHandler= new NotificationHandler((GetNotificationRequest)request);
                    Response r = new Response(UIDGenerator.generateuid(),null,ResponseCode.SUCCESS);
                    oos.writeObject(r);
                    oos.flush();
                    List<Notification> notifications = (List<Notification>) (notificationHandler.getResponse()).getResponseObject();
                    Socket socket = new Socket(this.socket.getInetAddress().getCanonicalHostName(),7575);
                    ObjectOutputStream coos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream cois = new ObjectInputStream(socket.getInputStream());
                    String cwd = System.getProperty("user.dir");
                    String loc = cwd+"/chat";
                    for(Notification notification:notifications){
                        String msg = "";
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(loc+"/"+notification.getSender().getUserUID()+notification.getReciever().getUserUID()));
                        String line = bufferedReader.readLine();
                        while(line!=null){
                            msg+=line;
                            msg+="\n";
                            line = bufferedReader.readLine();
                        }
                        Message message = new Message(msg,notification.getSender(),notification.getReciever());
                        coos.writeObject(new Response(UIDGenerator.generateuid(),message,ResponseCode.SUCCESS));
                        coos.flush();
                        File file = new File(loc+"/"+notification.getSender().getUserUID()+notification.getReciever().getUserUID());
                        if(file.exists()){
                            file.delete();
                        }
                        OfflineUserNotificationDelete offlineUserNotificationDelete =new OfflineUserNotificationDelete(notification);
                        offlineUserNotificationDelete.delete();
                    }

                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
