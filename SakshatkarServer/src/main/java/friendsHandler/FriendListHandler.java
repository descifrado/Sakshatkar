package friendsHandler;

import constants.ResponseCode;
import data.User;
import mainClasses.Main;
import request.FriendListRequest;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendListHandler {
    FriendListRequest friendListRequest;
    public FriendListHandler(FriendListRequest friendListRequest){
        this.friendListRequest = friendListRequest;
    }
    public Response getResponse(){
        String query = "SELECT Friend.friendUID , User.firstName,User.lastName,User.email,User.phone,User.Company," +
                " User.profilePicture FROM Friend INNER JOIN User ON Friend.friendUID = User.UserUID";
        System.out.println(query);
        try {
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<User> users = new ArrayList<User>();
            while (resultSet.next()){
                User user = new User();

                user.setUserUID(resultSet.getString(1));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setPhone(resultSet.getString(5));
                user.setCompany(resultSet.getString(6));
                user.setPpURL(resultSet.getString(7));
                users.add(user);
            }
            return new Response(UIDGenerator.generateuid(),users, ResponseCode.SUCCESS);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
    }
}
