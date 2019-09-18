package data;

import constants.NotificationType;

import java.io.Serializable;

public class Notification implements Serializable {

    private String sender;
    private String reciever;
    private NotificationType notificationType;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Notification(String sender, String reciever, NotificationType notificationType) {
        this.sender = sender;
        this.reciever = reciever;
        this.notificationType = notificationType;
    }
}
