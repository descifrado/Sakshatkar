package request;

import constants.ResponseCode;

import java.io.Serializable;

public class Response implements Serializable {
    String responseID;
    Object responseObject;
    ResponseCode responseCode;

    public Response(String responseID, Object responseObject, ResponseCode responseCode) {
        this.responseID = responseID;
        this.responseObject = responseObject;
        this.responseCode = responseCode;
    }

    public String getResponseID() {
        return responseID;
    }

    public void setResponseID(String responseID) {
        this.responseID = responseID;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }
}
