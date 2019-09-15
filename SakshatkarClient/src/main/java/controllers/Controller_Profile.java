package controllers;

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
import javafx.stage.Stage;
import mainApp.App;
import request.AddFriendRequest;
import request.ProfilePhotoRequest;
import request.Response;
import tools.FileReciever;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Controller_Profile {

    public JFXTextField lastname;
    public JFXTextField firstname;
    public JFXTextField phone;
    public JFXTextField email;
    public ImageView profilephoto;
    public JFXTextField company;

    private User user;
    @FXML
    public void initialize()
    {
        user=Controller_Dashboard.getUserprofile();
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        company.setText(user.getCompany());
        ProfilePhotoRequest profilePhotoRequest=new ProfilePhotoRequest(user.getUserUID());
        System.out.println(user.getUserUID());
        try{
            App.sockerTracker = new Socket(App.serverIP,App.portNo);
            App.oosTracker = new ObjectOutputStream(App.sockerTracker.getOutputStream());
            App.oisTracker = new ObjectInputStream(App.sockerTracker.getInputStream());
            App.oosTracker.writeObject(profilePhotoRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                String cwd=System.getProperty("user.dir");
                String folder=cwd+"/profilephotos/";
                System.out.println("Recieving PP");
                FileReciever fileReciever=new FileReciever();
                fileReciever.readFile(fileReciever.createSocketChannel(App.getServerSocketChannel()), user.getUserUID(),folder);
                System.out.println("File Recieved");
                BufferedImage bufferedImage;
                bufferedImage = ImageIO.read(new File(folder+"/"+ user.getUserUID()));
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                profilephoto.setImage(image);
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

    public void onvideocallclicked(ActionEvent actionEvent) {
    }

    public void onchatclicked(ActionEvent actionEvent) {
    }

    public void onsendclicked(ActionEvent actionEvent) {
    }

    public void onaddfriendclicked(ActionEvent actionEvent) {
        AddFriendRequest addFriendRequest=new AddFriendRequest(App.user.getUserUID(), user.getUserUID());
        try{
            App.sockerTracker = new Socket(App.serverIP,App.portNo);
            App.oosTracker = new ObjectOutputStream(App.sockerTracker.getOutputStream());
            App.oisTracker = new ObjectInputStream(App.sockerTracker.getInputStream());
            App.oosTracker.writeObject(addFriendRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Friend Added Successfully");
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
    }

    public void onbackclicked(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage primaryStage = (Stage) firstname.getScene().getWindow();
                Parent root = null;
                try {

                    root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
                }catch(IOException e){
                    e.printStackTrace();
                }
                primaryStage.setScene(new Scene(root, 1303, 961));

            }
        });
    }
}
