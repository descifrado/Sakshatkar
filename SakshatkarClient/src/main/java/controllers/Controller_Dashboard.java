package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import constants.NotificationType;
import constants.ResponseCode;
import constants.Status;
import data.Notification;
import data.User;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mainApp.App;
import request.*;
import tools.FileReciever;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.json.JSONObject.getNames;

public class Controller_Dashboard {

    public JFXComboBox availablestatus;
    private String currentSelectedStatus;
    private String userUID;

    public static User getUserprofile() {
        return userprofile;
    }
    private ArrayList<User> users;
    private static User userprofile;
    @FXML
    public ImageView profilephoto;
    public JFXTextField firstname,lastname,email,phone,company,namesearch,status;
    public JFXButton logout;
    public JFXListView onlineuserslist;
    public void initialize() throws IOException {
        GetStatusRequest getStatusRequest=new GetStatusRequest(App.user.getUserUID());
        try{
            App.oosTracker.writeObject(getStatusRequest);
            App.oosTracker.flush();
            Response response;
            System.out.println("Reading Object");
            response = (Response)App.oisTracker.readObject();
            System.out.println(response.getResponseCode().toString());
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                status.setText(response.getResponseObject().toString());
            }
            else
            {
                System.out.println("Error");
            }

        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        List<String> statuses= Stream.of(Status.values()).map(Status::name).collect(Collectors.toList());
        System.out.println(statuses);
        availablestatus.getItems().addAll(statuses);
        currentSelectedStatus=null;
        firstname.setText(App.user.getFirstName());
        lastname.setText(App.user.getLastName());
        email.setText(App.user.getEmail());
        phone.setText(App.user.getPhone());
        userUID = App.user.getUserUID();
        company.setText(App.user.getCompany());
        String cwd=System.getProperty("user.dir");
        String folder=cwd+"/profilephotos/";
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(new File(folder+"/"+App.user.getUserUID()));
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        this.profilephoto.setImage(image);

        new Thread(()->{
            GetNotificationRequest getNotificationRequest=new GetNotificationRequest(App.user);
            try {
                App.oosTracker.writeObject(getNotificationRequest);
                App.oosTracker.flush();
                List<Notification> notifications= (List<Notification>) App.oisTracker.readObject();
                for (Notification notification:
                    notifications ) {
                    String notificationMessage = null;
                    if (notification.getNotificationType().equals(NotificationType.CALL)){
                        notificationMessage="You missed a call by "+notification.getSender().getFirstName();
                        String finalNotificationMessage = notificationMessage;
                        Platform.runLater(()->{
                            Alert alert=new Alert(Alert.AlertType.INFORMATION, finalNotificationMessage, ButtonType.OK);
                            alert.showAndWait();
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }
    public void onlogoutclicked(ActionEvent actionEvent) {
        LogoutRequest logoutRequest=new LogoutRequest(App.user.getUserUID());
        try{
            App.oosTracker.writeObject(logoutRequest);
            App.oosTracker.flush();
            Response response;
            System.out.println("Reading Object");
            response = (Response)App.oisTracker.readObject();
            System.out.println(response.getResponseCode().toString());
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                System.out.println("Success to hua h");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Log Out Successfully");
                alert.showAndWait();
                App.user=null;
            }
            else
            {
                System.out.println("Error");
            }

        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        try{
            App.sockerTracker.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage primaryStage = (Stage) firstname.getScene().getWindow();
                Parent root = null;
                try {

                    root = FXMLLoader.load(getClass().getResource("/login.fxml"));
                }catch(IOException e){
                    e.printStackTrace();
                }
                primaryStage.setScene(new Scene(root, 1081, 826));

            }
        });

    }

    public void onsearchclicked(ActionEvent actionEvent) {
        onlineuserslist.getItems().clear();
        UserSearchRequest userSearchRequest=new UserSearchRequest(namesearch.getText());
        try{
            App.oosTracker.writeObject(userSearchRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                users =(ArrayList<User>)response.getResponseObject();
                if(!users.isEmpty()) {
                    for (User user : users) {
                        if(user.getUserUID()!=App.user.getUserUID())
                            onlineuserslist.getItems().add(user);
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No User Matched Your Search");
                    alert.showAndWait();
                }
            }
            else
            {
                System.out.println("Error");
            }

        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public void ononlineclicked(ActionEvent actionEvent) {
        onlineuserslist.getItems().clear();
        OnlineUserRequest onlineUserRequest=new OnlineUserRequest();
        try{
            App.oosTracker.writeObject(onlineUserRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                users =(ArrayList<User>)response.getResponseObject();
                if(!users.isEmpty())
                {
                    for(User user: users)
                    {
                        if(user.getUserUID()!=App.user.getUserUID())
                            onlineuserslist.getItems().add(user);
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No User Online");
                    alert.showAndWait();
                }

            }
            else
            {
                System.out.println("Error");
            }

        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public void onviewprofileclicked(ActionEvent actionEvent) {
        System.out.println(userprofile);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage primaryStage = (Stage) onlineuserslist.getScene().getWindow();
                Parent root = null;
                try {

                    root = FXMLLoader.load(getClass().getResource("/profile.fxml"));
                }catch(IOException e){
                    e.printStackTrace();
                }
                primaryStage.setScene(new Scene(root, 1303, 961));

            }
        });
    }

    public void onuserlistclivked(MouseEvent mouseEvent) {
        if(!onlineuserslist.getItems().isEmpty())
        {
            int idx=onlineuserslist.getSelectionModel().getSelectedIndex();
            userprofile=(User)users.get(idx);
        }
    }

    public void onfriendsclicked(ActionEvent actionEvent) {
        onlineuserslist.getItems().clear();
        FriendListRequest friendListRequest=new FriendListRequest(App.user.getUserUID());
        try{
            App.oosTracker.writeObject(friendListRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                users =(ArrayList<User>)response.getResponseObject();
                if(!users.isEmpty())
                {
                    for(User user: users) {
                        onlineuserslist.getItems().add(user);
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You Seem to be Quit Alone");
                    alert.showAndWait();
                }
            }
            else
            {
                System.out.println("Error");
            }

        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void onfriendsugclicked(ActionEvent actionEvent) {
        onlineuserslist.getItems().clear();
        FriendSuggestionRequest friendSuggestionRequest=new FriendSuggestionRequest(App.user.getUserUID());
        try{
            App.oosTracker.writeObject(friendSuggestionRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                users =(ArrayList<User>)response.getResponseObject();
                if(!users.isEmpty())
                {
                    for(User user: users) {
                        onlineuserslist.getItems().add(user);
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No Suggestion for You. Make some friends First");
                    alert.showAndWait();
                }
            }
            else
            {
                System.out.println("Error");
            }

        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public void onupdatestatusclicked(ActionEvent actionEvent) {
        String typeString=availablestatus.getSelectionModel().getSelectedItem().toString();
        Status status1;
        if (typeString==null)status1=Status.USING_SAKSHATKAR;
        else status1=Status.valueOf(typeString);
        StatusChangeRequest statusChangeRequest=new StatusChangeRequest(App.user.getUserUID(), status1);
        try{
            App.oosTracker.writeObject(statusChangeRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Status Successfully Updated");
                alert.showAndWait();
            }
            else
            {
                System.out.println("Error");
            }

        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        status.setText(status1.toString());
    }
}
