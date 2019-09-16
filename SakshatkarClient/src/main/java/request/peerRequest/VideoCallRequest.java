package request.peerRequest;

import constants.RequestCode;
import data.User;
import request.Request;

import java.io.Serializable;

public class VideoCallRequest extends Request implements Serializable {
    private User user;

    public VideoCallRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.VIDEO_CALL_REQUEST;
    }
}
