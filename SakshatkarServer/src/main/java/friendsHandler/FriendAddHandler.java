package friendsHandler;

import constants.ResponseCode;
import mainClasses.Main;
import request.AddFriendRequest;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FriendAddHandler {
    String userUID,friendUID;
    AddFriendRequest addFriendRequest;

    public FriendAddHandler(AddFriendRequest addFriendRequest) {
        this.addFriendRequest = addFriendRequest;
        this.userUID = addFriendRequest.getUserUID();
        this.friendUID = addFriendRequest.getFriendUID();
    }

    public Response getResponse(){
        String query  = "INSERT INTO Friend VALUES (?,?)";
        try{
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);
            preparedStatement.setString(1,userUID);
            preparedStatement.setString(2,friendUID);
            preparedStatement.executeUpdate();
            return new Response(UIDGenerator.generateuid(),null, ResponseCode.SUCCESS);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
    }

}
