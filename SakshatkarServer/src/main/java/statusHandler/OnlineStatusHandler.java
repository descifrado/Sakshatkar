package statusHandler;

import constants.ResponseCode;
import constants.Status;
import mainClasses.Main;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OnlineStatusHandler {
    private String userUID;

    public OnlineStatusHandler(String userId){
        this.userUID=userId;
    }

    public Response changeOnlineStatus(Status status){
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
