package searchHandler;

import constants.ResponseCode;
import data.User;
import mainClasses.Main;
import request.Response;
import request.UserSearchRequest;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SearchHandler {
    UserSearchRequest userSearchRequest;
    String userName;
    public  SearchHandler(UserSearchRequest userSearchRequest){
            this.userSearchRequest = userSearchRequest;
            this.userName = userSearchRequest.getName();
            // set this.userName = userSearchRequest.getUserName();
    }
    public Response getResponse(){
        String query = "SELECT * FROM User WHERE LOWER(name) LIKE \"" + userName.toLowerCase() +"\"";
        System.out.println(query);
        ArrayList<User> users = new ArrayList<User>();
        try {
            PreparedStatement statement= Main.connection.prepareStatement(query);
            ResultSet resultSet=statement.executeQuery();
            while(resultSet.next()){
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
    }

}
