package request;

import constants.RequestCode;
import data.User;

import java.io.Serializable;

public class SignUpRequest extends Request implements Serializable
{
    private String password;
    private User user;
    public SignUpRequest(String password, User user)
    {
        this.password=password;
        this.user=user;
    }
    public String getPassword()
    {
        return password;
    }
    public User getUser() throws CloneNotSupportedException
    {
        return (User) this.user.clone();
    }

    @Override
    public RequestCode getRequestCode(){
        return RequestCode.SIGNUP_REQUEST;
    }
}
