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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mainApp.App;
import request.OnlineUserRequest;
import request.Response;
import request.UserSearchRequest;
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
        onlineuserslist.getItems().removeAll();
        UserSearchRequest userSearchRequest=new UserSearchRequest(namesearch.getText());
        try{
            App.sockerTracker = new Socket(App.serverIP,App.portNo);
            App.oosTracker = new ObjectOutputStream(App.sockerTracker.getOutputStream());
            App.oisTracker = new ObjectInputStream(App.sockerTracker.getInputStream());
            App.oosTracker.writeObject(userSearchRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                ArrayList<User> users =(ArrayList<User>)response.getResponseObject();
                if(!users.isEmpty()) {
                    for (User user : users) {
                        onlineuserslist.getItems().add(user);
                    }
                    onlineuserslist.getItems().remove(App.user);
                }
                else
                {
                    onlineuserslist.getItems().add("No such user");
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
        onlineuserslist.getItems().removeAll();
        OnlineUserRequest onlineUserRequest=new OnlineUserRequest();
        try{
            App.sockerTracker = new Socket(App.serverIP,App.portNo);
            App.oosTracker = new ObjectOutputStream(App.sockerTracker.getOutputStream());
            App.oisTracker = new ObjectInputStream(App.sockerTracker.getInputStream());
            App.oosTracker.writeObject(onlineUserRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                ArrayList<User> users =(ArrayList<User>)response.getResponseObject();
                if(!users.isEmpty())
                {
                    for(User user: users)
                    {
                        onlineuserslist.getItems().add(user);
                    }
                    onlineuserslist.getItems().remove(App.user);
                }
                else {
                    onlineuserslist.getItems().add("No user online");
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

    public void onuserlistclicked(MouseEvent mouseEvent) {
        userprofile=(User)onlineuserslist.getSelectionModel().getSelectedItem();
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
}
