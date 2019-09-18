package chatHandler;

import controllers.Controller_Profile;
import data.Message;
import data.User;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import mainApp.App;
import request.Response;
import tools.EncryptDecrypt;

import java.io.*;
import java.net.Socket;

public class ChatHandler implements Runnable {

    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;


    @Override
    public void run() {
        Response response;
        Message message;
        User messagingUser;
        while (true){
            try {
                Socket socket= App.serverSocketMessage.accept();
                oos=new ObjectOutputStream(socket.getOutputStream());
                ois=new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response= (Response) ois.readObject();
                message= (Message) response.getResponseObject();
                messagingUser=message.getSender();

                String cwd=System.getProperty("user.dir");
                String filePath=cwd+"/chat/"+App.user.getUserUID() + messagingUser.getUserUID();
                File file = new File(filePath);
                try {
                    file.createNewFile(); // if file already exists will do nothing
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {

                    BufferedWriter out = new BufferedWriter( new FileWriter(filePath, true));
                    String msg = message.getMsg();
                    msg = EncryptDecrypt.decrypt(msg);
                    out.write(EncryptDecrypt.encrypt(message.getSender()+": "+msg+"\n"));
                    out.close();
                }
                catch (IOException e) {
                    System.out.println("exception occoured" + e);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                User finalMessagingUser = messagingUser;
                Platform.runLater(()->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, finalMessagingUser.getFirstName()+" has send a message to you.", ButtonType.OK);
                    alert.showAndWait();
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
