package controllers;

import com.jfoenix.controls.JFXTextField;
import constants.ResponseCode;
import data.User;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mainApp.App;
import request.*;
import request.peerRequest.VideoCallRequest;
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
    public Label videocallstatus;
    public JFXTextField status;
    private String userIP;
    private static Socket videoCallSocket=null;

    public static Socket getVideoCallSocket(){
        return videoCallSocket;
    }

    private User user;
    @FXML
    public void initialize()
    {
        user=Controller_Dashboard.getUserprofile();
        GetStatusRequest getStatusRequest=new GetStatusRequest(user.getUserUID());
        try{
            App.oosTracker.writeObject(getStatusRequest);
            App.oosTracker.flush();
            Response response;
            System.out.println("Reading Object");
            response = (Response)App.oisTracker.readObject();
            System.out.println(response.getResponseCode().toString());
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                if(!response.getResponseObject().toString().isEmpty())
                    status.setText(response.getResponseObject().toString());
                else
                    status.setText("Offline");
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
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        company.setText(user.getCompany());
        ProfilePhotoRequest profilePhotoRequest=new ProfilePhotoRequest(user.getUserUID());
        System.out.println(user.getUserUID());
        try{
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
        UserIPRequest userIPRequest=new UserIPRequest(user.getUserUID());
        try {
            App.oosTracker.writeObject(userIPRequest);
            App.oosTracker.flush();
            Response response= (Response) App.oisTracker.readObject();
            if (response.getResponseCode().equals(ResponseCode.FAILED)){
                videocallstatus.setText("User Offline");
            }
            else {
                userIP=((String)response.getResponseObject());
                videoCallSocket=new Socket(userIP,6963);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        VideoCallRequest videoCallRequest=new VideoCallRequest(App.user);
                        try {
                            ObjectOutputStream oos=new ObjectOutputStream(videoCallSocket.getOutputStream());
                            ObjectInputStream ois=new ObjectInputStream(videoCallSocket.getInputStream());

                            oos.writeObject(videoCallRequest);
                            oos.flush();

                            Response videoCallResponse= (Response) ois.readObject();
                            if (videoCallResponse.getResponseCode().equals(ResponseCode.FAILED)){
                                System.out.println("Request denied");
                                videoCallSocket.close();
                                return;
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            Socket socket=App.serverSocketFrame.accept();
                            videoCallSocket=socket;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(()->{
                            Parent root;
                            try {
                                FXMLLoader loader=new FXMLLoader(getClass().getResource("/videoCall.fxml"));
                                System.out.println(loader);
                                root = loader.load();
                                FXMLLoader fxmlLoader=new FXMLLoader();
                                System.out.println(videoCallSocket);
                                Controller_VideoCall videoCallController=loader.getController();
                                System.out.println(videoCallController);
                                Stage stage = new Stage();
                                stage.setTitle("Call to "+user);
                                stage.setScene(new Scene(root, 1303, 961));
                                stage.show();
                                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent windowEvent) {
                                        videoCallController.getCaptureFrame().setClosed();
                                    }
                                });
                                // Hide this current window (if this is what you want)
                                //((Node)(event.getSource())).getScene().getWindow().hide();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onchatclicked(ActionEvent actionEvent) {
    }

    public void onsendclicked(ActionEvent actionEvent) {
    }

    public void onaddfriendclicked(ActionEvent actionEvent) {
        AddFriendRequest addFriendRequest=new AddFriendRequest(App.user.getUserUID(), user.getUserUID());
        try{
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
