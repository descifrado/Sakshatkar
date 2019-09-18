package request;

import constants.RequestCode;
import data.Notification;

public class SendNotificationRequest extends Request {
    private Notification notification;

    public SendNotificationRequest(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public RequestCode getRequestCode() {
        return RequestCode.SEND_NOTIFICATION_REQUEST;
    }
}
