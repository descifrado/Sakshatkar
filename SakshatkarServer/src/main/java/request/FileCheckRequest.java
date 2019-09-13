package request;

import constants.RequestCode;
import data.File;

public class FileCheckRequest extends  Request {
    private File file;
    private String userUID;
    private String userIP;

    public FileCheckRequest(File file, String userUID, String userIP) {
        this.file = file;
        this.userUID = userUID;
        this.userIP = userIP;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }
    public File getFile() {
        return file;
    }
    @Override
    public RequestCode getRequestCode() {
        return RequestCode.FILECHECK_REQUEST;
    }


}
