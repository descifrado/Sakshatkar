package data;

import constants.NotificationType;

import java.io.Serializable;

public class Notification implements Serializable {

    private User sender;
    private User reciever;
    private NotificationType notificationType;

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

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Notification(User sender, User reciever, NotificationType notificationType) {
        this.sender = sender;
        this.reciever = reciever;
        this.notificationType = notificationType;
    }
}
