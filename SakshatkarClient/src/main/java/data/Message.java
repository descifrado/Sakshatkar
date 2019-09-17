package data;

import java.io.Serializable;

public class Message implements Serializable {
    public String msgFrom,msgTo,msg;

    public Message(String msgFrom, String msgTo, String msg) {
        this.msgFrom = msgFrom;
        this.msgTo = msgTo;
        this.msg = msg;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
