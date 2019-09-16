package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import constants.ResponseCode;
import data.User;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mainApp.App;
import request.*;
import tools.FileReciever;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Controller_Dashboard {

    private String userUID;

    public static User getUserprofile() {
        return userprofile;
    }
    private ArrayList<User> users;
    private static User userprofile;
    @FXML
    public ImageView profilephoto;
    public JFXTextField firstname,lastname,email,phone,company,namesearch;
    public JFXButton logout;
    public JFXListView onlineuserslist;
    public void initialize() throws IOException {
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


}
