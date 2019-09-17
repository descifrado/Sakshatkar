package data;

import java.io.Serializable;

public class Message implements Serializable {
    private String msg;
    private User sender,reciever;

    public Message(String msg, User sender, User reciever) {
        this.msg = msg;
        this.sender = sender;
        this.reciever = reciever;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }
}
