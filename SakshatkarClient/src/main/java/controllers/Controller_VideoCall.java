package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mainApp.HandleClientRequest;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import videoCallHandler.frameHandler.CaptureFrame;
import videoCallHandler.frameHandler.MatWrapper;
import videoCallHandler.frameHandler.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;

public class Controller_VideoCall {

    private ObjectOutputStream frameOOS;
    private ObjectInputStream frameOIS;
    private Socket userSocket;
    private String userIP;
    private volatile CaptureFrame captureFrame;
    private boolean videoEnabled;
    @FXML
    private JFXButton stopvideo;
    @FXML
    private ImageView videoFrame;

    public CaptureFrame getCaptureFrame() {
        return captureFrame;
    }

    public void initialize(){
        if (Controller_Profile.getVideoCallSocket()!=null){
            userSocket=Controller_Profile.getVideoCallSocket();
        }
        else {
            userSocket= HandleClientRequest.getUserSocket();
        }
        userIP=userSocket.getInetAddress().getCanonicalHostName();

        System.out.println(userSocket);

        videoEnabled=true;
        try {
            frameOOS=new ObjectOutputStream(userSocket.getOutputStream());
            frameOIS=new ObjectInputStream(userSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        captureFrame=new CaptureFrame();
        captureFrame.startCam(frameOOS);
        new Thread(() -> {
            while (true){
                try {
                    MatWrapper mat = (MatWrapper) frameOIS.readObject();
                    Mat frame = new Mat(mat.getRows(),mat.getCols(),mat.getType());
                    frame.put(0,0,mat.getMatArray());
                    Image image=Utils.mat2Image(frame);
                    updateImageView(videoFrame,image);
                }  catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Client Disconnected");
                    captureFrame.setClosed();

                    Platform.runLater( () -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Call disconnected !!! ", ButtonType.FINISH);
                        alert.showAndWait();
                        while (alert==null || alert.getResult()==null);
                        Stage primaryStage = (Stage) stopvideo.getScene().getWindow();
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("/feedback.fxml"));
                            primaryStage.setScene(new Scene(root, 1303, 961));
                        }catch(IOException ex){
                            Thread.currentThread().stop();
                        }
                        catch (NullPointerException ex){
                            Thread.currentThread().stop();
                        }

                    });
                    Thread.currentThread().stop();
                }
            }

        }).start();
    }


    public void onmuteclicked(ActionEvent actionEvent) {

    }

    public void oncancelclicked(ActionEvent actionEvent) {
        captureFrame.setClosed();
        try{
            userSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Platform.runLater( () -> {
            Stage primaryStage = (Stage) stopvideo.getScene().getWindow();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/feedback.fxml"));
            }catch(IOException e){
                e.printStackTrace();
            }
            primaryStage.setScene(new Scene(root, 1303, 961));
        });
    }

    public void onsharescreenclicked(ActionEvent actionEvent) {

    }

    public void onstopvideoclicked(ActionEvent actionEvent) {
        if (videoEnabled==true) {
            captureFrame.setClosed();
            videoEnabled=false;
            stopvideo.setText("Start Video");
        }
        else {
            captureFrame.startCam(frameOOS);
            videoEnabled=true;
            stopvideo.setText("Stop Video");
        }
    }

    public void onsendmessageclicked(ActionEvent actionEvent) {

    }

    public void initSocket(Socket socket) {
        this.userSocket=socket;
    }

    private void updateImageView(ImageView view, Image image)
    {
        Utils.onFXThread(view.imageProperty(), image);
    }
}