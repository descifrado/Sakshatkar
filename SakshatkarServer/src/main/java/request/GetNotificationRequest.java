package request;

import constants.RequestCode;
import data.User;

public class GetNotificationRequest extends Request {

    private User user;

    public GetNotificationRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestCode getRequestCode() {
        return RequestCode.GET_NOTIFICATION_REQUEST;
    }
}
