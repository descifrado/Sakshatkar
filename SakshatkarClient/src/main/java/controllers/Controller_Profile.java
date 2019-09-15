package controllers;

import com.jfoenix.controls.JFXTextField;
import data.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

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
    }

    public void onvideocallclicked(ActionEvent actionEvent) {
    }

    public void onchatclicked(ActionEvent actionEvent) {
    }

    public void onsendclicked(ActionEvent actionEvent) {
    }

    public void onaddfriendclicked(ActionEvent actionEvent) {
    }
}
