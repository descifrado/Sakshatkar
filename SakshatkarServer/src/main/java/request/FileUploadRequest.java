package request;

import constants.RequestCode;
import data.File;

import java.io.Serializable;

public class FileUploadRequest extends Request implements Serializable {
//    private static final long serialVersionUID=1L;
    private File file;
    private String userUID;
    private String userIP;

    public FileUploadRequest(File file, String userUID, String userIP) {
        this.file = file;
        this.userUID = userUID;
        this.userIP = userIP;
    }

    public File getFile() {
        return file;
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

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.FILEUPLOAD_REQUEST;
    }
}
