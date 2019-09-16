package request;

import constants.RequestCode;

import java.io.Serializable;

public class UserIPRequest extends Request implements Serializable  {
    String userUID;

    public UserIPRequest(String userUID) {
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public RequestCode getRequestCode() {
        return RequestCode.USERIP_REQUEST;
    }
}
