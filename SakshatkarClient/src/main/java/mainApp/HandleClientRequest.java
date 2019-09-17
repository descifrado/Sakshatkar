package mainApp;

import constants.RequestCode;
import constants.ResponseCode;
import controllers.Controller_VideoCall;
import data.User;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import request.Request;
import request.Response;
import request.peerRequest.VideoCallRequest;
import tools.UIDGenerator;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class HandleClientRequest implements Runnable {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private static Socket userSocket=null;
    private static volatile Alert alert;

    public static Socket getUserSocket() {
        return userSocket;
    }

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

                if (request.getRequestCode().equals(RequestCode.VIDEO_CALL_REQUEST)){
                    User requestingUser=((VideoCallRequest)request).getUser();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alert = new Alert(Alert.AlertType.CONFIRMATION, requestingUser + " wants to have a video call with you.\n Wanna Connect ?", ButtonType.YES, ButtonType.NO);
                            alert.showAndWait();
                        }
                    });
                    while (alert==null || alert.getResult()==null);
                    System.out.println(alert.getResult());
                    if (alert.getResult()==ButtonType.YES){
                        oos.writeObject(new Response(UIDGenerator.generateuid(),null, ResponseCode.SUCCESS));
                        oos.flush();
                        System.out.println(alert.getResult());
                        alert=null;
                        userSocket=new Socket(socket.getInetAddress().getCanonicalHostName(),7000);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Parent root;
                                try {
                                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/videoCall.fxml"));
                                    root = loader.load();
                                    Stage stage = new Stage();
                                    stage.setTitle("Call to "+requestingUser);
                                    stage.setScene(new Scene(root, 1303, 961));
                                    Controller_VideoCall videoCallController=loader.<Controller_VideoCall>getController();
                                    stage.show();
                                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                        @Override
                                        public void handle(WindowEvent windowEvent) {
                                            videoCallController.getCaptureFrame().setClosed();
                                            try {
                                                socket.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    // Hide this current window (if this is what you want)
                                    //((Node)(event.getSource())).getScene().getWindow().hide();
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                    }
                    else {
                        oos.writeObject(new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED));
                        oos.flush();
                    }
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();;
            }
        }
    }
}
