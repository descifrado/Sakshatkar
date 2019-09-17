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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mainApp.App;
import request.*;
import request.peerRequest.VideoCallRequest;
import tools.FileReciever;
import tools.FileSender;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Controller_Profile {

    public JFXTextField lastname,filePath;
    public JFXTextField firstname;
    public JFXTextField phone;
    public JFXTextField email;
    public ImageView profilephoto;
    public JFXTextField company;
    public Label videocallstatus;
    public JFXTextField status;
    private String userIP,userIP1;
    private static Socket videoCallSocket=null;
    private String filepath, fileName;

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
           // if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
            try {
                long lastSeen=Long.parseLong(response.getResponseObject().toString());
                long diff=new Date().getTime()-lastSeen;
                long min= TimeUnit.MINUTES.convert(diff,TimeUnit.MILLISECONDS);
                status.setText("Online "+(min/60)+" hrs "+(min%60)+" minutes ago");

            }
            catch (Exception e){
                status.setText(response.getResponseObject().toString());
            }

//                if(!response.getResponseObject().toString().isEmpty())
//                    status.setText(response.getResponseObject().toString());
//                else
//                    status.setText("Offline");
//            }
//            else
//            {
//                System.out.println("Error");
//            }

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
        UserIPRequest userIPRequest=new UserIPRequest(user.getUserUID());
//
        try{
            App.oosTracker.writeObject(userIPRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                userIP1=response.getResponseObject().toString();
                System.out.println("File Sent");
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
        FileTransferRequest fileTransferRequest=new FileTransferRequest(App.user.getUserUID(), App.user.getFirstName(), fileName);
        try{
            Socket socket=new Socket(userIP1,6963);
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
            oos.writeObject(fileTransferRequest);
            oos.flush();
            Response response=(Response) ois.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS))
            {
                FileSender fileSender=new FileSender();
                fileSender.sendFile(fileSender.createSocketChannel(userIP1),filepath);
                System.out.println("File Sent");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

    public void onbrowseclicked(ActionEvent actionEvent) {
        Stage stage = (Stage) firstname.getScene().getWindow();
        FileChooser fileChooser=new FileChooser();
        File file=fileChooser.showOpenDialog(stage);
        if(file!=null)
        {
            filepath=file.getAbsolutePath();
        }
        String temp[]=filepath.split("/");
        fileName=temp[temp.length-1];
        filePath.setText(fileName);
    }
}