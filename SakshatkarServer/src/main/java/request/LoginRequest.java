package request;

import constants.RequestCode;

import java.io.Serializable;

/**
 *
 */
public class LoginRequest extends Request implements Serializable {

    /**
     *
     */
    private String emailId , password ;

    /**
     * Constructor to initialize Login request
     * @param emailId EmailID of the User
     * @param password password of the User in Hashed format
     */
    public LoginRequest(String emailId,String password){
        this.emailId = emailId;
        this.password = password;
    }

    /**
     * Accesser method to get emailID of the user requesting for login
     * @return String of a emailID of User
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     *
     * @param emailId
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    @Override
    public RequestCode getRequestCode(){
        return RequestCode.LOGIN_REQUEST;
    }
}
