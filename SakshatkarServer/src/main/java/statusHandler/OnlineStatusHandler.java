package statusHandler;

import constants.ResponseCode;
import constants.Status;
import mainClasses.Main;
import request.Request;
import request.Response;
import request.StatusChangeRequest;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OnlineStatusHandler {
    private String userUID;
    private Status status;
    private StatusChangeRequest statusChangeRequest;
    public OnlineStatusHandler(StatusChangeRequest statusChangeRequest){
        this.statusChangeRequest=statusChangeRequest;
        this.userUID=statusChangeRequest.getUserUID();
        this.status=statusChangeRequest.getStatus();
    }

    public Response getResponse(){
        String query="UPDATE OnlineUser set status=? where userUID=?";
        try {
            PreparedStatement preparedStatement= Main.connection.prepareStatement(query);
            preparedStatement.setString(1,String.valueOf(status));
            preparedStatement.setString(2, this.userUID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.SUCCESS);
    }
}
