package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mainApp.App;

import java.io.*;

public class Controller_ChatWindow {

    public JFXButton sendfile;
    public JFXTextField message;
    public JFXTextArea chatarea;
    private BufferedReader reader;
    private String filePath="";
    private Thread refresh;
    public void initialize() {
        chatarea.clear();
        String cwd=System.getProperty("user.dir");
        filePath=cwd+"/chat/"+App.user.getUserUID() + Controller_Profile.getUser().getUserUID();
        File file = new File(filePath);
        try {
            file.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh=new Thread();
        refresh.start();
    }
    public void run()
    {

        while (true)
        {
            chatarea.clear();
            try
            {
                reader = new BufferedReader(new FileReader(filePath));
                String line = reader.readLine();
                while (line != null) {
                    chatarea.appendText(line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void onsendclicked(ActionEvent actionEvent)
    {
        try {

            BufferedWriter out = new BufferedWriter( new FileWriter(filePath, true));
            out.write(App.user.getFirstName()+": "+message.getText()+"\n");
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public void onbackclicked(ActionEvent actionEvent) {
        refresh.stop();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Stage primaryStage = (Stage) sendfile.getScene().getWindow();
                Parent root = null;
                try {

                    root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
                }catch(IOException e){
                    e.printStackTrace();
                }
                primaryStage.setScene(new Scene(root, 1081, 826));

            }
        });
    }
}
