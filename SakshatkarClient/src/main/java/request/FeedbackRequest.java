package request;

import constants.RequestCode;

import java.io.Serializable;

public class FeedbackRequest extends Request implements Serializable {

    private String userUID,video,audio,response,rating,comment;

    public FeedbackRequest(String userUID, String video, String audio, String response, String rating, String comment)
    {
        this.userUID=userUID;
        this.audio=audio;
        this.video=video;
        this.response=response;
        this.comment=comment;
        this.rating=rating;
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.FEEDBACK_REQUEST;
    }
}
