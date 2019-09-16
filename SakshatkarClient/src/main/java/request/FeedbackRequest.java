package request;

import constants.RequestCode;

import java.io.Serializable;

public class FeedbackRequest extends Request implements Serializable {

    private String userUID;
    private String video;

    public String getUserUID() {
        return userUID;
    }

    public String getVideo() {
        return video;
    }

    public String getAudio() {
        return audio;
    }

    public String getResponse() {
        return response;
    }

    public String getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    private String audio;
    private String response;
    private String rating;
    private String comment;

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
