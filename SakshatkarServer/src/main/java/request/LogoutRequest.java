package request;

import constants.RequestCode;

import java.io.Serializable;

public class LogoutRequest extends Request implements Serializable {

    private String userUID;
    public LogoutRequest(String userUID)
    {
        this.userUID=userUID;

    }

    public String getUserUID() {
        return userUID;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.LOGOUT_REQUEST;
    }
}