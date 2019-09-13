package authenticationHandler;

import constants.ResponseCode;
import data.User;
import mainClasses.Main;
import request.LoginRequest;
import request.Response;
import tools.UIDGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    private LoginRequest loginRequest;
    public Login(LoginRequest loginRequest){
        this.loginRequest = loginRequest;
    }
    public Response getResponse() {
        User user ;

        String query = "SELECT * FROM User INNER JOIN Password ON User.userUID = Password.userUID WHERE User.emailID=\"" +
                loginRequest.getEmailId() + "\" AND Password.password = \"" + loginRequest.getPassword()+"\"";
        try {
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            if(!rs.next()){
                return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);
            }
            user = new User();
            user.setUserUID(rs.getString(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setPhone(rs.getString(5));
            user.setCompany(rs.getString(6));
            user.setPpURL(rs.getString(7));
            return new Response((UIDGenerator.generateuid()),user, ResponseCode.SUCCESS);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return new Response(UIDGenerator.generateuid(),null, ResponseCode.FAILED);

    }

}
