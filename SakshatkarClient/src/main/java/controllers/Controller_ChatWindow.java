package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import mainApp.App;

import java.io.*;

public class Controller_ChatWindow {

    public JFXButton sendfile;
    public JFXTextField message;
    public JFXTextArea chatarea;
    private BufferedReader reader;
    private String filePath="";

    public void initialize() {
        String cwd=System.getProperty("user.dir");
        filePath=cwd+"/chat/"+App.user.getUserUID() + Controller_Profile.getUser().getUserUID();
        File file = new File(filePath);
        try {
            file.createNewFile(); // if file already exists will do nothing
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            while (true)
            {
                try
                {
                    reader = new BufferedReader(new FileReader("/Users/pankaj/Downloads/myfile.txt"));
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
        }).start();

    }

    public void onsendclicked(ActionEvent actionEvent)
    {
        chatarea.appendText(message.getText());
        try {

            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter( new FileWriter(filePath, true));
            out.write(message.getText());
            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public void onbackclicked(ActionEvent actionEvent) {
    }
}
