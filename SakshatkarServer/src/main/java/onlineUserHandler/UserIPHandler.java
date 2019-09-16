package onlineUserHandler;

import constants.ResponseCode;
import data.User;
import mainClasses.Main;
import request.Response;
import request.UserIPRequest;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserIPHandler {
    private UserIPRequest userIPRequest;
    private String userUID;

    public UserIPHandler(UserIPRequest userIPRequest) {
        this.userIPRequest = userIPRequest;
        this.userUID = userIPRequest.getUserUID();
    }

    public Response getResponse(){
        String query = "SELECT userIP FROM OnlineUser WHERE userUID = \"" + this.userUID + "\" ";
        try {
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.next()){
                return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
            }

            return new Response((UIDGenerator.generateuid()),rs.getString(1), ResponseCode.SUCCESS);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);

    }
}
