package feedbackHandler;

import constants.ResponseCode;
import mainClasses.Main;
import request.FeedbackRequest;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedBackHandler {
    FeedbackRequest feedbackRequest;

    public FeedBackHandler(FeedbackRequest feedbackRequest) {
        this.feedbackRequest = feedbackRequest;
    }

    public Response getResponse(){
        String q="INSERT INTO feedback VALUES (?,?,?,?,?,?);";
        try
        {
            PreparedStatement stmt= Main.connection.prepareStatement(q);
            stmt.setString(1,feedbackRequest.getUserUID());
            stmt.setString(2,feedbackRequest.getVideo());
            stmt.setString(3,feedbackRequest.getAudio());
            stmt.setString(4,feedbackRequest.getResponse());
            stmt.setString(5,feedbackRequest.getComment());
            stmt.setString(6,feedbackRequest.getRating());

            stmt.executeUpdate();

            return new Response((UIDGenerator.generateuid()),null, ResponseCode.SUCCESS);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);

    }
}
