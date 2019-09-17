package request;

import constants.RequestCode;
import data.Message;

public class MessageSendRequest extends Request {
   String senderIP,recieverIP;
   Message message;

    public MessageSendRequest(String senderIP, String recieverIP, Message message) {
        this.senderIP = senderIP;
        this.recieverIP = recieverIP;
        this.message = message;
    }

    public String getSenderIP() {
        return senderIP;
    }

    public void setSenderIP(String senderIP) {
        this.senderIP = senderIP;
    }

    public String getRecieverIP() {
        return recieverIP;
    }

    public void setRecieverIP(String recieverIP) {
        this.recieverIP = recieverIP;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.MESSAGESEND_REQUEST;
    }
}
