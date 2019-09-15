package request;

import constants.RequestCode;
import constants.Status;

import java.io.Serializable;

public class StatusChangeRequest extends Request implements Serializable {
    private String userUID;
    private Status status;

    public StatusChangeRequest(String userUID, Status status) {
        this.userUID = userUID;
        this.status = status;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public RequestCode getRequestCode() {
        return RequestCode.STATUS_CHANGE_REQUEST;
    }
}
