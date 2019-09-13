package data;

import java.io.Serializable;

public class Peer implements Serializable {
    private String userUID,ip;

    public String getUserUID() {
        return userUID;
    }

    public String getIp() {
        return ip;
    }

    public Peer(String userUID, String ip)
    {
        this.userUID=userUID;
        this.ip=ip;
    }
}
