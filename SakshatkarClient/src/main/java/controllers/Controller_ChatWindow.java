package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import constants.ResponseCode;
import data.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mainApp.App;
import request.MessageSendRequest;
import request.Response;
import request.UserIPRequest;
import tools.EncryptDecrypt;

import java.io.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Controller_ChatWindow {

    public JFXButton sendfile;
    public JFXTextField message;
    public JFXTextArea chatarea;
    private BufferedReader reader;
    private String filePath="";
    private Thread refresh;

    private String senderIP, recieverIP;

    public void initialize() {
        UserIPRequest userIPRequest=new UserIPRequest(App.user.getUserUID());
        try {
            App.oosTracker.writeObject(userIPRequest);
            App.oosTracker.flush();
            Response response;
            System.out.println("Reading Object");
            response = (Response) App.oisTracker.readObject();
            System.out.println(response.getResponseCode().toString());
            if (response.getResponseCode().equals(ResponseCode.SUCCESS)) {
                senderIP=response.getResponseObject().toString();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        userIPRequest=new UserIPRequest(Controller_Profile.getUser().getUserUID());
        try {
            App.oosTracker.writeObject(userIPRequest);
            App.oosTracker.flush();
            Response response;
            System.out.println("Reading Object");
            response = (Response) App.oisTracker.readObject();
            System.out.println(response.getResponseCode().toString());
            if (response.getResponseCode().equals(ResponseCode.SUCCESS)) {
                recieverIP=response.getResponseObject().toString();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        chatarea.clear();
        String cwd=System.getProperty("user.dir");
        filePath=cwd+"/chat/"+App.user.getUserUID() + Controller_Profile.getUser().getUserUID();
        File file = new File(filePath);
        try {
            file.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh=new Thread(()->{run();});
        refresh.start();
    }
    public void run()
    {

        while (true)
        {
            chatarea.clear();
            try
            {
                reader = new BufferedReader(new FileReader(filePath));
                String line = reader.readLine();
                while (line != null) {

                    String finalLine = EncryptDecrypt.decrypt(line);;
                    Platform.runLater(() -> chatarea.appendText(finalLine + "\n"));
                        try {
                            line = reader.readLine();
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                }



                reader.close();
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }
    public void onsendclicked(ActionEvent actionEvent)
    {
        Message msg= null;
        try {
            msg = new Message(EncryptDecrypt.encrypt(message.getText()), App.user, Controller_Profile.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageSendRequest messageSendRequest=new MessageSendRequest(senderIP, recieverIP, msg);
        try
        {
            App.oosTracker.writeObject(messageSendRequest);
            App.oosTracker.flush();
            Response response;
            System.out.println("Reading Object");
            response = (Response) App.oisTracker.readObject();
            System.out.println(response.getResponseCode().toString());
            if (response.getResponseCode().equals(ResponseCode.SUCCESS)) {
                try {

                    BufferedWriter out = new BufferedWriter( new FileWriter(filePath, true));
                    out.write(EncryptDecrypt.encrypt(App.user.getFirstName()+": "+message.getText())+"\n");
                    out.flush();
                    out.close();
                }
                catch (IOException e) {
                    System.out.println("exception occoured" + e.getMessage());
                }
            }
        }
        catch (Exception e){

        }
        message.clear();
    }

    public void onbackclicked(ActionEvent actionEvent) {
        refresh.stop();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage primaryStage = (Stage) sendfile.getScene().getWindow();
                Parent root = null;
                try {

                    root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
                }catch(IOException e){
                    e.printStackTrace();
                }
                primaryStage.setScene(new Scene(root, 1301, 960));

            }
        });
    }
}
