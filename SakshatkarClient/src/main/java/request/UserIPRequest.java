package request;

import java.io.Serializable;

public class UserIPRequest implements Serializable {
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
}
