package offlineUserChatHandler;

import data.Message;
import data.User;
import tools.EncryptDecrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OfflineUserMessageSender {
    private User reciever;
    private User sender;

    public List<Message> getMessages(){
        List<Message> messages=new ArrayList<>();

        String cwd = System.getProperty("user.dir");
        String loc = cwd+"/chat";
        System.out.println(loc);
        loc = loc+"/"+sender.getUserUID()+reciever.getUserUID();

        String filePath=loc;
        System.out.println(filePath);
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            System.out.println(line);
            while (line != null) {
                messages.add(new Message(line,sender,reciever));
                try {
                    line = reader.readLine();
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            reader.close();
//            File file=new File(filePath);
//            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public OfflineUserMessageSender(User reciever, User sender) {
        this.reciever = reciever;
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
