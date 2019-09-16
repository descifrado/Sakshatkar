package controllers;

import com.jfoenix.controls.JFXTextField;
import constants.ResponseCode;
import data.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import mainApp.App;
import request.FeedbackRequest;
import request.Response;

import java.io.IOException;
import java.util.ArrayList;

public class Controller_Feedback {

    public JFXTextField audio;
    public JFXTextField video;
    public JFXTextField response;
    public JFXTextField rating;
    public JFXTextField comment;

    public void onsubmitclicked(ActionEvent actionEvent) {
        FeedbackRequest feedbackRequest=new FeedbackRequest(App.user.getUserUID(), video.getText(), audio.getText(), response.getText(), rating.getText(), comment.getText());
        try{
            App.oosTracker.writeObject(feedbackRequest);
            App.oosTracker.flush();
            Response response;
            response = (Response)App.oisTracker.readObject();
            if(response.getResponseCode().equals(ResponseCode.SUCCESS)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Thank You For Your Valuable Feedback");
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

    public void oncloseclicked(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage primaryStage = (Stage) audio.getScene().getWindow();
                primaryStage.close();
            }
        });
    }
}
