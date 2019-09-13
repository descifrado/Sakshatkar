package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mainApp.App;
import tools.FileReciever;

import java.io.IOException;

public class Controller_Dashboard {

    private String userUID;
    @FXML
    public ImageView profilephoto;
    public JFXTextField firstname,lastname,email,phone,company;
    public JFXButton logout;
    public void initialize() {
        firstname.setText(App.user.getFirstName());
        lastname.setText(App.user.getLastName());
        email.setText(App.user.getEmail());
        phone.setText(App.user.getPhone());
        userUID = App.user.getUserUID();
        company.setText(App.user.getCompany());
        String cwd=System.getProperty("user.dir");
        String folder=cwd+"/profilephotos/";
        FileReciever fileReciever=new FileReciever();
        fileReciever.readFile(fileReciever.createSocketChannel(App.getServerSocketChannel()),folder, App.user.getUserUID());
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

}
