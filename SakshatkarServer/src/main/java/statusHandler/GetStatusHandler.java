package statusHandler;

import constants.ResponseCode;
import mainClasses.Main;
import request.GetStatusRequest;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetStatusHandler {
    private String userUID,status="";
    public GetStatusHandler(GetStatusRequest getStatusRequest)
    {
        this.userUID=getStatusRequest.getUserUID();
    }

    public Response getResponse()
    {
        String query="Select status from OnlineUser where userUID=?;";
        try{
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);
            preparedStatement.setString(1,userUID);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next())
                status=rs.getString(1);
            return new Response(UIDGenerator.generateuid(),status, ResponseCode.SUCCESS);
        }catch (SQLException e){
            e.printStackTrace();
        }
        String lastSeenQuery="Select time from LastSeen where userUID=?";
        try {
            PreparedStatement preparedStatement=Main.connection.prepareStatement(lastSeenQuery);
            preparedStatement.setString(1,userUID);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                status=resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Response(UIDGenerator.generateuid(),status, ResponseCode.FAILED);
    }
}
